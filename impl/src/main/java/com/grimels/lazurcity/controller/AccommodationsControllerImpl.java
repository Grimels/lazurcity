package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
public class AccommodationsControllerImpl implements AccommodationsController {

    private AccommodationService accommodationService;

    @Override
    public List<Accommodation> getAccommodations(Date startDate, Date endDate) {
        if (nonNull(startDate)) {
            return accommodationService.findAll(startDate, endDate);
        }
        return accommodationService.findAll();
    }

    @Override
    public Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest) {
        return accommodationService.saveAccommodation(accommodationCreationRequest);
    }

}
