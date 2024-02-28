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
public class DeliveryAddress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//name 
	@Column(name = "province_name", nullable = true, columnDefinition = "NVARCHAR(100)")
	private String provinceName;
	
	@Column(name = "district_name", nullable = true, columnDefinition = "NVARCHAR(100)")
	private String districtName;
	
	@Column(name = "ward_name", nullable = true, columnDefinition = "NVARCHAR(100)")
	private String wardName;
	
	//user
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	@JsonIgnore
	@Exclude
	Users users;
	
	@NotBlank(message = Constant.Message.NOT_EMPTY)
	@Column(name = "address_detail", nullable = false, columnDefinition = "NVARCHAR(100)")
	private String addressDetail;
	
	//order
	@OneToMany(mappedBy = "deliveryAddress", targetEntity = Orders.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@Exclude
	List<Orders> orders;
	
	
	@Column(name = "province_id", nullable = true)
	private Long provinceId;
	
	@Column(name = "district_id", nullable = true)
	private Long districtId;
	
	@Column(name = "ward_code", nullable = true)
	private String wardCode;
	
	@Override
	public String toString() {
		return addressDetail+", "+wardName+", "+districtName+", "+provinceName;
	}
	

}
