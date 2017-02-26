package team.register;

import team.model.User;
import team.model.enums.UserType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterBS {

    private static final Logger LOGGER = Logger.getLogger("RegisterBS");

    private static Connection connectToDatabase( )
    {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return c;
    }

    /**
     * CreateUserTable
     *
     * Creates the user table in the SQLLite database
     */
    public static void createUserTable() {
        try {
            Connection c = connectToDatabase();
            Statement smt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USER" +
                " (USERNAME          TEXT    NOT NULL    UNIQUE, " +
                " PASSWORD          TEXT    NOT NULL, " +
                " USERTYPE          TEXT    NOT NULL," +
                " EMAIL             TEXT" +
                ")";

            smt.executeUpdate(sql);
            smt.close();
            c.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    /**
     * addUser
     * Adds the user to the database
     * @param username Username of the user
     * @param password Password of the user
     * @param userType UserType of the user
     * @param email Email of the user
     * @return user object
     */
    public static User addUser(String username, String password, UserType userType, String email) {
            if ((username == null) || (password == null) || (userType == null)) {
                return null;
            }
            Connection c = connectToDatabase();
        try {
            Statement smt = c.createStatement();
            String sql = "INSERT INTO USER (USERNAME, PASSWORD, USERTYPE, EMAIL)" +
                    " VALUES " +
                    "('" + username + "'" +
                    ", '" + password + "'" +
                    ", '" + userType.toString() + "'" +
                    ", '" + email + "'" +
                    ")";
            smt.executeUpdate(sql);
            smt.close();
            c.close();
            return new User(username, password, userType, email);
        } catch (java.sql.SQLException sqlE) {
            //return a null for user if the username is not unique
            if (sqlE.getMessage().contains("UNIQUE")) {
                return null;
            } else {
                LOGGER.log(Level.SEVERE, sqlE.getMessage());
                return null;
            }
        }
         catch (Exception e){
             LOGGER.log(Level.SEVERE, e.getMessage());
             return null;
        }
    }


}
