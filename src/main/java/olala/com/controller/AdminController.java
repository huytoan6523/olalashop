package olala.com.controller;

import javax.validation.Valid;

import olala.com.entities.Cart;
import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import olala.com.model.PageInfo;
import olala.com.model.StatInfo;
import olala.com.service.CartService;
import olala.com.service.UsersService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UsersService usersService;
	
	@Autowired
	CartService cartService;

	@GetMapping("/employee/view")
	public String showAll(Model model) {
		model.addAttribute("pageInfo", new PageInfo());
		return "employee/view_employees";
	}

	@GetMapping("/employee/add")
	public String showAddForm(Model model) {
		model.addAttribute("employee", new Users());
		return "employee/add_employee";

	}

	@PostMapping("/employee/add")
	public String addUsers(Model model, @Valid @ModelAttribute("employee") Users employee, Errors errors) {
		System.out.println(employee);
		if (null != errors && errors.getErrorCount() > 0) {
			return "employee/add_employee";
		} else {
			String userName = employee.getPhoneNumber().trim();

			// check if employee name had already exits
			if (usersService.getByPhoneNumber(userName)!=null) {
				model.addAttribute("msg", userName + " has already existed!!");
				return "employee/add_employee";
			}
			//trim
			employee.setPhoneNumber(userName);
			usersService.save(employee);
			//
			Cart cart = new Cart();
			cart.setUsers(employee);
			cartService.save(cart);
			return "redirect:/admin/employee/view";
		}

	}
//
	@GetMapping("/employee/delete/{id}")
	public String deleteUsers(@PathVariable("id") Long id) {
		usersService.deleteById(id);
		return "redirect:/admin/employee/view";
	}
//
	@GetMapping("/employee/edit/{id}")
	public String showEditUsers(@PathVariable("id") Long id, Model model) {
		Users employee = usersService.findById(id);
		model.addAttribute("employee", employee);
		return "employee/edit_employee";
	}

	@PostMapping("/employee/edit")
	public String editUsers(Model model, @Valid @ModelAttribute("employee") Users employee, Errors errors) {
		System.out.println(employee);
		if (null != errors && errors.getErrorCount() > 0) {
			return "employee/edit_employee";
		} else {
			String userName = employee.getPhoneNumber().trim();
			// check if employee name had already exits
			if (usersService.getByPhoneNumber(userName)!=null) {
				model.addAttribute("msg", userName + " has already existed!!");
				return "employee/add_employee";
			}
			//trim
			Users u = usersService.findById(employee.getId());
			u.setPhoneNumber(userName);
			u.setFullName(employee.getFullName());
			u.setPassword(employee.getPassword());
			usersService.save(u);
			return "redirect:/admin/employee/view";
		}
	}
	
	@GetMapping("/dashboard")
	public String showDashBoard(Model model) {
		model.addAttribute("statInfo", new StatInfo());
		return "admin/admin_dashboard";
	}
}
