package org.toll;

import org.toll.entities.Vehicle;
import org.toll.services.HourFeeService;
import org.toll.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TollCalculator {
    private final HourFeeService hourFeeService = new HourFeeService();

    /**
     * Calculate the total toll fee for one day
     *
     * @param vehicle - the vehicle
     * @param dates   - date and time of all passes on one day
     * @return - the total toll fee for that day
     */
    public int getTollFee(Vehicle vehicle, Date... dates) {
        Date intervalStart = dates[0];
        int totalFee = 0;
        for (Date date : dates) {
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);

            TimeUnit timeUnit = TimeUnit.MINUTES;
            long diffInMillies = date.getTime() - intervalStart.getTime();
            long minutes = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (minutes <= 60) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }
        if (totalFee > 60) totalFee = 60;
        return totalFee;
    }

    public int getTollFee(final Date date, Vehicle vehicle) {
        if (isTollFreeDate(date) || vehicle.isFeeFree()) {
            return 0;
        }
        return HourFeeService.getTimeFee(DateUtils.convertDateToLocalDateTime(date));
    }

    private Boolean isTollFreeDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) return true;

        if (year == 2013) {
            if (month == Calendar.JANUARY && day == 1 ||
                    month == Calendar.MARCH && (day == 28 || day == 29) ||
                    month == Calendar.APRIL && (day == 1 || day == 30) ||
                    month == Calendar.MAY && (day == 1 || day == 8 || day == 9) ||
                    month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) ||
                    month == Calendar.JULY ||
                    month == Calendar.NOVEMBER && day == 1 ||
                    month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31)) {
                return true;
            }
        }
        return false;
    }
}

