package team.dashboard;


/**
 * Abstract Controller for all controllers spawned form DashboardController
 * Allows for automatic setting of callbacks
 */
public abstract class AbstractDashboardController {

    protected DashboardController dashboardController;

    void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    /**
     * Allows for initialization method to be called from @see DashboardController
     */
    public abstract void initController();
}
