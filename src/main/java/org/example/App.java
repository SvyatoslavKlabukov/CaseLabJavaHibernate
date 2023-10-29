package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoCriteria;
import org.example.entity.User;
import org.example.hibernate.HibernateUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class App 
{

    private static final UserDao USER_DAO = new UserDaoCriteria();

    public static void main( String[] args ) throws SQLException {

        //create
        User user1 = new User();
        user1.setFirstname("Tanya");
        user1.setLastname("Sidorova");
        user1.setEmail("tsidorova@mail.com");
        user1.setAge(20);
        java.time.LocalDateTime time = LocalDateTime.now();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user1.setEventDate(timestamp);
        USER_DAO.add(user1);

        //read
        List<User> users = USER_DAO.getAll();
        users.forEach(System.out::println);
        Integer id = 56;
        System.out.println(USER_DAO.getById(id));

        //read filter
        List<User> users2 = USER_DAO.getAll("firstname", "tos", "like");
        users2.forEach(System.out::println);
        List<User> users3 = USER_DAO.getAll("age", 30, "<");
        users3.forEach(System.out::println);

        //update
        user1.setAge(17);
        USER_DAO.update(user1);

        //delete
        USER_DAO.delete(users.get(0));

        //read HQL
        List<User> users4 = USER_DAO.getAllHQL();
        users4.forEach(System.out::println);
        Integer id2 = 64;
        System.out.println(USER_DAO.getIdHQL(id2));

        //read filter HQL
        List<User> users5 = USER_DAO.getAllHQL("age", 30, "<");
        users5.forEach(System.out::println);

        //update HQL
        user1.setAge(50);
        USER_DAO.updateHQL(user1);

        //delete HQL
        USER_DAO.deleteHQL(users4.get(0));

        HibernateUtil.shutdown();
    }
}
