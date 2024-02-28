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
public class Category extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String name;

	@Column(name = "description", columnDefinition = "NVARCHAR(500)")
	private String description;

	/* Relation with another table */
	@ManyToOne
	@JoinColumn(name = "parent_category_id")
	@Exclude
	Category parentCategory;

	@Transient
	// @NotBlank(message = "This field can not be empty!!!")
	private Long parentCategoryId;

	@OneToMany(mappedBy = "parentCategory", targetEntity = Category.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	List<Category> categories;

	@OneToMany(mappedBy = "category", targetEntity = ProductLine.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	private List<ProductLine> productLines;
}
