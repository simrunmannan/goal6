package team.dashboard.report;

import org.junit.Before;
import org.junit.Test;
import team.MainFxApplication;
import team.model.PurityReport;
import team.model.enums.PurityCondition;
import team.register.RegisterBS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReportBSTest {

    @Before
    public void initDatabase() {
        try {
            Files.deleteIfExists(Paths.get("./database.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterBS.createUserTable();
        ReportBS.createTables();

        for (int i = 0; i < 10; i++) {
            ReportBS.addPurityReport(
                    123,
                    PurityCondition.SAFE,
                    2,
                    3,
                    "default"
            );
        }
        ReportBS.addPurityReport(1234, PurityCondition.TREATABLE, 111, 222, "default");
        ReportBS.addPurityReport(1234, PurityCondition.UNSAFE, 111, 222, "default");
        ReportBS.addPurityReport(12345, PurityCondition.SAFE, 111, 222, "default");
    }

    @Test
    public void getPurityReports() throws Exception {

        /*Tests sizes of returned lists*/
        assertEquals(10, ReportBS.getPurityReports(123).size());
        assertEquals(2, ReportBS.getPurityReports(1234).size());
        assertEquals(1, ReportBS.getPurityReports(12345).size());

        /*No Purity Reports for given source ID*/
        assertEquals(0, ReportBS.getPurityReports(12356).size());
        assertEquals(0, ReportBS.getPurityReports(-1).size());
    }
}