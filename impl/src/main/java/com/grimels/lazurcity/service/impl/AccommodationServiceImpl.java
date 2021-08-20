package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.util.DateUtil;
import com.grimels.lazurcity.validation.AccommodationValidator;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.history.AccommodationInfo;
import com.grimels.lazurcityapi.model.history.RoomAccommodationsHistory;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private AccommodationRepository accommodationRepository;
    private ClientRepository clientRepository;
    private RoomRepository roomRepository;
    private AccommodationMapper accommodationMapper;
    private RoomMapper roomMapper;
    private AccommodationValidator accommodationValidator;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll().stream()
                .map(accommodationMapper::fromAccommodationEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Accommodation> findAll(LocalDate startDate, LocalDate endDate) {
        return accommodationRepository.findAllByDateInRange(startDate, endDate).stream()
                .map(accommodationMapper::fromAccommodationEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomAccommodationsHistory> getRoomsAccommodationsHistory(LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAll().stream()
                .map(roomEntity -> getRoomAccommodationsHistory(roomEntity, startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public RoomAccommodationsHistory getRoomAccommodationsHistory(RoomEntity roomEntity, LocalDate startDate, LocalDate endDate) {
        List<AccommodationInfo> accommodations = roomEntity.getAccommodationList().stream()
                .filter(DateUtil.isAccommodationInDateRange(startDate, endDate))
                .map(accommodationMapper::fromAccommodationEntity)
                .sorted(Comparator.comparing(Accommodation::getStartDate))
                .map(this::createAccommodationDay)
                .collect(Collectors.toList());
        return RoomAccommodationsHistory.builder()
                .startRange(startDate)
                .endRange(endDate)
                .room(roomMapper.toRoomInfo(roomEntity))
                .accommodations(accommodations)
                .build();
    }

    private AccommodationInfo createAccommodationDay(Accommodation accommodation) {
        int daysLeft = (int) accommodation.getStartDate().datesUntil(accommodation.getEndDate()).count();
        return AccommodationInfo.builder()
                .id(accommodation.getId())
                .startDate(accommodation.getStartDate())
                .endDate(accommodation.getEndDate())
                .daysLeft(daysLeft)
                .client(accommodation.getClient())
                .price(accommodation.getPrice())
                .build();
    }

    @Override
    public List<Accommodation> findAll(Integer roomId) {
        return accommodationRepository.findAllByRoomId(roomId).stream()
                .map(accommodationMapper::fromAccommodationEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Accommodation> findAll(LocalDate date) {
        return accommodationRepository.findAllByDate(date).stream()
                .map(accommodationMapper::fromAccommodationEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest) {
        ClientEntity client
                = clientRepository.getById(accommodationCreationRequest.getClientId());
        RoomEntity room
                = roomRepository.getById(accommodationCreationRequest.getClientId());

        accommodationValidator.validate(accommodationCreationRequest, client, room);

        AccommodationEntity accommodationEntity
                = accommodationMapper.toAccommodationEntity(accommodationCreationRequest, client, room);
        AccommodationEntity savedAccommodation
                = accommodationRepository.save(accommodationEntity);

        return accommodationMapper.fromAccommodationEntity(savedAccommodation);
    }

}
