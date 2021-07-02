package com.grimels.lazurcity.validation;

import com.grimels.lazurcityapi.model.AccommodationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

public class AccommodationValidation implements DataValidation<AccommodationDTO> {
    private static final String INVALID_DATE_ERROR_MSG = "Invalid accommodation dates: start date '%s' is greater than end date '%s'.";

    @Override
    public void validate(AccommodationDTO accommodationDTO) {
        Date startDate = accommodationDTO.getStartDate();
        Date endDate = accommodationDTO.getEndDate();
        if (startDate.before(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(INVALID_DATE_ERROR_MSG, startDate, endDate));
        }
    }

}
