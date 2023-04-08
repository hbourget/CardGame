package com.groupe1.atelier3.room.models;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.room.models.Room;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class RoomResponse {
    private Room room;
    private List<Card> cards;

    private ResponseEntity<String> responseEntity;

    public RoomResponse(Room room, List<Card> cards, ResponseEntity<String> responseEntity) {
        this.room = room;
        this.cards = cards;
        this.responseEntity = responseEntity;
    }

    public RoomResponse(Room room, List<Card> cards) {
        this.room = room;
        this.cards = cards;
    }

    public RoomResponse(Room room, ResponseEntity<String> responseEntity) {
        this.room = room;
        this.responseEntity = responseEntity;
    }

    public RoomResponse(Room room) {
        this.room = room;
    }

    public RoomResponse(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }

    public RoomResponse() {
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity<String> responseEntity) {
        this.responseEntity = responseEntity;
    }

}