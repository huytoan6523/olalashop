package olala.com.api.olalashop;

import java.util.List;

import olala.com.entities.Orders;
import olala.com.entities.Product;
import olala.com.entities.ProductItem;
import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.OrderInfo;
import olala.com.model.PageData;
import olala.com.service.OrdersService;
import olala.com.service.ProductService;
import olala.com.service.UsersService;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
	@Autowired
	OrdersService ordersService;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	ProductService productService;

	@PostMapping("/findByOrderInfo")
	public PageData<Orders> findByOrderInfo(@ModelAttribute("orderInfo") OrderInfo orderInfo) {

		if (orderInfo.getPageSize() == null) {
			orderInfo.setPageSize(10);
		}

		if (orderInfo.getPageNumber() == null) {
			orderInfo.setPageNumber(0);
		}

		PageData<Orders> pageData = ordersService.findByOrderInfo(orderInfo);
		return pageData;
	}

	@PostMapping("/updateOrderStatus")
	public String updateStatus(@ModelAttribute("orderInfo") OrderInfo orderInfo) {
		Long id = orderInfo.getId();

		Orders orders = ordersService.findById(id);
		orders.setStatus(orderInfo.getStatus());
		
		//update sold
		List<ProductItem> productItems = orders.getProductItems();
		for( ProductItem p: productItems ) {
			Product product = p.getProduct();
			product.setSold(product.getSold()+p.getQuantity());
		}
		
		Users currentUser = usersService.getCurrentUserLogged();
		orders.setEmployee(currentUser);
		
		if (orders.getStatus() == 3) {
			orders.setPaidAmount(orders.getTotalAmount());
		}

		try {
			ordersService.save(orders);
		} catch (Exception e) {
			return "error";
		}
		return "success";
	}
	
	@PostMapping("/viewDetailOrder/{id}")
	public Orders getDetailOrder(@PathVariable("id") Long id  ) {
		return ordersService.findById(id);
	}
}
