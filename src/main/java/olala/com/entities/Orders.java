package olala.com.entities;

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
public class Orders extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ProductItem
	@OneToMany(mappedBy = "orders", targetEntity = ProductItem.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@JsonIgnore
	@Exclude
	List<ProductItem> productItems;

	// User
	@ManyToOne
	@JoinColumn(name = "user_id")
	//@JsonIgnore
	@Exclude
	Users users;

	// User
	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonIgnore
	@Exclude
	Users employee;

	// Delivery Address
	@ManyToOne
	@JoinColumn(name = "delivery_address")
	@JsonIgnore
	@Exclude
	private DeliveryAddress deliveryAddress;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "vnp_TxnRef", nullable = false)
	private String vnpTxnRef;

	@Column(name = "order_info", nullable = false)
	private String orderInfo;

	/* FOR PRICE */
	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "total_amount", nullable = false)
	private Long totalAmount;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "delivery_fee", nullable = false)
	private Long deliveryFee;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "product_price", nullable = false)
	private Long productPrice;
	
	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "paid_amount", nullable = false)
	private Long paidAmount;
	
	
	// Payment Type
	//@NotBlank(message = Message.NOT_EMPTY)
	@Column(name = "payment_type", nullable = false)
	private String paymentType;
	
	// Payment Status
//	@NotNull(message = Message.NOT_EMPTY)
	@Column(name = "status", nullable = false)
	private Integer status;
	
	

}
