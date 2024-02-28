package olala.com.repository;

import olala.com.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
	public Users getByPhoneNumber(String phoneNumber);

	public Page<Users> findByPhoneNumberContains(String phoneNumber, PageRequest pageRequest);
}
