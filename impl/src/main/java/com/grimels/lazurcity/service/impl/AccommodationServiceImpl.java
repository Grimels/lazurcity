package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.validation.AccommodationValidation;
import com.grimels.lazurcityapi.model.AccommodationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Data
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private AccommodationRepository accommodationRepository;
    private AccommodationMapper accommodationMapper;
    private AccommodationValidation accommodationValidation;

    @Override
    public List<AccommodationDTO> findAll() {
        return accommodationRepository.findAll().stream()
            .map(accommodationMapper::toAccommodationDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccommodationDTO saveAccommodation(AccommodationDTO accommodationCreationRequest) {
        if (isNull(accommodationCreationRequest.getStartDate())) {
            accommodationCreationRequest.setStartDate(new Date());
        }
        accommodationValidation.validate(accommodationCreationRequest);
        AccommodationEntity accommodationEntity = accommodationMapper.toAccommodationEntity(accommodationCreationRequest);
        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodationEntity);

        return accommodationMapper.toAccommodationDTO(savedAccommodation);
    }

}
