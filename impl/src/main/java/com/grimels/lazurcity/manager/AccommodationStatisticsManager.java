package com.grimels.lazurcity.manager;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.statistics.AccommodationStatistics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccommodationStatisticsManager {
    private static final String DATE_FORMAT_KEY = "dd.MM";

    private final List<RoomEntity> rooms;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<AccommodationEntity> accommodations;

    public AccommodationStatisticsManager(LocalDate startDate, LocalDate endDate, List<RoomEntity> rooms) {
        this.rooms = rooms;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accommodations = rooms.stream()
                .flatMap(room -> room.getAccommodationList().stream())
                .filter(accommodation ->
                        (startDate.isBefore(accommodation.getStartDate()) || startDate.isEqual(accommodation.getStartDate()))
                                && (endDate.isAfter(accommodation.getEndDate())) || endDate.isEqual(accommodation.getEndDate()))
                .collect(Collectors.toList());
    }

    public AccommodationStatistics getStatistics(LocalDate currentDate) {
        AccommodationStatistics.AccommodationStatisticsBuilder statisticsBuilder = AccommodationStatistics.builder();
        statisticsBuilder.day(currentDate);

        List<AccommodationEntity> accommodationsInDate = accommodations.stream()
                .filter(accommodation -> (currentDate.isAfter(accommodation.getStartDate()) || currentDate.isEqual(accommodation.getStartDate()))
                        && (currentDate.isBefore(accommodation.getEndDate()) || currentDate.isEqual(accommodation.getEndDate())))
                .collect(Collectors.toList());

        Integer roomsLeavingToday = Math.toIntExact(accommodationsInDate.stream()
                .filter(accommodationEntity -> accommodationEntity.getEndDate().isEqual(currentDate))
                .count());
        statisticsBuilder.roomsLeavingToday(roomsLeavingToday);

        Map<RoomEntity, Long> seasonIncomeByRoom = rooms.stream()
                .collect(Collectors.toMap(Function.identity(), room -> calculateIncome(room, currentDate.getYear())));
        statisticsBuilder.seasonIncomeByRoomName(getSeasonIncomeByRoomName(seasonIncomeByRoom));
        statisticsBuilder.seasonIncomeByRoomCategory(getSeasonIncomeByRoomType(seasonIncomeByRoom));

        Long dailyIncome = accommodationsInDate.stream()
                .reduce(0L, (totalIncome, accommodation) -> totalIncome + accommodation.getPrice().longValue(), Long::sum);
        statisticsBuilder.dailyIncome(dailyIncome);

        Map<String, Long> totalIncomeByDateKey = startDate.datesUntil(endDate)
                .collect(Collectors.toMap(date -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_KEY)), this::calculateTotalDailyIncome));
        statisticsBuilder.incomesByKey(totalIncomeByDateKey);

        statisticsBuilder.busyRooms(accommodationsInDate.size());
        statisticsBuilder.freeRooms(rooms.size() - accommodationsInDate.size());

        return statisticsBuilder.build();
    }

    private Map<String, Long> getSeasonIncomeByRoomName(Map<RoomEntity, Long> seasonIncomeByRoom) {
        return seasonIncomeByRoom.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    private Map<String, Long> getSeasonIncomeByRoomType(Map<RoomEntity, Long> seasonIncomeByRoom) {
        Map<String, Long> seasonIncomeByRoomType = new HashMap<>();
        for (Map.Entry<RoomEntity, Long> entry : seasonIncomeByRoom.entrySet()) {
            String roomType = entry.getKey().getType();
            Long currentIncome = Optional.ofNullable(seasonIncomeByRoomType.get(roomType)).orElse(0L);
            seasonIncomeByRoomType.put(roomType, currentIncome + entry.getValue());
        }
        return seasonIncomeByRoomType;
    }

    private Long calculateIncome(RoomEntity roomEntity, int year) {
        return roomEntity.getAccommodationList().stream()
                .filter(accommodationEntity ->
                        accommodationEntity.getStartDate().getYear() == year && accommodationEntity.getEndDate().getYear() == year)
                .reduce(0L, (totalPrice, accommodation) ->
                        totalPrice + calculateAccommodationIncome(accommodation.getPrice(), accommodation.getStartDate(), accommodation.getEndDate()), Long::sum);
    }

    private Long calculateAccommodationIncome(Double price, LocalDate startDate, LocalDate endDate) {
        return (startDate.datesUntil(endDate).count() * price.longValue());
    }

    private Long calculateTotalDailyIncome(LocalDate date) {
        return accommodations.stream()
                .filter(accommodation -> (accommodation.getStartDate().isBefore(date) || accommodation.getStartDate().isEqual(date))
                        && (accommodation.getEndDate().isAfter(date) || accommodation.getEndDate().isEqual(date)))
                .reduce(0L, (totalIncome, accommodation) -> totalIncome + accommodation.getPrice().longValue(), Long::sum);
    }

}
