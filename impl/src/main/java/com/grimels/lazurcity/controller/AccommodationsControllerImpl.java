package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class AccommodationsControllerImpl implements AccommodationsController {

    private AccommodationService accommodationService;

    @Override
    public List<Accommodation> getAccommodations() {
        return accommodationService.findAll();
    }

    @Override
    public List<Accommodation> getAccommodationsExistedByDate(Date date) {
        return accommodationService.findAll(date);
    }

    @Override
    public List<Accommodation> getAccommodationsExistedInDateRange(Date startDate, Date endDate) {
        return accommodationService.findAll(startDate, endDate);
    }

    @Override
    public Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest) {
        return accommodationService.saveAccommodation(accommodationCreationRequest);
    }

}
