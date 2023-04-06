package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.controllers.InventoryService;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InventoryCrt {
    @Autowired
    private final InventoryService inventoryService;
    private final RestTemplate restTemplate = new RestTemplate();
    public InventoryCrt(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @PostMapping("/inventory/add/{inventoryId}/{cardId}")
    public void AddCardToInv(@PathVariable Integer inventoryId, @PathVariable Integer cardId) {
        System.out.println("TEST CTRL INVENTORY CARTE ID:" + cardId);
         inventoryService.addCardToInv(inventoryId, cardId);
    }

    @PostMapping("/inventory/remove/{inventoryId}/{cardId}")
    public void RemoveCardFromInv(@PathVariable Integer inventoryId, @PathVariable Integer cardId) {
        inventoryService.removeCardFromInv(inventoryId, cardId);
    }

    @GetMapping("/inventory/{username}")
    public Inventory getInventory(@PathVariable String username) {
        User user = restTemplate.getForObject("http://localhost:8081/user/" + username, User.class);
        return inventoryService.getInventory(user.getIdInventory());
    }

    @PostMapping("/inventory/create")
    public ResponseEntity<Inventory> createInventory() {
        Inventory inventory = new Inventory();
        inventoryService.saveInventory(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }
}
