package olala.com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import olala.com.entities.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import olala.com.model.OrderDto;
import olala.com.service.ProductItemService;

@Controller
@RequestMapping("/user/checkout")
public class CheckoutController {
	@Autowired
	ProductItemService productItemService;
	
	@PostMapping("/")
	public String showCheckoutForm(Model model, HttpServletRequest  request) {
		//get All productItemId
		List<String> list = Collections.list(request.getParameterNames());
		
		
		//convert String to Integer
		List<ProductItem> productItems= new ArrayList<>();
		Long totalPrice = (long) 0;
		//
		for( String x: list ) {
			ProductItem p = productItemService.findById(Long.parseLong(x));
			productItems.add(p);
			totalPrice += p.getProduct().getActualPrice()*(long)p.getQuantity();
		}
		//
		OrderDto  o= new OrderDto();
		o.setProductItems(productItems);
		o.setTotalPrice(totalPrice);
		o.setTotalProductItem(productItems.size());
		
		
		
		model.addAttribute("orderDto", o);
		return "ptitshop/checkout";
	}
	
}
