package com.groupe1.atelier3.auth;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.config.JwtService;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.token.Token;
import com.groupe1.atelier3.token.TokenRepository;
import com.groupe1.atelier3.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final RestTemplate restTemplate = new RestTemplate();
  private final String userServiceUrl = "http://localhost:8081";
  private final String cardServiceUrl = "http://localhost:8082";
  private final String inventoryServiceUrl = "http://localhost:8083";

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

  public AuthResponse register(RegisterRequest request) {
    request.setPassword(passwordEncoder.encode(request.getPassword()));

    String urlGetAllUsers = userServiceUrl + "/users";
    ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(urlGetAllUsers, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {});
    List<UserDTO> usersDTO = responseEntity.getBody();

    if (usersDTO != null) {
      for (UserDTO userDTO : usersDTO) {
        if (userDTO.getUsername().equals(request.getUsername())) {
            return null;
        }
      }
    }

    User savedUser = restTemplate.postForObject(userServiceUrl + "/users", request, User.class);

    String url = userServiceUrl + "/users/" + savedUser.getId();
    UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

    ArrayList<Card> cardsList = getStarterCards();

    for (int i = 0; i < 3 && i < cardsList.size(); i++) {
      Integer cardId = cardsList.get(i).getId();
      String inventoryAddCard = inventoryServiceUrl + "/inventories/users/{userId}/cards/{cardId}";
      restTemplate.postForEntity(inventoryAddCard, null, Void.class, userdto.getId(), cardId);
    }

    UserDetails userDetails = usertoUserDetails(savedUser);
    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);
    saveUserToken(savedUser, jwtToken);
    return AuthResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private ArrayList<Card> getStarterCards(){

    String urlCardSvc = cardServiceUrl + "/cards";
    ResponseEntity<List<Card>> responseEntity = restTemplate.exchange(urlCardSvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
    List<Card> cards = responseEntity.getBody();
    ArrayList<Card> cardsList = new ArrayList<>();

    String urlInventorySvc = inventoryServiceUrl + "/inventories";
    ResponseEntity<List<Inventory>> responseEntity2 = restTemplate.exchange(urlInventorySvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Inventory>>() {});
    List<Inventory> inventories = responseEntity2.getBody();

    Iterator<Card> cardIterator = cards.iterator();
    while (cardIterator.hasNext()) {
      Card currentCard = cardIterator.next();
      for (Inventory inventory : inventories) {
        if (inventory.getCards().contains(currentCard.getId())) {
          cardIterator.remove();
          break;
        }
      }
    }
    Collections.shuffle(cards);
    for (int i = 0; i < 3 && i < cards.size(); i++) {
      cardsList.add(cards.get(i));
    }
    return cardsList;
  }

  private User getUserByUsername(String username) {
    try {
      return restTemplate.getForObject(userServiceUrl + "/users/auth/{idOrUsername}", User.class, username);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().value() == 404) {
        throw new UsernameNotFoundException("User not found");
      } else {
        throw new AuthenticationServiceException("An error occurred while fetching user data", e);
      }
    }
  }


  public AuthResponse authenticate(AuthRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (Exception e) {
      return null;
    }

    var user = getUserByUsername(request.getUsername());
    UserDetails userDetails = usertoUserDetails(user);
    var jwtToken = jwtService.generateToken(userDetails);
    var refreshToken = jwtService.generateRefreshToken(userDetails);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthResponse.builder()
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
        var authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
