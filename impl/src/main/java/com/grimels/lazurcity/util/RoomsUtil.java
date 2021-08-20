package com.grimels.lazurcity.util;

import com.grimels.lazurcity.entity.RoomEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static org.apache.commons.collections4.SetUtils.emptyIfNull;

@UtilityClass
public class RoomsUtil {

    public static Boolean isBusyRoom(RoomEntity roomEntity) {
        LocalDate currentDate = LocalDate.now();
        return emptyIfNull(roomEntity.getAccommodationList()).stream()
                .anyMatch(accommodation -> accommodation.getStartDate().isBefore(currentDate)
                        && accommodation.getEndDate().isAfter(currentDate));
    }

    public static Boolean isFreeRoom(RoomEntity roomEntity) {
        return !isBusyRoom(roomEntity);
    }

}
