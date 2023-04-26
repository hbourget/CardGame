package com.groupe1.atelier3.room.controllers;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.InventoryResponse;
import com.groupe1.atelier3.room.models.Room;
import com.groupe1.atelier3.room.models.RoomRepository;
import com.groupe1.atelier3.room.models.RoomResponse;
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
import java.util.Random;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userServiceUrl = "http://localhost:8081";
    private final String cardServiceUrl = "http://localhost:8082";
    private final String marketServiceUrl = "http://localhost:8083";

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
        RoomResponse roomResponse;
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            List<Card> cards = new ArrayList<>();
            if (room.getIdUser_1() != 0) {
                if (room.getIdCardUser_1() != 0) {
                    Card card1 = restTemplate.getForObject(cardServiceUrl + "/cards/" + room.getIdCardUser_1(), Card.class);
                    cards.add(card1);
                }
            }
            if (room.getIdUser_2() != 0) {
                if (room.getIdCardUser_1() != 0) {
                    Card card2 = restTemplate.getForObject(cardServiceUrl + "/cards/" + room.getIdCardUser_2(), Card.class);
                    cards.add(card2);
                }
            }
            roomResponse = new RoomResponse(room, cards);
            return roomResponse;
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object GetRoomByName(String name) {
        Optional<Room> roomOpt = roomRepository.findByName(name);
        RoomResponse roomResponse;
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            List<Card> cards = new ArrayList<>();
            if (room.getIdUser_1() != 0) {
                Card card1 = restTemplate.getForObject(cardServiceUrl + "/cards/" + room.getIdCardUser_1(), Card.class);
                cards.add(card1);
            }
            if (room.getIdUser_2() != 0) {
                Card card2 = restTemplate.getForObject(cardServiceUrl + "/cards/" + room.getIdCardUser_2(), Card.class);
                cards.add(card2);
            }
            roomResponse = new RoomResponse(room, cards);
            return roomResponse;
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Iterable<Room> getAllRooms() {
        Iterable<Room> rooms = roomRepository.findAll();
        return rooms;
    }

    public Object deleteAllRooms() {
        roomRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("Toutes les rooms ont été supprimées.");
    }


    public Object joinRoom(int id, int idUser) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        String url = userServiceUrl + "/users/" + idUser;
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        if (userdto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        if (checkIfUserIsInARoom(idUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur est déjà dans une room.");
        }

        if (roomOpt.isPresent()) {
            if (roomOpt.get().getStatus().equals("Ended")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La room est terminée.");
            }
            Room room = roomOpt.get();
            if (room.getIdUser_1() == 0) {
                room.setIdUser_1(idUser);
            } else if (room.getIdUser_2() == 0) {
                room.setIdUser_2(idUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La room est pleine.");
            }
            if (room.getIdUser_1() == 0 || room.getIdUser_2() == 0) {
                room.setStatus("Waiting 0/2");
            }
            if (room.getIdUser_1() != 0 && room.getIdUser_2() == 0) {
                room.setStatus("Waiting 1/2");
            }
            if (room.getIdUser_1() == 0 && room.getIdUser_2() != 0) {
                room.setStatus("Waiting 1/2");
            }
            if (room.getIdUser_1() != 0 && room.getIdUser_2() != 0) {
                room.setStatus("Ready");
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
        String url = userServiceUrl + "/users/" + idUser;
        UserDTO userdto = restTemplate.getForObject(url, UserDTO.class);

        if (userdto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getIdUser_1() == idUser) {
                if (room.getStatus().equals("Started")) {
                    room.setStatus("Ended");
                    room.setIdUserWinner(room.getIdUser_2());
                    roomRepository.save(room);
                    return room;
                }
                else {
                    room.setIdUser_1(0);
                    room.setIdCardUser_1(0);
                }
            } else if (room.getIdUser_2() == idUser) {
                if (room.getStatus().equals("Started")) {
                    room.setStatus("Ended");
                    room.setIdUserWinner(room.getIdUser_1());
                    roomRepository.save(room);
                    return room;
                }
                else {
                    room.setIdUser_2(0);
                    room.setIdCardUser_2(0);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'est pas dans la room.");
            }
            if (room.getIdUser_1() == 0 || room.getIdUser_2() == 0) {
                room.setStatus("Waiting 0/2");
            }
            if (room.getIdUser_1() != 0 && room.getIdUser_2() == 0) {
                room.setStatus("Waiting 1/2");
            }
            if (room.getIdUser_1() == 0 && room.getIdUser_2() != 0) {
                room.setStatus("Waiting 1/2");
            }
            roomRepository.save(room);
            return room;
        }
        else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object addCardToRoom(int roomId, int cardId, int playerId) {


        Optional<Room> roomOpt = roomRepository.findById(roomId);
        String urlUser = userServiceUrl + "/users/" + playerId;
        Object objUser = restTemplate.getForObject(urlUser, UserDTO.class);

        if (!(objUser instanceof UserDTO)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
        }

        String urlCard = cardServiceUrl + "/cards/" + cardId;
        Object objCard = restTemplate.getForObject(urlCard, Card.class);

        if (!(objCard instanceof Card)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
        }

        //check if user has the card using the market service url
        String urlInventory = marketServiceUrl + "/inventories/users/" + playerId;
        //this will return the list of the cards that the user has in his inventory
        InventoryResponse invR = restTemplate.getForObject(urlInventory, InventoryResponse.class);
        List<Integer> cards = invR.getInventory().getCards();

        if (!cards.contains(cardId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'a pas la carte.");
        }

        //check if card energy is > 5
        Card card = (Card) objCard;
        if (card.getEnergy() <= 5) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La carte n'a pas assez d'energy pour participer.");
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
                room.setStatus("Started");
            }
            roomRepository.save(room);
            List<Card> cardsList = new ArrayList<>();
            cardsList.add(card);
            return new RoomResponse(room, cardsList);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    public Object playRound(int roomId, int playerId)
    {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        String urlUser = userServiceUrl + "/users/" + playerId;
        Object objUser = restTemplate.getForObject(urlUser, UserDTO.class);


        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getStatus().equals("Ended")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La room est terminée.");
            }
            if (!room.getStatus().equals("Started")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La partie n'a pas encore commencé, les 2 joueurs doivent selectionné une carte.");
            }

            if (!(objUser instanceof UserDTO)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur n'existe pas.");
            }
            if (room.getIdUser_1() != playerId && room.getIdUser_2() != playerId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur n'est pas dans la room.");
            }

            if(playerId == roomOpt.get().getIdUser_1()) {
                if(room.getCooldownUser_1() == 1) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur a déjà joué ce tour.");
                }
            }
            else {
                if(room.getCooldownUser_2() == 1) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur a déjà joué ce tour.");
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
            String urlCard = cardServiceUrl + "/cards/" + idCardVictim;
            Object objCardVictim = restTemplate.getForObject(urlCard, Card.class);

            if (!(objCardVictim instanceof Card)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas.");
            }

            Card cardVictim = (Card) objCardVictim;
            int powerVictim = cardVictim.getPower();
            int healthVictim = cardVictim.getHealth();
            String typeVictim = cardVictim.getType();

            urlCard = cardServiceUrl + "/cards/" + idCardAttacker;
            Object objCardAttacker = restTemplate.getForObject(urlCard, Card.class);

            if (!(objCardAttacker instanceof Card)) {
                return new RoomResponse(room, ResponseEntity.status(HttpStatus.NOT_FOUND).body("La carte n'existe pas."));
            }

            Card cardAttacker = (Card) objCardAttacker;
            int powerAttacker = cardAttacker.getPower();
            int healthAttacker = cardAttacker.getHealth();
            String typeAttacker = cardAttacker.getType();

            if(typeAttacker.equals("Fire")) {
                if(typeVictim.equals("Water")) {
                    healthVictim = healthVictim - (powerAttacker / 2);
                }
                else if(typeVictim.equals("Earth")) {
                    healthVictim = healthVictim - (powerAttacker * 2);
                }
                else {
                    healthVictim = healthVictim - powerAttacker;
                }
            }
            else if(typeAttacker.equals("Water")) {
                if(typeVictim.equals("Earth")) {
                    healthVictim = healthVictim - (powerAttacker / 2);
                }
                else if(typeVictim.equals("Fire")) {
                    healthVictim = healthVictim - (powerAttacker * 2);
                }
                else {
                    healthVictim = healthVictim - powerAttacker;
                }
            }
            else if(typeAttacker.equals("Earth")) {
                if(typeVictim.equals("Fire")) {
                    healthVictim = healthVictim - (powerAttacker / 2);
                }
                else if(typeVictim.equals("Water")) {
                    healthVictim = healthVictim - (powerAttacker * 2);
                }
                else {
                    healthVictim = healthVictim - powerAttacker;
                }
            }
            else {
                healthVictim = healthVictim - powerAttacker;
            }

            Random rand = new Random();
            int random = rand.nextInt(100);
            if(random < 30) {
                healthVictim = healthVictim - powerAttacker / 2;
            }

            cardVictim.setHealth(healthVictim);
            restTemplate.put(cardServiceUrl + "/cards/" + idCardVictim, cardVictim);

            //set remove 1 coup from the attacker
            if(playerId == roomOpt.get().getIdUser_1()) {
                room.setCooldownUser_1(1);
                room.setCooldownUser_2(0);
            }
            else {
                room.setCooldownUser_2(1);
                room.setCooldownUser_1(0);
            }

            //check if both remaningCoups are 0
            if(cardVictim.getHealth() <= 0) {
                room.setStatus("Ended");
                if(playerId == room.getIdUser_1()) {
                    room.setIdUserWinner(room.getIdUser_1());
                    String urlUserWinner = userServiceUrl + "/users/" + room.getIdUser_1() + "/addbalance";
                    restTemplate.put(urlUserWinner, room.getReward(), UserDTO.class);
                }
                else {
                    room.setIdUserWinner(room.getIdCardUser_2());
                    String urlUserWinner = userServiceUrl + "/users/" + room.getIdUser_2() + "/addbalance";
                    restTemplate.put(urlUserWinner, room.getReward(), UserDTO.class);
                }
                List<Card> cards = new ArrayList<>();
                Card instantVictim = new Card(cardVictim.getId() ,cardVictim.getName(), cardVictim.getDescription(), cardVictim.getPower(),  cardVictim.getHealth(), cardVictim.getPrice(), cardVictim.getImage(), cardVictim.getType(), cardVictim.getEnergy());
                Card instantAttacker = new Card(cardAttacker.getId() ,cardAttacker.getName(), cardAttacker.getDescription(), cardAttacker.getPower(),  cardAttacker.getHealth(), cardAttacker.getPrice(), cardAttacker.getImage(), cardAttacker.getType(), cardAttacker.getEnergy());
                cards.add(instantVictim);
                cards.add(instantAttacker);
                cardVictim.setEnergy(cardVictim.getEnergy() -10);
                cardVictim.setHealth(150);
                cardAttacker.setEnergy(cardVictim.getEnergy() -5);
                cardAttacker.setHealth(150);
                restTemplate.put(cardServiceUrl + "/cards/" + idCardVictim, cardVictim);
                restTemplate.put(cardServiceUrl + "/cards/" + idCardAttacker, cardAttacker);
                roomRepository.save(room);

                return new RoomResponse(room, cards, ResponseEntity.status(HttpStatus.OK).body("La partie est terminée."));
            }
            else
            {
                List<Card> cards = new ArrayList<>();
                cards.add(cardVictim);
                cards.add(cardAttacker);
                roomRepository.save(room);
                return new RoomResponse(room, cards, ResponseEntity.status(HttpStatus.OK).body("La partie continue."));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
    }

    public Object deleteRoom(int id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            roomRepository.delete(room);
            return new RoomResponse(room, ResponseEntity.status(HttpStatus.OK).body("La room a été supprimée."));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La room n'existe pas.");
        }
    }

    //check if user is already in a room
    public boolean checkIfUserIsInARoom(int idUser) {
        Iterable<Room> rooms = roomRepository.findAll();
        for(Room room : rooms) {
            if(room.getIdUser_1() == idUser || room.getIdUser_2() == idUser) {
                if (!room.getStatus().equals("Ended")) {
                    return true;
                }
            }
        }
        return false;
    }
}
