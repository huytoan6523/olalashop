package olala.com.api.admin;

import olala.com.entities.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.model.PageInfo;
import olala.com.service.BrandService;

@RestController
@RequestMapping("admin/brand")
public class BrandRestController {
	@Autowired
	public BrandService brandService;

	@PostMapping("view")
	public Page<Brand> getBrandByPageInfo(@ModelAttribute("pageInfo") PageInfo pageInfo) {
		return brandService.findByPageInfo(pageInfo);
	}

}
