package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import com.groupe1.atelier3.users.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.groupe1.atelier3.inventory.models.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper = new UserMapper();

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

    public Inventory getInventoryById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();
        Inventory inv = findInventoryById(user.getIdInventory());
        return user.getInventory();
    }


}
