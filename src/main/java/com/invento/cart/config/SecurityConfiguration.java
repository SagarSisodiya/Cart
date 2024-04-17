package com.invento.cart.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.invento.cart.filter.CsrfCookieFilter;
import com.invento.cart.filter.JWTTokenValidatorFilter;
import com.invento.cart.util.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfiguration {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
		handler.setCsrfRequestAttributeName(Constants.CSRF_REQUEST_ATTR_NAME);
		
		http
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedMethods(Collections.singletonList(Constants.ASTERISK));					
					config.setAllowedOrigins(Collections.singletonList(Constants.ASTERISK));					
					config.setAllowedHeaders(Collections.singletonList(Constants.ASTERISK));
					config.setAllowCredentials(true);
					config.setMaxAge(3600L);
					return config;
				}
			}))
			.csrf(csrf -> csrf
				.csrfTokenRequestHandler(handler)
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			.addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(request -> request
				.requestMatchers("/cart/**").hasAnyRole(Constants.ADMIN, Constants.READ))
		.formLogin(Customizer.withDefaults())
		.httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
