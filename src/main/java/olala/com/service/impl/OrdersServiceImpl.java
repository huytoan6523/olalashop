package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.Orders;
import olala.com.model.OrderInfo;
import olala.com.model.PageData;
import olala.com.model.StatDto;
import olala.com.model.StatInfo;
import olala.com.repository.OrdersRepository;
import olala.com.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	OrdersRepository ordersRepository;

	@Override
	public Orders save(Orders orders) {
		return ordersRepository.save(orders);
	}

	@Override
	public Orders findById(Long id) {
		Optional<Orders> ordersOptional = ordersRepository.findById(id);
		if (ordersOptional.isPresent()) {
			return ordersOptional.get();
		}
		return null;
	}

	@Override
	public Orders findByVnpTxnRef(String vnpTxnRef) {
		return ordersRepository.findByVnpTxnRef(vnpTxnRef);
	}

	@Override
	public PageData<Orders> findByOrderInfo(OrderInfo orderInfo) {
		return ordersRepository.findByOrderInfo(orderInfo);
	}

	@Override
	public List<StatDto> findStatByStatInfo(StatInfo statInfo) {
		return ordersRepository.findStatByStatInfo(statInfo);
	}

}
