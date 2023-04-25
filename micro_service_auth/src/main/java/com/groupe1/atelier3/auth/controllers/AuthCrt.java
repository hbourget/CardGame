package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.users.models.User;

import java.util.Map;

import com.groupe1.atelier3.users.models.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class AuthCrt {
    @Autowired
    private final AuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public AuthCrt(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Object obj = restTemplate.getForObject("http://localhost:8081/users/auth/" + username, User.class);
        UserDTO userdto = authService.checkAuth(obj, password);
        if (userdto != null) {
            return ResponseEntity.ok(userdto);
        } else {
            return ResponseEntity.status(401).build();
        }
    }


    @PostMapping("/auth/register")
    public Object registerPost(@RequestBody AuthDTO auth) {
    UserDTO userdto = authService.register(auth);
    if (userdto != null) {
        return ResponseEntity.ok(userdto);
    } else {
        return ResponseEntity.status(401).build();
        }
    }
}
