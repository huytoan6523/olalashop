package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.DeliveryAddress;
import olala.com.entities.Users;
import olala.com.repository.DeliveryAddressRepository;
import olala.com.service.DeliveryAddressService;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService{
	
	@Autowired
	public DeliveryAddressRepository deliveryAddressRepository;
	
	@Override
	public DeliveryAddress save(DeliveryAddress DeliveryAddress) {
		return deliveryAddressRepository.save(DeliveryAddress);
	}

	@Override
	public DeliveryAddress findById(Long id) {
		Optional<DeliveryAddress> deliveryAddress = deliveryAddressRepository.findById(id);
		if (deliveryAddress.isPresent()) {
			return deliveryAddress.get();
		}
		return null;
	}

	@Override
	public List<DeliveryAddress> findByUsers(Users users) {
		return deliveryAddressRepository.findByUsers(users);
	}

}
