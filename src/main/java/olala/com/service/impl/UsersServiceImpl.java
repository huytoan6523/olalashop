package olala.com.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import olala.com.auth.CustomUserDetail;
import olala.com.constant.Constant;
import olala.com.entities.Users;
import olala.com.model.PageInfo;
import olala.com.repository.UsersRepository;
import olala.com.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Users getByPhoneNumber(String phoneNumber) {
		return usersRepository.getByPhoneNumber(phoneNumber);
	}

	@Override
	public Users getCurrentUserLogged() {
		CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return customUserDetail.getUsers();

	}

	public Users save(Users users) {
		users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
		users.setIsActive(true);
		return usersRepository.save(users);
	}

	@Override
	public Users saveIgnorePassword(Users users) {
		return usersRepository.save(users);
	}

	@Override
	public void deleteById(Long id) {
		usersRepository.deleteById(id);

	}

	@Override
	public Users findById(Long id) {
		Optional<Users> otp = usersRepository.findById(id);
		if (otp.isPresent()) {
			return otp.get();
		}
		return null;
	}

	/* find By PageInfo */
	@Override
	public Page<Users> findByPageInfo(PageInfo pageInfo) {
		String phoneNumber = pageInfo.getKeyword().trim();
		Integer pageSize = pageInfo.getPageSize();
		Integer pageNumber = pageInfo.getPageNumber();

		Integer sortType = pageInfo.getSortType();
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("phoneNumber").ascending());

		if (sortType == Constant.SortType.DESCENDING) {
			pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("phoneNumber").descending());
		}

		Page<Users> page = usersRepository.findByPhoneNumberContains(phoneNumber, pageRequest);

		return page;
	}

}
