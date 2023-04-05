package com.groupe1.atelier3.inventory.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.inventory.models.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public void addCardToInv(Integer inventoryId, Card card) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (inventory.getCards().contains(card)) {
            return;
        }
        inventory.getCards().add(card);
        inventoryRepository.save(inventory);
    }

    public void removeCardFromInv(Integer inventoryId, Card card) {
        Inventory inventory = inventoryRepository.findById(inventoryId).get();
        if (!inventory.getCards().contains(card)) {
            return;
        }
        inventory.getCards().remove(card);
        inventoryRepository.save(inventory);
    }

}
