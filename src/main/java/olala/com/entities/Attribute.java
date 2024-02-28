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
import javax.validation.constraints.NotBlank;

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
public class Attribute extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String name;

	@Column(name = "description", columnDefinition = "NVARCHAR(500)")
	private String description;

	/* Relation with other table */
	@OneToMany(mappedBy = "attribute", targetEntity = ProductAttribute.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Exclude
	@JsonIgnore
	private List<ProductAttribute> productAttributes;

	// ProductLine
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_line_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	ProductLine productLine;

}
