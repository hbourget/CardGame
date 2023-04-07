package com.groupe1.atelier3.cards.models;

import java.util.List;

public class CardWrapper {
    private CardDTO card;
    private List<CardDTO> cards;

    // Getters and Setters
    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }
}
