package olala.com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import olala.com.entities.Attribute;
import olala.com.entities.Brand;
import olala.com.entities.ProductLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import olala.com.entities.Category;
import olala.com.model.PageInfo;
import olala.com.service.BrandService;
import olala.com.service.CategoryService;
import olala.com.service.ProductLineService;
import olala.com.utils.FileUploadUtil;

@Controller
@RequestMapping("/admin/productLine")
public class ProductLineController {

	@Autowired
	ProductLineService productLineService;

	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;

	@GetMapping("/view")
	public String showAll(Model model) {
		model.addAttribute("pageInfo", new PageInfo());
		return "productLine/view_productLines";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		// Get list Brand
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("listBrand", listBrand);
		// Get list Category (not parent Category)
		List<Category> listCategory = categoryService.findReadyCategory();
		model.addAttribute("listCategory", listCategory);

		// atribute
		List<Attribute> attributes = new ArrayList<>();
		Attribute a = new Attribute();
		attributes.add(a);
		attributes.add(a);
		//
		ProductLine productLine = new ProductLine();
		productLine.setAttributes(attributes);

		model.addAttribute("productLine", productLine);
		return "productLine/add_productLine";

	}

	// TODO handle plus + minus + validate record
	@PostMapping("/add")
	public String addProductLine(Model model, @Valid @ModelAttribute("productLine") ProductLine productLine,
			Errors errors) {
		if (null != errors && errors.getErrorCount() > 0) {

			// Get list Brand
			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("listBrand", listBrand);
			// Get list Category (not parent Category)
			List<Category> listCategory = categoryService.findReadyCategory();
			model.addAttribute("listCategory", listCategory);

			return "productLine/add_productLine";
		} else {
			String productLineName = productLine.getName().trim();
			// check if productLine name had already exits
			if (productLineService.existsByName(productLineName)) {
				// add msg
				model.addAttribute("msg", productLineName + " has already existed!!");

				// Get list Brand
				List<Brand> listBrand = brandService.findAll();
				model.addAttribute("listBrand", listBrand);
				// Get list Category (not parent Category)
				List<Category> listCategory = categoryService.findReadyCategory();
				model.addAttribute("listCategory", listCategory);
				return "productLine/add_productLine";
			}

			// Save productLine for attribute
			for (Attribute a : productLine.getAttributes()) {
				a.setProductLine(productLine);
			}

			// get Brand
			Long brandId = productLine.getBrandId();
			Brand brand = brandService.findById(brandId);
			productLine.setBrand(brand);
			// getCategory
			Long categoryId = productLine.getCategoryId();
			Category category = categoryService.findById(categoryId);
			productLine.setCategory(category);

			// save productLine --> to get Id --> to Save File
			productLineService.save(productLine);
			// get Id
			productLine = productLineService.findByName(productLineName);

			// handle image
			MultipartFile image1 = productLine.getImgFile1();

			String imageName1 = StringUtils.cleanPath(image1.getOriginalFilename());

			MultipartFile image2 = productLine.getImgFile2();
			String imageName2 = StringUtils.cleanPath(image2.getOriginalFilename());

			MultipartFile image3 = productLine.getImgFile3();
			String imageName3 = StringUtils.cleanPath(image3.getOriginalFilename());

			String uploadDir = "images/productline" + "/" + productLine.getId();

			String imgUrl1 = "/" + uploadDir + "/" + imageName1;
			String imgUrl2 = "/" + uploadDir + "/" + imageName2;
			String imgUrl3 = "/" + uploadDir + "/" + imageName3;

			// System.out.println(imgUrl1);
			productLine.setImgUrl1(imgUrl1);
			// System.out.println(imgUrl2);
			productLine.setImgUrl2(imgUrl2);
			// System.out.println(imgUrl3);
			productLine.setImgUrl3(imgUrl3);

			// save to File
			try {
				FileUploadUtil.saveFile(uploadDir, imageName1, image1);
				FileUploadUtil.saveFile(uploadDir, imageName2, image2);
				FileUploadUtil.saveFile(uploadDir, imageName3, image3);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// save productLine
			productLineService.save(productLine);
			return "redirect:/admin/productLine/view";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteProductLine(@PathVariable("id") Long id) {
		productLineService.deleteById(id);

		return "redirect:/admin/productLine/view";
	}

	@GetMapping("/edit/{id}")
	public String showEditProductLine(@PathVariable("id") Long id, Model model) {
		// Get list Brand
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("listBrand", listBrand);
		// Get list Category (not parent Category)
		List<Category> listCategory = categoryService.findReadyCategory();
		model.addAttribute("listCategory", listCategory);

		ProductLine productLine = productLineService.findById(id);

		// set BrandId
		productLine.setBrandId(productLine.getBrand().getId());
		// set CategoryId
		productLine.setCategoryId(productLine.getCategory().getId());

		model.addAttribute("productLine", productLine);
		return "productLine/edit_productLine";
	}

	// TODO handle Edit ProductLine
	@PostMapping("/edit")
	public String editProductLine(Model model, @Valid @ModelAttribute("productLine") ProductLine productLine,
			Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			// Get list Brand
			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("listBrand", listBrand);
			// Get list Category (not parent Category)
			List<Category> listCategory = categoryService.findReadyCategory();
			model.addAttribute("listCategory", listCategory);

			return "productLine/edit_productLine";
		} else {
			String productLineName = productLine.getName().trim();
			// check if productLine name had already exits
			ProductLine currentProductLine = productLineService.findById(productLine.getId());

			if (productLineService.existsByName(productLineName)
					&& !(productLineName.equals(currentProductLine.getName()))) {
				// add msg
				model.addAttribute("msg", productLineName + " has already existed!!");

				// Get list Brand
				List<Brand> listBrand = brandService.findAll();
				model.addAttribute("listBrand", listBrand);
				// Get list Category (not parent Category)
				List<Category> listCategory = categoryService.findReadyCategory();
				model.addAttribute("listCategory", listCategory);
				return "productLine/edit_productLine";
			}

			// Save productLine for attribute
			for (Attribute a : productLine.getAttributes()) {
				a.setProductLine(productLine);
			}

			// get Brand
			Long brandId = productLine.getBrandId();
			Brand brand = brandService.findById(brandId);
			productLine.setBrand(brand);
			// getCategory
			Long categoryId = productLine.getCategoryId();
			Category category = categoryService.findById(categoryId);
			productLine.setCategory(category);

			// handle image
			String uploadDir = "images/productline" + "/" + productLine.getId();

			MultipartFile image1 = productLine.getImgFile1();

			String imageName1 = StringUtils.cleanPath(image1.getOriginalFilename().trim());
			if (!imageName1.equals("")) {
				String imgUrl1 = "/" + uploadDir + "/" + imageName1;
				productLine.setImgUrl1(imgUrl1);
				try {
					FileUploadUtil.saveFile(uploadDir, imageName1, image1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				productLine.setImgUrl1(currentProductLine.getImgUrl1());
			}

			MultipartFile image2 = productLine.getImgFile2();
			String imageName2 = StringUtils.cleanPath(image2.getOriginalFilename().trim());
			if (!imageName2.equals("")) {
				String imgUrl2 = "/" + uploadDir + "/" + imageName2;
				productLine.setImgUrl2(imgUrl2);
				try {
					FileUploadUtil.saveFile(uploadDir, imageName2, image2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				productLine.setImgUrl2(currentProductLine.getImgUrl2());
			}

			MultipartFile image3 = productLine.getImgFile3();
			String imageName3 = StringUtils.cleanPath(image3.getOriginalFilename().trim());
			if (!imageName3.equals("")) {
				String imgUrl3 = "/" + uploadDir + "/" + imageName3;
				productLine.setImgUrl3(imgUrl3);
				try {
					FileUploadUtil.saveFile(uploadDir, imageName3, image3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				productLine.setImgUrl3(currentProductLine.getImgUrl3());
			}

			// save productLine
			productLineService.save(productLine);
			return "redirect:/admin/productLine/view";
		}
	}
}
