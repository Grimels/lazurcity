package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.entity.RoomTypeEntity;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.repository.RoomTypeRepository;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcityapi.model.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private RoomTypeRepository roomTypeRepository;
    private RoomMapper roomMapper;

    @Override
    public List<RoomDTO> findAll() {
        return roomRepository.findAll().stream()
            .map(roomMapper::toRoomDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDTO> findById(int roomId) {
        return roomRepository.findById(roomId)
            .map(roomMapper::toRoomDTO);
    }

    @Override
    @Transactional
    public RoomDTO saveRoom(RoomDTO roomCreationRequest) {
        String roomType = roomCreationRequest.getType();
        RoomTypeEntity roomTypeEntity = roomTypeRepository.findOneByType(roomType)
            .orElseGet(() -> roomTypeRepository.save(RoomTypeEntity.builder().type(roomType).build()));
        RoomEntity roomEntity = roomMapper.toRoomEntity(roomCreationRequest, roomTypeEntity);
        RoomEntity savedRoomEntity = roomRepository.save(roomEntity);
        return roomMapper.toRoomDTO(savedRoomEntity);
    }

}
