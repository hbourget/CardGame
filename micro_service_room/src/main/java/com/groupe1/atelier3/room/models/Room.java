package com.groupe1.atelier3.room.models;

import jakarta.persistence.*;

import java.util.Random;

@Entity
@Table(name = "ROOM")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int idUser_1;
    private int idUser_2;
    private int idCardUser_1;
    private int idCardUser_2;
    private int cooldownUser_1;
    private int cooldownUser_2;
    private String status;
    private int idUserWinner;
    private int reward;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.idUser_1 = 0;
        this.idUser_2 = 0;
        this.idCardUser_1 = 0;
        this.idCardUser_2 = 0;
        this.cooldownUser_1 = 0;
        this.cooldownUser_2 = 0;
        this.status = "waiting";
        this.idUserWinner = 0;
        Random random = new Random();
        this.reward = random.nextInt(30 - 10 + 1) + 10;

    }
    public Room(String name) {
        this.name = name;
        this.idUser_1 = 0;
        this.idUser_2 = 0;
        this.idCardUser_1 = 0;
        this.idCardUser_2 = 0;
        this.cooldownUser_1 = 0;
        this.cooldownUser_2 = 0;
        this.status = "waiting";
        this.idUserWinner = 0;
        Random random = new Random();
        this.reward = random.nextInt(30 - 10 + 1) + 10;
    }
    public Room() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdUser_1() {
        return idUser_1;
    }

    public void setIdUser_1(int idUser_1) {
        this.idUser_1 = idUser_1;
    }

    public int getIdUser_2() {
        return idUser_2;
    }

    public void setIdUser_2(int idUser_2) {
        this.idUser_2 = idUser_2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdCardUser_1() {
        return idCardUser_1;
    }

    public void setIdCardUser_1(int idCardUser_1) {
        this.idCardUser_1 = idCardUser_1;
    }

    public int getIdCardUser_2() {
        return idCardUser_2;
    }

    public void setIdCardUser_2(int idCardUser_2) {
        this.idCardUser_2 = idCardUser_2;
    }

    public int getCooldownUser_1() {
        return cooldownUser_1;
    }

    public void setCooldownUser_1(int cooldownUser_1) {
        this.cooldownUser_1 = cooldownUser_1;
    }

    public int getCooldownUser_2() {
        return cooldownUser_2;
    }

    public void setCooldownUser_2(int cooldownUser_2) {
        this.cooldownUser_2 = cooldownUser_2;
    }

    public int getIdUserWinner() {
        return idUserWinner;
    }

    public void setIdUserWinner(int idUserWinner) {
        this.idUserWinner = idUserWinner;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
