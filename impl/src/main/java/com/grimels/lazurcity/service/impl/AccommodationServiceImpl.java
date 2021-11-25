package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.manager.AccommodationStatisticsManager;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.ClientMapper;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Data
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final AccommodationMapper accommodationMapper;
    private final RoomMapper roomMapper;
    private final ClientMapper clientMapper;
    private final AccommodationValidator accommodationValidator;

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
                .isFinal(accommodation.getIsFinal())
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
    public Accommodation saveAccommodation(CreateAccommodationRequest request) {
        Client client = getOrCreateClient(request.getClientName(), request.getClientPhoneNumber());
        RoomEntity room = roomRepository.getById(request.getRoomId());
        accommodationValidator.validate(request, client, room);

        ClientEntity clientEntity = clientRepository.getById(client.getId());
        AccommodationEntity accommodationEntity = accommodationMapper
                .toAccommodationEntity(request, clientEntity, room);
        AccommodationEntity savedAccommodation = accommodationRepository
                .save(accommodationEntity);

        return accommodationMapper.fromAccommodationEntity(savedAccommodation);
    }

    @Override
    public void updateAccommodation(Integer accommodationId, UpdateAccommodationRequest request) {
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find accommodation"));

        if (nonNull(request.getStartDate())) {
            accommodation.setStartDate(request.getStartDate());
        }
        if (nonNull(request.getEndDate())) {
            accommodation.setEndDate(request.getEndDate());
        }
        if (nonNull(request.getQuantity())) {
            accommodation.setQuantity(request.getQuantity());
        }
        if (nonNull(request.getPrice())) {
            accommodation.setPrice(request.getPrice().doubleValue());
        }
        if (nonNull(request.getComment())) {
            accommodation.setComment(request.getComment());
        }
        if (nonNull(request.getIsFinal())) {
            accommodation.setIsFinal(request.getIsFinal());
        }
        if (nonNull(request.getClientName()) || nonNull(request.getClientPhoneNumber())) {
            Client client = getOrCreateClient(request.getClientName(), request.getClientPhoneNumber());
            accommodation.setClient(clientMapper.toClientEntity(client));
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

    private Client getOrCreateClient(String clientName, String phoneNumber) {
        return findClientByName(clientName)
                .or(() -> findClientByPhoneNumber(phoneNumber))
                .orElse(createClient(clientName, phoneNumber));
    }

    private Optional<Client> findClientByName(String clientName) {
        return isNotEmpty(clientName) ? clientService.findFirstByName(clientName) : Optional.empty();
    }

    private Optional<Client> findClientByPhoneNumber(String phoneNumber) {
        return isNotEmpty(phoneNumber) ? clientService.findFirstByPhoneNumber(phoneNumber) : Optional.empty();
    }

    private Client createClient(String clientName, String phoneNumber) {
        Client creationRequest = Client.builder()
                .name(clientName)
                .phoneNumber(phoneNumber)
                .build();
        return clientService.saveClient(creationRequest);
    }

}
