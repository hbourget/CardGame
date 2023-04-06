package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserCrt {
    //User controller from SOA project using UserService
    @Autowired
    private UserService uService;

    @GetMapping("/user/{username}")
    public UserDTO GetCard(@PathVariable String username) {
        return uService.GetUser(username);
    }

    @GetMapping("/users")
    public Iterable<UserDTO> GetCard() {
        return uService.GetUsers();
    }

    @PostMapping("/user/substractbalance/{username}/{balance}")
    public void SubstractBalance(@PathVariable String username, @PathVariable Integer balance) {
        uService.substractBalance(username, balance);
    }
    @PostMapping("/user/addbalance/{username}/{balance}")
    public void AddBalance(@PathVariable String username, @PathVariable Integer balance) {
        uService.addBalance(username, balance);
    }
    @PostMapping("/user/save/{username}/{password}")
    public void SaveUser(@PathVariable String username, @PathVariable String password) {
        User user = new User(username, password);
        uService.saveUser(user);
    }

    /*@PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = uService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }*/

}