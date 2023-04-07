package com.groupe1.atelier3.room.models;

import jakarta.persistence.*;

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
    private int remainingCoupsUser_1;
    private int remainingCoupsUser_2;
    private String status;
    private int idUserWinner;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.idUser_1 = 0;
        this.idUser_2 = 0;
        this.idCardUser_1 = 0;
        this.idCardUser_2 = 0;
        this.remainingCoupsUser_1 = 5;
        this.remainingCoupsUser_2 = 5;
        this.status = "waiting";
        this.idUserWinner = 0;
    }
    public Room(String name) {
        this.name = name;
        this.idUser_1 = 0;
        this.idUser_2 = 0;
        this.idCardUser_1 = 0;
        this.idCardUser_2 = 0;
        this.remainingCoupsUser_1 = 5;
        this.remainingCoupsUser_2 = 5;
        this.status = "waiting";
        this.idUserWinner = 0;
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

    public int getRemainingCoupsUser_1() {
        return remainingCoupsUser_1;
    }

    public void setRemainingCoupsUser_1(int remainingCoupsUser_1) {
        this.remainingCoupsUser_1 = remainingCoupsUser_1;
    }

    public int getRemainingCoupsUser_2() {
        return remainingCoupsUser_2;
    }

    public void setRemainingCoupsUser_2(int remainingCoupsUser_2) {
        this.remainingCoupsUser_2 = remainingCoupsUser_2;
    }

    public int getIdUserWinner() {
        return idUserWinner;
    }

    public void setIdUserWinner(int idUserWinner) {
        this.idUserWinner = idUserWinner;
    }
}
