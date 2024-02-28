package olala.com.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ProductItem
	@OneToMany(mappedBy = "cart", targetEntity = ProductItem.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	//@JsonIgnore
	@Exclude
	List<ProductItem> productItems;
	
	
	//User
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	@lombok.EqualsAndHashCode.Exclude
	@Exclude
	Users users;
}
