package olala.com.api.olalashop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.ProductInfo;
import olala.com.service.ProductService;

@RestController
public class PtitShopProductRestController {
	@Autowired
	ProductService productService;

	@PostMapping("/visitor/product/view")
	public ProductInfo getProductByProductInfo(@ModelAttribute("productInfo") ProductInfo productInfo) {
		return productService.findByProductInfo(productInfo);
	}

}
