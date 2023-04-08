package com.groupe1.atelier3.cards.models;

public class CardDTO {
    //class Card with attributes name, description, power , health, price and image
    private Integer id;
    private String name;
    private String description;
    private int power;
    private int health;
    private String type;
    private int energy;
    private int price;
    private String image;

    //constructor
    public CardDTO(Integer id, String name, String description, int power, int health, int price, String image, String type, int energy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.power = power;
        this.health = health;
        this.price = price;
        this.image = image;
        this.type = type;
        this.energy = energy;
    }
    public CardDTO(String name, String description, int power, int health, int price, String image, String type, int energy) {
        this.name = name;
        this.description = description;
        this.power = power;
        this.health = health;
        this.price = price;
        this.image = image;
        this.type = type;
        this.energy = energy;
    }

    public CardDTO() {

    }
    //getters and setters

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

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

}