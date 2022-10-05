package com.example.shyneeds_be.global.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderConfig extends BCryptPasswordEncoder {
}
