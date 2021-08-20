package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.exception.NotFoundStatusException;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.history.RoomAccommodationsHistory;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
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
    public List<RoomAccommodationsHistory> getRoomsAccommodationsHistoryInRange(LocalDate startDate, LocalDate endDate) {
        return accommodationService.getRoomsAccommodationsHistory(startDate, endDate);
    }

    @Override
    public List<Accommodation> getAccommodationsExistedByDate(LocalDate date) {
        return accommodationService.findAll(date);
    }

    @Override
    public List<Accommodation> getAccommodationsExistedInDateRange(LocalDate startDate, LocalDate endDate) {
        return accommodationService.findAll(startDate, endDate);
    }

    @Override
    public Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest) {
        return accommodationService.saveAccommodation(accommodationCreationRequest);
    }

}
