package olala.com.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import olala.com.entities.ProductLine;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineDto {
	// for display in overview
	private Long id;
	private String name;
	private String imgUrl1;

	private Long minPrice;
	private Long maxPrice;
	private Long totalStock;
	private Long totalSold;
	private Float discount;

	// for display in details
	private List<AttributeDto> attributeDtos;
	private String imgUrl2;
	private String imgUrl3;
	private String description;

	public ProductLineDto(Long id, String name, Long minPrice, Long maxPrice, Long totalStock, Float discount) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl1 = imgUrl1;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.totalStock = totalStock;
		this.discount = discount;
	}

	public ProductLineDto(Long id, String name, String imgUrl1, Long minPrice, Long maxPrice,
			Long totalSold, Float discount) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl1 = imgUrl1;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.totalSold = totalSold;
		this.discount = discount;
	}

	public ProductLineDto(ProductLine productLine) {
		this.id = productLine.getId();
		this.name = productLine.getName();
		this.imgUrl1 = productLine.getImgUrl1();
		this.imgUrl2 = productLine.getImgUrl2();
		this.imgUrl3 = productLine.getImgUrl3();
		this.description = productLine.getDescription();
	}

}
