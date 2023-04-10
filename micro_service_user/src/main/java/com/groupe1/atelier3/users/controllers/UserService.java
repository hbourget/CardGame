package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import com.groupe1.atelier3.users.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.groupe1.atelier3.inventory.models.Inventory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper = new UserMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String inventoryServiceUrl = "http://localhost:8083";

    public UserDTO getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UserDTO userDto = userMapper.toDTO(userOptional.get());
            return userDto;
        } else {
            return null;
        }
    }

    public UserDTO getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserDTO userDto = userMapper.toDTO(userOptional.get());
            return userDto;
        } else {
            return null;
        }
    }

    public Object getUserAuthById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    public Object getUserAuthByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    public List<UserDTO> getUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(userMapper.toDTO(user));
        }
        return usersDTO;
    }

    public UserDTO addBalance(Integer idUser, int amount) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDTO subtractBalance(Integer idUser, int amount) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public User saveUser(User user) {
        userRepository.save(user);
        String urlSaveInv = inventoryServiceUrl + "/inventories";
        Inventory inventory = restTemplate.postForObject(urlSaveInv, null, Inventory.class);
        user.setIdInventory(inventory.getId());
        User savedUser = userRepository.save(user);
        return savedUser;
    }


    public UserDTO updateUser(int id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setBalance(user.getBalance());
            userRepository.save(userToUpdate);
            return userMapper.toDTO(userToUpdate);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToDelete = userOptional.get();
            String urlDeleteInv = inventoryServiceUrl + "/inventories/users/" + userToDelete.getIdInventory();
            restTemplate.delete(urlDeleteInv);
            userRepository.delete(userToDelete);
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
