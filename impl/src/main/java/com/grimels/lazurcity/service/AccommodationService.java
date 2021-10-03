package com.grimels.lazurcity.service;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.history.RoomAccommodationsHistory;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import com.grimels.lazurcityapi.model.request.UpdateAccommodationRequest;
import com.grimels.lazurcityapi.model.statistics.AccommodationStatistics;

import java.time.LocalDate;
import java.util.List;

public interface AccommodationService {

    List<Accommodation> findAll();

    List<Accommodation> findAll(LocalDate startDate, LocalDate endDate);

    List<RoomAccommodationsHistory> getRoomsAccommodationsHistory(LocalDate startDate, LocalDate endDate);

    RoomAccommodationsHistory getRoomAccommodationsHistory(RoomEntity roomEntity, LocalDate startDate, LocalDate endDate);

    List<Accommodation> findAll(LocalDate date);

    List<Accommodation> findAll(Integer roomId);

    Accommodation saveAccommodation(CreateAccommodationRequest accommodationCreationRequest);

    void updateAccommodation(Integer accommodationId, UpdateAccommodationRequest updateAccommodationRequest);

    void deleteAccommodation(Integer accommodationId);

    AccommodationStatistics getAccommodationStatistics(LocalDate startDate, LocalDate endDate, LocalDate date);

}
