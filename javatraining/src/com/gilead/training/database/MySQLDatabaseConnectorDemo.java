package com.gilead.training.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabaseConnectorDemo {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "Letmein@123");
            if (connection != null) {
                Statement statement;
                statement = connection.createStatement();
                ResultSet resultSet;
                resultSet = statement.executeQuery("select *from actor order by first_name asc");
                String actorFirstName;
                String actorLastName;
                int actorId;
                Timestamp lastUpdate;
                List<Actor> actorsinDB = new ArrayList<Actor>();
                if (resultSet != null) {
                    System.out.println("Database Connected and query executed!!!");
                    while (resultSet.next()) {
                        Actor actor = new Actor();
                        actorId = resultSet.getInt("actor_id");
                        actorFirstName = resultSet.getString("first_name");
                        actorLastName = resultSet.getString("last_name");
                        lastUpdate = resultSet.getTimestamp("last_update");
                        actor.setActorId(actorId);
                        actor.setFirstName(actorFirstName);
                        actor.setLastName(actorLastName);
                        actor.setLastUpdate(lastUpdate);
                        actorsinDB.add(actor);
                    }
                }
                for (Actor actorFromDB : actorsinDB) {
                    //print the actor's first name whose ID is 132
                    if (actorFromDB.getActorId() == 132) {
                        System.out.println("Full Name ::: " + actorFromDB.getFirstName() + " , " + actorFromDB.getLastName());
                    }
                }
            } else {
                System.out.println("Database Not Connected - Check your DB properties");
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }

    }
}
