package olala.com.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import olala.com.constant.Constant;
import olala.com.entities.Attribute;
import olala.com.model.AttributeDto;
import olala.com.model.PageInfo;
import olala.com.repository.AttributeRepository;
import olala.com.repository.ProductAttributeRepository;
import olala.com.service.AttributeService;

@Service
public class AttributeServiceImpl implements AttributeService {
	@Autowired
	AttributeRepository attributeRepository;
	
	@Autowired
	ProductAttributeRepository productAttributeRepository;

	/*
	 * Save Set status to is active
	 */
	@Override
	public Attribute save(Attribute attribute) {
		// check if record have already exist
		String name = attribute.getName();
		if (existsByName(name)) {
			// set Id to existed Id
			attribute.setId(findByName(name).getId());
		}
		// status to active
		return attributeRepository.save(attribute);
	}

	/* existByName */
	@Override
	public Boolean existsByName(String name) {
		return attributeRepository.existsByName(name);
	}

	/* find By PageInfo */
	@Override
	public Page<Attribute> findByPageInfo(PageInfo pageInfo) {
		String name = pageInfo.getKeyword().trim();
		Integer pageSize = pageInfo.getPageSize();
		Integer pageNumber = pageInfo.getPageNumber();

		Integer sortType = pageInfo.getSortType();
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

		if (sortType == Constant.SortType.DESCENDING) {
			pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
		}

		Page<Attribute> page = attributeRepository.findByNameContains(name, pageRequest);

		return page;
	}

	/*
	 * deleteById set status to not active
	 */
	@Override
	public void deleteById(Long id) {
		attributeRepository.deleteById(id);
	}

	/* findById */
	@Override
	public Attribute findById(Long id) {
		Optional<Attribute> attribute = attributeRepository.findById(id);
		if (attribute.isPresent()) {
			return attribute.get();
		}
		return null;
	}

	/* findByName */
	@Override
	public Attribute findByName(String name) {
		Optional<Attribute> attribute = attributeRepository.findByName(name);
		if (attribute.isPresent()) {
			return attribute.get();
		}
		return null;
	}

	
	@Override
	public AttributeDto getAttributeDto(Attribute attribute) {
		AttributeDto dto = new AttributeDto();
		dto.setId(attribute.getId());
		dto.setName(attribute.getName());
		Set<String> values = productAttributeRepository.findValuesByAttributeId(attribute.getId());
		dto.setValues(values);
		return dto;
	}
}
