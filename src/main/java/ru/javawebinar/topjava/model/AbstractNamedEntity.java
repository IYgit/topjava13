package ru.javawebinar.topjava.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/* клас є базовим для @Entity (User, Meal).
    Сам не є @Entity, але його поля і методи вспадковуються, тому ми його анотуємо.*/
@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank                   // поле не може бути пустим
    @Size(min = 2, max = 100)   // розмір поля від 2 до 100 символів
    @Column(name = "name", nullable = false) // відобразити в БД з іменем "name"
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), id, name);
    }
}