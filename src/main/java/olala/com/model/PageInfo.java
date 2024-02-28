package olala.com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
	private Integer pageNumber;
	private Integer pageSize;
	private String keyword;
	private Integer searchType;
	private Integer sortType;

}
