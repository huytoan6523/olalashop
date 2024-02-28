package olala.com.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity;

	// product
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = true)
	//@JsonIgnore
	@Exclude
	Product product;

	// cart
	@ManyToOne
	@JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	Cart cart;

	// order
	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	Orders orders;

}
