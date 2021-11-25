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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AccommodationStatisticsManager {
    private static final String DATE_FORMAT_KEY = "dd.MM";

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<RoomEntity> rooms;
    private final List<AccommodationEntity> accommodations;

    public AccommodationStatisticsManager(LocalDate startDate, LocalDate endDate, List<RoomEntity> rooms) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rooms = rooms;
        this.accommodations = rooms.stream()
            .flatMap(room -> room.getAccommodationList().stream())
            .filter(isValidAccommodationDateRange(startDate, endDate))
            .collect(Collectors.toList());
    }

    public AccommodationStatistics getStatistics(LocalDate currentDate) {
        AccommodationStatistics.AccommodationStatisticsBuilder statisticsBuilder = AccommodationStatistics.builder();
        populateDailyStatistics(statisticsBuilder, currentDate);
        populateSeasonStatistics(statisticsBuilder, currentDate.getYear());

        return statisticsBuilder.build();
    }

    private void populateDailyStatistics(AccommodationStatistics.AccommodationStatisticsBuilder statisticsBuilder,
                                         LocalDate currentDate) {
        statisticsBuilder.day(currentDate);
        statisticsBuilder.roomsLeavingToday(countRoomsLeavingInDay(currentDate));

        List<AccommodationEntity> accommodationsInDate = getAccommodationsInDate(currentDate);
        Long dailyIncome = accommodationsInDate.stream()
            .reduce(0L, (totalIncome, accommodation) -> totalIncome + accommodation.getPrice().longValue(), Long::sum);
        statisticsBuilder.dailyIncome(dailyIncome);
        statisticsBuilder.busyRooms(accommodationsInDate.size());
        statisticsBuilder.freeRooms(rooms.size() - accommodationsInDate.size());
    }

    private List<AccommodationEntity> getAccommodationsInDate(LocalDate currentDate) {
        return accommodations.stream()
            .filter(isAccommodationInDate(currentDate))
            .collect(Collectors.toList());
    }

    private Integer countRoomsLeavingInDay(LocalDate currentDate) {
        long roomsLeavingInDay = accommodations.stream()
            .filter(accommodationEntity -> accommodationEntity.getEndDate().isEqual(currentDate)).count();
        return (int) roomsLeavingInDay;
    }

    private void populateSeasonStatistics(AccommodationStatistics.AccommodationStatisticsBuilder statisticsBuilder,
                                          Integer seasonYear) {
        Map<RoomEntity, Long> seasonIncomeByRoom = rooms.stream()
            .collect(Collectors.toMap(Function.identity(), room -> calculateIncome(room, seasonYear)));
        Map<String, Long> seasonIncomeByRoomName = getSeasonIncomeByRoomName(seasonIncomeByRoom);
        statisticsBuilder.seasonIncomeByRoomName(seasonIncomeByRoomName);
        statisticsBuilder.totalSeasonIncome(seasonIncomeByRoomName.values().stream().reduce(0L, Long::sum, Long::sum));
        statisticsBuilder.seasonIncomeByRoomCategory(getSeasonIncomeByRoomType(seasonIncomeByRoom));

        Map<String, Long> totalIncomeByDateKey = startDate.datesUntil(endDate)
            .collect(Collectors.toMap(this::formatDate, this::calculateTotalDailyIncome));
        statisticsBuilder.incomesByKey(totalIncomeByDateKey);
    }

    private Map<String, Long> getSeasonIncomeByRoomName(Map<RoomEntity, Long> seasonIncomeByRoom) {
        return seasonIncomeByRoom.entrySet().stream()
            .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    private Map<String, Long> getSeasonIncomeByRoomType(Map<RoomEntity, Long> seasonIncomeByRoom) {
        Map<String, Long> seasonIncomeByRoomType = new HashMap<>();
        for (Map.Entry<RoomEntity, Long> entry : seasonIncomeByRoom.entrySet()) {
            String roomType = entry.getKey().getType();
            Long currentIncome = Optional
                .ofNullable(seasonIncomeByRoomType.get(roomType))
                .orElse(0L);
            seasonIncomeByRoomType.put(roomType, currentIncome + entry.getValue());
        }
        return seasonIncomeByRoomType;
    }

    private Long calculateIncome(RoomEntity roomEntity, int year) {
        return roomEntity.getAccommodationList().stream()
            .filter(accommodationEntity -> isAccommodationExistsInYear(accommodationEntity, year))
            .reduce(0L, (totalPrice, accommodation) -> totalPrice + calculateAccommodationIncome(accommodation), Long::sum);
    }

    private Long calculateAccommodationIncome(AccommodationEntity accommodation) {
        LocalDate accommodationStartDate = accommodation.getStartDate();
        LocalDate accommodationEndDate = accommodation.getEndDate();
        long daysCount = accommodationStartDate.datesUntil(accommodationEndDate).count();
        return daysCount * accommodation.getPrice().longValue();
    }

    private Long calculateTotalDailyIncome(LocalDate date) {
        return accommodations.stream().filter(
                accommodation -> (accommodation.getStartDate().isBefore(date) || accommodation.getStartDate().isEqual(date))
                    && (accommodation.getEndDate().isAfter(date) || accommodation.getEndDate().isEqual(date)))
            .reduce(0L, (totalIncome, accommodation) -> totalIncome + accommodation.getPrice().longValue(), Long::sum);
    }

    private Predicate<AccommodationEntity> isValidAccommodationDateRange(LocalDate startDate,
                                                                                LocalDate endDate) {
        return (accommodation) ->
            (startDate.isBefore(accommodation.getStartDate()) || startDate.isEqual(accommodation.getStartDate()))
                && (endDate.isAfter(accommodation.getEndDate())) || endDate.isEqual(accommodation.getEndDate());
    }

    private Predicate<AccommodationEntity> isAccommodationInDate(LocalDate currentDate) {
        return (accommodation) ->
            (currentDate.isAfter(accommodation.getStartDate()) || currentDate.isEqual(accommodation.getStartDate()))
                && (currentDate.isBefore(accommodation.getEndDate()) || currentDate.isEqual(
                accommodation.getEndDate()));
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_KEY));
    }

    private boolean isAccommodationExistsInYear(AccommodationEntity accommodation, int year) {
        return accommodation.getStartDate().getYear() == year && accommodation.getEndDate().getYear() == year;
    }

}
