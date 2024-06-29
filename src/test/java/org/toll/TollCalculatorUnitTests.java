package org.toll;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.toll.entities.Car;
import org.toll.entities.Motorbike;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TollCalculatorUnitTests {
    private final TollCalculator tollCalculator = new TollCalculator();
    private final Pair<Date, Integer> dateTimeAt6 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 06:00"), 8);
    private final Pair<Date, Integer> dateTimeAt7 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 07:00"), 18);
    private final Pair<Date, Integer> dateTimeAt7_1 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 07:01"), 18);
    private final Pair<Date, Integer> dateTimeAt8 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 08:00"), 13);
    private final Pair<Date, Integer> dateTimeAt8_30 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 08:30"), 8);
    private final Pair<Date, Integer> dateTimeAt9 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 09:00"), 0);
    private final Pair<Date, Integer> dateTimeAt14 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 14:00"), 0);
    private final Pair<Date, Integer> dateTimeAt14_45 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2024 14:45"), 8);
    private final Pair<Date, Integer> dateTimeHolidayAt6 = Pair.of(TollCalculatorTestUtils.getTripDateFromString("1-1-2023 06:00"), 8);
    private final Date saturdayDate = TollCalculatorTestUtils.getTripDateFromString("6-1-2024 06:00");
    private final Date sundayDate = TollCalculatorTestUtils.getTripDateFromString("7-1-2024 07:00");

    @Test
    void test_car_trip_with_empty_dates() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(car);
        Assertions.assertEquals(totalFee, 0);
    }

    @Test
    void test_dates_with_different_days() {
        Car car = new Car();
        assertThrows(IllegalArgumentException.class, () -> {
            tollCalculator.getDayTollFee(car, saturdayDate, sundayDate);
        });
    }

    @Test
    void test_car_trip_fee_at_non_rush_hours() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(car, TollCalculatorTestUtils.getNonRushHours().toArray(new Date[0]));
        Assertions.assertEquals(totalFee, 0);
    }

    @Test
    void test_car_trip_with_fee() {
        Car car = new Car();
        Integer totalFee1 = tollCalculator.getDayTollFee(car, dateTimeAt6.getLeft());
        Assertions.assertEquals(totalFee1, dateTimeAt6.getRight());

        Integer totalFee2 = tollCalculator.getDayTollFee(car, dateTimeAt7.getLeft());
        Assertions.assertEquals(totalFee2, dateTimeAt7.getRight());

        Integer totalFee3 = tollCalculator.getDayTollFee(car, dateTimeAt8.getLeft());
        Assertions.assertEquals(totalFee3, dateTimeAt8.getRight());
    }


    @Test
    void test_car_trip_two_fees_at_same_hour_in_rush_hour() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(car,
                dateTimeAt6.getLeft(),
                dateTimeAt7.getLeft()
        );
        Assertions.assertEquals(totalFee, Math.max(dateTimeAt6.getRight(), dateTimeAt7.getRight()));
    }

    @Test
    void test_car_trip_two_fees_at_different_hour_in_rush_hour() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(car,
                dateTimeAt6.getLeft(),
                dateTimeAt8.getLeft()
        );
        Assertions.assertEquals(totalFee, Math.addExact(dateTimeAt6.getRight(), dateTimeAt8.getRight()));
    }

    @Test
    void test_car_trip_fees_at_mix_of_different_and_same_hour_in_rush_hour() {
        Car car = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(car,
                dateTimeAt6.getLeft(),
                dateTimeAt7.getLeft(),
                dateTimeAt7_1.getLeft(),
                dateTimeAt8.getLeft()
        );
        Assertions.assertEquals(totalFee, Math.addExact(Math.max(dateTimeAt6.getRight(), dateTimeAt7.getRight()), Math.max(dateTimeAt7_1.getRight(), dateTimeAt8.getRight())));
    }

    @Test
    void test_car_trip_fees_from_8_to_14() {
        Car car = new Car();
        Integer totalFee1 = tollCalculator.getDayTollFee(car, dateTimeAt8.getLeft());
        Assertions.assertEquals(totalFee1, dateTimeAt8.getRight());

        Integer totalFee2 = tollCalculator.getDayTollFee(car, dateTimeAt8_30.getLeft());
        Assertions.assertEquals(totalFee2, dateTimeAt8_30.getRight());

        Integer totalFee3 = tollCalculator.getDayTollFee(car, dateTimeAt9.getLeft());
        Assertions.assertEquals(totalFee3, dateTimeAt9.getRight());

        Integer totalFee4 = tollCalculator.getDayTollFee(car, dateTimeAt14.getLeft());
        Assertions.assertEquals(totalFee4, dateTimeAt14.getRight());

        Integer totalFee5 = tollCalculator.getDayTollFee(car, dateTimeAt14_45.getLeft());
        Assertions.assertEquals(totalFee5, dateTimeAt14_45.getRight());
    }

    @Test
    void test_car_trip_in_holidays_in_rush_hour() {
    }

    @Test
    void test_car_trip_in_weekends_in_rush_hour() {
        Car car = new Car();
        Integer totalSatFee = tollCalculator.getDayTollFee(car, saturdayDate);
        Assertions.assertEquals(totalSatFee, 0);

        Integer totalSunFee = tollCalculator.getDayTollFee(car, sundayDate);
        Assertions.assertEquals(totalSunFee, 0);
    }

    @Test
    void test_car_trip_with_fees_greater_than_max() {
    }

    @Test
    void test_fee_free_vehicle() {
        Motorbike vehicle = new Motorbike();
        Integer totalFee = tollCalculator.getDayTollFee(vehicle, dateTimeAt6.getLeft());
        Assertions.assertEquals(totalFee, 0);
    }

    @Test
    void test_car_trip_at_holiday() {
        Car vehicle = new Car();
        Integer totalFee = tollCalculator.getDayTollFee(vehicle, dateTimeHolidayAt6.getLeft());
        Assertions.assertEquals(totalFee, 0);
    }

}
