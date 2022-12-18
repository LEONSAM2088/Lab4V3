package com.example.lab4v3.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {
    CustomPasswordEncoder() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return bCryptPasswordEncoder;
    }
}
