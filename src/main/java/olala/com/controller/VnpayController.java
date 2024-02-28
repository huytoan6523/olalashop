package olala.com.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import olala.com.constant.Constant;
import olala.com.entities.Orders;
import olala.com.entities.ProductItem;
import olala.com.utils.VnpayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import olala.com.service.CartService;
import olala.com.service.OrdersService;
import olala.com.service.ProductItemService;
import olala.com.service.UsersService;

@Controller
public class VnpayController {
	@Autowired
	OrdersService ordersService;

	@Autowired
	ProductItemService productItemService;

	@Autowired
	UsersService usersService;

	@Autowired
	CartService cartService;

	@GetMapping("/return_vnpay")
	public String showResult(Model model, HttpServletRequest request, HttpSession session)
			throws UnsupportedEncodingException {
		Map fields = new HashMap();
		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
			String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
			String fieldValue = URLEncoder.encode(request.getParameter(fieldName),
					StandardCharsets.US_ASCII.toString());
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				fields.put(fieldName, fieldValue);
			}
		}

		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}

		String signValue = VnpayConfig.hashAllFields(fields);
		if (signValue.equals(vnp_SecureHash)) {
			if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
				String vnp_TxnRef = (String) fields.get("vnp_TxnRef");
				Orders orders = ordersService.findByVnpTxnRef(vnp_TxnRef);
				Long paidAmount = Long.parseLong((String) fields.get("vnp_Amount"));
				System.out.println("paidAmount: " + paidAmount);

				//
				orders.setPaidAmount(paidAmount / 100);
				// clear cart
				List<ProductItem> productItems = orders.getProductItems();
				System.out.println(productItems);
				for (ProductItem p : productItems) {
					p.setCart(null);
					productItemService.save(p);
				}
				// change status
				// status
				orders.setStatus(Constant.OrderStatus.IN_PROCESS);
				// save to db
				orders = ordersService.save(orders);

				//// Update cart item quantity
				if (session.getAttribute("cartItem") != null) {
					Integer currentCartItemQuantity = (Integer) session.getAttribute("cartItem");
					session.setAttribute("cartItem", currentCartItemQuantity - productItems.size());
				}

				return "ptitshop/createOrderSucessFul";
			} else {
				return "ptitshop/paymentError";
			}
		} else {
			return "ptitshop/paymentError";
		}
	}

}
