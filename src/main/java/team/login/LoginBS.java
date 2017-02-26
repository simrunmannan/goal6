package team.login;

import team.model.User;
import team.model.enums.UserType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginBS {
    private static final Logger LOGGER = Logger.getLogger("LoginBS");

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
     * Login
     * Checks if username and password are valid and returns user object if so
     * @param user Username to attempt to log in
     * @param pass Password for user
     * @return user object if successful, null if it is not.
     */
    public static User login(String user, String pass) {
        Connection c = connectToDatabase();
        try {
            Statement smt = c.createStatement();
            String sql = "SELECT * FROM USER WHERE USERNAME = " +
                    "'" + user + "';";
            ResultSet rs = smt.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                //Checks if result set is empty
                smt.close();
                c.close();
                LOGGER.log(Level.INFO, "Bad login attempt: User not found");
                return null;
            }
            String username = rs.getString("USERNAME");
            String password = rs.getString("PASSWORD");
            UserType userType = UserType.valueOf(rs.getString("USERTYPE").toUpperCase());
            String email = rs.getString("EMAIL");
            smt.close();
            c.close();
            if (!password.equals(pass)) {
                LOGGER.log(Level.INFO, "Bad login attempt: User password incorrect");
                return null;
            }
            LOGGER.log(Level.INFO, "Successful login attempt: User: "+ username);
            return new User(username, password, userType, email);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}
