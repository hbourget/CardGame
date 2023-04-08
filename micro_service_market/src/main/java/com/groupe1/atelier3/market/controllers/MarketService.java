package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.controllers.InventoryService;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketService {
    private final String userServiceUrl = "http://localhost:8081";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private InventoryService inventoryService;

    public Object buyCard(User user, Card card) {
        if (user != null) {
            //check if balance is enough
            if (user.getBalance() < card.getPrice()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte est trop chère.");
            }
            //check if card is already in inventory
            Integer idInv = user.getIdInventory();
            if (inventoryService.getInventory(idInv).getCards().contains(card.getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur possède déjà la carte.");
            }

            String url = userServiceUrl + "/user/{id}/subtractbalance";
            HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());

            inventoryService.addCardToInv(user.getIdInventory(), card.getId());
        }
        //return cardMapper.toDTO(card);
        return card;
    }

    public Object sellCard(User user, Card card) {
        if (user != null) {
            //check if card is in inventory
            Integer idInv = user.getIdInventory();
            if (!inventoryService.getInventory(idInv).getCards().contains(card.getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur ne possède pas la carte.");
            }
            String url = userServiceUrl + "/user/{id}/addbalance";
            HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());

            inventoryService.removeCardFromInv(user.getIdInventory(), card.getId());
        }
        //return cardMapper.toDTO(card.get());
        return card;
    }
    public Object sellAllCards(User user) {
        int sommetotal = 0;
        if (user != null) {
            //check if card is in inventory
            Integer idInv = user.getIdInventory();
            if (inventoryService.getInventory(idInv).getCards().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur ne possède pas de carte.");
            }
            //sell all cards
            for (Integer cardId : inventoryService.getInventory(idInv).getCards()) {
                String urlCard = "http://localhost:8082/card/" + cardId;
                ResponseEntity<Card> response = restTemplate.getForEntity(urlCard, Card.class);
                Card card = response.getBody();

                String url = userServiceUrl + "/user/{id}/addbalance";
                HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());

                sommetotal += card.getPrice();
            }
            inventoryService.removeAllCardFromInv(user.getIdInventory());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Toutes les cartes de " + user.getUsername() + " ont été vendues pour un montant de " + sommetotal + "€.");
    }
}
