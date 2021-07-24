package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.Room;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    List<Room> findAll();

    List<Room> findAll(boolean isBusy);

    Optional<Room> findById(int roomId);

    Room saveRoom(Room roomEntity);

    Set<String> findAllRoomTypes();

}
