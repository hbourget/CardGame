package com.groupe1.atelier3.users.controllers;

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
}
