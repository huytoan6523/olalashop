package olala.com.repository;

import java.util.Optional;

import olala.com.entities.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
	public Boolean existsByName(String name);

	public Page<Attribute> findByNameContains(String name, Pageable pageable);

	public Optional<Attribute> findByName(String name);
}
