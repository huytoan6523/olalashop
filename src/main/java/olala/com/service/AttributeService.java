package olala.com.service;

import org.springframework.data.domain.Page;

import olala.com.entities.Attribute;
import olala.com.model.AttributeDto;
import olala.com.model.PageInfo;

public interface AttributeService {
	public Attribute save(Attribute attribute);

	public Boolean existsByName(String name);

	public Page<Attribute> findByPageInfo(PageInfo pageInfo);

	public void deleteById(Long id);

	public Attribute findById(Long id);
	
	public Attribute findByName(String name);
	
	public AttributeDto getAttributeDto( Attribute attribute);
}
