package com.grimels.lazurcity.util;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.Optional;

import static org.apache.commons.collections4.SetUtils.emptyIfNull;

@UtilityClass
public class RoomsUtil {

    public static Boolean isBusyRoom(RoomEntity roomEntity) {
        Date currentDate = new Date();
        return emptyIfNull(roomEntity.getAccommodationList()).stream()
                .anyMatch(accommodation -> accommodation.getStartDate().before(currentDate)
                        && accommodation.getEndDate().after(currentDate));
    }

    public static Boolean isFreeRoom(RoomEntity roomEntity) {
        return !isBusyRoom(roomEntity);
    }

}
