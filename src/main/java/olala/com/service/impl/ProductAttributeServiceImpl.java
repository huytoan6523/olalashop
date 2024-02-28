package olala.com.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.ProductAttribute;
import olala.com.repository.ProductAttributeRepository;
import olala.com.service.ProductAttributeService;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService{
	@Autowired
	ProductAttributeRepository productAttributeRepository;
	
	@Override
	public ProductAttribute save(ProductAttribute productAttribute) {
	
		return productAttributeRepository.save(productAttribute);
	}

	@Override
	public Set<String> findValuesByAttributeId(Long attributeId) {
		return productAttributeRepository.findValuesByAttributeId(attributeId);
	}
	
}
