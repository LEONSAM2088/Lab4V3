package com.example.lab4v3.config;


import com.example.lab4v3.JwtFilter;
import com.example.lab4v3.service.UserDetailServiceImpl;
import com.example.lab4v3.utils.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    JwtFilter jwtFilter;

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable().cors().disable();
       http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

       http = http.exceptionHandling()
               .authenticationEntryPoint((request, response, exception) -> {
                   response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
               }).and();
       http.authorizeRequests().
               antMatchers("/api/auth/**").permitAll()
        .anyRequest().authenticated();
       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(customPasswordEncoder.getbCryptPasswordEncoder());

    }
}
