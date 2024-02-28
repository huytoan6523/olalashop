package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.Product;
import olala.com.entities.ProductLine;
import olala.com.model.ProductInfo;
import olala.com.model.ProductLineDto;
import olala.com.repository.ProductReopsioty;
import olala.com.service.ProductLineService;
import olala.com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductReopsioty productRepository;
	
	@Autowired
	ProductLineService productLineService;

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	/* findById */
	@Override
	public Product findById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return product.get();
		}
		return null;
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	/* delete By Id */
	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public ProductInfo findByProductInfo(ProductInfo productInfo) {
		
		//get ProductLine
		Long productLineId = productInfo.getProductLineId();
		ProductLine  productLine = productLineService.findById(productLineId);
		
		//set Min and Max Price
		ProductLineDto productLineDto = productLineService.findProductLineDtoById(productLineId);
		productInfo.setMaxPrice(productLineDto.getMaxPrice());
		productInfo.setDiscount(productLineDto.getDiscount());
		productInfo.setMinPrice(productLineDto.getMinPrice());
		productInfo.setTotalStock(productLineDto.getTotalStock());
		// => get List Product
		List<Product> products = productLine.getProducts();
		System.out.println(products);
		Product product = new Product();
		
		//search By List Values
		for( Product p: products ) {
			if( p.getValues().equals(productInfo.getValues()) ) {
				product = p;
				break;
			}	
		}
		if( product.getId() != null ) {
			productInfo.setId(product.getId());
			productInfo.setPrice(product.getPrice());
			productInfo.setSold(product.getSold());
			productInfo.setStock(product.getStock());
		}
		return productInfo;
	}

}
