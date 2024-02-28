package olala.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import olala.com.entities.Cart;
import olala.com.entities.ProductItem;
import olala.com.repository.CartRepository;
import olala.com.repository.ProductItemRepository;
import olala.com.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ProductItemRepository productItemRepository;
	
	@Override
	public Cart save(Cart cart) {
		return cartRepository.save(cart);
	}

	@Override
	public Boolean addProductItem(Cart cart, ProductItem productItem) {
			try {
				List<ProductItem> productItems = cart.getProductItems();
				
				// if cart has already product--> add quantity 
				int count = 0;
				for( ProductItem p: productItems) {
					if( p.getProduct().getId() == productItem.getProduct().getId() ) {
						//add quantity
						p.setQuantity(p.getQuantity()+productItem.getQuantity());
						//save to db 
						productItemRepository.save(p);
						count++;
						break;
					}
				}
				
				//else add new productItem to cart
				if( count == 0  ) {
					productItemRepository.save(productItem);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
	}
	
	
	/* findById */
	@Override
	public Cart findById(Long id) {
		Optional<Cart> cart = cartRepository.findById(id);
		if (cart.isPresent()) {
			return cart.get();
		}
		return null;
	}

	@Override
	public Boolean isUpdateProductIteam(Cart cart, ProductItem productItem) {
		int count = 0;
		try {
			List<ProductItem> productItems = cart.getProductItems();
			
			for( ProductItem p: productItems) {
				if( p.getProduct().getId() == productItem.getProduct().getId() ) {
					count++;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if( count > 0 ) {
			return true;
		}
		return false;
		
	}

}
