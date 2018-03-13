package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

@Controller
public class MealRestController extends AbstractMealController{

    public List<Meal> getAll(int userId){return super.getAll(userId);}


    public List<Meal> getFilteredList(int userId, SearchFilter filter){
        return super.getFilteredList(userId, filter);
    }

    public Meal get(int id){
        return super.get(id);
    }

    public Meal create(Meal meal){
        return super.create(meal);
    }

    public void delete(int id){
        super.delete(id);
    }

    public void update(Meal meal, int id){
        super.update(meal, id);
    }

}