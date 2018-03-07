package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iy on 07.03.18.
 */
public class MealDaoImpl implements MealDao {

    @Override
    public void addMeal(Meal meal) {
        MealsUtil.mealsMap.put(Meal.ID.get(), meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        MealsUtil.mealsMap.replace(meal.getId(), meal);
    }

    @Override
    public void removeMeal(int id) {
        MealsUtil.mealsMap.remove(getMealById(id));
    }

    @Override
    public Meal getMealById(int id) {
        return MealsUtil.mealsMap.get(id);
    }

    @Override
    public List<Meal> listMeals() {
        return new ArrayList<>(MealsUtil.mealsMap.values());
    }
}
