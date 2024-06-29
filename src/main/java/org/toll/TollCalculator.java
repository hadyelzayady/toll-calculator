package org.toll;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.toll.entities.Vehicle;
import org.toll.services.TimeFeeService;
import org.toll.services.ValidationService;
import org.toll.utils.DateUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Log4j2
public class TollCalculator {
    private final int MAX_TOTAL_FEES_PER_DAY = 60;
    private final int FEE_WINDOW_DURATION_IN_MINUTES = 60;

    /**
     * Calculate the total toll fee for one day
     *
     * @param vehicle - the vehicle
     * @param dates   - date and time of all passes on one day
     * @return - the total toll fee for that day
     */
    public int getDayTollFee(Vehicle vehicle, Date... dates) throws IllegalArgumentException {
        List<Date> datesList = Arrays.asList(dates);
        var isValid = ValidationService.validateDates(datesList);
        if (!isValid.getLeft()) {
            log.info("Dates are not valid with validation message: {}", isValid.getRight());
            throw new IllegalArgumentException(String.format("Dates are not valid with validation message: %s", isValid.getRight()));
        }
        log.info("get day toll fee of vehicle with type: {} with dates: {} ", vehicle.getType(), datesList);
        var totalFees = getFeeHourWindows(datesList)
                .stream()
                .map(window ->
                        {
                            var windowElements = datesList.subList(window.getLeft(), window.getRight() + 1);
                            log.info("get fee of window from: {} to: {} ", windowElements.get(0), windowElements.get(windowElements.size() - 1));
                            return getWindowListFee(windowElements, vehicle);
                        }
                )
                .mapToInt(Integer::intValue).sum();

        var appliedFee = Math.min(totalFees, MAX_TOTAL_FEES_PER_DAY);
        log.info("total fees is {}, max fee is {}, applied fee: {}", totalFees, MAX_TOTAL_FEES_PER_DAY, appliedFee);
        return appliedFee;
    }

    private int getWindowListFee(List<Date> windowList, Vehicle vehicle) {
        return windowList.stream().map(feeDate -> getDateTollFee(feeDate, vehicle))
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    private int getDateTollFee(final Date date, Vehicle vehicle) {
        if (TimeFeeService.isDateFreeFee(date) || vehicle.isFeeFree()) {
            return 0;
        }
        return TimeFeeService.getTimeFee(DateUtils.convertDateToLocalDateTime(date));
    }

    /**
     * Get fee windows from list of dates
     *
     * @param dates - list of dates
     * @return - start and end index of the fee window (1h by default)
     */
    private List<Pair<Integer, Integer>> getFeeHourWindows(List<Date> dates) {
        if (dates.isEmpty()) {
            return new ArrayList<>();
        }
        var sortedDates = dates.stream().sorted().toList();
        List<Pair<Integer, Integer>> feeWindows = new ArrayList<>();
        int windowStartIndex = 0;
        int windowEndIndex;
        for (windowEndIndex = 1; windowEndIndex < sortedDates.size(); windowEndIndex++) {
            var localDate = DateUtils.convertDateToLocalDateTime(sortedDates.get(windowEndIndex));
            Duration duration = Duration.between(DateUtils.convertDateToLocalDateTime(sortedDates.get(windowStartIndex)), localDate);
            if (duration.toMinutes() > FEE_WINDOW_DURATION_IN_MINUTES) {
                feeWindows.add(Pair.of(windowStartIndex, windowEndIndex - 1));
                windowStartIndex = windowEndIndex;
            }
        }
        if (feeWindows.isEmpty() || feeWindows.get(feeWindows.size() - 1).getRight() != windowEndIndex - 1) {
            feeWindows.add(Pair.of(windowStartIndex, windowEndIndex - 1));
        }
        return feeWindows;
    }
}

