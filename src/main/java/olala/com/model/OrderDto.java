package olala.com.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import olala.com.entities.ProductItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private List<ProductItem> productItems;
	private Long totalPrice;
	private Integer totalProductItem;
	
	//for checkout
	private List<Long> productItemIds;
	private Long deliveryAddressId;
	private String paymentType;
	private Long finalPrice;
	
}
