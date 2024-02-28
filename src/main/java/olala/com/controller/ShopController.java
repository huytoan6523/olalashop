package olala.com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import olala.com.entities.Attribute;
import olala.com.entities.Category;
import olala.com.entities.ProductLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import olala.com.model.PageData;
import olala.com.model.ProductInfo;
import olala.com.service.CategoryService;
import olala.com.service.ProductAttributeService;
import olala.com.service.ProductLineService;
import olala.com.service.ProductService;

@Controller
public class ShopController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductAttributeService productAttributeService;
	
	@Autowired
	ProductLineService productLineService;
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/")
	public String showHomePage(Model model) {
		List<Category> parentCategories = categoryService.findByParentCategory(null);
		model.addAttribute("parentCategories", parentCategories);	
		model.addAttribute("pageData",new  PageData<>() );
		return "ptitshop/homepage2";
	}
	
	@PostMapping("/testt")
	public String test(@RequestParam Map<String, String> reqParams, Model model) {
		// get All parameter and value 
		
		//getProductLineId
		Long productLineId = Long.parseLong(reqParams.get("productLineId"));
		
		ProductLine productLine = productLineService.findById(productLineId);
		
		//get Attribute
		List<Attribute> attributes = productLine.getAttributes();
		
		//get ProductLine Infor
		//+ productLineId
		// ==> Attribute ==> Values of each attribute
		
		/* ProductInfo 
		 *   productLineId
		 *   List<String> value
		 */
		
		System.out.println(reqParams.toString());
		List<String> values = new ArrayList<>();
		for( Attribute a: attributes ) {
			values.add(reqParams.get(a.getId()+""));
		}
		//Cretate productInfo
		ProductInfo productInfo = new ProductInfo();
		productInfo.setProductLineId(productLineId);
		productInfo.setValues(values);
		
		//Get information from db
		productInfo = productService.findByProductInfo(productInfo);
		model.addAttribute("productInfo", productInfo);
		return "hello";
	}
	
	
	@GetMapping("/user/create-order-successful")
	public String showCreateOrderSuccessful() {
		return "ptitshop/createOrderSucessFul";
	}
	
	@GetMapping("/testshop")
	public String testsop() {
//		ProductLineDto productLineDto = productLineService.findProductLineDtoById(2L);
//		System.out.println(productLineDto);
		
		return "hello";
	}
}
