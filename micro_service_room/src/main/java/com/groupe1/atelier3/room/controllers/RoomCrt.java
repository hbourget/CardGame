package com.groupe1.atelier3.room.controllers;

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

    @GetMapping("/room/{idOrName}")
    public Object GetRoom(@PathVariable String idOrName) {
        try {
            int id = Integer.parseInt(idOrName);
            return rService.GetRoomById(id);
        } catch (NumberFormatException e) {
            return rService.GetRoomByName(idOrName);
        }
    }

    @PostMapping("/room")
    public ResponseEntity<List<Room>> addRooms(@RequestBody RoomWrapper roomWrapper) {
        if (roomWrapper.getRoom() != null && roomWrapper.getRooms() == null) {
            Room result = rService.addRoom(roomWrapper.getRoom());
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(Collections.singletonList(result));
        } else if (roomWrapper.getRoom() == null && roomWrapper.getRooms() != null) {
            List<Room> results = rService.addRooms(roomWrapper.getRooms());
            if (results == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(results);
        } else {
            throw new IllegalArgumentException("Veuillez fournir une carte ou une liste de cartes, mais pas les deux.");
        }
    }

    @GetMapping("/rooms")
    public Iterable<Room> getAllRooms() {
        return rService.getAllRooms();
    }

    @DeleteMapping("/room/{id}")
    public Object deleteRoom(@PathVariable int id) {
        return rService.deleteRoom(id);
    }

    @DeleteMapping("/rooms")
    public Object deleteAllRooms() {
        return rService.deleteAllRooms();
    }

    @PostMapping("/room/{id}/join/{playerId}")
    public Object joinRoom(@PathVariable int id, @PathVariable int playerId) {
        return rService.joinRoom(id, playerId);
    }

    @PostMapping("/room/{id}/leave/{playerId}")
    public Object leaveRoom(@PathVariable int id, @PathVariable int playerId) {
        return rService.leaveRoom(id, playerId);
    }
    @PostMapping("/room/{id}/addCard/{cardId}/{playerId}")
    public Object addCardToRoom(@PathVariable int id, @PathVariable int cardId, @PathVariable int playerId) {
        return rService.addCardToRoom(id, cardId, playerId);
    }

    @PostMapping("/room/{id}/playround/{idUser}")
    public Object playRound(@PathVariable int id, @PathVariable int idUser) {
        return rService.playRound(id, idUser);
    }

    @PostMapping("/room/{id}/delete")
    public Object stopRoom(@PathVariable int id) {
        return rService.deleteRoom(id);
    }
}


