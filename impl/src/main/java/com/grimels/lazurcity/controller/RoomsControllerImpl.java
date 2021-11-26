package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.exception.NotFoundStatusException;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcityapi.controller.RoomsController;
import com.grimels.lazurcityapi.model.Room;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class RoomsControllerImpl implements RoomsController {

    private RoomService roomService;

    @Override
    public List<Room> getRooms() {
        return roomService.findAll();
    }

    @Override
    public List<Room> getFreeRooms() {
        return roomService.findAllFree();
    }

    @Override
    public List<Room> getBusyRooms() {
        return roomService.findAllBusy();
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
