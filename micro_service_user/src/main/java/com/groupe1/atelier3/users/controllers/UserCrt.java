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

    @GetMapping("/users")
    public Iterable<UserDTO> GetUsers() {
        return uService.GetUsers();
    }

    @PutMapping("/user/{id}/addbalance")
    public UserDTO addBalance(@PathVariable Integer id, @RequestBody Integer balanceToAdd) {
        return uService.addBalance(id, balanceToAdd);
    }

    @PutMapping("/user/{id}/subtractbalance")
    public UserDTO subtractBalance(@PathVariable Integer id, @RequestBody Integer balanceToSubtract) {
        return uService.subtractBalance(id, balanceToSubtract);
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
    @PostMapping("/user/save/{username}/{password}")
    public User SaveUser(@PathVariable String username, @PathVariable String password) {
        User user = new User(username, password);
        return uService.saveUser(user);
    }

}