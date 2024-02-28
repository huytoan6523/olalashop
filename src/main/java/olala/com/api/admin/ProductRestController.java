package olala.com.api.admin;

import olala.com.entities.ProductLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.PageInfo;
import olala.com.service.ProductLineService;

@RestController
@RequestMapping("admin/productLine")
public class ProductRestController {
	@Autowired
	public ProductLineService productLineService;

	@PostMapping("view")
	public Page<ProductLine> getProductByPageInfo(@ModelAttribute("pageInfo") PageInfo pageInfo) {
		return productLineService.findByPageInfo(pageInfo);
	}

}
