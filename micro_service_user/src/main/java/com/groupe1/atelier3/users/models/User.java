package com.groupe1.atelier3.users.models;
import com.groupe1.atelier3.inventory.models.Inventory;
import jakarta.persistence.*;

@Entity
@Table(name = "utilisateur")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String username;
  private String password;
  private double balance;
  private Integer idInventory;

  public User(Integer id, String username, String password, Integer idInventory) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.balance = 0;
    this.idInventory = idInventory;
  }

  public User(String username, String password, Integer idInventory) {
    this.username = username;
    this.password = password;
    this.balance = 0;
    this.idInventory = idInventory;
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.balance = 0;
  }

  public User() {}

  public Integer getId() { return id; }

  public String getUsername() { return username; }

  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }

  public void setPassword(String password) { this.password = password; }

  public double getBalance() { return balance; }

  public void setBalance(double balance) { this.balance = balance; }

  public Integer getIdInventory() { return this.idInventory; }

  public void setIdInventory(Integer idInventory) { this.idInventory = idInventory; }

  public String toString() {
    return ("User: " + username + " has " + balance + " dollars");
  }

  public void setInventory(Inventory inventory) {
    this.idInventory = inventory.getId();
  }
}
