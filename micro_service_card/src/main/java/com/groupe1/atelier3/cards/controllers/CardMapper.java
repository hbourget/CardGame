package com.groupe1.atelier3.cards.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;

public class CardMapper {

    public CardDTO toDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setName(card.getName());
        cardDTO.setDescription(card.getDescription());
        cardDTO.setPower(card.getPower());
        cardDTO.setHealth(card.getHealth());
        cardDTO.setPrice(card.getPrice());
        cardDTO.setImage(card.getImage());
        cardDTO.setType(card.getType());
        cardDTO.setEnergy(card.getEnergy());
        return cardDTO;
    }

    public Card toEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setName(cardDTO.getName());
        card.setDescription(cardDTO.getDescription());
        card.setPower(cardDTO.getPower());
        card.setHealth(cardDTO.getHealth());
        card.setPrice(cardDTO.getPrice());
        card.setImage(cardDTO.getImage());
        card.setType(cardDTO.getType());
        card.setEnergy(cardDTO.getEnergy());
        return card;
    }

}