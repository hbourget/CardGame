package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.users.models.User;
import java.util.Map;

import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
  @GetMapping("/login")
  public String login() {
    return "login";
  }
  @PostMapping("/login")
  public User loginPost(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");
    User user = restTemplate.getForObject("http://localhost:8081/user/" + username, User.class);
    return AuthService.checkAuth(user, password);
  }
  @PostMapping("/register")
  public UserDTO registerPost(@RequestBody AuthDTO auth) {
    return AuthService.register(auth);
  }
}
