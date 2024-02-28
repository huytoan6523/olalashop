package olala.com.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import olala.com.entities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {
	Users users;

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	CustomUserDetail() {

	}

	CustomUserDetail(Users users) {
		this.users = users;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> result = new ArrayList<>();
		result.add(new SimpleGrantedAuthority(users.getRole()));
		return result;
	}

	@Override
	public String getPassword() {
		if (users == null)
			return bCryptPasswordEncoder.encode("password");
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		if (users == null)
			return "usersname";
		return users.getPhoneNumber();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
