package olala.com.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Brand extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String name;

	@Column(name = "description", columnDefinition = "NVARCHAR(500)")
	private String description;

	/* Relation with other table */
	@OneToMany(mappedBy = "brand", targetEntity = ProductLine.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	private List<ProductLine> productLines;

}
