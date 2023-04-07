package com.groupe1.atelier3.room.controllers;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.room.models.Room;
import com.groupe1.atelier3.room.models.RoomRepository;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userServiceUrl = "http://localhost:8081";

    private final String cardServiceUrl = "http://localhost:8082";

    public Room addRoom(Room room) {
        Room roomFinal = new Room(room.getName());
        //check if room name is already taken
        Optional<Room> roomOpt = roomRepository.findByName(room.getName());
        if (roomOpt.isPresent()) {
            return null;
        }
        roomFinal = roomRepository.save(roomFinal);
        return roomFinal;
    }

    public List<Room> addRooms(List<Room> rooms) {
        List<Room> savedRooms = new ArrayList<>();

        for (Room room : rooms) {
            Optional<Room> roomOpt = roomRepository.findByName(room.getName());
            if (roomOpt.isPresent()) {
                return null;
            }
            Room roomFinal = new Room(room.getName());
            roomFinal = roomRepository.save(roomFinal);
            savedRooms.add(roomFinal);
        }
        return savedRooms;
    }

    public Object GetRoomById(int id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            return roomOpt.get();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
    }

    public Object GetRoomByName(String name) {
        Optional<Room> roomOpt = roomRepository.findByName(name);
        if (roomOpt.isPresent()) {
            return roomOpt.get();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
    }

    public Iterable<Room> getAllRooms() {
        Iterable<Room> rooms = roomRepository.findAll();
        return rooms;
    }

    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }

    public void addIdUser_1(int id, int idUser_1) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setIdUser_1(idUser_1);
            roomRepository.save(room);
        }
    }

    public void addIdUser_2(int id, int idUser_2) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setIdUser_2(idUser_2);
            roomRepository.save(room);
        }
    }

    public void updateStatus(int id, String status) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setStatus(status);
            roomRepository.save(room);
        }
    }

    public Object joinRoom(int id, int idUser) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        String url = userServiceUrl + "/user/" + idUser;
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        if (userdto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getIdUser_1() == 0) {
                room.setIdUser_1(idUser);
            } else if (room.getIdUser_2() == 0) {
                room.setIdUser_2(idUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La room est pleine.");
            }
            if (room.getIdUser_1() != 0 && room.getIdUser_2() != 0) {
                room.setStatus("ready");
            }

            roomRepository.save(room);
            return room;
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object leaveRoom(int id, int idUser) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        String url = userServiceUrl + "/user/" + idUser;
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        if (userdto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getIdUser_1() == idUser) {
                room.setIdUser_1(0);
            } else if (room.getIdUser_2() == idUser) {
                room.setIdUser_2(0);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'est pas dans la room.");
            }
            if (room.getIdUser_1() == 0 || room.getIdUser_2() == 0) {
                room.setStatus("waiting");
            }
            roomRepository.save(room);
            return room;
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object addCardToRoom(int roomId, int cardId, int playerId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        String urlUser = userServiceUrl + "/user/" + playerId;
        Object objUser = restTemplate.getForObject(urlUser, UserDTO.class);

        if (!(objUser instanceof UserDTO)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        String urlCard = cardServiceUrl + "/card/" + cardId;
        Object objCard = restTemplate.getForObject(urlCard, Card.class);

        if (!(objCard instanceof Card)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
        }

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getIdUser_1() == 0 || room.getIdUser_2() == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La room n'est pas prête.");
            }
            if (room.getIdUser_1() != playerId && room.getIdUser_2() != playerId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'est pas dans la room.");
            }
            if (room.getIdUser_1() == playerId) {
                room.setIdCardUser_1(cardId);
            } else if (room.getIdUser_2() == playerId) {
                room.setIdCardUser_2(cardId);
            }
            if (room.getIdCardUser_1() != 0 && room.getIdCardUser_2() != 0) {
                room.setStatus("started");
            }
            roomRepository.save(room);
            return room;
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object playRound(int roomId, int playerId)
    {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        String urlUser = userServiceUrl + "/user/" + playerId;
        Object objUser = restTemplate.getForObject(urlUser, UserDTO.class);

        if (!(objUser instanceof UserDTO)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getStatus().equals("ended")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La partie est terminée.");
            }
            if (room.getIdUser_1() != playerId && room.getIdUser_2() != playerId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'est pas dans la room.");
            }

            if(playerId == roomOpt.get().getIdUser_1()) {
                if(room.getRemainingCoupsUser_1() == 0) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'a plus de coups.");
                }
            }
            else {
                if(room.getRemainingCoupsUser_2() == 0) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'a plus de coups.");
                }
            }

            int idCardVictim = 0;
            int idCardAttacker = 0;
            if(playerId == roomOpt.get().getIdUser_1()) {
                idCardVictim = roomOpt.get().getIdCardUser_2();
                idCardAttacker = roomOpt.get().getIdCardUser_1();
            }
            else {
                idCardVictim = roomOpt.get().getIdCardUser_1();
                idCardAttacker = roomOpt.get().getIdCardUser_2();
            }
            String urlCard = cardServiceUrl + "/card/" + idCardVictim;
            Object objCardVictim = restTemplate.getForObject(urlCard, Card.class);

            if (!(objCardVictim instanceof Card)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
            }
            Card cardVictim = (Card) objCardVictim;
            int powerVictim = cardVictim.getPower();
            int healthVictim = cardVictim.getHealth();
            String typeVictim = cardVictim.getType();

            urlCard = cardServiceUrl + "/card/" + idCardAttacker;
            Object objCardAttacker = restTemplate.getForObject(urlCard, Card.class);

            if (!(objCardAttacker instanceof Card)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
            }

            Card cardAttacker = (Card) objCardAttacker;
            int powerAttacker = cardAttacker.getPower();
            int healthAttacker = cardAttacker.getHealth();
            String typeAttacker = cardAttacker.getType();

            //set health of victim according to powerAttacker and adjust regarding types (water, fire and earth)
            if(typeVictim.equals("water") && typeAttacker.equals("fire")) {
                healthVictim -= powerAttacker * 2;
            }
            else if(typeVictim.equals("fire") && typeAttacker.equals("earth")) {
                healthVictim -= powerAttacker * 2;
            }
            else if(typeVictim.equals("earth") && typeAttacker.equals("water")) {
                healthVictim -= powerAttacker * 2;
            }
            else if(typeVictim.equals("water") && typeAttacker.equals("earth")) {
                healthVictim -= powerAttacker / 2;
            }
            else if(typeVictim.equals("fire") && typeAttacker.equals("water")) {
                healthVictim -= powerAttacker / 2;
            }
            else if(typeVictim.equals("earth") && typeAttacker.equals("fire")) {
                healthVictim -= powerAttacker / 2;
            }
            else {
                healthVictim -= powerAttacker;
            }

            //set remove 1 coup from the attacker
            if(playerId == roomOpt.get().getIdUser_1()) {
                room.setRemainingCoupsUser_1(roomOpt.get().getRemainingCoupsUser_1() - 1);
            }
            else {
                room.setRemainingCoupsUser_2(roomOpt.get().getRemainingCoupsUser_2() - 1);
            }

            //check if both remaningCoups are 0
            if(roomOpt.get().getRemainingCoupsUser_1() == 0 && roomOpt.get().getRemainingCoupsUser_2() == 0) {
                room.setStatus("ended");
                //check whether its the card of the victim or the attacker that has the most health
                if(healthVictim > healthAttacker) {
                    room.setIdUserWinner(roomOpt.get().getIdUser_2());
                }
                else if(healthVictim < healthAttacker) {
                    room.setIdUserWinner(roomOpt.get().getIdUser_1());
                }
                else {
                    room.setIdUserWinner(0);
                }
                roomRepository.save(room);
                return room;
            }
        }
        return null;
    }

    public Object deleteRoom(int id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            roomRepository.delete(room);
            return ResponseEntity.status(HttpStatus.OK).body("La room " + room.getName() +" a été supprimée.");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }
}
