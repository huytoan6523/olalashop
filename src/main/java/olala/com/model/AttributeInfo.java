package olala.com.model;

import lombok.Data;
import lombok.NoArgsConstructor;

public class AttributeInfo {
	Long id;
	private String value;

	public AttributeInfo() {

	}

	public AttributeInfo(Long id, String value) {
		this.id = id;
		if (value == null) {
			this.value = "";
		} else
			this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value == null) {
			this.value = "";
		} else
			this.value = value;
	}

	@Override
	public String toString() {
		return "AttributeInfo [id=" + id + ", value=" + value + "]";
	}

}
