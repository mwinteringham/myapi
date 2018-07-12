import org.approvaltests.Approvals;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wirebridge.api.WirebridgeStartup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySqlQueryTest extends TestSupport {

    private static Map<String, String> connectionDetails = new HashMap<String, String>(){{
        put("connection", "jdbc:mysql://localhost:3306/talks?allowMultiQueries=true");
        put("username", "root");
        put("password", "password");
    }};

    @BeforeClass
    public static void populateDB() throws SQLException {
        String query = "INSERT INTO talks (title) VALUES ('COOL API DEMO'), ('My talk title');" +
                       "INSERT INTO sample VALUES (1, 'Title text 1', 10.01, '2017-01-19 03:14:07.999999');" +
                       "INSERT INTO sample VALUES (2, 'An interesting topic 2', 12.31, '2017-11-11 03:14:07.999999');";


        executeSqlQuery(connectionDetails, query);
    }

    @Test
    public void testInsertIntoDB() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("title", "Cool API Demo!");
        }};

        verifyQuery("mysql_add_table.json", "POST", "/talk", parameters);
    }

    @Test
    public void testMultipleParameters() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("contains", "a");
            put("excludes", "demo");
        }};

        verifyQuery("mysql_query_table.json", "GET", "/talk", parameters);
    }

    @Test
    public void testErrors() throws IOException {
        try{
            WirebridgeStartup myApi = new WirebridgeStartup();
            myApi.call("myMethod", "route", null);

            Assert.fail();
        } catch (RuntimeException e){
            Approvals.verify(e.getMessage());
        }
    }

    @Test
    public void testJSON() throws IOException {
        verifyQuery("sample.json", "GET", "/sample", null);
    }

    @AfterClass
    public static void stripDB() throws SQLException {
        executeSqlQuery(connectionDetails, "DELETE from talks; DELETE from sample;");
    }

}
