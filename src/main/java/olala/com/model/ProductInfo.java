package olala.com.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Class for search Product By ProductLine and  AttributeValues */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductInfo {
	private Long id;
	private Long productLineId;
	private List<String> values;
	private Long price;
	private Long stock;
	private Long sold;
	private Long minPrice;
	private Long maxPrice;
	private Long totalStock;
	private Float discount;
	
}
