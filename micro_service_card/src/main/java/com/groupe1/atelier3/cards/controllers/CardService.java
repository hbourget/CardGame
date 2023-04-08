package com.groupe1.atelier3.cards.controllers;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.cards.models.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    private CardMapper cardMapper = new CardMapper();

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

    public Object GetCard(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            return cardOpt.get();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
    }


    public void deleteCard(int id) {
        cardRepository.deleteById(id);
    }

    public void deleteAllCards() {
        cardRepository.deleteAll();
    }

    public Iterable<Card> getAllCards() {
        Iterable<Card> cards = cardRepository.findAll();
        return cards;
    }

    public Object updateCard(int id, Card cardNew) {
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
    }


}
