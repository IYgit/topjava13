package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {
    private static final Sort SORT_DATETIME = new Sort(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        // умова виконується при доступі до чужої їжі
        if (!meal.isNew() && get(meal.getId(), userId) == null)
            return null;
        // set user?

        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal != null){
            crudRepository.delete(meal);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).get();
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> mealList = crudRepository.findAll(SORT_DATETIME);
        mealList.sort((m1, m2) -> {
            int result = m2.getDate().compareTo(m1.getDate());
            if (result != 0) return result;
            else return m1.getTime().compareTo(m2.getTime());
        });

        return mealList;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        return null;
    }
}
