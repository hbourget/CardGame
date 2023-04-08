package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.inventory.models.InventoryResponse;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("/inventory/add/{inventoryId}/{cardId}")
    public Object AddCardToInv(@PathVariable Integer inventoryId, @PathVariable Integer cardId) {
        return inventoryService.addCardToInv(inventoryId, cardId);
    }
    @PostMapping("/inventory/remove/{inventoryId}/{cardId}")
    public Object RemoveCardFromInv(@PathVariable Integer inventoryId, @PathVariable Integer cardId) {
        return inventoryService.removeCardFromInv(inventoryId, cardId);
    }

    @PostMapping("/inventory/add/{inventoryId}")
    public Object AddAllCardToInv(@PathVariable Integer inventoryId) {

        String urlCardSvc = cardServiceUrl + "/cards";
        ResponseEntity<List<Card>> responseEntity = restTemplate.exchange(urlCardSvc, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        List<Card> cards = responseEntity.getBody();
        return inventoryService.addAllCardToInv(inventoryId, cards);
    }
    @PostMapping("/inventory/remove/{inventoryId}")
    public Object RemoveAllCardFromInv(@PathVariable Integer inventoryId) {
        return inventoryService.removeAllCardFromInv(inventoryId);
    }

    @GetMapping("/inventory/{idUser}")
    public InventoryResponse getInventory(@PathVariable Integer idUser) {
        User user = restTemplate.getForObject("http://localhost:8081/user/" + idUser, User.class);
        return inventoryService.getInventoryCards(user.getIdInventory());
    }

    @PostMapping("/inventory/create")
    public ResponseEntity<Inventory> createInventory() {
        Inventory inventory = new Inventory();
        inventoryService.saveInventory(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }
}
