package com.example.diary.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtService jwtService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
					    .requestMatchers(
					        "/",
					        "/css/**",
					        "/js/**",
					        "/images/**",
					        "/media/**",
					        "/plugins/**",
					        "/publishing/**",
					        "/diary/**",
					        "/highlight/**",
					        "/auth/**",
					        "/api/auth/**",
					        "/mypage/**",
					        "/ai/**",
					        "/api/users/**",
					        "/api/books/search",
					        "/api/diary/**"
					    ).permitAll()
					    .requestMatchers("/api/admin/**").hasRole("ADMIN")
					    .requestMatchers("/test/**").permitAll()
					    .anyRequest().authenticated()
					)
				.addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}