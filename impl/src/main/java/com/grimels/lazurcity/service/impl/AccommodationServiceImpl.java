package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.validation.AccommodationValidator;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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
    private AccommodationValidator accommodationValidator;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll().stream()
            .map(accommodationMapper::fromAccommodationEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<Accommodation> findAll(Date startDate, Date endDate) {
        return accommodationRepository.findAllByDateInRange(startDate, endDate).stream()
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
