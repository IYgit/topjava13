package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_1 = START_SEQ;
    public static final int MEAL_ID_2 = START_SEQ + 1;
    public static final int MEAL_ID_3 = START_SEQ + 2;
    public static final int USER_ID_1 = 100000;
    public static final int USER_ID_2 = 100001;

    public static final Meal MEAL_1 = new Meal(MEAL_ID_1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .parse("2015-05-31 10:00", LocalDateTime::from), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_ID_2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .parse("2015-05-30 13:00", LocalDateTime::from), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_ID_3, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .parse("2015-05-29 15:00", LocalDateTime::from), "Обед", 1000);

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected){ // important order of expected
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected){ // important order of expected
        assertThat(actual).isEqualTo(expected);
    }
}
