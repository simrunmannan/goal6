package team.model;

import team.model.enums.SourceCondition;
import team.model.enums.SourceType;

import java.util.Date;

/**
 * Data object of a water source report. Knows all info needed for a report.
 */
public class SourceReport {
    private final int reportId;
    private final Date date;
    private final Location location;
    private final SourceType sourceType;
    private final SourceCondition sourceCondition;
    private final String reporterName;

    public SourceReport(
            int reportId,
            Date date,
            Location location,
            SourceType sourceType,
            SourceCondition sourceCondition,
            String reporterName) {
        this.reportId = reportId;
        this.date = date;
        this.location = location;
        this.sourceType = sourceType;
        this.sourceCondition = sourceCondition;
        this.reporterName = reporterName;
    }

    @Override
    public String toString() {
        return sourceType.toString() + ": " + sourceCondition.toString();
    }

    public int getReportId() {
        return reportId;
    }

    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public SourceCondition getSourceCondition() {
        return sourceCondition;
    }

    public String getReporterName() {
        return reporterName;
    }
}
