package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext // об'єкт для роботи з БД
    private EntityManager em; // bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()){
            User ref = em.getReference(User.class, userId);
            meal.setUser(ref);
            em.persist(meal);
        } else{
            Meal oldMeal = em.find(Meal.class, meal.getId());
            em.getTransaction().begin();
            oldMeal.setDateTime(meal.getDateTime());
            oldMeal.setDescription(meal.getDescription());
            oldMeal.setCalories(meal.getCalories());
            em.getTransaction().commit();
            return em.merge(meal);
        }

        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal.getUser().getId() == userId){
            em.getTransaction().begin();
            em.remove(meal);
            em.getTransaction().commit();
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal= em.find(Meal.class, id);
        if (meal.getUser().getId() == userId){
            return meal;
        } else throw new NotFoundException("Illegal access exception.");
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Meal> query = em.createQuery("select meal from Meal meal where meal.user.id =: userid", Meal.class);
        return query.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        TypedQuery<Meal> query = em.createQuery("select meal from Meal meal where meal.user.id =: userid" +
                " and meal.dateTime >=: startDate and meal.dateTime <=: endDate", Meal.class);
        return query.getResultList();
    }
}