package olala.com.service;

import java.util.Set;

import olala.com.entities.ProductAttribute;

public interface ProductAttributeService {
	public ProductAttribute save(ProductAttribute productAttribute);
	Set<String> findValuesByAttributeId( Long attributeId);
}
