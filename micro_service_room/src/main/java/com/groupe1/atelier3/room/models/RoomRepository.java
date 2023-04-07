package com.groupe1.atelier3.room.models;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RoomRepository extends CrudRepository<Room, Integer>{
    public Optional<Room> findById(Integer roomID);
    public Optional<Room> findByName(String name);

}