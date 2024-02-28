package olala.com.service;

import java.util.List;

import olala.com.entities.Brand;
import olala.com.model.PageInfo;
import org.springframework.data.domain.Page;

public interface BrandService {
	public Brand save(Brand brand);

	public Boolean existsByName(String name);

	public Page<Brand> findByPageInfo(PageInfo pageInfo);

	public void deleteById(Long id);

	public Brand findById(Long id);

	public Brand findByName(String name);
	
	public List<Brand> findAll();
}
