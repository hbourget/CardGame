package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserCrt {
    @Autowired
    private UserService uService;

    @GetMapping("/user/{idOrUsername}")
    public Object GetUser(@PathVariable String idOrUsername) {
        try {
            Integer id = Integer.parseInt(idOrUsername);
            return uService.GetUserById(id);
        } catch (NumberFormatException e) {
            return uService.GetUserByUsername(idOrUsername);
        }
    }

    @GetMapping("/user/auth/{idOrUsername}")
    public Object GetUserAuth(@PathVariable String idOrUsername) {
        try {
            Integer id = Integer.parseInt(idOrUsername);
            return uService.GetUserAuthById(id);
        } catch (NumberFormatException e) {
            return uService.GetUserAuthByUsername(idOrUsername);
        }
    }

    @GetMapping("/users")
    public Iterable<UserDTO> GetUsers() {
        return uService.GetUsers();
    }

    @PostMapping("/user/substractbalance/{id}/{balance}")
    public UserDTO SubstractBalance(@PathVariable Integer id, @PathVariable Integer balance) {
        return uService.substractBalance(id, balance);
    }
    @PostMapping("/user/addbalance/{id}/{balance}")
    public UserDTO AddBalance(@PathVariable Integer id, @PathVariable Integer balance) {
        return uService.addBalance(id, balance);
    }
    @PostMapping("/user/save/{username}/{password}")
    public User SaveUser(@PathVariable String username, @PathVariable String password) {
        User user = new User(username, password);
        return uService.saveUser(user);
    }

    /*@PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = uService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }*/

}