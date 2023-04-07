package com.groupe1.atelier3.room.models;

import java.util.List;

public class RoomWrapper {
    private Room room;
    private List<Room> rooms;

    // Getters and Setters
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
