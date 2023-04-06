package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import com.groupe1.atelier3.users.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.groupe1.atelier3.inventory.models.Inventory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper = new UserMapper();

    private final RestTemplate restTemplate = new RestTemplate();
    private final String inventoryServiceUrl = "http://localhost:8083";

    public UserDTO GetUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        UserDTO userDto = userMapper.toDTO(userOptional.get());
        return userDto;
    }

    public Iterable<UserDTO> GetUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(userMapper.toDTO(user));
        }
        return usersDTO;
    }

    public void setBalance(String username, int amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        user.setBalance(amount);
        userRepository.save(user);
    }

    public void addBalance(String username, int amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public void substractBalance(String username, int amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }

    public void addInventory(String username, Inventory inventory) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        user.setInventory(inventory);
        userRepository.save(user);
    }

    public User saveUser(User user) {
        userRepository.save(user);

        String urlSaveInv = inventoryServiceUrl + "/inventory/create";
        Inventory inventory = restTemplate.postForObject(urlSaveInv, null, Inventory.class);

        user.setIdInventory(inventory.getId()); // Associer l'ID de l'inventaire à l'utilisateur
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
