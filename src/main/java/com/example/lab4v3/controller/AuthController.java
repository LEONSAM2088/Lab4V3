package com.example.lab4v3.controller;

import com.example.lab4v3.AuthCredentialRequest;
import com.example.lab4v3.security.jwt.JwtUtil;
import com.example.lab4v3.model.User;
import com.example.lab4v3.repository.UserRepository;
import com.example.lab4v3.utils.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("login")
    @CrossOrigin(origins = {"http://192.168.1.173:3000", "http://localhost:3000"}, allowCredentials = "true", allowedHeaders = "*")
    public ResponseEntity<?> login(@RequestBody AuthCredentialRequest req) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    req.getUsername(), req.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();
            String token = jwtUtil.generateToken(user);

            HttpCookie cookie = ResponseCookie.from("token_data", token)
                    .path("/")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(user);
            /*return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,token

                    )
                    .body(user);*/
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    @CrossOrigin(origins = {"http://192.168.1.173:3000", "http://localhost:3000"}, allowCredentials = "true", allowedHeaders = "*")
    public  ResponseEntity<?> register(@RequestBody AuthCredentialRequest req) {
        try {
            String login = req.getUsername();
            String pass =  req.getPassword();

            User user = new User();
            user.setPassword(passwordEncoder.getbCryptPasswordEncoder().encode(pass));
            user.setUsername(login);


            if(userRepository.findByUsername(login).isEmpty())
                user = userRepository.save(user);
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User login already exist");

            String token = jwtUtil.generateToken(user);

            HttpCookie cookie = ResponseCookie.from("token_data", token)
                    .path("/")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(user);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("checkToken")
    @CrossOrigin(origins = {"http://192.168.1.173:3000", "http://localhost:3000"}, allowCredentials = "true", allowedHeaders = "*")
    public  ResponseEntity<?> checkToken(HttpServletRequest req) {

        javax.servlet.http.Cookie[] cookies = req.getCookies();
        if(cookies==null) {
           return ResponseEntity.status(401).build();
        }
        final String token;
        List<Cookie> cookieList = Arrays.stream(cookies).filter(c->c.getName().equals("token_data")).collect(Collectors.toList());
        if(cookieList.size()!=0) {
            token = cookieList.get(0).getValue();
            boolean isValid = jwtUtil.checkToken(token);
            return ResponseEntity.status(isValid?200:401).build();
        }
        return ResponseEntity.status(401).build();

    }
}
