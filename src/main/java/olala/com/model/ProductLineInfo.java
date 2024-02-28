package olala.com.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineInfo {
	
	private Long parentCategoryId;
	private String keyword;
	private List<Long> categoryIds;
	private String orderBy;
	private Long minPrice;
	private Long maxPrice;
	
	private Integer pageNumber;
	private Integer pageSize;

	
}
