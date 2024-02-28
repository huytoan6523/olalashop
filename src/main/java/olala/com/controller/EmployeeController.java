package olala.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import olala.com.model.OrderInfo;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@GetMapping("/handleOrder")
	public String showHandleOrder(Model model) {
		OrderInfo orderInfo = new OrderInfo();
		model.addAttribute("orderInfo", orderInfo);
		return "employee/handle_order";
	}
}
