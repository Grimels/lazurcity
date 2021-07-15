package com.grimels.lazurcity.validation;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Set;
import java.util.function.Predicate;

public class AccommodationValidator {
    private static final String INVALID_END_DATE_ERROR_MSG
        = "Invalid accommodation dates: start date '%s' is greater than end date '%s'.";
    private static final String INVALID_ACCOMMODATION_DATE_ERROR_MSG
        = "Invalid accommodation dates: start date '%s' is not available because of the already existing accommodation.";

    public void validate(CreateAccommodationRequest createAccommodationRequest, ClientEntity clientEntity, RoomEntity roomEntity) {
        Date startDate = createAccommodationRequest.getStartDate();
        Date endDate = createAccommodationRequest.getEndDate();
        if (isStartDateBeforeEndDate(startDate, endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(INVALID_END_DATE_ERROR_MSG, startDate, endDate));
        }
        if (isRoomNotAvailableForDate(roomEntity, startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(INVALID_ACCOMMODATION_DATE_ERROR_MSG, startDate));
        }
    }

    private boolean isStartDateBeforeEndDate(Date startDate, Date endDate) {
        return startDate.before(endDate);
    }

    private boolean isRoomNotAvailableForDate(RoomEntity roomEntity, Date startDate) {
        Set<AccommodationEntity> accommodationList = roomEntity.getAccommodationList();
        return accommodationList.stream()
            .anyMatch(isAccommodationNotAvailableForDate(startDate));
    }

    private Predicate<AccommodationEntity> isAccommodationNotAvailableForDate(Date startDate) {
        return (accommodationEntity) -> accommodationEntity.getEndDate().after(startDate);
    }

}
