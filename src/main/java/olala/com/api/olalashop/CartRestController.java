package olala.com.api.olalashop;

import java.util.List;

import javax.servlet.http.HttpSession;

import olala.com.entities.Cart;
import olala.com.entities.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import olala.com.service.CartService;
import olala.com.service.ProductItemService;
import olala.com.service.UsersService;

@RestController
@RequestMapping("/user/cart")
public class CartRestController {
	@Autowired
	UsersService usersService;

	@Autowired
	CartService cartService;

	@Autowired
	ProductItemService productItemService;

	@PostMapping("/")
	public List<ProductItem> getCart() {
		Long cartId = usersService.getCurrentUserLogged().getCart().getId();
		// get newest cart
		Cart cart = cartService.findById(cartId);
		return cart.getProductItems();
	}
	
	@PostMapping("/update-product-item")
	public String updateProductItem( @ModelAttribute("productItem") ProductItem productItem, HttpSession session) {
		Long id = productItem.getId();
		ProductItem currentProductItem = productItemService.findById(id);
		currentProductItem.setQuantity(productItem.getQuantity());
		productItemService.save(currentProductItem);
		return "success";
	}
}
