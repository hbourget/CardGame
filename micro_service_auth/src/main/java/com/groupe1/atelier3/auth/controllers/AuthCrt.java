package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.users.models.User;
import java.util.Map;

import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Object login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Object obj = restTemplate.getForObject("http://localhost:8081/user/auth/" + username, User.class);
        return AuthService.checkAuth(obj, password);
    }

      @PostMapping("/register")
      public Object registerPost(@RequestBody AuthDTO auth) {
        return AuthService.register(auth);
      }
}
