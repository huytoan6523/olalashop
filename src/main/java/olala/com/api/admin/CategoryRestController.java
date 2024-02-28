package olala.com.api.admin;

import olala.com.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.PageInfo;
import olala.com.service.CategoryService;

@RestController
@RequestMapping("admin/category")
public class CategoryRestController {
	@Autowired
	public CategoryService categoryService;

	@PostMapping("view")
	public Page<Category> getCategoryByPageInfo(@ModelAttribute("pageInfo") PageInfo pageInfo) {
		return categoryService.findByPageInfo(pageInfo);
	}

}
