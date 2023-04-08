package com.groupe1.atelier3.inventory.models;

import com.groupe1.atelier3.cards.models.Card;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class InventoryResponse {
    private Inventory inventory;
    private List<Card> cards;
    private ResponseEntity<String> responseEntity;

    public InventoryResponse(Inventory inventory, List<Card> cards, ResponseEntity<String> responseEntity) {
        this.inventory = inventory;
        this.cards = cards;
        this.responseEntity = responseEntity;
    }

    public InventoryResponse(Inventory inventory, List<Card> cards) {
        this.inventory = inventory;
        this.cards = cards;
    }

    public InventoryResponse(Inventory inventory, ResponseEntity<String> responseEntity) {
        this.inventory = inventory;
        this.responseEntity = responseEntity;
    }

    public InventoryResponse(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryResponse(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }

    public InventoryResponse(List<Card> cards) {
        this.cards = cards;
    }

    public InventoryResponse() {
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }
}
