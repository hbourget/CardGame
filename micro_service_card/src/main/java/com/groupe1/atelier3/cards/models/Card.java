package com.groupe1.atelier3.cards.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CARD")
public class Card {
    //class Card with attributes id, name, description, power , health, price and image
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private int power;
    private int health;
    private int price;
    private String image;

    //constructor
    public Card(Integer id, String name, String description, int power, int health, int price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.power = power;
        this.health = health;
        this.price = price;
        this.image = image;
    }
    public Card() {

    }
    //getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString () {
        return "Card [id=" + id + ", name=" + name + ", description=" + description + ", power=" + power + ", health=" + health + ", price=" + price + ", image=" + image + "]";
    }
}