package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcity.util.RoomsUtil;
import com.grimels.lazurcityapi.model.Room;
import com.grimels.lazurcityapi.model.history.RoomInfo;
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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final AccommodationMapper accommodationMapper;

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::fromRoomEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllFree() {
        return roomRepository.findAll().stream()
                .filter(RoomsUtil::isFreeRoom)
                .map(roomEntity -> roomMapper.fromRoomEntity(roomEntity, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAllBusy() {
        return roomRepository.findAll().stream()
                .filter(RoomsUtil::isBusyRoom)
                .map(roomEntity -> roomMapper.fromRoomEntity(roomEntity, true))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Room> findById(int roomId) {
        return roomRepository.findById(roomId)
                .map(roomMapper::fromRoomEntity);
    }

    @Override
    public Optional<RoomInfo> findInfoById(int roomId) {
        return Optional.of(roomRepository.getById(roomId))
                .map(roomMapper::toRoomInfo);
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
