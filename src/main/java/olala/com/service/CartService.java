package olala.com.service;

import olala.com.entities.Cart;
import olala.com.entities.ProductItem;

public interface CartService {
	public Cart save(Cart cart);

	public Boolean addProductItem(Cart cart, ProductItem productItem);
	
	public Boolean isUpdateProductIteam(Cart cart, ProductItem productItem);

	public Cart findById(Long id);
}
