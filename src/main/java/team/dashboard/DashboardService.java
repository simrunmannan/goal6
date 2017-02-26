package team.dashboard;

import team.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;

class DashboardService {
    private static final Logger LOGGER = Logger.getLogger("DashboardService");
    private final User user;

    DashboardService(User user) {
        this.user = user;
    }

    public void logout() {
        LOGGER.log(Level.INFO, "End session: " + user.getUsername() + " (" + user.getUserType() + ")" + " logged out");
    }
}
