package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by iy on 05.03.18.
 */
public class MealServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealWithExceed> mealWithExceeds = MealsUtil.convertToMealWithExceed(MealsUtil.meals, 2000);
        request.setAttribute("meals", mealWithExceeds);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
