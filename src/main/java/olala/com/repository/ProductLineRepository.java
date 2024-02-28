package olala.com.repository;

import java.util.Optional;

import olala.com.entities.ProductLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLineRepository extends JpaRepository<ProductLine, Long>, CustomProductLineRepository {
	public Boolean existsByName(String name);
	
	public Page<ProductLine> findByNameContains(String name, Pageable pageable);

	public Optional<ProductLine> findByName(String name);
}
