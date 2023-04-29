package com.groupe1.atelier3.room.controllers;

import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardDTO;
import com.groupe1.atelier3.room.models.Room;
import com.groupe1.atelier3.room.models.RoomWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Order(1)
public class RoomCrt {
    @Autowired
    private RoomService rService;

    public RoomCrt(RoomService rService) {
        this.rService = rService;
    }

    @GetMapping("/rooms/{idOrName}")
    public Object GetRoom(@PathVariable String idOrName) {
        try {
            int id = Integer.parseInt(idOrName);
            return rService.GetRoomById(id);
        } catch (NumberFormatException e) {
            return rService.GetRoomByName(idOrName);
        }
    }

    @GetMapping("/rooms")
    public Iterable<Room> getAllRooms() {
        return rService.getAllRooms();
    }

    @PostMapping("/rooms")
    public ResponseEntity<Room> addCard(@RequestBody Room room) {
        Room createdRoom = rService.addRoom(room);
        if (createdRoom == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        }
    }

    @PostMapping("/rooms/bulk")
    public ResponseEntity<List<Room>> addCards(@RequestBody List<Room> rooms) {
        List<Room> createdRooms = rService.addRooms(rooms);
        if (createdRooms.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRooms);
        }
    }

    @DeleteMapping("/rooms/{idRoom}")
    public Object deleteRoom(@PathVariable int idRoom) {
        return rService.deleteRoom(idRoom);
    }

    @DeleteMapping("/rooms")
    public Object deleteAllRooms() {
        return rService.deleteAllRooms();
    }

    @PutMapping("/rooms/join/{idRoom}/users/{playerId}")
    public Object joinRoom(@PathVariable int idRoom, @PathVariable String playerId) {
        return rService.joinRoom(idRoom, playerId);
    }

    @PutMapping("/rooms/leave/{idRoom}/users/{playerId}")
    public Object leaveRoom(@PathVariable int idRoom, @PathVariable String playerId) {
        return rService.leaveRoom(idRoom, playerId);
    }
    @PutMapping("/rooms/{idRoom}/users/{playerId}/cards/{cardId}")
    public Object addCardToRoom(@PathVariable int idRoom, @PathVariable int cardId, @PathVariable String playerId) {
        return rService.addCardToRoom(idRoom, cardId, playerId);
    }

    @PutMapping("/rooms/play/{idRoom}/users/{idUser}")
    public Object playRound(@PathVariable int idRoom, @PathVariable String idUser) {
        return rService.playRound(idRoom, idUser);
    }
}


