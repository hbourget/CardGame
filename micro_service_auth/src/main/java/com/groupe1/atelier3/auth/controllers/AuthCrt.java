package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.users.models.User;
import java.util.Map;

import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Order(2)
public class AuthCrt {
      @Autowired private final AuthService AuthService;
      private final RestTemplate restTemplate = new RestTemplate();
      public AuthCrt(AuthService authService) { this.AuthService = authService; }
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Object obj = restTemplate.getForObject("http://localhost:8081/users/auth/" + username, User.class);
        UserDTO userdto = AuthService.checkAuth(obj, password);
        if (userdto != null) {
            return ResponseEntity.ok(userdto);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

      @PostMapping("/register")
      public Object registerPost(@RequestBody AuthDTO auth) {
        UserDTO userdto = AuthService.register(auth);
        if (userdto != null) {
            return ResponseEntity.ok(userdto);
        } else {
            return ResponseEntity.status(401).build();
        }
      }
}
