package olala.com.config;

import olala.com.auth.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    CustomUserDetailService customUserDetailService;
	
	
//	Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	//Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/register/**").permitAll()
			.antMatchers("/visitor/**").permitAll()
			.antMatchers("/view").permitAll()
			.antMatchers("/test/**").permitAll()
			.antMatchers("/user/**").hasRole("USER")
			.antMatchers("/employee/**").hasAnyRole("EMPLOYEE","ADMIN","USER")
			.antMatchers("/admin/**").hasRole("ADMIN").anyRequest()
			.authenticated()
			.and()
			.csrf().disable().
			formLogin().loginPage("/login").failureUrl("/login?error=true")
			.defaultSuccessUrl("/default")
				.usernameParameter("phoneNumber").
				passwordParameter("password")
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true).logoutSuccessUrl("/login").and()
			.exceptionHandling().accessDeniedPage("/access-denied");
		
	//      Cấu hình Remember Me. Ở form login có nút remember me. Nếu người dùng tick vào đó ta sẽ dùng
	//      cookie lưu lại trong 24h
		     http.authorizeRequests().and() //
		             .rememberMe().tokenRepository(this.persistentTokenRepository()) //
		             .tokenValiditySeconds(1 * 24 * 60 * 60);
	}
	
	  @Bean
	    public PersistentTokenRepository persistentTokenRepository() {
	        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl(); // Ta lưu tạm remember me trong memory (RAM). Nếu cần mình có thể lưu trong database
	        return memory;
	    }

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/markup/**", "/images/**");
	}

}
