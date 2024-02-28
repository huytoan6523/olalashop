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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import olala.com.constant.Constant;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductLine extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String name;

	@Column(name = "description", columnDefinition = "NVARCHAR(500)")
	private String description;
	
	@NotNull(message = Constant.Message.NOT_EMPTY)
	@Column(name = "discoutn")
	private Float discount;

	/* Relation with other table */
	// Brand
	@ManyToOne
	@JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	Brand brand;

	@Transient
	private Long brandId;

	// Category
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	Category category;

	@Transient
	private Long categoryId;
	
	// Product
	@OneToMany(mappedBy = "productLine", targetEntity = Product.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	private List<Product> products;
	
	// Attribute
	@OneToMany(mappedBy = "productLine", targetEntity = Attribute.class , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	//@Exclude
	private List<Attribute> attributes;

	/* Image */
	@Transient
	private MultipartFile imgFile1;
	private String imgUrl1;

	@Transient
	private MultipartFile imgFile2;
	private String imgUrl2;

	@Transient
	private MultipartFile imgFile3;
	private String imgUrl3;

}
