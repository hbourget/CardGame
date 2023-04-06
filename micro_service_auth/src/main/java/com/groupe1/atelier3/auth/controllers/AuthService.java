package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private AuthMapper authMapper = new AuthMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userServiceUrl = "http://localhost:8081";
    private final String cardServiceUrl = "http://localhost:8082";
    private final String inventoryServiceUrl = "http://localhost:8083";
    public User checkAuth(User user, String password) {
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public Object register(AuthDTO authDTO) {
        String usr = authDTO.getUsername();

        String urlGetAllUsers = userServiceUrl + "/users";
        ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(urlGetAllUsers, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {});
        List<UserDTO> usersDTO = responseEntity.getBody();

        //check if usr already exists
        for (UserDTO userDTO : usersDTO) {
            if (userDTO.getUsername().equals(usr)) {
                return ResponseEntity.status(409).body("Un utilisateur existe déjà avec le nom " + usr + ".");
            }
        }

        User userNew = authMapper.toEntity(authDTO);

        String urlSave = userServiceUrl + "/user/save/{username}/{password}";
        User myUser = restTemplate.postForObject(urlSave, null, User.class, userNew.getUsername(), userNew.getPassword());

        // On récupère le UserDTO correspondant a retourner
        String url = userServiceUrl + "/user/" + myUser.getId();
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        ArrayList<Card> cardsList = getStarterCards();

        for (int i = 0; i < 3 && i < cardsList.size(); i++) {
            Integer cardId = cardsList.get(i).getId();
            Integer inventoryId = userdto.getIdInventory();
            String inventoryAddCard = inventoryServiceUrl + "/inventory/add/{inventoryId}/{cardId}";
            restTemplate.postForObject(inventoryAddCard, null, Void.class, inventoryId, cardId);
        }

        return userdto;
    }

    private ArrayList<Card> getStarterCards(){

        String urlCardSvc = cardServiceUrl + "/cards";
        ResponseEntity<List<Card>> responseEntity = restTemplate.exchange(urlCardSvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        List<Card> cards = responseEntity.getBody();
        ArrayList<Card> cardsList = new ArrayList<>();

        Collections.shuffle(cards);
        for (int i = 0; i < 3 && i < cards.size(); i++) {
            cardsList.add(cards.get(i));
        }
        return cardsList;
    }

}
