package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    private final String cardServiceUrl = "http://localhost:8082";

    private final RestTemplate restTemplate = new RestTemplate();

    public void addCardToInv(Integer inventoryId, Integer cardId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (inventory.getCards().contains(cardId)) {
            return;
        }

        inventory.getCards().add(cardId);
        inventoryRepository.save(inventory);
    }

    public void removeCardFromInv(Integer inventoryId, Integer cardId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (!inventory.getCards().contains(cardId)) {
            return;
        }
        inventory.getCards().remove(cardId);
        inventoryRepository.save(inventory);
    }

    public Object addAllCardToInv(Integer inventoryId, List<Card> cards) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        for (Card card : cards) {
            if (!inventory.getCards().contains(card.getId())) {
                inventory.getCards().add(card.getId());
            }
        }
        inventoryRepository.save(inventory);
        return ResponseEntity.status(HttpStatus.OK).body("Les cartes qu'il manquait ont été ajoutées à l'inventaire.");
    }

    //remove all existing cards in inventory
    public Object removeAllCardFromInv(Integer inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        inventory.getCards().clear();
        inventoryRepository.save(inventory);
        return ResponseEntity.status(HttpStatus.OK).body("Toutes les cartes ont été retirées de l'inventaire.");
    }

    public Inventory getInventory(Integer idInv) {
        return inventoryRepository.findById(idInv).get();
    }

    public List<Card> getInventoryCards(Integer idInv) {
        Inventory inv = inventoryRepository.findById(idInv).get();

        String urlGetCards = cardServiceUrl + "/card/{id}";
        return inv.getCards().stream().map(cardId -> restTemplate.getForObject(urlGetCards, Card.class, cardId)).collect(Collectors.toList());
    }

    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }
}
