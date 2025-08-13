package com.practice.setoka.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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
				//로그인 하지않아도 접근 가능
			    .requestMatchers("/CSS/**",
					    		"/",
					    		"/Login",
					    		"/SignUp",
					    		"/Adopt",
					    		"/AdoptDetail/{num}",
					    		"/AnimalPride",
					    		"/AnimalPrideDetail/{num}",
					    		"/WalkTrail",
					    		"/WalkTrailDetail/{num}",
					    		"/UsedGoods",
					    		"/UsedGoodsDetail/{num}",
					    		"/KnowHow",
					    		"/KnowHowDetail/{num}",
					    		"/sendSingUpCode",
					    		"/loginUser",
					    		"/images/**",
					    		"/imagesDefault/**",
					    		"/passwordFind",
					    		"/health"
					    		).permitAll()
			    //hasRole 관리자 권한을 가진 계정만 접근 가능
			    //.requestMatchers("/AllUsers").hasRole("관리자")
			    .anyRequest().authenticated()
			)
			.formLogin(form -> form
				//로그인페이지
			    .loginPage("/Login")
			    .loginProcessingUrl("/doLogin")
			    .usernameParameter("id")
			    .passwordParameter("password")
			    //로그인시 기존 접근하려던 페이지로 이동
			    .defaultSuccessUrl("/", false)
			    .permitAll()
			)
			.logout(logout -> logout
			    .logoutUrl("/Logout")
			    //로그아웃시 홈으로 이동
			    .logoutSuccessUrl("/")
			    //로그아웃시 세션 삭제
			    .invalidateHttpSession(true)
			)
			.sessionManagement(session -> session
				// 한 계정당 최대 로그인 가능한 세션
				.maximumSessions(1)
				// 새 로그인 거부 (기존 로그인 유지)
                .maxSessionsPreventsLogin(false)
                // 세션 만료 시 이동할 URL
                .expiredUrl("/Login?expired")
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
	
	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}
}
