package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;

import java.util.Date;
import java.util.List;

public interface AccommodationService {

    List<Accommodation> findAll();

    List<Accommodation> findAll(Date startDate, Date endDate);

    Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest);

}
