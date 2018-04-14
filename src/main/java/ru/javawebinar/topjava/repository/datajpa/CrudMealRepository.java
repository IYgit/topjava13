package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Override
    @Transactional
    Meal save(Meal meal);

    @Query("DELETE FROM Meal m WHERE m.id = :id and m.user.id = :userId")
    boolean delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m where m.id = :id and m.user.id = :userId")
    Meal get(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m where m.user.id = :userId ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("userId") int userId);

    @SuppressWarnings("JpaQlInspection") // компілятор вказує на невідповідність типів DateTime
    @Query("SELECT m from Meal m where m.user.id = :userId and m.dateTime between :startDate and :endDate order by m.dateTime DESC")
    List<Meal> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId);
}