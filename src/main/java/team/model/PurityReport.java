package team.model;

import team.model.enums.PurityCondition;

import java.util.Date;

public class PurityReport {

    private final int reportId;
    private final int waterSourceId;
    private final Date date;
    private final PurityCondition purityCondition;
    private final String reporterName;
    private final int virusPPM;
    private final int contaminantPPM;

    public PurityReport(
            int reportId,
            int waterSourceId,
            Date date,
            PurityCondition purityCondition,
            String reporterName,
            int virusPPM,
            int contaminantPPM) {
        this.reportId = reportId;
        this.waterSourceId = waterSourceId;
        this.date = date;
        this.purityCondition = purityCondition;
        this.reporterName = reporterName;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }

    @Override
    public String toString() {
        return purityCondition.toString() + ":\n\t" + "VirusPPM: " +
                virusPPM + "\n\t" + "ContaminantPPM: " + contaminantPPM;
    }

    public int getWaterSourceId() {
        return waterSourceId;
    }

    public int getReportId() {
        return reportId;
    }

    public Date getDate() {
        return date;
    }

    public PurityCondition getPurityCondition() {
        return purityCondition;
    }

    public String getReporterName() {
        return reporterName;
    }

    public int getVirusPPM() {
        return virusPPM;
    }

    public int getContaminantPPM() {
        return contaminantPPM;
    }
}
