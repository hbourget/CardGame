package com.groupe1.atelier3.users.models;
import com.groupe1.atelier3.inventory.models.Inventory;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "utilisateur")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String username;
  private String password;
  private double balance;
  @OneToOne
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  private Inventory inventory;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.balance = 0;
    this.inventory = new Inventory();
  }

  public User() {}

  public Integer getId() { return id; }

  public String getUsername() { return username; }

  public String getPassword() { return password; }

  public double getBalance() { return balance; }

  public void setBalance(double balance) { this.balance = balance; }
  public Inventory getInventory() { return inventory; }

  public String toString() {
    return ("User: " + username + " has " + balance + " coins and " +
            inventory.getCards().size() + " cards");
  }
}
