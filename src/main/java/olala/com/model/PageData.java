package olala.com.model;

import java.util.List;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageData<T> {
	private Class<T> type;
	private List<T> content;
	private Integer totalPages;
}
