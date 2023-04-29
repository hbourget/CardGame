package com.groupe1.atelier3.inventory.controllers;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.inventory.models.InventoryRepository;
import com.groupe1.atelier3.inventory.models.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    private final String cardServiceUrl = "http://localhost:8082";
    private final String userServiceUrl = "http://localhost:8081";

    private final RestTemplate restTemplate = new RestTemplate();

    public InventoryResponse addCardToInv(String userId, Integer cardId) {
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + userId, User.class);
        if (user == null) {
            return null;
        }

        ResponseEntity<Card> responseEntity = restTemplate.exchange(cardServiceUrl + "/cards/" + cardId, HttpMethod.GET, null, new ParameterizedTypeReference<Card>() {});
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        Inventory inventory = inventoryRepository.findById(user.getIdInventory()).get();
        if (inventory.getCards().contains(cardId)) {
            return null;
        }
        if (getInventoryByCardId(cardId) != null) {
            return null;
        }

        inventory.getCards().add(cardId);
        inventoryRepository.save(inventory);
        return getInventoryCards(user.getIdInventory());
    }

    public InventoryResponse removeCardFromInv(String userId, Integer cardId) {
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + userId, User.class);
        if (user == null) {
            return null;
        }
        Inventory inventory = inventoryRepository.findById(user.getIdInventory()).get();
        if (!inventory.getCards().contains(cardId)) {
            return null;
        }
        inventory.getCards().remove(cardId);
        inventoryRepository.save(inventory);
        return getInventoryCards(user.getIdInventory());
    }

    public InventoryResponse addAllCardToInv(String userId, List<Card> cards) {
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + userId, User.class);
        if (user == null) {
            return null;
        }
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(user.getIdInventory());
        if (!inventoryOptional.isPresent()) {
            return null;
        }
        Inventory inventory = inventoryOptional.get();
        for (Card card : cards) {
            if (!inventory.getCards().contains(card.getId())) {
                inventory.getCards().add(card.getId());
            }
        }
        inventoryRepository.save(inventory);
        return getInventoryCards(user.getIdInventory());
    }

    //remove all existing cards in inventory
    public InventoryResponse removeAllCardFromInv(String userId) {
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + userId, User.class);
        if (user == null) {
            return null;
        }
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(user.getIdInventory());
        if (!inventoryOptional.isPresent()) {
            return null;
        }
        Inventory inventory = inventoryOptional.get();
        inventory.getCards().clear();
        inventoryRepository.save(inventory);
        return getInventoryCards(user.getIdInventory());
    }

    public boolean deleteInventory(String idUser) {
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + idUser, User.class);
        if (user == null) {
            return false;
        }
        Integer idInv = user.getIdInventory();
        if (inventoryRepository.findById(idInv).isPresent()) {
            inventoryRepository.deleteById(idInv);
            return true;
        }
        return false;
    }

    public Inventory getInventory(Integer idInv) {
        return inventoryRepository.findById(idInv).get();
    }

    public InventoryResponse getInventoryCards(Integer idUser) {
        List<Card> cards = new ArrayList<>();
        User user = restTemplate.getForObject(userServiceUrl + "/users/" + idUser, User.class);
        if (user == null) {
            return null;
        }
        Inventory inv = inventoryRepository.findById(user.getIdInventory()).get();
        for (Integer cardId : inv.getCards()) {
            Card card = restTemplate.getForObject(cardServiceUrl + "/cards/" + cardId, Card.class);
            cards.add(card);
        }
        return new InventoryResponse(inv, cards);
    }

    public List<Integer> getInventoryCardsIds(Integer idInv) {
        Inventory inv = inventoryRepository.findById(idInv).get();
        return inv.getCards();
    }

    public Inventory getInventoryByCardId(Integer cardId) {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        List<Inventory> inventoriesList = new ArrayList<>();
        inventories.forEach(inventoriesList::add);
        for (Inventory inventory : inventoriesList) {
            if (inventory.getCards().contains(cardId)) {
                return inventory;
            }
        }
        return null;
    }

    public List<Inventory> getAllInventories() {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        List<Inventory> inventoriesList = new ArrayList<>();
        inventories.forEach(inventoriesList::add);
        return inventoriesList;
    }

    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public Card buyCard(User user, Card card) {
        if (user != null) {

            if (user.getBalance() < card.getPrice()) {
                return null;
            }

            Integer idInv = user.getIdInventory();
            if (getInventory(idInv).getCards().contains(card.getId())) {
                return null;
            }

            if (getInventoryByCardId(card.getId()) != null) {
                return null;
            }

            String url = userServiceUrl + "/users/{id}/subtractbalance";
            HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());

            addCardToInv(user.getUsername(), card.getId());
        }
        return card;
    }

    public Card sellCard(User user, Card card) {
        if (user != null) {
            Integer idInv = user.getIdInventory();
            if (!getInventory(idInv).getCards().contains(card.getId())) {
                return null;
            }
            String url = userServiceUrl + "/users/{id}/addbalance";
            HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());

            removeCardFromInv(user.getUsername(), card.getId());
        }
        return card;
    }
    public boolean sellAllCards(User user) {
        if (user != null) {

            Integer idInv = user.getIdInventory();
            if (getInventory(idInv).getCards().isEmpty()) {
                return false;
            }

            for (Integer cardId : getInventory(idInv).getCards()) {
                String urlCard = "http://localhost:8082/cards/" + cardId;
                ResponseEntity<Card> response = restTemplate.getForEntity(urlCard, Card.class);
                Card card = response.getBody();

                String url = userServiceUrl + "/users/{id}/addbalance";
                HttpEntity<Integer> requestEntity = new HttpEntity<>(card.getPrice());
                restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class, user.getId());
            }
            removeAllCardFromInv(user.getUsername());
        }
        else {
            return false;
        }
        return true;
    }

    public List<Card> getAllAvailableCards() {
        //get cards that are not already present in a user's inventory
        List<Card> cards = new ArrayList<>();
        List<Inventory> inventories = getAllInventories();
        List<Integer> cardsInInventories = new ArrayList<>();
        for (Inventory inventory : inventories) {
            cardsInInventories.addAll(inventory.getCards());
        }
        String url = cardServiceUrl + "/cards";
        ResponseEntity<List<Card>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {
        });
        List<Card> allCards = response.getBody();
        for (Card card : allCards) {
            if (!cardsInInventories.contains(card.getId())) {
                cards.add(card);
            }
        }
        return cards;
    }
}
