package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.exception.NotFoundStatusException;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcityapi.controller.RoomsController;
import com.grimels.lazurcityapi.model.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomsControllerImpl implements RoomsController {

    private RoomService roomService;

    @Override
    public List<RoomDTO> getRooms() {
        return roomService.findAll();
    }

    @Override
    public RoomDTO getRoom(int roomId) {
        return roomService.findById(roomId)
            .orElseThrow(() -> NotFoundStatusException.createEntityNotFoundByIdError(RoomEntity.class.getName(), roomId));
    }

    @Override
    public RoomDTO saveRoom(RoomDTO roomCreationRequest) {
        return roomService.saveRoom(roomCreationRequest);
    }

}
