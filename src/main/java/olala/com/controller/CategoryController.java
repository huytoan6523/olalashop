package olala.com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import olala.com.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import olala.com.model.PageInfo;
import olala.com.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("/view")
	public String showAll(Model model) {
		model.addAttribute("pageInfo", new PageInfo());
		return "category/view_categorys";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		// get List parent Category
//		List<Category> parentCategories = categoryService.findByParentCategory(null);
//		model.addAttribute("parentCategories", parentCategories);

		Category category = new Category();
		List<Category> listCategory = new ArrayList<>();
		Category c = new Category();
		listCategory.add(c);
		category.setCategories(listCategory);

		model.addAttribute("category", category);
		return "category/add_category2";

	}

	@PostMapping("/add")
	public String addCategory(Model model, @Valid @ModelAttribute("category") Category category, Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			return "category/add_category2";
		} else {
			String categoryName = category.getName().trim();

			// check if category name had already exits
			if (categoryService.existsByName(categoryName)) {
				model.addAttribute("msg", categoryName + " has already existed!!");
				return "category/add_category2";
			}

			// trim
			category.setName(categoryName);
			categoryService.save(category);

			// save all category child
			List<Category> list = category.getCategories();
			for (Category c : list) {
				c.setParentCategory(category);
				categoryService.save(c);
			}

			return "redirect:/admin/category/view";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Long id) {
		categoryService.deleteById(id);

		return "redirect:/admin/category/view";
	}

	@GetMapping("/edit/{id}")
	public String showEditCategory(@PathVariable("id") Long id, Model model) {
		Category category = categoryService.findById(id);

		model.addAttribute("category", category);
		return "category/edit_category";
	}

	@PostMapping("/edit")
	public String editCategory(Model model, @Valid @ModelAttribute("category") Category category, Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			return "category/edit_category";
		} else {
			String categoryName = category.getName().trim();
			Category currentCategory = categoryService.findById(category.getId());
			// check if category name had already exits
			if (categoryService.existsByName(categoryName) && !(categoryName.equals(currentCategory.getName()))) {
				model.addAttribute("msg", categoryName + " has already existed!!");
				return "category/edit_category";
			}
			
			List<Category> list = category.getCategories();
			// trim
			category.setName(categoryName);
			categoryService.save(category);

//			// save all category child
			for (Category c : list) {
				c.setParentCategory(category);
				categoryService.save(c);
			}
			return "redirect:/admin/category/view";
		}
	}
}
