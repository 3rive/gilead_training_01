package com.gilead.rest.training.dao;

import com.gilead.rest.training.dto.PersonDTO;

import java.sql.*;

public class PersonDao {

    public void insert(PersonDTO person) {
        PreparedStatement statement;
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila",
                    "root",
                    "Letmein@123");

            statement = connection.prepareStatement("INSERT INTO sakila.person values(?,?,?,?,?,?)");

            statement.setString(1, person.getId());
            statement.setString(2, person.getFirstname());
            statement.setString(3, person.getLastname());
            statement.setString(4, person.getGender());
            statement.setDate(5, person.getDob());
            statement.setString(6, person.getMobile());
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public PersonDTO get(int id) {
        PersonDTO person = new PersonDTO();
        return person;
    }
}
