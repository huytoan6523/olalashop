package olala.com.repository;

import java.util.Optional;

import olala.com.entities.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	public Boolean existsByName(String name);

	public Page<Brand> findByNameContains(String name, Pageable pageable);

	public Optional<Brand> findByName(String name);
}
