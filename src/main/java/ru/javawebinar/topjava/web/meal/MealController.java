package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by iy on 12.03.18.
 */
public interface MealController {
    List<MealWithExceed> getAll(int userId);

    List<MealWithExceed> getFilteredList(int userId, SearchFilter filter);

    Meal get(int id);

    Meal create(Meal meal);

    void delete(int id);

    void update(Meal meal, int id);


}
