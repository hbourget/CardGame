package com.groupe1.atelier3.cards.controllers;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.cards.models.CardRepository;
import org.hibernate.cache.spi.access.CachedDomainDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    private CardMapper cardMapper = new CardMapper();

    public CardDTO addCard(CardDTO cardDTO) {
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        Iterable<Card> cards = cardRepository.findAll();
        return cardMapper.toDTO(card);
    }

    public List<CardDTO> addCards(List<CardDTO> cards) {
        List<CardDTO> savedCards = new ArrayList<>();

        for (CardDTO card : cards) {
            savedCards.add(card);
            cardRepository.save(cardMapper.toEntity(card));
        }

        return savedCards;
    }

    public CardDTO GetCard(int id) {
        Optional<Card> cardOpt = cardRepository.findById(id);
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            return cardMapper.toDTO(card);
        }
        return null;
    }

    public Iterable<Card> getAllCardsIntern() {
        Iterable<Card> cards = cardRepository.findAll();
        /*List<Card> cardsList = cards.Tolist();
        List<CardDTO> cardsDTO = new ArrayList<>();
        for (Card card : cards) {
            cardsDTO.add(cardMapper.toDTO(card));
        }*/
        return cards;
    }

    /*public Iterable<CardDTO> getAllCards() {
        Iterable<Card> cards = cardRepository.findAll();
        List<CardDTO> cardsDTO = new ArrayList<>();
        for (Card card : cards) {
            cardsDTO.add(cardMapper.toDTO(card));
        }
        return cardsDTO;
    }*/
    public Iterable<Card> getAllCards() {
        Iterable<Card> cards = cardRepository.findAll();
        return cards;
    }


}
