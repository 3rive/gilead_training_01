package com.gilead.training.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDatabaseConnectorDemo {
    public static void main(String[] args) {
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila","root","Letmein@123");
            if(connection !=null){
                Statement statement;
                statement = connection.createStatement();
                ResultSet resultSet;
                resultSet = statement.executeQuery("select *from actor order by first_name asc");
                String actorFirstName;
                String actorLastName;
                if(resultSet !=null){
                    System.out.println("Database Connected and query executed!!!");
                    while(resultSet.next()){
                        actorFirstName = resultSet.getString("first_name");
                        actorLastName = resultSet.getString("last_name");
                        System.out.println("Name :::: First Name : "+ actorFirstName + "  Last Name : "+ actorLastName);
                    }
                }
            }
            else{
                System.out.println("Database Not Connected - Check your DB properties");
            }
        }
        catch(Exception exception){
            System.out.println(exception.toString());
        }

    }
}
