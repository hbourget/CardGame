package com.groupe1.atelier3.inventory.models;

import com.groupe1.atelier3.cards.models.Card;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<Card> cards;

    public Inventory(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
    }
    public Inventory(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Inventory() {
        this.cards = new ArrayList<Card>();
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
