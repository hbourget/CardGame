package com.groupe1.atelier3.cards.controllers;

import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Order(1)
public class CardCrt {
    @Autowired
    private CardService cService;


    public CardCrt(CardService cService) {
        this.cService = cService;

    }

    @GetMapping("/card/{id}")
    public CardDTO GetCard(@PathVariable int id) {
        return cService.GetCard(id);
    }

    @PostMapping("/card")
    public List<CardDTO> addCards(@RequestBody CardWrapper cardWrapper) {
        if (cardWrapper.getCard() != null && cardWrapper.getCards() == null) {
            return Collections.singletonList(cService.addCard(cardWrapper.getCard()));
        } else if (cardWrapper.getCard() == null && cardWrapper.getCards() != null) {
            return cService.addCards(cardWrapper.getCards());
        } else {
            throw new IllegalArgumentException("Veuillez fournir une carte ou une liste de cartes, mais pas les deux.");
        }
    }

    @GetMapping("/cardsinterne")
    public Iterable<Card> getAllCardsInterne() {
        return cService.getAllCardsIntern();
    }

    @GetMapping("/cards")
    public Iterable<Card> getAllCards() {
        return cService.getAllCards();
    }

}


