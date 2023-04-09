package com.groupe1.atelier3.cards.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardCrt {
    @Autowired
    private CardService cService;

    public CardCrt(CardService cService) {
        this.cService = cService;
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<Card> getCard(@PathVariable int id) {
        Card card = cService.getCard(id);
        if (card == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(card);
        }
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cService.getAllCards();
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(cards);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<Card> addCard(@RequestBody CardDTO card) {
        Card createdCard = cService.addCard(card);
        if (createdCard == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
        }
    }

    @PostMapping("/cards/bulk")
    public ResponseEntity<List<Card>> addCards(@RequestBody List<CardDTO> cards) {
        List<Card> createdCards = cService.addCards(cards);
        if (createdCards.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCards);
        }
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable int id) {
        boolean cardDeleted = cService.deleteCard(id);
        if (cardDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cards")
    public ResponseEntity<Void> deleteAllCards() {
        cService.deleteAllCards();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cards/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable int id, @RequestBody Card card) {
        Card updatedCard = cService.updateCard(id, card);
        if (updatedCard == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedCard);
        }
    }
}
