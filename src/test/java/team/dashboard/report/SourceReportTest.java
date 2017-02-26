package team.dashboard.report;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import team.model.Location;
import team.model.SourceReport;
import team.model.enums.SourceCondition;
import team.model.enums.SourceType;
import team.register.RegisterBS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SourceReportTest {

    @Before
    public void setUp() {
        try {
            Files.deleteIfExists(Paths.get("./database.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterBS.createUserTable();
        ReportBS.createTables();
        ReportBS.addSourceReport(new Location(12, 21), SourceType.LAKE, SourceCondition.POTABLE, "Sara");
    }

    @Test
    public void testGetSourceReports() {
        /* test fail to make source report*/
        if (ReportBS.getSourceReports() == null || ReportBS.getSourceReports().size() == 0) {
            Assert.fail();
        }
        /* test success*/
            assertEquals(ReportBS.getSourceReports().size(), 1);
            SourceReport tempReport = ReportBS.getSourceReports().get(0);
            assertEquals(12.0, tempReport.getLocation().getLatitude(), 0.001);
            assertEquals(21.0, tempReport.getLocation().getLongitude(), 0.001);
            assertEquals(tempReport.getSourceType(), SourceType.LAKE);
            assertEquals(tempReport.getSourceCondition(), SourceCondition.POTABLE);
            assertEquals(tempReport.getReporterName(), "Sara");
    }


}

