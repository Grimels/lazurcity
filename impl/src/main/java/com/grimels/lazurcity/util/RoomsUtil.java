package com.grimels.lazurcity.util;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;

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

    public static AccommodationEntity getLatestAccommodationEntity(RoomEntity roomEntity) {
        return CollectionUtils.emptyIfNull(roomEntity.getAccommodationList()).stream()
                .max((a1, a2) -> a1.getStartDate().after(a2.getStartDate()) ? 1 : 0)
                .orElse(null);
    }

}
