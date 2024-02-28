package olala.com.constant;

public class Constant {
	public static class URLConstant {
		public static final String CREATE_ORDER_SUCCESSFUL = "http://localhost:8080/user/create-order-successful";
	}
	/* SORT TYPE */
	public static class SortType {
		public static final int ASECENDING = 0;
		public static final int DESCENDING = 1;
	}
	/* END SORT TYPE */

	/* MESSAGE */
	public static class Message {
		public static final String NOT_EMPTY = "Vui lòng không để trống trường này!";

	}

	/* STATUS */
	public static class EntityStatus {
		public static final Boolean IS_ACTIVE = true;
		public static final Boolean NOT_ACTIVE = false;
	}

	/* PRODUCTLINE ORDER BY */
	public static class ProductLineOrderBy {
		public static final String BEST_SELLER = "bestseller";
		public static final String NEWEST = "newest";
		public static final String PRICE_LOW_TO_HIGH = "priceLowToHigh";
		public static final String PRICE_HIGH_TO_LOW = "priceHighToLow";
	}

	public static class OrderStatus {
		public static final int DRAFT = 0;
		public static final int IN_PROCESS = 1;
		public static final int BEING_SHIPPED = 2;
		public static final int RECIEVED = 3;
		public static final int RETURNED = 4;
	}
	
	public static class ProductItemStatus {
		public static final int IN_CART = 0;
		public static final int IN_ORDER = 1;
	}
	
	public static class MessageSMS {
		public static final String REGISTER_SUCCESSFULLY_MESSAGE = "Bạn đã đăng ký tài khoản thành công với số điện thoại là: ";
	}

}
