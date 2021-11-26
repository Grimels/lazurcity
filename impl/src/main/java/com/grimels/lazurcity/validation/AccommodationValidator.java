package com.grimels.lazurcity.validation;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Client;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;

public class AccommodationValidator {
    private static final String INVALID_END_DATE_ERROR_MSG
            = "Invalid accommodation dates: start date '%s' is greater than end date '%s'.";
    private static final String INVALID_ACCOMMODATION_DATE_ERROR_MSG
            = "Invalid accommodation dates: start date '%s' is not available because of the already existing accommodation.";

    public void validate(CreateAccommodationRequest createAccommodationRequest, Client clientEntity, RoomEntity roomEntity) {
        LocalDate startDate = createAccommodationRequest.getStartDate();
        LocalDate endDate = createAccommodationRequest.getEndDate();
        if (isStartDateAfterEndDate(startDate, endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(INVALID_END_DATE_ERROR_MSG, startDate, endDate));
        }
        if (isRoomNotAvailableForDate(roomEntity, startDate, endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(INVALID_ACCOMMODATION_DATE_ERROR_MSG, startDate));
        }
    }

    private boolean isStartDateAfterEndDate(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate);
    }

    private boolean isRoomNotAvailableForDate(RoomEntity roomEntity, LocalDate startDate, LocalDate endDate) {
        Set<AccommodationEntity> accommodationList = roomEntity.getAccommodationList();
        return accommodationList.stream()
                .anyMatch(isAccommodationNotAvailableForDate(startDate, endDate));
    }

    private Predicate<AccommodationEntity> isAccommodationNotAvailableForDate(LocalDate startDate, LocalDate endDate) {
        return (accommodationEntity) -> (startDate.isAfter(accommodationEntity.getStartDate()) && startDate.isBefore(accommodationEntity.getEndDate())) ||
                (endDate.isAfter(accommodationEntity.getStartDate()) && endDate.isBefore(accommodationEntity.getEndDate()));
    }

}
