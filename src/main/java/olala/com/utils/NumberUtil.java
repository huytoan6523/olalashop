package olala.com.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
	 public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	 
	 public static boolean isPassword( String password) {
		 if( password.length()<6 ) return false;
		 return true;
	 }
}
