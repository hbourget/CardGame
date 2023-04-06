package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.controllers.InventoryService;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketService {
    private final String userServiceUrl = "http://localhost:8080/user-service";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private InventoryService inventoryService;

    public Card buyCard(User user, Card card) {
        if (user != null) {
            //check if balance is enough
            if (user.getBalance() < card.getPrice()) {
                return null;
            }
            //check if card is already in inventory
            Integer idInv = user.getIdInventory();
            if (inventoryService.getInventory(idInv).getCards().contains(card.getId())) {
                return null;
            }
            String url = userServiceUrl + "/user/substractbalance/{username}/{balance}";
            restTemplate.postForObject(url, null, Void.class, user.getUsername(), card.getPrice());

            inventoryService.addCardToInv(user.getIdInventory(), card.getId());
        }
        //return cardMapper.toDTO(card);
        return card;
    }

    public Card sellCard(User user, Card card) {
        if (user != null) {
            //check if card is in inventory
            Integer idInv = user.getIdInventory();
            if (!inventoryService.getInventory(idInv).getCards().contains(card.getId())) {
                return null;
            }
            String url = userServiceUrl + "/user/addbalance/{username}/{balance}";
            restTemplate.postForObject(url, null, Void.class, user.getUsername(), card.getPrice());

            inventoryService.removeCardFromInv(user.getIdInventory(), card.getId());
        }
        //return cardMapper.toDTO(card.get());
        return card;
    }
}
