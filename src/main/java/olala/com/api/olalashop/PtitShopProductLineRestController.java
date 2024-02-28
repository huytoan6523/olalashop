package olala.com.api.olalashop;

import java.util.ArrayList;
import java.util.List;

import olala.com.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.model.ProductLineInfo;
import olala.com.service.CategoryService;
import olala.com.service.ProductLineService;

@RestController
public class PtitShopProductLineRestController {
	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductLineService productLineService;

	@PostMapping("/visitor/productLine/view")
	public PageData<ProductLineDto> getProductLineByPageInfo(
			@ModelAttribute("productLineInfo") ProductLineInfo productLineInfo) {
		// handle data before send
		// find all categoryIds, null --> all categoryIds that has parentCategoryId
		List<Long> categoryIds = productLineInfo.getCategoryIds();
		if (categoryIds == null) {
			Long parentCategoryId = productLineInfo.getParentCategoryId();
			Category parentCategory = categoryService.findById(parentCategoryId);
			List<Category> categories = categoryService.findByParentCategory(parentCategory);
			List<Long> ids = new ArrayList<>();
			for (Category c : categories) {
				ids.add(c.getId());
			}
			categoryIds = ids;
			productLineInfo.setCategoryIds(categoryIds);
		}

		PageData<ProductLineDto> pageData = productLineService.findProductLineDtoProductLineInfo(productLineInfo);
		return pageData;
	}

	@PostMapping("/visitor/productLine/bestseller")
	public PageData<ProductLineDto> getBestSellerProductLineByPageInfo(@ModelAttribute("pageInfo") PageInfo pageInfo) {
		PageData<ProductLineDto> pageData = productLineService.findBestSellerProductLineDto(pageInfo);
		return pageData;
	}
}
