package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.AccommodationDTO;

import java.util.List;

public interface AccommodationService {

    List<AccommodationDTO> findAll();

    AccommodationDTO saveAccommodation(AccommodationDTO accommodationCreationRequest);

}
