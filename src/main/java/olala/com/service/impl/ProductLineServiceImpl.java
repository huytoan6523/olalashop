package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import olala.com.constant.Constant;
import olala.com.entities.Attribute;
import olala.com.entities.ProductLine;
import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.model.ProductLineInfo;
import olala.com.repository.ProductLineRepository;
import olala.com.service.ProductLineService;

@Service
public class ProductLineServiceImpl implements ProductLineService {
	@Autowired
	ProductLineRepository productLineRepository;

	/*
	 * Save Set status to is active
	 */
	@Override
	public ProductLine save(ProductLine product) {
		// check if record have already exist
		String name = product.getName();
		if (existsByName(name)) {
			// set Id to existed Id
			product.setId(findByName(name).getId());
		}
		// status to active
		return productLineRepository.save(product);
	}

	/* existByName */
	@Override
	public Boolean existsByName(String name) {
		return productLineRepository.existsByName(name);
	}

	/* find By PageInfo */
	@Override
	public Page<ProductLine> findByPageInfo(PageInfo pageInfo) {
		String name = pageInfo.getKeyword().trim();
		Integer pageSize = pageInfo.getPageSize();
		Integer pageNumber = pageInfo.getPageNumber();

		Integer sortType = pageInfo.getSortType();
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

		if (sortType == Constant.SortType.DESCENDING) {
			pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
		}

		Page<ProductLine> page = productLineRepository.findByNameContains(name, pageRequest);

		return page;
	}

	/*
	 * deleteById set status to not active
	 */
	@Override
	public void deleteById(Long id) {
		productLineRepository.deleteById(id);
	}

	/* findById */
	@Override
	public ProductLine findById(Long id) {
		Optional<ProductLine> product = productLineRepository.findById(id);
		if (product.isPresent()) {
			return product.get();
		}
		return null;
	}

	/* findByName */
	@Override
	public ProductLine findByName(String name) {
		Optional<ProductLine> product = productLineRepository.findByName(name);
		if (product.isPresent()) {
			return product.get();
		}
		return null;
	}
	
	
	/* findListAttributeById */
	@Override
	public List<Attribute> findListAttributeById(Long id) {
		return findById(id).getAttributes();
	}

	@Override
	public PageData<ProductLineDto> findProductLineDtoByCategoryId(Long categoryId, PageInfo pageInfo) {
		return productLineRepository.findProductLineDtoByCategoryId(categoryId, pageInfo);
	}

	@Override
	public ProductLineDto findProductLineDtoById(Long productLineId) {
		return productLineRepository.findProductLineDtoById(productLineId);
	}

	@Override
	public PageData<ProductLineDto> findProductLineDtoProductLineInfo(ProductLineInfo productLineInfo) {
		return productLineRepository.findProductLineDtoProductLineInfo(productLineInfo);
	}
	
	@Override
	public PageData<ProductLineDto> findBestSellerProductLineDto(PageInfo pageInfo){
		return productLineRepository.findBestSellerProductLineDto(pageInfo);
	}

}
