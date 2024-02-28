package olala.com.repository;

import olala.com.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long>{
	@Modifying
	@Query("delete from ProductItem p where p.id = ?1")
	void deleteById(Long id);
}
