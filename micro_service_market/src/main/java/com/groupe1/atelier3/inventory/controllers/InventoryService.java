package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.InventoryRepository;
import com.groupe1.atelier3.inventory.models.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    private final String cardServiceUrl = "http://localhost:8082";

    private final RestTemplate restTemplate = new RestTemplate();

    public Object addCardToInv(Integer inventoryId, Integer cardId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (inventory.getCards().contains(cardId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La carte "+ cardId +" est déjà dans l'inventaire.");
        }
        if (getInventoryByCardId(cardId) != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte est déjà attribué à un autre utilisateur.");
        }

        inventory.getCards().add(cardId);
        inventoryRepository.save(inventory);
        return getInventoryCardsIds(inventoryId);
    }

    public Object removeCardFromInv(Integer inventoryId, Integer cardId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (!inventory.getCards().contains(cardId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La carte n'est pas dans l'inventaire.");
        }
        inventory.getCards().remove(cardId);
        inventoryRepository.save(inventory);
        return getInventoryCardsIds(inventoryId);
    }

    public Object addAllCardToInv(Integer inventoryId, List<Card> cards) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        for (Card card : cards) {
            if (!inventory.getCards().contains(card.getId())) {
                inventory.getCards().add(card.getId());
            }
        }
        inventoryRepository.save(inventory);
        return getInventoryCards(inventoryId);
    }

    //remove all existing cards in inventory
    public Object removeAllCardFromInv(Integer inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        inventory.getCards().clear();
        inventoryRepository.save(inventory);
        return getInventoryCards(inventoryId);
    }

    public Inventory getInventory(Integer idInv) {
        return inventoryRepository.findById(idInv).get();
    }

    public InventoryResponse getInventoryCards(Integer idInv) {
        List<Card> cards = new ArrayList<>();
        Inventory inv = inventoryRepository.findById(idInv).get();
        for (Integer cardId : inv.getCards()) {
            Card card = restTemplate.getForObject(cardServiceUrl + "/cards/" + cardId, Card.class);
            cards.add(card);
        }
        return new InventoryResponse(inv, cards);
    }

    public List<Integer> getInventoryCardsIds(Integer idInv) {
        Inventory inv = inventoryRepository.findById(idInv).get();
        return inv.getCards();
    }

    public Inventory getInventoryByCardId(Integer cardId) {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        List<Inventory> inventoriesList = new ArrayList<>();
        inventories.forEach(inventoriesList::add);
        for (Inventory inventory : inventoriesList) {
            if (inventory.getCards().contains(cardId)) {
                return inventory;
            }
        }
        return null;
    }

    public List<Inventory> getAllInventories() {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        List<Inventory> inventoriesList = new ArrayList<>();
        inventories.forEach(inventoriesList::add);
        return inventoriesList;
    }

    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }
}
