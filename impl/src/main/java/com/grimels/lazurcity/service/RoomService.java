package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.RoomDTO;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<RoomDTO> findAll();

    Optional<RoomDTO> findById(int roomId);

    RoomDTO saveRoom(RoomDTO roomEntity);

}
