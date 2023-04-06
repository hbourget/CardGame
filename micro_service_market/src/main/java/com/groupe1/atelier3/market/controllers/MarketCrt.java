package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MarketCrt {
    @Autowired
    private final MarketService marketService;
    private final RestTemplate restTemplate = new RestTemplate();
    public MarketCrt(MarketService marketService) {
        this.marketService = marketService;
    }
    @PostMapping("/buy/{username}/{cardId}")
    public Card BuyCard(@PathVariable String username, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/card/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/user/" + username, User.class);
        return marketService.buyCard(user, card);
    }

    @PostMapping("/sell/{username}/{cardId}")
    public Card SellCard(@PathVariable String username, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/card/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/user/" + username, User.class);
        return marketService.sellCard(user, card);
    }
}
