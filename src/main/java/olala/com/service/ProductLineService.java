package olala.com.service;

import java.util.List;

import olala.com.entities.Attribute;
import olala.com.entities.ProductLine;
import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.model.ProductLineInfo;
import org.springframework.data.domain.Page;

public interface ProductLineService {
	public ProductLine save(ProductLine product);

	public Boolean existsByName(String name);
	
	public Page<ProductLine> findByPageInfo(PageInfo pageInfo);

	public void deleteById(Long id);

	public ProductLine findById(Long id);

	public ProductLine findByName(String name);
	
	public List<Attribute> findListAttributeById(Long id);
	
	public PageData<ProductLineDto> findProductLineDtoByCategoryId(Long categoryId, PageInfo pageInfo);
	
	public ProductLineDto findProductLineDtoById(Long productLineId);
	
	public PageData<ProductLineDto> findProductLineDtoProductLineInfo(ProductLineInfo productLineInfo);
	
	PageData<ProductLineDto> findBestSellerProductLineDto(PageInfo pageInfo);
}
