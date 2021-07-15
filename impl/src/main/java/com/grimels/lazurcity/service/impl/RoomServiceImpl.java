package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcityapi.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private RoomMapper roomMapper;

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll().stream()
            .map(roomMapper::fromRoomEntity)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> findById(int roomId) {
        return roomRepository.findById(roomId)
            .map(roomMapper::fromRoomEntity);
    }

    @Override
    @Transactional
    public Room saveRoom(Room roomCreationRequest) {
        RoomEntity roomEntity = roomMapper.toRoomEntity(roomCreationRequest);
        RoomEntity savedRoomEntity = roomRepository.save(roomEntity);
        return roomMapper.fromRoomEntity(savedRoomEntity);
    }

    @Override
    public Set<String> findAllRoomTypes() {
        return roomRepository.findAll().stream()
            .map(RoomEntity::getType)
            .collect(Collectors.toSet());
    }

}
