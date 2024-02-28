package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.ProductItem;
import olala.com.repository.ProductItemRepository;
import olala.com.service.ProductItemService;

@Service
public class ProductItemServiceImpl implements ProductItemService {

	@Autowired
	ProductItemRepository productItemRepository;

	@Override
	public ProductItem save(ProductItem productItem) {
		return productItemRepository.save(productItem);
	}

	
	@Override
	public void deleteById(Long id) {
		productItemRepository.deleteById(id);
	}

	/* findById */
	@Override
	public ProductItem findById(Long id) {
		Optional<ProductItem> productItem = productItemRepository.findById(id);
		if (productItem.isPresent()) {
			return productItem.get();
		}
		return null;
	}

	
	@Override
	public List<ProductItem> findAll() {
		return productItemRepository.findAll();
	}
}
