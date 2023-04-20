package com.groupe1.atelier3.cards.controllers;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.cards.models.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    private CardMapper cardMapper = new CardMapper();

    private final String userServiceUrl = "http://localhost:8081";

    private final String inventoryServiceUrl = "http://localhost:8083";

    public Card addCard(CardDTO cardDTO) {
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return card;
    }

    public List<Card> addCards(List<CardDTO> cards) {
        List<Card> savedCards = new ArrayList<>();

        for (CardDTO cardDTO : cards) {
            Card card = cardMapper.toEntity(cardDTO);
            card = cardRepository.save(card);
            savedCards.add(card);
        }
        return savedCards;
    }

    public Card getCard(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            return cardOpt.get();
        }
        return null;
    }


    public boolean deleteCard(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            cardRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllCards() {
        cardRepository.deleteAll();
    }

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        cardRepository.findAll().forEach(cards::add);
        return cards;
    }

    public Card updateCard(int id, Card cardNew) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.setHealth(cardNew.getHealth());
            card.setEnergy(cardNew.getEnergy());
            card.setDescription(cardNew.getDescription());
            card.setImage(cardNew.getImage());
            card.setName(cardNew.getName());
            card.setType(cardNew.getType());
            card.setPrice(cardNew.getPrice());
            card.setPower(cardNew.getPower());
            card = cardRepository.save(card);
            return card;
        }
        return null;
    }


}
