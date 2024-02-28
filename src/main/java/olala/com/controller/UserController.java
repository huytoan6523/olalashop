package olala.com.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import olala.com.constant.Constant;
import olala.com.entities.Cart;
import olala.com.entities.Otp;
import olala.com.entities.Users;
import olala.com.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import olala.com.model.OrderInfo;
import olala.com.service.CartService;
import olala.com.service.UsersService;
import olala.com.utils.GenerateOTP;

@Controller
public class UserController {

	@Autowired
	UsersService usersService;

	@Autowired
	CartService cartService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("u", new Users());
		return "register";
	}

	@PostMapping("/register")
	public String register(Model model, @Valid @ModelAttribute("u") Users u, Errors errors, HttpSession session) {
		if (null != errors && errors.getErrorCount() > 0) {
			return "register";
		} else {
			// check phoneNumber is already exists
			if (!(usersService.getByPhoneNumber(u.getPhoneNumber().trim()) == null)) {
				model.addAttribute("msg", "Số điện thoại đã được đăng trước đó !!!");
				return "register";
			} else if (!NumberUtil.isPassword(u.getPassword())) {
				model.addAttribute("pass_msg", "Mật khẩu ít nhất 6 ký tự!");
				return "register";
			}
			
			//save user to session and send otp
			sendOtp(u, session);
			return "confirmOtp";
		}
	}

	@PostMapping("/register/confirmOtp")
	public String confirmOtp(Model model, @RequestParam("otp_number1") String otp_number1,
			@RequestParam("otp_number2") String otp_number2, @RequestParam("otp_number3") String otp_number3,
			@RequestParam("otp_number4") String otp_number4, HttpSession session) {
		Otp o = (Otp) session.getAttribute("o");
		if( o!=null ) {
			String otp = otp_number1.trim() + otp_number2.trim() + otp_number3.trim() + otp_number4.trim();
			if ((o.getOtp().equals(otp.trim())) && GenerateOTP.isOptTimeValid(o.getCreateAt(), LocalDateTime.now())) {
				// get User from sesssion
				Users u = (Users) session.getAttribute("u");
				u.setRole("ROLE_USER");
				// save to db
				usersService.save(u);

				// create a cart for user
				Cart cart = new Cart();
				cart.setUsers(u);
				cartService.save(cart);
				
				
				// Send SMS for register sucessfully
				Twilio.init("AC518ef94cb8e9a610d419a09decf7f7eb", "6443adbe56bd6dbec6acbb8b1f330edc");
				Message.creator(new PhoneNumber("+84967475094"), new PhoneNumber("+19289188190"), Constant.MessageSMS.REGISTER_SUCCESSFULLY_MESSAGE+ "+84967475094")
						.create();
				return "redirect:" + "http://localhost:8080/login";
			} else {
				model.addAttribute("msg", "Mã Otp không chính xác!");
				return "confirmOtp";
			}
		}
		else  {
			model.addAttribute("u", new Users());
			return "register";
		}
		

	}

	@GetMapping("/user/management")
	public String showUserManagement(Model model) {
		OrderInfo orderInfo = new OrderInfo();
		model.addAttribute("orderInfo", orderInfo);
		orderInfo.setUserId(usersService.getCurrentUserLogged().getId() + "");
		return "user/user_management";
	}

	public void sendOtp(Users u, HttpSession session) {
		// save user to Session
		session.setAttribute("u", u);

		// generateOtp
		String otp = GenerateOTP.generateOTP();
		// System.out.println(otp);
		// set Otp Create Time
		LocalDateTime createAt = LocalDateTime.now();

		// set Object otp
		Otp o = new Otp(null, otp, createAt);
		session.setAttribute("o", o);

		// sendSMS
		Twilio.init("AC518ef94cb8e9a610d419a09decf7f7eb", "6443adbe56bd6dbec6acbb8b1f330edc");
		Message.creator(new PhoneNumber("+84967475094"), new PhoneNumber("+19289188190"), "Mã Otp của bạn là: " + otp)
				.create();

	}

	@GetMapping("/register/resendOtp")
	public String resendOtp(HttpSession session, Model model) {
		Users u = (Users) session.getAttribute("u");
		if (u != null) {
			sendOtp(u, session);
			return "confirmOtp";
		} else {
			model.addAttribute("u", new Users());
			return "register";
		}
	}

}
