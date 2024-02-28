package olala.com.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
	private Long id;
	private String productName;
	private String imgUrl1;
	private Long price;
	private Long sold;
	private Long totalIncome;
}
