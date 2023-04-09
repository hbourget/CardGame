package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class AuthService {
    private AuthMapper authMapper = new AuthMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userServiceUrl = "http://localhost:8081";
    private final String cardServiceUrl = "http://localhost:8082";
    private final String inventoryServiceUrl = "http://localhost:8083";
    public UserDTO checkAuth(Object obj, String password) {
        if (obj == null) {
            return null;
        }
        User user = (User) obj;
        if (user.getPassword().equals(password)) {
            String url = userServiceUrl + "/users/" + user.getId();
            UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);
            return userdto;
        } else {
            return null;
        }
    }
    public UserDTO register(AuthDTO authDTO) {
        String usr = authDTO.getUsername();

        String urlGetAllUsers = userServiceUrl + "/users";
        ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(urlGetAllUsers, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDTO>>() {});
        List<UserDTO> usersDTO = responseEntity.getBody();

        if (usersDTO != null) {
            for (UserDTO userDTO : usersDTO) {
                if (userDTO.getUsername().equals(usr)) {
                    return null;
                }
            }
        }

        User userNew = authMapper.toEntity(authDTO);

        String urlSave = userServiceUrl + "/users";
        User myUser = restTemplate.postForObject(urlSave, userNew, User.class);


        // On récupère le UserDTO correspondant a retourner
        String url = userServiceUrl + "/users/" + myUser.getId();
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        ArrayList<Card> cardsList = getStarterCards();

        for (int i = 0; i < 3 && i < cardsList.size(); i++) {
            Integer cardId = cardsList.get(i).getId();
            Integer inventoryId = userdto.getIdInventory();
            String inventoryAddCard = inventoryServiceUrl + "/inventory/add/{inventoryId}/{cardId}";
            ResponseEntity<Void> inventoryAddCardResponse = restTemplate.postForEntity(inventoryAddCard, null, Void.class, inventoryId, cardId);
        }

        return userdto;
    }

    private ArrayList<Card> getStarterCards(){

        String urlCardSvc = cardServiceUrl + "/cards";
        ResponseEntity<List<Card>> responseEntity = restTemplate.exchange(urlCardSvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        List<Card> cards = responseEntity.getBody();
        ArrayList<Card> cardsList = new ArrayList<>();

        String urlInventorySvc = inventoryServiceUrl + "/inventory";
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
        System.out.println("after" + cards.size());
        Collections.shuffle(cards);
        for (int i = 0; i < 3 && i < cards.size(); i++) {
            cardsList.add(cards.get(i));
        }
        return cardsList;
    }

}
