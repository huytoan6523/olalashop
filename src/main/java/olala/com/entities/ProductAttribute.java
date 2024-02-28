package olala.com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import olala.com.constant.Constant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "product_attribute")
public class ProductAttribute extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	//@Exclude
	Product product;
	
	@ManyToOne
	@JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = true)
	//@JsonIgnore
	//@Exclude
	Attribute attribute;
	
	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "value", nullable = false, columnDefinition = " NVARCHAR(60)")
	private String value;
}
