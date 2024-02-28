package olala.com.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import olala.com.constant.Constant;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String fullName;

	@NotNull(message = Constant.Message.NOT_EMPTY)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "passoword", nullable = false)
	private String password;

	// @NotBlank
	@Column(name = "role", nullable = false)
	private String role;

	private Boolean isActive;

	// cart
	@OneToOne(mappedBy = "users", targetEntity = Cart.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	Cart cart;

	// order
	@OneToMany(mappedBy = "users", targetEntity = Orders.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	List<Orders> orders;
	
//	// order
//	@OneToMany(mappedBy = "employee", targetEntity = Orders.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonIgnore
//	@Exclude
//	List<Orders> orders;
	
	// delivery Address
	@OneToMany(mappedBy = "users", targetEntity=DeliveryAddress.class ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//@JsonIgnore
	@Exclude
	List<DeliveryAddress> deliveryAddresses;
	
	
	@Column(name = "delivery_address_default_id")
	private Long deliveryAddressDefaultId;
	
}
