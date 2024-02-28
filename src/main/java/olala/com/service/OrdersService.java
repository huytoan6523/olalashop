package olala.com.service;

import java.util.List;

import olala.com.entities.Orders;
import olala.com.model.OrderInfo;
import olala.com.model.PageData;
import olala.com.model.StatDto;
import olala.com.model.StatInfo;

public interface OrdersService {
	public Orders save(Orders orders );
	public Orders findById( Long id );
	public Orders findByVnpTxnRef(String vnpTxnRef);
	public PageData<Orders> findByOrderInfo(OrderInfo orderInfo);
	List<StatDto> findStatByStatInfo(StatInfo statInfo);
}
