package com.practice.setoka.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
			    .requestMatchers("/","/Login").permitAll()
//			    .requestMatchers("/memos","/memo/detail","/animals").hasRole("관리자")
			    .anyRequest().authenticated()
			)
			.formLogin(form -> form
			    .loginPage("/Login")
			    .loginProcessingUrl("/doLogin")
			    .usernameParameter("id")
			    .passwordParameter("password")
			    .defaultSuccessUrl("/", false)
			    .permitAll()
			)
			.logout(logout -> logout
			    .logoutUrl("/Logout")
			    .logoutSuccessUrl("/")
			    .invalidateHttpSession(true)
			);

		return http.build();
	}
	
	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder())
                   .and()
                   .build();
    }
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
