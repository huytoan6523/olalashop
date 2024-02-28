package olala.com.repository;

import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.model.ProductLineInfo;

public interface CustomProductLineRepository {
	PageData<ProductLineDto> findProductLineDtoByCategoryId(Long categoryId, PageInfo pageInfo);

	PageData<ProductLineDto> findProductLineDtoProductLineInfo(ProductLineInfo productLineInfo);

	ProductLineDto findProductLineDtoById(Long productLineDtoId);

	PageData<ProductLineDto> findBestSellerProductLineDto(PageInfo pageInfo);
}
