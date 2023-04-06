package com.groupe1.atelier3.auth.controllers;

import com.groupe1.atelier3.auth.models.AuthDTO;
import com.groupe1.atelier3.users.models.User;

public class AuthMapper {
    public static AuthDTO toDTO(User user) {
        return new AuthDTO(user.getUsername(), user.getPassword());
    }

    public static User toEntity(AuthDTO authDTO) {
        return new User(authDTO.getUsername(), authDTO.getPassword());
    }
}
