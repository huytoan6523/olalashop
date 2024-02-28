package olala.com.repository;

import java.util.List;

import olala.com.entities.DeliveryAddress;
import olala.com.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long>{
	List<DeliveryAddress> findByUsers(Users users);
}
