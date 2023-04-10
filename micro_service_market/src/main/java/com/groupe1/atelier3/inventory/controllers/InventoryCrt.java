package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.inventory.models.InventoryResponse;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class InventoryCrt {
    @Autowired
    private final InventoryService inventoryService;
    private final String cardServiceUrl = "http://localhost:8082";
    private final RestTemplate restTemplate = new RestTemplate();
    public InventoryCrt(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @PostMapping("/inventories/users/{userId}/cards/{cardId}")
    public ResponseEntity<InventoryResponse> addCardToInventory(@PathVariable Integer userId, @PathVariable Integer cardId) {
        InventoryResponse inv = inventoryService.addCardToInv(userId, cardId);
        if (inv == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(inv, HttpStatus.OK);
    }

    @DeleteMapping("/inventories/users/{userId}/cards/{cardId}")
    public ResponseEntity<InventoryResponse> removeCardFromInventory(@PathVariable Integer userId, @PathVariable Integer cardId) {
        InventoryResponse inv = inventoryService.removeCardFromInv(userId, cardId);
        if (inv == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/inventories/users/{userId}/cards")
    public ResponseEntity<InventoryResponse> addAllCardsToInventory(@PathVariable Integer userId) {
        String urlCardSvc = cardServiceUrl + "/cards";
        ResponseEntity<List<Card>> responseEntity = restTemplate.exchange(urlCardSvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        List<Card> cards = responseEntity.getBody();
        InventoryResponse inv = inventoryService.addAllCardToInv(userId, cards);
        if (inv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inv, HttpStatus.OK);
    }

    @DeleteMapping("/inventories/users/{userId}/cards")
    public Object removeAllCardsFromInventory(@PathVariable Integer userId) {
        return inventoryService.removeAllCardFromInv(userId);
    }

    @GetMapping("/inventories/users/{userId}")
    public ResponseEntity<InventoryResponse> getInventoryByUser(@PathVariable Integer userId) {
        UserDTO user;
        try {
            user = restTemplate.getForObject("http://localhost:8081/users/" + userId, UserDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                throw e;
            }
        }
        InventoryResponse inventoryResponse = inventoryService.getInventoryCards(user.getIdInventory());
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @GetMapping("/inventories")
    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return inventories;
    }

    @PostMapping("/inventories")
    public ResponseEntity<Inventory> createInventory() {
        Inventory inventory = new Inventory();
        inventoryService.saveInventory(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @PostMapping("/inventories/buy/users/{idUser}/cards/{cardId}")
    public ResponseEntity<Card> BuyCard(@PathVariable Integer idUser, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/cards/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        Card c = inventoryService.buyCard(user, card);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
    }

    @PostMapping("/inventories/sell/users/{idUser}/cards/{cardId}")
    public ResponseEntity<Card> SellCard(@PathVariable Integer idUser, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/cards/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        Card c = inventoryService.sellCard(user, card);
        if (c == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
    }

    @PostMapping("/inventories/sell/users/{idUser}")
    public ResponseEntity<Boolean> SellAllCards(@PathVariable Integer idUser) {
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        boolean b = inventoryService.sellAllCards(user);
        if (!b) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}