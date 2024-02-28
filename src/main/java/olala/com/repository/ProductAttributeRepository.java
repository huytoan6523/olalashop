package olala.com.repository;

import java.util.Set;

import olala.com.entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long>{
	
	@Query(value = "SELECT pa.value "
	+ " FROM 		ProductAttribute pa "
	+ " WHERE 		pa.attribute.id = ?1")
	Set<String> findValuesByAttributeId( Long attributeId);
}
