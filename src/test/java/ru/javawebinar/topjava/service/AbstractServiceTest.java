package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.TimingRules;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
abstract public class AbstractServiceTest {
    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    /*клас для іимірювання часу виконання тестів
    public static final Stopwatch STOPWATCH = new Stopwatch() {
        *//*метод автоматично викликається при завершенні тестового методу. Параметри:
        nanos - кількість часу (нс) виконання тесту (методу); description - об'єкт, з якого можна отримати клас, метод та іншу інформацію про тест. *//*
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-95s %7d", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };*/
    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;

    /* junit анотація, яка дозволяє вказати в тестовому методі тип класу виключення, перевірити повідомлення:
        @Test
          public void throwsNullPointerExceptionWithMessage() {
            thrown.expect(NullPointerException.class); // очікуване виключення
            thrown.expectMessage("happened?");
            thrown.expectMessage(startsWith("What"));
            throw new NullPointerException("What happened?");
          }
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }
}