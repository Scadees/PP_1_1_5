package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists users" +
                    "(id bigint primary key auto_increment, name varchar(45), lastName varchar(45), age smallint)");
        } catch (SQLException ignored) {}
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table if exists users");
        } catch (SQLException ignored) {}
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into users" +
                "(name, lastName, age)" + "values (?, ?, ?)")) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();

        } catch (SQLException ignored) {}
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from users where id = ?")) {
            statement.setLong(1, id);
        } catch (SQLException ignored) {}
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("lastName"),
                        rs.getByte("age"));
                user.setId(rs.getLong("id"));
                allUsers.add(user);
            }

        } catch (SQLException ignored) {}

        return allUsers;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {

            statement.executeUpdate("truncate table users");

        } catch (SQLException ignored) {}
    }
}
