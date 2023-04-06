package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketCrt {
    @Autowired
    private final MarketService marketService;
    public MarketCrt(MarketService marketService) {
        this.marketService = marketService;
    }
    @PostMapping("/buy/{username}/{cardId}")
    public Card BuyCard(@PathVariable String username, @PathVariable Integer cardId) {
        return marketService.buyCard(username, cardId);
    }

    @PostMapping("/sell/{username}/{cardId}")
    public Card SellCard(@PathVariable String username, @PathVariable Integer cardId) {
        return marketService.sellCard(username, cardId);
    }

}
