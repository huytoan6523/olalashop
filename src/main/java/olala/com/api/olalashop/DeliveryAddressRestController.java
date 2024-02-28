package olala.com.api.olalashop;

import java.util.List;

import olala.com.entities.DeliveryAddress;
import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import olala.com.service.DeliveryAddressService;
import olala.com.service.UsersService;

@RestController
@RequestMapping("/user/delivery-address")
public class DeliveryAddressRestController {
	
	@Autowired 
	UsersService usersService;
	
	@Autowired
	DeliveryAddressService deliveryAddressService;
	
	@GetMapping("/")
	public List<DeliveryAddress> findAllByUser() {
		//get newest status
		String phoneNumber  = usersService.getCurrentUserLogged().getPhoneNumber();		
		Users u = usersService.getByPhoneNumber(phoneNumber);
		return deliveryAddressService.findByUsers(u);
	}
	
	@GetMapping("/default")
	public DeliveryAddress getDefault(){
		String phoneNumber  = usersService.getCurrentUserLogged().getPhoneNumber();		
		Users u = usersService.getByPhoneNumber(phoneNumber);
		
		Long id = u.getDeliveryAddressDefaultId();
		return id != null ?  deliveryAddressService.findById(id) : null;
	}
	

	@GetMapping("/find-by-id")
	public DeliveryAddress findById(@RequestParam("id") Long id){
		return id != null ?  deliveryAddressService.findById(id) : null;
	}
	
	@PostMapping("/add")
	public String add(@ModelAttribute("deliveryAddress") DeliveryAddress deliveryAddress, @RequestParam("isDefault") String isDefault) {
		System.out.println("?????");
		System.out.println(deliveryAddress);
		//get newset User
		String phoneNumber  = usersService.getCurrentUserLogged().getPhoneNumber();		
		Users u = usersService.getByPhoneNumber(phoneNumber);
		
		//set User
		deliveryAddress.setUsers(u);
		
		//save to db
		deliveryAddress  = deliveryAddressService.save(deliveryAddress);
		
		Boolean is_default = Boolean.parseBoolean(isDefault);
		
		//set default delivery address
		if( is_default ) {
			u.setDeliveryAddressDefaultId(deliveryAddress.getId());
		}
		
		
		//save to db
		usersService.saveIgnorePassword(u);
		
		return "success";
	}
}
