package com.groupe1.atelier3.inventory.models;

import java.util.*;

public class Inventory {

    private int id;
    private List<Integer> idCards;

    public Inventory(int id, List<Integer> idCards) {
        this.id = id;
        this.idCards = idCards;
    }
    public Inventory(ArrayList<Integer> idCards) {
        this.idCards = idCards;
    }

    public Inventory() {
        this.idCards = new ArrayList<Integer>();
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public List<Integer> getCards() {
        return this.idCards;
    }

    public void setCards(List<Integer> idCards) {
        this.idCards = idCards;
    }
}
