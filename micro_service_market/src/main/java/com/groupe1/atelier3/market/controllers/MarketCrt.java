package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/buy/{idUser}/{cardId}")
    public Object BuyCard(@PathVariable Integer idUser, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/cards/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        return marketService.buyCard(user, card);
    }

    @PostMapping("/sell/{idUser}/{cardId}")
    public Object SellCard(@PathVariable Integer idUser, @PathVariable Integer cardId) {
        Card card = restTemplate.getForObject("http://localhost:8082/cards/" + cardId, Card.class);
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        return marketService.sellCard(user, card);
    }

    @PostMapping("/sell/{idUser}")
    public Object SellAllCards(@PathVariable Integer idUser) {
        User user = restTemplate.getForObject("http://localhost:8081/users/" + idUser, User.class);
        return marketService.sellAllCards(user);
    }
}
