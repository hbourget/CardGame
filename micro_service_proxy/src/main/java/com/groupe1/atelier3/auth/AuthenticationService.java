package com.groupe1.atelier3.auth;

import com.groupe1.atelier3.config.JwtService;
import com.groupe1.atelier3.token.Token;
import com.groupe1.atelier3.token.TokenRepository;
import com.groupe1.atelier3.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final RestTemplate restTemplate = new RestTemplate();

  private UserDetails usertoUserDetails(User user) {
    return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities("USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
  }

  public AuthenticationResponse register(RegisterRequest request) {
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    User savedUser = restTemplate.postForObject("http://localhost:8081/users", request, User.class);
    UserDetails userDetails = usertoUserDetails(savedUser);
    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private User getUserByUsername(String username) {
    try {
      return restTemplate.getForObject("http://localhost:8081/users/auth/{idOrUsername}", User.class, username);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().value() == 404) {
        throw new UsernameNotFoundException("User not found");
      } else {
        throw new AuthenticationServiceException("An error occurred while fetching user data", e);
      }
    }
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = getUserByUsername(request.getUsername());
    UserDetails userDetails = usertoUserDetails(user);
    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .userId(user.getId())
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userName;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userName = jwtService.extractUsername(refreshToken);
    if (userName != null) {
      var user = getUserByUsername(userName);
      UserDetails userDetails = usertoUserDetails(user);
      if (jwtService.isTokenValid(refreshToken, userDetails)) {
        var accessToken = jwtService.generateToken(userDetails);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
