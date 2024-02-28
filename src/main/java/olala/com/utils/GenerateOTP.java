package olala.com.utils;

import java.time.LocalDateTime;

public class GenerateOTP {
	public static String generateOTP() {
		int randomPin = (int) (Math.random() * 9000) + 1000;
		String otp = String.valueOf(randomPin);
		return otp; // returning value of otp
	}
	
	public static Boolean isOptTimeValid( LocalDateTime createTime, LocalDateTime confirmTime) {
		if( createTime.plusMinutes(2).isAfter(confirmTime))
			return true;
		return false;
	}

}
