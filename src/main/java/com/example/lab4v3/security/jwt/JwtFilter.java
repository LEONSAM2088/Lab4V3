package com.example.lab4v3.security.jwt;

import com.example.lab4v3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");

        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if(cookies==null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token;
        List<Cookie> cookieList = Arrays.stream(cookies).filter(c->c.getName().equals("token_data")).collect(Collectors.toList());
        if(cookieList.size()!=0) {
           token = cookieList.get(0).getValue();

        } else {
            filterChain.doFilter(request, response);
            return;
        }

        //final String token = header.split(" ")[1].trim();
       /* if(!StringUtils.hasText(header) || StringUtils.hasText(header) && !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }*/

        UserDetails userDetails = userRepository.findByUsername(jwtUtil.extractUsername(token)).orElse(null);

        assert userDetails != null;
        if(!jwtUtil.validateToken(token, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails==null? List.of():userDetails.getAuthorities()
        );

        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
