package olala.com.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import olala.com.constant.Constant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "stock", nullable = false)
	private Long stock;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "price", nullable = false)
	private Long price;
	
	
	@Column(name = "sold",nullable = true)
	private Long sold;

	/* Relation with other table */
	// product
	@ManyToOne
	@JoinColumn(name = "product_line_id", referencedColumnName = "id", nullable = true)
	//@JsonIgnore
	@Exclude
	ProductLine productLine;
	
	@Transient
	Long productLineId;

	// productAttribute
	@OneToMany(mappedBy = "product", targetEntity = ProductAttribute.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Exclude
	//@JsonIgnore
	private List<ProductAttribute> productAttributes;
	
	@Transient
	private List<String> values;
	
	public List<String> getValues(){
		List<String> values = new ArrayList<>();
		for( ProductAttribute p: productAttributes) {
			values.add(p.getValue());
		}
		return values;
		
	}
	
	
	//ProductItem
	@OneToMany(mappedBy = "product", targetEntity = ProductItem.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	List<ProductItem> productItems;
	
	@Transient
	private Long actualPrice;
	public Long getActualPrice() {
		Long actualPrice = (long) (this.price*(1-this.getProductLine().getDiscount()/100));
		return actualPrice;
		//return actualPrice;
	}

}
