package com.groupe1.atelier3.users.controllers;

import com.groupe1.atelier3.inventory.models.Inventory;
import com.groupe1.atelier3.users.models.User;
import com.groupe1.atelier3.users.models.UserDTO;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getUsername(), user.getBalance(), user.getInventory());
    }
}
