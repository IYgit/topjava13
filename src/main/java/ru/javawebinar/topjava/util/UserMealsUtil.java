package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (mealList == null || mealList.isEmpty() || startTime == null || endTime == null)
            return null;

            List<UserMealWithExceed> mealWithExceeds = getUserMealWithExceeds_3(mealList, startTime, endTime, caloriesPerDay);

        return mealWithExceeds;
    }

    private static List<UserMealWithExceed> getMealWithExceedWithAggregator(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // accumulator
        final class Aggregate {
            final private List<UserMeal> dailyMeals = new ArrayList<>();
            private int dailySumOfCalories;

            private void accumulate(UserMeal meal){
                dailySumOfCalories += meal.getCalories();
                if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    dailyMeals.add(meal);
            }

            // never invoked if upstream is sequential
            private Aggregate combine(Aggregate that){
                this.dailySumOfCalories += that.dailySumOfCalories;
                this.dailyMeals.addAll(dailyMeals);

                return this;
            }

            private Stream<UserMealWithExceed> finisher(){
                boolean exceed = dailySumOfCalories > caloriesPerDay;

                return dailyMeals.stream().map(meal -> new UserMealWithExceed(meal, exceed));
            }
        }

        // accumulating meals with the rules of accumulator (Aggregator)
        Map<LocalDate, Stream<UserMealWithExceed>> streamMap = mealList.stream().collect(
                Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), // grouping key
                        // accumulator
                        Collector.of(Aggregate::new, Aggregate::accumulate, Aggregate::combine, Aggregate::finisher))
        );

        // gathering together all streams to single stream and it converting to List
        List<UserMealWithExceed> mealWithExceeds = streamMap.values().stream().flatMap(stream -> stream).collect(Collectors.toList());

        return mealWithExceeds;
    }

    /**
     * Method is equivalent to getUserMealWithExceeds()*/
    private static List<UserMealWithExceed> getUserMealWithExceeds_3(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        // getting Map<date, summary_calories_per_day>
        for (UserMeal meal : mealList)
            caloriesPerDayMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);

        List<UserMealWithExceed> withExceeds = mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) // getting time filtered stream
                // converting  UserMeal to UserMealWithExceed stream
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());  // getting List<UserMealWithExceed>

        return withExceeds;
    }

    /**
     * Method is equivalent to getUserMealWithExceeds()*/
    private static List<UserMealWithExceed> getUserMealWithExceeds_2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // getting Map<date, summary_calories_per_day>
        Map<LocalDate, Integer> caloriesPerDayMap = mealList.stream().collect(
                /** if caret is placed on Collectors.grouping: ALT + ENTER -> suggests to replace Stream API to loop */
                Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        // same like in getUserMealWithExceeds_3() method
        List<UserMealWithExceed> withExceeds = mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        return withExceeds;
    }

    /**
     * Method converts List<UserMeal> to List<UserMealWithExceed> using Stream API
     * @param mealList - list of UserMeal;
     * @param startTime - start time for filtering;
     * @param endTime - end time for filtering;
     * @param caloriesPerDay - value for calculating UserMealWithExceed.exceed field.
     * If summary calories per day overflows caloriesPerDay UserMealWithExceed.exceed is set to true otherwise false.*/
    private static List<UserMealWithExceed> getUserMealWithExceeds(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // getting Map<date, UserMeal_per_day>
        Map<LocalDate, List<UserMeal>> listMap = mealList.stream().collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()));

        // getting UserMealWithExceed stream
        Stream<UserMealWithExceed> exceedStream = Stream.empty();
        for (LocalDate date: listMap.keySet()){
            boolean exceed;
            // if calories per day exceeds caloriesPerDay value
            if (listMap.get(date).stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay){
                exceed = true;
            } else exceed = false;
            // concatenating streams
            exceedStream = Stream.concat(exceedStream, // old stream
            // new stream
                    listMap.get(date).stream().map( // mapping UserMeal to UserMealWithExceed
                            meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed))
            .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(),startTime, endTime))); // time filtering
        }

        return exceedStream.collect(Collectors.toList());
    }

    /**
     * Method is equivalent to getUserMealWithExceeds()*/
    private static List<UserMealWithExceed> getUserMealWithExceedsWithoutStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Set<LocalDate> localDatesSet = getLocalDatesSet(mealList);

        List<UserMealWithExceed> withExceedList = new ArrayList<>();

        for (LocalDate localDate: localDatesSet){
            List<UserMeal> dayMealList = getDayMealList(mealList, localDate);
            int summaryCalories = getSummaryCalories(dayMealList);
            for (UserMeal meal: dayMealList){
                withExceedList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), summaryCalories > caloriesPerDay));
            }
        }
        return getTimeFilteredList(withExceedList, startTime, endTime);
    }

    /**
     * Method calculates summary calories per day
     * @param dayMealList - list of meal per day
     * @return - summary calories per day*/
    private static int getSummaryCalories(List<UserMeal> dayMealList) {
        int summaryCalories = 0;
        for (UserMeal meal: dayMealList){
            summaryCalories+= meal.getCalories();
        }
        return summaryCalories;
    }

    /**
     * Method return list of meal for certain day
     * @param mealList - total list of meal
     * @param date - date for getting list of meal
     * @return list of meal*/
    private static List<UserMeal> getDayMealList(List<UserMeal> mealList, LocalDate date) {
        if (mealList == null || mealList.isEmpty() || date == null)
            return null;

        List<UserMeal> dayMealList = new ArrayList<>();
        for (UserMeal meal: mealList){
            if (meal.getDateTime().toLocalDate().isEqual(date))
                dayMealList.add(meal);
        }
        return dayMealList;
    }

    /**
     * Method return the Set of days for given list
     * @param mealList - list of meals for getting Set of dates
     * @return set of days*/
    private static Set<LocalDate> getLocalDatesSet(List<UserMeal> mealList) {
        if (mealList == null || mealList.isEmpty())
            return null;

        Set<LocalDate> localDates = new HashSet<>();
        for(UserMeal meal: mealList){
            localDates.add(meal.getDateTime().toLocalDate());
        }
        return localDates;
    }


    /**
     * Method reject meals out of date limits
     * @param mealList - list of meal for checking data limits
     * @param startTime - start time
     * @param endTime - end time
     * @return filtered list of meals*/
    private static List<UserMealWithExceed> getTimeFilteredList(List<UserMealWithExceed> mealList, LocalTime startTime, LocalTime endTime) {
        if (mealList == null || mealList.isEmpty() || startTime == null || endTime == null)
            return null;

        List<UserMealWithExceed> timeFiltered = new ArrayList<>();

        for (UserMealWithExceed meal: mealList){
            if (!TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                continue;
            else timeFiltered.add(meal);
        }

        return timeFiltered;
    }
}
