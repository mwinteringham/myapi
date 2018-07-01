import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class H2QueryTest extends TestSupport {

    private static Map<String, String> connectionDetails = new HashMap<String, String>(){{
        put("connection", "jdbc:h2:mem:talks");
        put("username", "h2");
        put("password", "password");
    }};

    @BeforeClass
    public static void populateDB() throws SQLException {
        String query = "CREATE table talks ( title varchar(255) );" +
                       "CREATE table sample ( id int, title varchar(255), money double, added datetime);" +
                       "INSERT INTO talks (title) VALUES ('COOL API DEMO'), ('My talk title');" +
                       "INSERT INTO sample VALUES (1, 'Title text 1', 10.01, '2017-01-19 03:14:07.999999');" +
                       "INSERT INTO sample VALUES (2, 'An interesting topic 2', 12.31, '2017-11-11 03:14:07.999999');";

        executeSqlQuery(connectionDetails, query);
    }

    @Test
    public void testInsertIntoDB() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("title", "Cool API Demo!");
        }};

        verifyQuery("h2_add_table.json", "POST", "/talk", parameters);
    }

    @Test
    public void testMultipleParameters() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("contains", "a");
            put("excludes", "demo");
        }};

        verifyQuery("h2_query_table.json", "GET", "/talk", parameters);
    }

}
