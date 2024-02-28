package olala.com.controller;

import java.time.LocalDate;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import olala.com.entities.Cart;
import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import olala.com.service.CartService;
import olala.com.service.UsersService;

@Controller
@Slf4j
public class LoginController {
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	CartService cartService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(name="error", required = false) Boolean error, Model model) {
    	if( error !=null ) {
    		model.addAttribute("msg", "Sai tài khoản/mật khẩu");
    	}
    	else model.addAttribute("msg", "");
        return "login";
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(HttpSession session) {
    	Users u = usersService.getCurrentUserLogged();
    	//set cart item quantity
    	if( u.getCart() != null ) {
    		session.setAttribute("cartItem", u.getCart().getProductItems().size());
    	}
        Collection<? extends GrantedAuthority> authorities;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authorities = auth.getAuthorities();
        String myRole = authorities.toArray()[0].toString();
        if (myRole.equals("ROLE_ADMIN")) {
            return "redirect:/admin/category/add";
        } else if (myRole.equals("ROLE_USER")) {
        	return "redirect:/";
        } else if (myRole.equals("ROLE_EMPLOYEE")) {
        	return "redirect:/employee/handleOrder";
        }
         else {
           return "hello";
        }
    }
    
    
    @GetMapping("/test")
    public String test() {
//    	Users u1 = new Users();
//    	u1.setPhoneNumber("employee");
//    	u1.setPassword("123456");
//    	u1.setRole("ROLE_EMPLOYEE");
//    	u1.setDob(LocalDate.now());
//    	u1.setFullName("hii");
//    	usersService.save(u1);
    	
    	
    	Users u = new Users();
    	u.setPhoneNumber("012345");
    	//u.setPhoneNumber("admin");
    	u.setPassword("123456");
    	u.setRole("ROLE_USER");
    	///u.setRole("ROLE_ADMIN");
    	u.setDob(LocalDate.now());
    	u.setFullName("hii");
    	usersService.save(u);
    	
    	Cart cart = new Cart();
		cart.setUsers(u);
		cartService.save(cart);
    	
    	return "hello";
    }
}
