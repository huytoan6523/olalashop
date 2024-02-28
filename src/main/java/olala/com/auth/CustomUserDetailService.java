package olala.com.auth;

import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import olala.com.service.UsersService;


@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UsersService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userService.getByPhoneNumber(username);
		return new CustomUserDetail(user);
	}

}
