package team.dashboard.report;

import team.model.Location;
import team.model.PurityReport;
import team.model.SourceReport;
import team.model.enums.PurityCondition;
import team.model.enums.SourceCondition;
import team.model.enums.SourceType;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportBS {
    private static final Logger LOGGER = Logger.getLogger("ReportService");
    private static final DateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

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
     * createTables
     * Creates the Purity and Source report tables
     */
    public static void createTables() {
        try {
            Connection c = connectToDatabase();
            Statement smt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SOURCE" +
                    " (SOURCE_ID   INTEGER PRIMARY KEY," +
                    " SOURCE_TYPE  INT NOT NULL, " +
                    " LONG         REAL NOT NULL, " +
                    " LAT          REAL NOT NULL," +
                    " CONDITION    INT  NOT NULL," +
                    " DATE         TEXT NOT NULL," +
                    " REPORTER     TEXT" +
                    ")";

            smt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS PURITY" +
                    " (ID               INTEGER PRIMARY KEY," +
                    " SOURCE_ID          INTEGER NOT NULL, " +
                    " CONDITION           INT NOT NULL, " +
                    " VIRUS_PPM           INT NOT NULL," +
                    " CONT_PPM            INT NOT NULL," +
                    " DATE                TEXT NOT NULL," +
                    " REPORTER            TEXT" +
                    ")";
            smt.executeUpdate(sql);
            smt.close();
            c.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    /**
     * Method for submitting new SourceReport.
     * @param location location of reported water source
     * @param sourceType type of water source
     * @param sourceCondition condition of water source
     * @param reporterName name of reporter
     */
    public static void addSourceReport(
            Location location,
            SourceType sourceType,
            SourceCondition sourceCondition,
            String reporterName) {

        Connection c = connectToDatabase();
        int sourceTypeInt = getIntFromSourceType(sourceType);
        int conditionTypeInt = getIntFromCondition(sourceCondition);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String date = dateFormat.format(new Date());
        try {
            Statement smt = c.createStatement();
            String sql = "INSERT INTO SOURCE (SOURCE_TYPE, LONG, LAT, " +
                    "CONDITION, DATE, REPORTER)" +
                    " VALUES " +
                    "(" + sourceTypeInt +
                    "," + longitude +
                    "," + latitude +
                    "," + conditionTypeInt +
                    ", '" + date + "'" +
                    ", '" + reporterName + "'" +
                    ")";
            LOGGER.log(Level.INFO, "Source report added to database");
            smt.executeUpdate(sql);
            smt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    /**
     * Method for submitting new PurityReport.
     * @param waterSourceID id of water source
     * @param purityCondition    purity condition (safe, unsafe, treatable)
     * @param virusPPM           virus part per million
     * @param contaminantPPM     contaminant part per million
     * @param reporterName       username of reporter
     */
    public static void addPurityReport(
            int waterSourceID,
            PurityCondition purityCondition,
            int virusPPM,
            int contaminantPPM,
            String reporterName) {
        Connection c = connectToDatabase();
        int purityConditionInt = getIntFromPurityCondition(purityCondition);
        String date = dateFormat.format(new Date());
        try {
            Statement smt = c.createStatement();
            String sql = "INSERT INTO PURITY (SOURCE_ID, CONDITION, VIRUS_PPM, " +
                    "CONT_PPM, DATE, REPORTER) " +
                    "VALUES" +
                    "( " + waterSourceID +
                    ", " + purityConditionInt +
                    ", " + virusPPM +
                    ", " + contaminantPPM +
                    ", '" + date + "'" +
                    ", '" + reporterName + "'" +
                    ")";
            smt.executeUpdate(sql);
            smt.close();
            c.close();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    public static List<SourceReport> getSourceReports() {
        Connection c = connectToDatabase();
        List<SourceReport> list = new ArrayList<>();
        try {
            Statement smt = c.createStatement();
            String sql = "SELECT * FROM SOURCE";
            ResultSet rs = smt.executeQuery(sql);
            while (rs.next()) {
                int sourceID = rs.getInt("SOURCE_ID");
                SourceType sourceType = getSourceTypeFromInt(
                        rs.getInt("SOURCE_TYPE"));
                double longitude = rs.getDouble("LONG");
                double latitude = rs.getDouble("LAT");
                Location location = new Location(latitude, longitude);
                SourceCondition conditionType = getConditionFromInt(
                        rs.getInt("CONDITION"));
                Date date = dateFormat.parse(rs.getString("DATE"));
                String reporter = rs.getString("REPORTER");
                SourceReport sourceReport = new SourceReport(sourceID, date,
                        location, sourceType, conditionType, reporter);
                list.add(sourceReport);
            }
            return list;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    /**
     * Gets all purity reports for provided water source ID.
     * @param waterSourceID     ID of water source Report
     * @return                  List of purity reports for the water source
     */
    public static List<PurityReport> getPurityReports(int waterSourceID) {
        Connection c = connectToDatabase();
        List<PurityReport> list = new ArrayList<>();
        try {
            Statement smt = c.createStatement();
            String sql = "SELECT * FROM PURITY WHERE SOURCE_ID = " +
                    waterSourceID;
            ResultSet rs = smt.executeQuery(sql);
            while (rs.next()) {
                int ID = rs.getInt("ID");
                PurityCondition condition = getPurityConditionFromInt(rs.getInt("CONDITION"));
                int virusppm = rs.getInt("VIRUS_PPM");
                int contppm = rs.getInt("CONT_PPM");
                Date date = dateFormat.parse(rs.getString("DATE"));
                String reporter = rs.getString("REPORTER");
                PurityReport report = new PurityReport(ID, waterSourceID, date, condition,
                        reporter, virusppm, contppm);
                list.add(report);
            }
            return list;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }





    private static int getIntFromSourceType(SourceType sourceType) {
        switch (sourceType) {
            case BOTTLED:   return 0;
            case WELL:      return 1;
            case STREAM:    return 2;
            case LAKE:      return 3;
            case SPRING:    return 4;
            case OTHER:     return 5;
            default: return 5;

        }
    }

    private static SourceType getSourceTypeFromInt(int i) {
        switch (i) {
            case 0: return SourceType.BOTTLED;
            case 1: return SourceType.WELL;
            case 2: return SourceType.STREAM;
            case 3: return SourceType.LAKE;
            case 4: return SourceType.SPRING;
            default: return SourceType.OTHER;
        }
    }

    private static int getIntFromCondition(SourceCondition condition) {
        switch (condition) {
            case WASTE:   return 0;
            case TREATABLE_CLEAR:      return 1;
            case TREATABLE_MUDDY:    return 2;
            case POTABLE:      return 3;
            default: return 3;

        }
    }

    private static SourceCondition getConditionFromInt(int i) {
        switch (i) {
            case 0: return SourceCondition.WASTE;
            case 1: return SourceCondition.TREATABLE_CLEAR;
            case 2: return SourceCondition.TREATABLE_MUDDY;
            default: return SourceCondition.POTABLE;

        }
    }

    private static PurityCondition getPurityConditionFromInt(int i) {
        switch (i) {
            case 0: return PurityCondition.SAFE;
            case 1: return PurityCondition.TREATABLE;
            default: return PurityCondition.UNSAFE;

        }
    }

    private static int getIntFromPurityCondition(PurityCondition condition) {
        switch (condition) {
            case SAFE:   return 0;
            case TREATABLE: return 1;
            case UNSAFE: return 2;
        }
        return 2;
    }


}
