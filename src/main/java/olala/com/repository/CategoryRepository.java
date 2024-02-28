package olala.com.repository;

import java.util.List;
import java.util.Optional;

import olala.com.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	public Boolean existsByName(String name);

	public Page<Category> findByNameContainsAndParentCategoryNull(String name, Pageable pageable);

	public Optional<Category> findByName(String name);
	
	public List<Category> findByParentCategory( Category parentCategory);
	
	/* PARENT CATEGORY NOT NULL */
	@Query(value = "SELECT 	c "
				 + "FROM 	Category c "
				 + "WHERE 	c.parentCategory IS NOT NULL ")
	public List<Category> findReadyCategory();
}
