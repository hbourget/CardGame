package com.groupe1.atelier3.cards.controllers;

import com.groupe1.atelier3.cards.models.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

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
    public CardDTO AddCard(@RequestBody CardDTO card) {
        System.out.println("test");
        return cService.addCard(card);
    }

    @GetMapping("/cards")
    public Iterable<CardDTO> getAllCards() {
        return cService.getAllCards();
    }

}


