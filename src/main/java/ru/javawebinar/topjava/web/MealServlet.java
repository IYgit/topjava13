package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iy on 05.03.18.
 */
//TODO: create error page
public class MealServlet extends HttpServlet {
    private int id;
    private boolean add;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");  // set encoding (only for method Post!)
        try {
            LocalDateTime localDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(request.getParameter("date").trim(), LocalDateTime::from);
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            if (add)
                MealsUtil.mealsMap.put(MealsUtil.mealsMap.size(), new Meal(localDateTime, description, calories));
            else MealsUtil.mealsMap.replace(id, new Meal(localDateTime, description, calories));
        } catch (Exception e) {
            e.printStackTrace();
        }

        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
            if (request.getParameter("del") != null)
                MealsUtil.mealsMap.remove(id);
        } else id = -1;

        if (request.getParameter("add") != null)
            add = true;
        else add = false;

        List<MealWithExceed> mealWithExceeds = MealsUtil.convertToMealWithExceed(new ArrayList<>(MealsUtil.mealsMap.values()), 2000);
        request.setAttribute("meals", mealWithExceeds);


        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
