package org.toll;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.toll.entities.Car;
import org.toll.entities.Motorbike;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TollCalculatorUnitTests {
    private final TollCalculator tollCalculator = new TollCalculator();
    private final Pair<Date, Integer> dateTimeSameHour1WithFee1 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 06:00"), 8);
    private final Pair<Date, Integer> dateTimeSameHour1WithFee2 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 07:00"), 18);
    private final Date saturdayDate = TollCalculatorTestUtils.getTripDateFromString("6-1-2024 06:00");
    private final Date sundayDate = TollCalculatorTestUtils.getTripDateFromString("7-1-2024 07:00");

    @Test
    void test_car_trip_fee_at_non_rush_hours() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getTollFee(car, TollCalculatorTestUtils.getNonRushHours().toArray(new Date[0]));
        Assertions.assertEquals(totalFee, 0);
    }

    @Test
    void test_car_trip_with_fee() {
        Car car = new Car();
        Integer totalFee1 = tollCalculator.getTollFee(car, dateTimeSameHour1WithFee1.getLeft());
        Assertions.assertEquals(totalFee1, dateTimeSameHour1WithFee1.getRight());
        Integer totalFee2 = tollCalculator.getTollFee(car, dateTimeSameHour1WithFee2.getLeft());
        Assertions.assertEquals(totalFee2, dateTimeSameHour1WithFee2.getRight());
    }


    @Test
    void test_car_trip_two_fees_at_same_hour_in_rush_hour() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getTollFee(car,
                dateTimeSameHour1WithFee1.getLeft(),
                dateTimeSameHour1WithFee2.getLeft()
        );
        Assertions.assertEquals(totalFee, Math.max(dateTimeSameHour1WithFee1.getRight(), dateTimeSameHour1WithFee2.getRight()));
    }

    @Test
    void test_car_trip_in_holidays_in_rush_hour() {
    }

    @Test
    void test_car_trip_in_weekends_in_rush_hour() {
        Car car = new Car();
        Integer totalSatFee = tollCalculator.getTollFee(car,saturdayDate);
        Assertions.assertEquals(totalSatFee, 0);
        Integer totalSunFee = tollCalculator.getTollFee(car,saturdayDate);
        Assertions.assertEquals(totalSunFee, 0);
    }

    @Test
    void test_car_trip_with_fees_greater_than_max() {
    }

    @Test
    void test_fee_free_vehicle() {
        Motorbike vehicle = new Motorbike();
        Integer totalFee = tollCalculator.getTollFee(vehicle, dateTimeSameHour1WithFee1.getLeft());
        Assertions.assertEquals(totalFee, 0);
    }
}
