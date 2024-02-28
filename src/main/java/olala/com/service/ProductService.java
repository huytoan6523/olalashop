package olala.com.service;

import java.util.List;

import olala.com.entities.Product;
import olala.com.model.ProductInfo;

public interface ProductService {
	public Product save(Product product);

	public Product findById(Long id);

	public List<Product> findAll();
	
	public void deleteById( Long id);
	
	public ProductInfo findByProductInfo(ProductInfo productInfo);
}
