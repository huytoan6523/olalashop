package olala.com.repository;

import olala.com.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long>, CustomOrderRepository{
	public Orders  findByVnpTxnRef(String vnpTxnRef);
}
