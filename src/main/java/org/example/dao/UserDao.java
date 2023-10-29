package org.example.dao;

import org.example.entity.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface UserDao {

    //create
    void add(User user) throws SQLException;

    //read
    List<User> getAll() throws SQLException;
    User getById(Integer id) throws SQLException;
    <V> List<User> getAll(String field, V value, String sign) throws SQLException;

    //update
    void update(User user) throws SQLException;

    //delete
    void delete(User user) throws SQLException;


    List<User> getAllHQL() throws SQLException;

    User getIdHQL(Integer id) throws SQLException;

    void updateHQL(User user) throws SQLException;

    void deleteHQL(User user) throws SQLException;

    <V> List<User> getAllHQL(String field, V value, String sign) throws SQLException;
}
