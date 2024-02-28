package olala.com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import olala.com.entities.Attribute;
import olala.com.entities.Category;
import olala.com.entities.ProductLine;
import olala.com.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import olala.com.entities.Product;
import olala.com.entities.ProductAttribute;
import olala.com.model.AttributeDto;
import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.service.CategoryService;
import olala.com.service.ProductAttributeService;
import olala.com.service.ProductLineService;
import olala.com.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductLineService productLineService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductAttributeService productAttributeService;

	@Autowired
    AttributeService attributeService;

	/* =========================== FOR ADMIN ============================= */

	@GetMapping("/admin/productLine/product/view/{id}")
	public String showAll(Model model, @PathVariable("id") Long productLineId) {

		ProductLine productLine = productLineService.findById(productLineId);

		List<Product> products = productLine.getProducts();

		model.addAttribute("productLine", productLine);
		model.addAttribute("products", products);
		return "product/view_products";
	}

	// TODO add product + view luôn trực tiếp
	@GetMapping("/admin/productLine/product/add/{id}")
	public String showAddForm(Model model, @PathVariable("id") Long productLineId) {
		Product product = new Product();

		// get ProductLine
		ProductLine productLine = productLineService.findById(productLineId);
		model.addAttribute("productLine", productLine);
		product.setProductLineId(productLineId);

		// get List attributes from ProductLine
		List<Attribute> attributes = productLineService.findListAttributeById(productLineId);
		// set productAttribute
		List<ProductAttribute> productAttributes = new ArrayList<>();

		for (Attribute a : attributes) {
			ProductAttribute p = new ProductAttribute();
			p.setAttribute(a);
			productAttributes.add(p);
		}

		// set product
		product.setProductAttributes(productAttributes);
		// System.out.println(productAttributes);
		
		
		//List product 
//		List<Product> products = new ArrayList<>();
//		model.addAttribute("products",products);
		
		//
		model.addAttribute("product", product);
		return "product/add_product2";

	}

//	@PostMapping("/admin/product/add")
//	public String addProduct(Model model, @Valid @ModelAttribute("product") Product product, Errors errors) {
//
//		if (null != errors && errors.getErrorCount() > 0) {
//			return "product/add_product";
//		} else {
//			// set productLine
//			Long productLineId = product.getProductLineId();
//			ProductLine productLine = productLineService.findById(productLineId);
//			product.setProductLine(productLine);
//
//			// set Product for each ProductAttribute
//			List<ProductAttribute> productAttributes = product.getProductAttributes();
//
//			// product have Id ---> Save productAttribute
//			for (ProductAttribute p : productAttributes) {
//				p.setProduct(product);
//			}
//			
//			//set sold
//			product.setSold(0L);
//			
//			// save to db
//			productService.save(product);
//			return "redirect:/admin/productLine/product/view/" + productLine.getId();
//			// return "redirect:/admin/product/view";
//		}
//
//	}
//	

	@PostMapping("/admin/product/add")
	public String addProduct(Model model, @ModelAttribute("productLine") ProductLine productLine) {
		Long productLineId = productLine.getId();
		List<Product> products = productLine.getProducts();
		productLine = productLineService.findById(productLineId);
		List<Attribute> attributes = productLine.getAttributes();
		//
		List<ProductAttribute> productAttributes = new ArrayList<>();
		//set ProductLine
		for( int i=0 ; i< products.size() ; i++) {
			Product p = products.get(i);
			p.setProductLine(productLine);
			List<ProductAttribute> list = p.getProductAttributes();
			for( int j=0 ; j< list.size() ; j++ ) {
				list.get(j).setProduct(p);
				list.get(j).setAttribute(attributes.get(j));
			}
			p.setSold(0L);
			productService.save(p);
		}
		//System.out.println(products);
		//return "hello";
		return "redirect:/admin/productLine/product/view/" + productLine.getId();
		// return "redirect:/admin/product/view";
	}

	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		// get ProductLine
		Product product = productService.findById(id);
		ProductLine productLine = product.getProductLine();

		// delete
		productService.deleteById(id);
		return "redirect:/admin/productLine/product/view/" + productLine.getId();
	}

	@GetMapping("/admin/product/edit/{id}")
	public String showEditForm(Model model, @PathVariable("id") Long productId) {
		// get Product
		Product product = productService.findById(productId);
		// set ProductLineId
		product.setProductLineId(product.getProductLine().getId());

		model.addAttribute("product", product);
		return "product/edit_product";

	}

	@PostMapping("/admin/product/edit")
	public String editProduct(Model model, @Valid @ModelAttribute("product") Product product, Errors errors) {

		if (null != errors && errors.getErrorCount() > 0) {
			return "product/edit_product";
		} else {
			// set productLine
			Long productLineId = product.getProductLineId();
			ProductLine productLine = productLineService.findById(productLineId);
			product.setProductLine(productLine);

			List<ProductAttribute> productAttributes = product.getProductAttributes();
			// product have Id ---> Save productAttribute
			for (ProductAttribute p : productAttributes) {
				p.setProduct(product);
			}
			// save
			productService.save(product);
			return "redirect:/admin/productLine/product/view/" + productLineId;
		}
	}

	/* ====================FOR SHOPPING ============================= */
	@GetMapping("/visitor/product/view-by-parent-category")
	public String showProductByParentCategory(@RequestParam("parentCategoryId") Long parentCategoryId, Model model) {
		Category parentCategory = categoryService.findById(parentCategoryId);

		/* category */
		List<Category> categories = categoryService.findByParentCategory(parentCategory);
		model.addAttribute("categories", categories);

		/* parentCategory */
		model.addAttribute("parentCategoryName", parentCategory.getName());
		model.addAttribute("parentCategoryId", parentCategory.getId());

		/* show product */
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(0);
		pageInfo.setPageSize(5);

		PageData<ProductLineDto> pageData = productLineService.findProductLineDtoByCategoryId(parentCategoryId,
				pageInfo);

		model.addAttribute("pageData", pageData);

		System.out.println(pageData.getContent());

		return "ptitshop/categories";
	}

	@GetMapping("/visitor/product/view-by-product-line")
	public String showProduct(@RequestParam("productLineId") Long productLineId, Model model) {
		// get ProductLine
		ProductLine productLine = productLineService.findById(productLineId);

		// get ProductLineDto
		ProductLineDto productLineDto = new ProductLineDto(productLine);
		// set List attribute
		List<AttributeDto> attributeDtos = new ArrayList<>();
		for (Attribute a : productLine.getAttributes()) {
			attributeDtos.add(attributeService.getAttributeDto(a));
		}
		productLineDto.setAttributeDtos(attributeDtos);
		model.addAttribute("productLineDto", productLineDto);
		return "ptitshop/single";
	}
}
