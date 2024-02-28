package olala.com.repository;

import java.util.List;

import olala.com.entities.Orders;
import olala.com.model.OrderInfo;
import olala.com.model.PageData;
import olala.com.model.StatDto;
import olala.com.model.StatInfo;

public interface CustomOrderRepository {
	PageData<Orders> findByOrderInfo(OrderInfo orderInfo);
	
	List<StatDto> findStatByStatInfo( StatInfo statInfo);
}
