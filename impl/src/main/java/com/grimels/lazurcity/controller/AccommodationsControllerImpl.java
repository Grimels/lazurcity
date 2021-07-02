package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.model.AccommodationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccommodationsControllerImpl implements AccommodationsController {

    private AccommodationService accommodationService;

    @Override
    public List<AccommodationDTO> getAccommodations() {
        return accommodationService.findAll();
    }

    @Override
    public AccommodationDTO saveAccommodation(AccommodationDTO accommodationCreationRequest) {
        return accommodationService.saveAccommodation(accommodationCreationRequest);
    }

}
