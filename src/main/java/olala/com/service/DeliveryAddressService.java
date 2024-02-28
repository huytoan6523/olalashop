package olala.com.service;

import java.util.List;

import olala.com.entities.DeliveryAddress;
import olala.com.entities.Users;

public interface DeliveryAddressService {
	public DeliveryAddress save(DeliveryAddress DeliveryAddress);
	public DeliveryAddress findById(Long id);
	public List<DeliveryAddress> findByUsers( Users users);
}
