package olala.com.service;

import olala.com.entities.Users;
import olala.com.model.PageInfo;
import org.springframework.data.domain.Page;

public interface UsersService {
	Users getByPhoneNumber(String phoneNumber);
	
	Users getCurrentUserLogged();
	
    Users save(Users users);
    Users saveIgnorePassword(Users users);

    void deleteById(Long id);

	Users findById(Long id);

	Page<Users> findByPageInfo(PageInfo pageInfo);
}
