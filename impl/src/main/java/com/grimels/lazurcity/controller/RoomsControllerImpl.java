package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.exception.NotFoundStatusException;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcityapi.controller.RoomsController;
import com.grimels.lazurcityapi.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
public class RoomsControllerImpl implements RoomsController {

    private RoomService roomService;

    @Override
    public List<Room> getRooms(Boolean isBusy) {
        if (nonNull(isBusy)) {
            return roomService.findAll(isBusy);
        }
        return roomService.findAll();
    }

    @Override
    public Room getRoom(int roomId) {
        return roomService.findById(roomId)
            .orElseThrow(() -> NotFoundStatusException.createEntityNotFoundByIdError(RoomEntity.class.getName(), roomId));
    }

    @Override
    public Room saveRoom(Room roomCreationRequest) {
        return roomService.saveRoom(roomCreationRequest);
    }

    @Override
    public Set<String> getRoomTypes() {
        return roomService.findAllRoomTypes();
    }

}
