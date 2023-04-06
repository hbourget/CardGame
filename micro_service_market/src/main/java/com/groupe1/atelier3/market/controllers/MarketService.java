package com.groupe1.atelier3.market.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.controllers.InventoryService;
import com.groupe1.atelier3.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MarketService {
    @Autowired
    private InventoryService inventoryService;

    public Card buyCard(String username, Integer cardId) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Card> card = cardRepository.findById(cardId);
        if (user.isPresent()) {
            //check if balance is enough
            if (user.get().getBalance() < card.get().getPrice()) {
                return null;
            }
            //check if card is already in inventory
            if (user.get().getInventory().getCards().contains(card.get())) {
                return null;
            }
            userService.substractBalance(username, card.get().getPrice());
            Card cardEntity = card.get();
            inventoryService.addCardToInv(user.get().getInventory().getId(), cardEntity);
        }
        return cardMapper.toDTO(card.get());
    }

    public Card sellCard(String username, Integer cardId) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Card> card = cardRepository.findById(cardId);
        if (user.isPresent()) {
            //check if card is in inventory
            if (!user.get().getInventory().getCards().contains(card.get())) {
                return null;
            }
            userService.addBalance(username, card.get().getPrice());
            Card cardEntity = card.get();
            inventoryService.removeCardFromInv(user.get().getInventory().getId(), cardEntity);
        }
        return cardMapper.toDTO(card.get());
    }
}
