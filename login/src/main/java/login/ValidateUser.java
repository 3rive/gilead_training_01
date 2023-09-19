package login;

import java.sql.*;

public class ValidateUser {

    public static User validateUser(String username, String password){
        Connection connection = null;
        User userinDB = new User();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "Letmein@123");
            if (connection != null) {
                Statement statement;
                statement = connection.createStatement();
                ResultSet resultSet;
                resultSet = statement.executeQuery("select *from user where username ='"+username+"'");
                if (resultSet != null) {
                    System.out.println("Database Connected and query executed!!!");
                    while (resultSet.next()) {
                        userinDB.setUsername(resultSet.getString("username"));
                        userinDB.setPassword(resultSet.getString("password"));
                        userinDB.setRole(resultSet.getString("role"));
                    }
                }
                if(userinDB.getUsername().equalsIgnoreCase(username)
                && userinDB.getPassword().equalsIgnoreCase(password)){
                    return userinDB;
                }
            } else {
                System.out.println("Database Not Connected - Check your DB properties");
                return userinDB;
            }

        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
        return userinDB;
    }
}
