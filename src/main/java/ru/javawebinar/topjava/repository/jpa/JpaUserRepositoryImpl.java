package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Методи, позначені @Transactional виконуються як транзакція, т.т. метод або повнісю виконався, або повністю не виконався.
 * Кожна транзакція - це нове з'єднання з БД. При вході в метод транзакція відкривається, при виході - закривається.
 * В JPA транзакції треба здійснювати вручну:
 *
 * UserTransaction utx = entityManager.getTransaction();
 * try {
 *      utx.begin();
 *      businessLogic();
 *      utx.commit();
 * } catch(Exception ex) {
 *      utx.rollback();
 *      throw ex;
 * }
 *
 * В Spring це робиться з допомогою транзакцій @Transactional(readOnly = true, propagation=Propagation.REQUIRED, rollbackFor=Exception.class):
 * readOnly = true - дозволяються лише операції читання. Якщо здійснити вставку в БД, відбудеться Exception при роботі через JDBC
 * (при ORM в більшості випадків прапор readOnly ігнорується);
 * propagation=Propagation.REQUIRED - для метода розпочати нову транзакцію (режим за замовчанням);
 * rollbackFor=Exception.class - будь-яке викллючення призведе до відкату транзакції.
 *
 * @Transactional(readOnly = true, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
 * public long insertTrade(TradeData trade) throws Exception {
        //Робота через JDBC...
        }
 *
 * propagation=Propagation.SUPPORTS - виконати метод в поточній (вже відкритій) транзкації;
 * readOnly = true - проігнорується, оскільки спрацьовує лише при відкритті нової транзакції.
 * @Transactional(readOnly = true, propagation=Propagation.SUPPORTS)
 * public long insertTrade(TradeData trade) throws Exception {
        //Работа через JDBC...
        }
*/

@Repository
@Transactional(readOnly = true)
public class JpaUserRepositoryImpl implements UserRepository {

/*
    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }
*/

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    @Override
    public User get(int id) {
        return em.find(User.class, id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {

/*      User ref = em.getReference(User.class, id);
        em.remove(ref);

        Query query = em.createQuery("DELETE FROM User u WHERE u.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
*/
        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = em.createNamedQuery(User.BY_EMAIL, User.class)
                .setParameter(1, email)
                .getResultList();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.ALL_SORTED, User.class).getResultList();
    }
}
