package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/user/save/{user}")
    public void SaveUser(@RequestBody User user) {
        uService.saveUser(user);
    }
}