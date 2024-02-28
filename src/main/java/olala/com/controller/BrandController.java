package olala.com.controller;

import javax.validation.Valid;

import olala.com.entities.Brand;
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
import olala.com.service.BrandService;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {

	@Autowired
	BrandService brandService;

	@GetMapping("/view")
	public String showAll(Model model) {
		model.addAttribute("pageInfo", new PageInfo());
		return "brand/view_brands";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {

		model.addAttribute("brand", new Brand());
		return "brand/add_brand";

	}

	@PostMapping("/add")
	public String addBrand(Model model, @Valid @ModelAttribute("brand") Brand brand, Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			return "brand/add_brand";
		} else {
			String brandName = brand.getName().trim();

			// check if brand name had already exits
			if (brandService.existsByName(brandName)) {
				model.addAttribute("msg", brandName + " has already existed!!");
				return "brand/add_brand";
			}
			//trim
			brand.setName(brandName);
			brandService.save(brand);
			return "redirect:/admin/brand/view";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteBrand(@PathVariable("id") Long id) {
		brandService.deleteById(id);

		return "redirect:/admin/brand/view";
	}

	@GetMapping("/edit/{id}")
	public String showEditBrand(@PathVariable("id") Long id, Model model) {
		Brand brand = brandService.findById(id);
		model.addAttribute("brand", brand);
		return "brand/edit_brand";
	}

	@PostMapping("/edit")
	public String editBrand(Model model, @Valid @ModelAttribute("brand") Brand brand, Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			return "brand/edit_brand";
		} else {
			String brandName = brand.getName().trim();
			Brand currentBrand = brandService.findById(brand.getId());

			// check if brand name had already exits
			if (brandService.existsByName(brandName) && !(brandName.equals(currentBrand.getName())) ) {
				model.addAttribute("msg", brandName + " has already existed!!");
				return "brand/edit_brand";
			}
			//trim
			brand.setName(brandName);
			brandService.save(brand);
			return "redirect:/admin/brand/view";
		}
	}
}
