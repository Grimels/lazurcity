package com.grimels.lazurcity.util;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

@UtilityClass
public class DateUtil {

    public static Comparator<Accommodation> compareAccommodationsByStartDate() {
        return (a1, a2) -> {
            LocalDate startDate1 = a1.getStartDate();
            LocalDate startDate2 = a2.getStartDate();

            return startDate1.compareTo(startDate2);
        };
    }

    public static boolean isDateInDateRange(LocalDate date, LocalDate startRange, LocalDate endRange) {
        return date.isAfter(startRange) && date.isBefore(endRange);
    }

    public static Predicate<AccommodationEntity> isAccommodationInDateRange(LocalDate startRange, LocalDate endRange) {
        return (accommodation) -> {
            LocalDate startDate = accommodation.getStartDate();
            LocalDate endDate = accommodation.getEndDate();

            return (startDate.isAfter(startRange) && startDate.isBefore(endRange))
                    || (endDate.isAfter(startRange) && endDate.isBefore(endRange));
        };
    }

}
