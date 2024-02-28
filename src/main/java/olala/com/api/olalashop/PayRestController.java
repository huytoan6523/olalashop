package olala.com.api.olalashop;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import olala.com.constant.Constant;
import olala.com.entities.DeliveryAddress;
import olala.com.entities.Orders;
import olala.com.entities.ProductItem;
import olala.com.entities.Users;
import olala.com.utils.VnpayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import olala.com.model.OrderDto;
import olala.com.service.CartService;
import olala.com.service.DeliveryAddressService;
import olala.com.service.OrdersService;
import olala.com.service.ProductItemService;
import olala.com.service.UsersService;

@RestController
@RequestMapping("user/payment")
public class PayRestController {
	private static final String GET_DELIVERY_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee?token=https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

	@Autowired
	ProductItemService productItemService;

	@Autowired
	UsersService usersService;

	@Autowired
	OrdersService ordersService;

	@Autowired
	CartService cartService;

	@Autowired
	DeliveryAddressService deliveryAddressService;
	RestTemplate restTemplate = new RestTemplate();

	@PostMapping("/")
	public Map<String, Object> payOrder(@ModelAttribute("orderDto") OrderDto orderDto, HttpServletRequest req,
			HttpSession session) {
		// create Order
		Orders orders = new Orders();

		// set DeliveryAddress
		DeliveryAddress deliveryAddress = deliveryAddressService.findById(orderDto.getDeliveryAddressId());
		orders.setDeliveryAddress(deliveryAddress);

		// set User
		Users users = usersService.getCurrentUserLogged();
		orders.setUsers(users);

		// TxnRef for Query
		String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
		while (ordersService.findByVnpTxnRef(vnp_TxnRef) != null) {
			vnp_TxnRef = VnpayConfig.getRandomNumber(8);
		}
		orders.setVnpTxnRef(vnp_TxnRef);

		// productPrice
		Long productPrice = (long) 0;
		List<ProductItem> productItems = new ArrayList<>();
		for (Long x : orderDto.getProductItemIds()) {
			ProductItem p = productItemService.findById(x);
			productItems.add(p);
			productPrice += p.getProduct().getActualPrice() * (long) p.getQuantity();
		}

		// productPrice
		orders.setProductPrice(productPrice);

		// ship fee
		Long deliveryFee = 0L;
		if (productPrice < 200000) {
			deliveryFee = getDeliveryFee(orderDto.getDeliveryAddressId(), productPrice);
		}
		orders.setDeliveryFee(deliveryFee);
		// totalAmount;
		Long totalAmount = deliveryFee + productPrice;
		Long amount = totalAmount * 100;
		orders.setTotalAmount(totalAmount);

		// paidAmount
		orders.setPaidAmount(0L);

		// payment Type
		orders.setPaymentType(orderDto.getPaymentType());

		// order info
		orders.setOrderInfo("thanh toan hoa don" + vnp_TxnRef);

		// for return
		Map<String, Object> map = new HashMap<>();
		map.put("code", "00");
		map.put("message", "success");

		if (orderDto.getPaymentType().equals("vnpay")) {
			// vnp_Version
			String vnp_Version = "2.1.0";
			String vnp_Command = "pay";

			String vnp_OrderInfo = "thanh toan hoa don ";
			String orderType = "billpayment";

			String vnp_IpAddr = VnpayConfig.getIpAddress(req);
			String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

			Map<String, String> vnp_Params = new HashMap<>();
			vnp_Params.put("vnp_Version", vnp_Version);
			vnp_Params.put("vnp_Command", vnp_Command);
			vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
			vnp_Params.put("vnp_Amount", String.valueOf(amount));
			vnp_Params.put("vnp_CurrCode", "VND");
			String bank_code = req.getParameter("bankcode");
			if (bank_code != null && !bank_code.isEmpty()) {
				vnp_Params.put("vnp_BankCode", bank_code);
			}
			vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
			vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
			vnp_Params.put("vnp_OrderType", orderType);

			String locate = req.getParameter("language");
			if (locate != null && !locate.isEmpty()) {
				vnp_Params.put("vnp_Locale", locate);
			} else {
				vnp_Params.put("vnp_Locale", "vn");
			}
			vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
			vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

			Date dt = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String vnp_CreateDate = formatter.format(dt);
			vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

			Calendar cldvnp_ExpireDate = Calendar.getInstance();
			cldvnp_ExpireDate.add(Calendar.SECOND, 60);
			Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();
			String vnp_ExpireDate = formatter.format(vnp_ExpireDateD);

			vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

			List fieldNames = new ArrayList(vnp_Params.keySet());
			Collections.sort(fieldNames);
			StringBuilder hashData = new StringBuilder();
			StringBuilder query = new StringBuilder();
			Iterator itr = fieldNames.iterator();
			while (itr.hasNext()) {
				String fieldName = (String) itr.next();
				String fieldValue = (String) vnp_Params.get(fieldName);
				if ((fieldValue != null) && (fieldValue.length() > 0)) {
					// Build hash data
					hashData.append(fieldName);
					hashData.append('=');
					// hashData.append(fieldValue);
					try {
						hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // s盻ｭ d盻･ng
						// v2.1.0 check
						// sum sha512
						// Build query
					try {
						query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					query.append('=');
					try {
						query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (itr.hasNext()) {
						query.append('&');
						hashData.append('&');
					}
				}

			}
			String queryUrl = query.toString();
			// String vnp_SecureHash = VnpayConfig.Sha256(VnpayConfig.vnp_HashSecret +
			// hashData.toString());
			String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
			queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
			String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
			map.put("data", paymentUrl);

			// status
			orders.setStatus(Constant.OrderStatus.DRAFT);

			// save to db
			orders = ordersService.save(orders);

			// setProductItem
			setProductItem(orders, productItems);

			return map;
		}

		// for cash payment
		// status
		orders.setStatus(Constant.OrderStatus.IN_PROCESS);
		// save to db
		orders = ordersService.save(orders);

		// clear Cart
		clearCart(productItems);

		// setProductItem
		setProductItem(orders, productItems);

		//// Update cart item quantity
		if( session.getAttribute("cartItem") !=null ) {
			Integer currentCartItemQuantity = (Integer)session.getAttribute("cartItem");
			session.setAttribute("cartItem", currentCartItemQuantity - productItems.size() );
		}
		

		// return URL
		map.put("data", Constant.URLConstant.CREATE_ORDER_SUCCESSFUL);
		return map;
	}

	public Long getDeliveryFee(Long deliveryAdressId, Long totalPrice) {
		Long res = (long) 0;

		DeliveryAddress deliveryAddress = deliveryAddressService.findById(deliveryAdressId);

		Map<String, Object> params = new HashMap<>();
		params.put("from_district_id", 1542);
		params.put("to_district_id", deliveryAddress.getDistrictId());
		params.put("to_ward_code", deliveryAddress.getWardCode());
		params.put("height", 10);
		params.put("length", 10);
		params.put("weight", 1000);
		params.put("width", 10);
		// chuen phat thuong mai dien tu
		params.put("service_id", 100039);
		params.put("coupon", null);
		params.put("insurance_value", totalPrice.longValue());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GET_DELIVERY_URL);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}

		// header
		HttpHeaders headers = new HttpHeaders();
		headers.set("token", "606ae2a4-5da1-11ed-b824-262f869eb1a7");
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				new HttpEntity(headers), String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Request Successful.");
			String body = response.getBody();
			Gson gson = new Gson();
			Map<String, Object> map = gson.fromJson(body, HashMap.class);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			String deliveryFee = data.get("total").toString();
			res = (long) Double.parseDouble(deliveryFee);
		} else {
			System.out.println("Request Failed");
			System.out.println(response.getStatusCode());
		}

		return res;
	}

	public void clearCart(List<ProductItem> productItems) {
		for (ProductItem p : productItems) {
			p.setCart(null);
			productItemService.save(p);
		}

	}

	public void setProductItem(Orders orders, List<ProductItem> productItems) {
		for (ProductItem p : productItems) {
			p.setOrders(orders);
			productItemService.save(p);
		}

	}
}
