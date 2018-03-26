package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
/** Як анотаціями (та xml) зв'язувати таблиці з класами:
 *  https://en.wikibooks.org/wiki/Java_Persistence/Identity_and_Sequencing*/
// іменовані jpa-запити. Код перевіряється при завантаженні програми. Якщо є помилка в запиті, програма не завантажиться.
@NamedQueries({
        // видалити користувача по id
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        // отримати користувача по email разом з його ролями
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=?1"),
        // отримати всіх користувачів разом з їх ролями, впорядкувати по імені та email
        @NamedQuery(name = User.ALL_SORTED, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.name, u.email"),
})
@Entity /* таблиця user з унікальним email */
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity {

    public static final String DELETE = "User.delete";
    public static final String BY_EMAIL = "User.getByEmail";
    public static final String ALL_SORTED = "User.getAllSorted";

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    /* відображення поля класу password в поле таблиці password: */
    @Column(name = "password", nullable = false) // в таблиці поле не може бути null
    @NotBlank                                    // в класі поле не може бути пустим
    @Size(min = 5, max = 100)                    // min/max значення поля
    private String password;

    // вказуємо тип в таблиці, в який треба відобразити поле, та знаення за замовчанням ("bool default true")
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    // вказуємо назву поля, тип в таблиці, в який треба відобразити поле, та знаення за замовчанням ("timestamp default now()")
    @Column(name = "registered", columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

    /* створення таблиці ролей:
     * 1. визначаємо спосіб збереження ролей (String або порядковий номер ролі);
     * 2. вказуємо назву табилці ("user_roles") та об'єднуюче поле (user_id, куди буде записано primary key
     *      таблиці user) для користувача та його ролей;
     * 3. вказуємо назву поля ("role"), в якому будуть зберігатися ролі;
     * 4. вказуємо спосіб отримання ролей (FetchType.EAGER) - разом із даними про користувача (інакше потрібен бін для ролей).*/
    @Enumerated(EnumType.STRING) // ролі будемо зберігати як String (можна EnumType.ORDINAL - по номерам)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) // створення зв'язки (таблиці user) з таблицею user_roles по кллючу user_id
    @Column(name = "role")
    /*FetchType.EAGER - ролі достаються з БД разом з user (користувачем). Якщо покласти FetchType.LAZY, - користувач буде отриманий без ролей.*/
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "calories_per_day", columnDefinition = "int default 2000")
    @Range(min = 10, max = 10000)
    private int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getCaloriesPerDay(), u.isEnabled(), u.getRegistered(), u.getRoles());
    }

    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name, email, password, DEFAULT_CALORIES_PER_DAY, true, new Date(), EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay, boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", caloriesPerDay=" + caloriesPerDay +
                '}';
    }
}