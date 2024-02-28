package olala.com.controller;

import javax.servlet.http.HttpSession;

import olala.com.entities.Cart;
import olala.com.entities.Product;
import olala.com.entities.ProductItem;
import olala.com.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import olala.com.service.CartService;
import olala.com.service.ProductItemService;
import olala.com.service.ProductService;
import olala.com.service.UsersService;

@Controller
@RequestMapping("/user/cart")
public class CartController {
	@Autowired
	UsersService usersService;

	@Autowired
	ProductService productService;

	@Autowired
	CartService cartService;

	@Autowired
	ProductItemService productItemService;

	@GetMapping("/")
	public String showCart() {
		return "ptitshop/cart";
	}

	@GetMapping("/add-product-item")
	public String addProduct(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity,
			Model model, HttpSession session) {
		//get cart item quantity
		getCartItemQuantity(session);
		
		// get Cart from User ( 1 User - 1 cart )
		Long cartId = usersService.getCurrentUserLogged().getCart().getId();

		// get newest cart
		Cart cart = cartService.findById(cartId);

		// get product by Id
		Product product = productService.findById(productId);

		// create productItem from product and quantity
		ProductItem productItem = new ProductItem();
		productItem.setCart(cart);
		productItem.setProduct(product);
		productItem.setQuantity(quantity);
		
		// update cart
		if ( !cartService.isUpdateProductIteam(cart, productItem)) {
			session.setAttribute("cartItem", (Integer)session.getAttribute("cartItem")+1);
		}
		// add to card
		cartService.addProductItem(cart, productItem);
		return "ptitshop/cart";
	}

	@GetMapping("/delete-product-item")
	public String deleteProducItem(@RequestParam("id") Long productItemId, HttpSession session) {
		productItemService.deleteById(productItemId);

		// update user
		Users currentUser =  usersService.getCurrentUserLogged();
		Users u = usersService.findById(currentUser.getId());
		Integer cartItemQuantity = cartService.findById(u.getCart().getId()).getProductItems().size();
		session.setAttribute("cartItem", cartItemQuantity);
		return "ptitshop/cart";
	}
	
	public void getCartItemQuantity( HttpSession session ) {
		Users currentUser =  usersService.getCurrentUserLogged();
		Users u = usersService.findById(currentUser.getId());
		Integer cartItemQuantity = cartService.findById(u.getCart().getId()).getProductItems().size();
		session.setAttribute("cartItem", cartItemQuantity);
	}
}
