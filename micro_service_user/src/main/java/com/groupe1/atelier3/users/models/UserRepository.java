package com.groupe1.atelier3.users.models;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer>{

    public Optional<User> findByUsername(String username);

    public Optional<User> findById(Integer idUser);
}