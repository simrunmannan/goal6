package team.dashboard.settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

class SettingsBS {
    private static final Logger LOGGER = Logger.getLogger("SettingsBS");

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
     * updateUser
     * Updates user credentials
     * @param username Username of the user to update credentials
     * @param email New email of the user
     * @param password New password of the user
     */
    public static void updateUser(String username, String email, String password) {
        Connection c = connectToDatabase();
        try {
            Statement smt = c.createStatement();
            //Not sure why I have to do this but the tutorial does it..
            c.setAutoCommit(false);
            if (email != null) {
                String sql = "UPDATE USER SET EMAIL = '" + email +
                        "' WHERE USERNAME = '" + username + "';";
                smt.executeUpdate(sql);
                c.commit();
                LOGGER.log(Level.INFO, "Updated user's email");
            }
            if (password != null) {
                String sql = "UPDATE USER SET PASSWORD = '" + password +
                        "' WHERE USERNAME = '" + username + "';";
                smt.executeUpdate(sql);
                c.commit();
                LOGGER.log(Level.INFO, "Updated user's password");
            }
            smt.close();
            c.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
