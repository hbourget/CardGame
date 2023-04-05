package com.groupe1.atelier3.cards.models;
import com.groupe1.atelier3.cards.models.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface CardRepository extends CrudRepository<Card, Integer>{
    //public Card findByCardID(Integer cardID);
}