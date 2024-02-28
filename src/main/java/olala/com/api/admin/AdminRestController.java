package olala.com.api.admin;

import java.util.List;

import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.PageInfo;
import olala.com.model.StatDto;
import olala.com.model.StatInfo;
import olala.com.service.OrdersService;
import olala.com.service.UsersService;

@RestController
@RequestMapping("admin")
public class AdminRestController {
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	OrdersService ordersService;
	
	@PostMapping("/employee/view")
	public Page<Users> getUsersByPageInfo(@ModelAttribute("pageInfo") PageInfo pageInfo) {
		return usersService.findByPageInfo(pageInfo);
	}
	
	@PostMapping("/dashboard")
	public List<StatDto> getStat(@ModelAttribute("statInfo") StatInfo statInfo) {
		System.out.println(statInfo);
		return ordersService.findStatByStatInfo(statInfo);
	}

}
