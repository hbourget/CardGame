package com.groupe1.atelier3.users.models;
import com.groupe1.atelier3.inventory.models.Inventory;
import jakarta.persistence.*;

public class UserDTO {
    private String username;
    private double balance;
    @OneToOne
    private Inventory inventory;

    public UserDTO(String username, double balance, Inventory inventory) {
        this.username = username;
        this.balance = balance;
        this.inventory = inventory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
