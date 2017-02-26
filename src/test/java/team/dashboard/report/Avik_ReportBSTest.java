package team.dashboard.report;

import com.lynden.gmapsfx.javascript.object.LatLong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

import team.model.enums.PurityCondition;
import team.model.PurityReport;
import team.register.RegisterBS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Avik_ReportBSTest {

    @Before
    public void setUp() {
        try {
            Files.deleteIfExists(Paths.get("./database.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegisterBS.createUserTable();
        ReportBS.createTables();
    }

    @Test
    public void testAddPurityReport() throws Exception {
        /*Empty table*/
        Assert.assertEquals(ReportBS.getPurityReports(123).size(), 0);

        /*Adding an entry*/
        ReportBS.addPurityReport(
                123,
                PurityCondition.TREATABLE,
                2,
                3,
                "Avik"
        );

        /*Table should have one entry*/
        Assert.assertEquals(ReportBS.getPurityReports(123).size(), 1);

        /*Check that entry is as expected*/
        Assert.assertEquals(ReportBS.getPurityReports(123).get(0).getReporterName(), "Avik");
        Assert.assertEquals(ReportBS.getPurityReports(123).get(0).getVirusPPM(), 2);

        /*Adding another entry*/
        ReportBS.addPurityReport(
                123,
                PurityCondition.SAFE,
                1,
                2,
                "default"
        );
        ReportBS.addPurityReport(
                12345,
                PurityCondition.SAFE,
                0,
                0,
                "default"
        );

        /*Table should have two entries for ID 123*/
        Assert.assertEquals(ReportBS.getPurityReports(123).size(), 2);

        /*Check that entries are as expected*/
        Assert.assertEquals(ReportBS.getPurityReports(123).get(0).getReporterName(), "Avik");
        Assert.assertEquals(ReportBS.getPurityReports(12345).get(0).getReporterName(), "default");
    }

}