package org.example.dao;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Path;
import org.example.entity.User;
import org.example.hibernate.HibernateUtil;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

public class UserDaoCriteria implements UserDao{
    private final SessionFactory sessionFactory;
    private static int pageNumber = 1;
    private static final int pageSize = 5;

    public UserDaoCriteria() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void add(User user) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(user);

        transaction.commit();
        session.close();
    }
    @Override
    public List<User> getAllHQL() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String countQ = "Select count (u.id) from User u";//pagination
        Query<Long> countQuery = session.createQuery(countQ, Long.class);//pagination
        Long countResults = countQuery.uniqueResult();//pagination
        int lastPageNumber = (int) (Math.ceil(countResults / pageSize));//pagination

        Query query = session.createQuery("from User");
        //pagination
        List<User> results = new ArrayList<>();
        pageNumber = 1;
        while (pageNumber < countResults.intValue()) {
            query.setFirstResult(pageNumber-1);
            query.setMaxResults(pageSize);
            results.addAll(query.list());
            pageNumber += pageSize;
        }
        //pagination

        transaction.commit();
        session.close();
        return results;
    }
    @Override
    public List<User> getAll() throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);//pagination
        countQuery.select(builder.count(countQuery.from(User.class)));//pagination
        Long count = session.createQuery(countQuery).getSingleResult();//pagination

        CriteriaQuery<User> critQuery = builder.createQuery(User.class);
        Root<User> root = critQuery.from(User.class);
        CriteriaQuery<User> select = critQuery.select(root);

        //pagination
        List<User> results = new ArrayList<>();
        pageNumber = 1;
        TypedQuery<User> typedQuery = session.createQuery(select);
        while (pageNumber < count.intValue()) {
            typedQuery.setFirstResult(pageNumber - 1);
            typedQuery.setMaxResults(pageSize);
            results.addAll(typedQuery.getResultList());
            pageNumber += pageSize;
        }
        //pagination

        transaction.commit();
        session.close();
        return results;
    }
    @Override
    public User getIdHQL(Integer id) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from User where id = :paramName");
        query.setParameter("paramName", id);
        User user = (User) query.getSingleResult();

        transaction.commit();
        session.close();
        return user;
    }
    @Override
    public User getById(Integer id) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> critQuery = builder.createQuery(User.class);
        Root<User> root = critQuery.from(User.class);
        critQuery.select(root);
        critQuery.where(builder.equal(root.get("id"), id));

        Query<User> query = session.createQuery(critQuery);
        User user = query.getSingleResult();

        transaction.commit();
        session.close();
        return user;
    }
    @Override
    public <V> List<User> getAllHQL(String field, V value, String sign) throws SQLException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String countQ = "Select count (u.id) from User u";//pagination
        Query<Long> countQuery = session.createQuery(countQ, Long.class);//pagination
        Long countResults = countQuery.uniqueResult();//pagination
        int lastPageNumber = (int) (Math.ceil(countResults / pageSize));//pagination

        Query query = session.createQuery("from User where "+field+" "+sign+" :paramName");
        query.setParameter("paramName", value);

        //pagination
        List<User> results = new ArrayList<>();
        pageNumber = 1;
        while (pageNumber < countResults.intValue()) {
            query.setFirstResult(pageNumber-1);
            query.setMaxResults(pageSize);
            results.addAll(query.list());
            pageNumber += pageSize;
        }
        //pagination

        transaction.commit();
        session.close();
        return results;
    }
    @Override
    public <V> List<User> getAll(String field, V value, String sign) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);//pagination
        countQuery.select(builder.count(countQuery.from(User.class)));//pagination
        Long count = session.createQuery(countQuery).getSingleResult();//pagination

        CriteriaQuery<User> critQuery = builder.createQuery(User.class);
        Root<User> root = critQuery.from(User.class);
        CriteriaQuery<User> select = critQuery.select(root);

        switch (sign) {
            case "=" -> critQuery.where(builder.equal(root.get(field), value));
            case ">" -> critQuery.where(builder.gt(root.get(field), (Number) value));
            case "<" -> critQuery.where(builder.lt(root.get(field), (Number) value));
            case "like" -> critQuery.where(builder.like(root.get(field), "%"+ value +"%"));
        }


        //pagination
        List<User> results = new ArrayList<>();
        pageNumber = 1;
        TypedQuery<User> typedQuery = session.createQuery(select);
        while (pageNumber < count.intValue()) {
            typedQuery.setFirstResult(pageNumber - 1);
            typedQuery.setMaxResults(pageSize);
            results.addAll(typedQuery.getResultList());
            pageNumber += pageSize;
        }
        //pagination

        transaction.commit();
        session.close();
        return results;
    }

    @Override
    public void updateHQL(User user) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();


        Query query = session.createQuery("update User set firstname = :nameParam, lastname = :lastNameParam" +
                ", email = :emailParam, age = :ageParam, eventDate = :eventDateParam"+
                " where id = :nameCode");

        query.setParameter("nameCode", user.getId());
        query.setParameter("nameParam", user.getFirstname());
        query.setParameter("lastNameParam", user.getLastname() );
        query.setParameter("emailParam", user.getEmail() );
        query.setParameter("ageParam", user.getAge() );
        query.setParameter("eventDateParam", user.getEventDate());

        int result = query.executeUpdate();

        transaction.commit();
        session.close();
    }
    @Override
    public void update(User user) throws SQLException {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
        Root<User> root = update.from(User.class);
        update.set("firstname", user.getFirstname());
        update.set("lastname", user.getLastname());
        update.set("email", user.getEmail());
        update.set("age", user.getAge());
        Path<Integer> eventDatePath = root.get("eventDate");
        update.set(eventDatePath, root.get("eventDate"));
        update.where(cb.equal(root.get("id"), user.getId()));
        //update.set("event_date", user.getEventDate());

        session.createQuery(update).executeUpdate();

        transaction.commit();
        session.close();
    }
    @Override
    public void deleteHQL(User user) throws SQLException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();


        Query query =  session.createQuery("delete User where id = :param");
        query.setParameter("param", user.getId());
        int result = query.executeUpdate();

        transaction.commit();
        session.close();
    }
    @Override
    public void delete(User user) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<User> criteriaDelete = cb.createCriteriaDelete(User.class);
        Root<User> root = criteriaDelete.from(User.class);
        criteriaDelete.where(cb.equal(root.get("id"), user.getId()));

        session.createQuery(criteriaDelete).executeUpdate();
        transaction.commit();
        session.close();

    }

}
