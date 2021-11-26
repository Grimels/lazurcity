package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.Room;
import com.grimels.lazurcityapi.model.history.RoomInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    List<Room> findAll();

    List<Room> findAllFree();

    List<Room> findAllBusy();

    Optional<Room> findById(int roomId);

    Optional<RoomInfo> findInfoById(int roomId);

    Room saveRoom(Room roomEntity);

    Set<String> findAllRoomTypes();

}
