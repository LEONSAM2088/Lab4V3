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

@RestController
@CrossOrigin(origins="http://localhost:3000", allowCredentials = "true")
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
                    .build();
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
}
