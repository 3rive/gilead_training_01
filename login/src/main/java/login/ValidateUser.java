package login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValidateUser {

    public static boolean validateUser(String username, String password){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "Letmein@123");
            if (connection != null) {
                Statement statement;
                statement = connection.createStatement();
                ResultSet resultSet;
                resultSet = statement.executeQuery("select *from user where username ='"+username+"'");
                User userinDB = new User();
                if (resultSet != null) {
                    System.out.println("Database Connected and query executed!!!");
                    while (resultSet.next()) {
                        userinDB.setUsername(resultSet.getString("username"));
                        userinDB.setPassword(resultSet.getString("password"));
                    }
                }
                if(userinDB.getUsername().equalsIgnoreCase(username)
                && userinDB.getPassword().equalsIgnoreCase(password)){
                    return true;
                }
            } else {
                System.out.println("Database Not Connected - Check your DB properties");
                return false;
            }

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        return false;
    }
}
