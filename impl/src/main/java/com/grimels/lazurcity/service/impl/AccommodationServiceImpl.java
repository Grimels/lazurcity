package com.grimels.lazurcity.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.manager.AccommodationStatisticsManager;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcity.util.DateUtil;
import com.grimels.lazurcity.validation.AccommodationValidator;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.Client;
import com.grimels.lazurcityapi.model.history.AccommodationInfo;
import com.grimels.lazurcityapi.model.history.RoomAccommodationsHistory;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import com.grimels.lazurcityapi.model.request.UpdateAccommodationRequest;
import com.grimels.lazurcityapi.model.statistics.AccommodationStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Data
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private AccommodationRepository accommodationRepository;
    private ClientService clientService;
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
                .roomName(accommodation.getRoom().getName())
                .quantity(accommodation.getQuantity())
                .roomId(accommodation.getRoom().getId())
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
        Client client
                = clientService.findFirstByName(accommodationCreationRequest.getClientName())
                .orElse(clientService.findFirstByPhoneNumber(accommodationCreationRequest.getClientPhoneNumber())
                        .orElse(null));
        if (isNull(client)) {
            Client creationRequest = Client.builder()
                    .name(accommodationCreationRequest.getClientName())
                    .phoneNumber(accommodationCreationRequest.getClientPhoneNumber())
                    .build();
            client = clientService.saveClient(creationRequest);
        }

        RoomEntity room
                = roomRepository.getById(accommodationCreationRequest.getRoomId());

        accommodationValidator.validate(accommodationCreationRequest, client, room);

        ClientEntity clientEntity = clientRepository.getById(client.getId());
        AccommodationEntity accommodationEntity
                = accommodationMapper.toAccommodationEntity(accommodationCreationRequest, clientEntity, room);
        AccommodationEntity savedAccommodation
                = accommodationRepository.save(accommodationEntity);

        return accommodationMapper.fromAccommodationEntity(savedAccommodation);
    }

    @Override
    public void updateAccommodation(Integer accommodationId, UpdateAccommodationRequest updateAccommodationRequest) {
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find accommodation"));

        if (nonNull(updateAccommodationRequest.getStartDate())) {
            accommodation.setStartDate(updateAccommodationRequest.getStartDate());
        }
        if (nonNull(updateAccommodationRequest.getEndDate())) {
            accommodation.setEndDate(updateAccommodationRequest.getEndDate());
        }

        if (nonNull(updateAccommodationRequest.getQuantity())) {
            accommodation.setQuantity(updateAccommodationRequest.getQuantity());
        }
        if (nonNull(updateAccommodationRequest.getPrice())) {
            accommodation.setPrice(updateAccommodationRequest.getPrice().doubleValue());
        }

        accommodationRepository.save(accommodation);
    }

    @Override
    public void deleteAccommodation(Integer accommodationId) {
        accommodationRepository.deleteById(accommodationId);
    }

    @Override
    public AccommodationStatistics getAccommodationStatistics(LocalDate startDate, LocalDate endDate, LocalDate date) {
        List<RoomEntity> rooms = roomRepository.findAll();
        AccommodationStatisticsManager statisticsManager = new AccommodationStatisticsManager(startDate, endDate, rooms);

        return statisticsManager.getStatistics(date);
    }

}
