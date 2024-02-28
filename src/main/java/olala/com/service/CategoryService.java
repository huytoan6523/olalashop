package olala.com.service;

import java.util.List;

import olala.com.entities.Category;
import olala.com.model.PageInfo;
import org.springframework.data.domain.Page;

public interface CategoryService {
	public Category save(Category category);

	public Boolean existsByName(String name);

	public Page<Category> findByPageInfo(PageInfo pageInfo);

	public void deleteById(Long id);

	public Category findById(Long id);

	public Category findByName(String name);
	
	public List<Category> findByParentCategory( Category parentCategory);
	
	public List<Category> findReadyCategory();
}
