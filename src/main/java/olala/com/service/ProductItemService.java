package olala.com.service;

import java.util.List;

import olala.com.entities.ProductItem;

public interface ProductItemService {
	ProductItem save(ProductItem productItem);

	public void deleteById(Long id);

	public ProductItem findById(Long id);

	public List<ProductItem> findAll();
}
