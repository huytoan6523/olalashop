package olala.com.repository;

import olala.com.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReopsioty extends JpaRepository<Product, Long>, CustomProductRepository {

}
