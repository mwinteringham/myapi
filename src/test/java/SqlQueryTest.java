import com.spun.util.io.FileUtils;
import org.approvaltests.Approvals;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.myapi.MyAPIStartup;
import org.myapi.QueryResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlQueryTest{

    @BeforeClass
    public static void populateDB() throws SQLException {
        String talksQuery  = "CREATE DATABASE talks;" +
                             " USE talks; " +
                             "CREATE TABLE talks ( title varchar(255) ); " +
                             "INSERT INTO talks (title) VALUES ('COOL API DEMO'), ('My talk title');";

        String sampleQuery = "USE talks;" +
                             "CREATE TABLE sample ( id int, title varchar(255), money double, added datetime);" +
                             "INSERT INTO sample VALUES (1, \"Title text 1\", 10.01, '2017-01-19 03:14:07.999999');" +
                             "INSERT INTO sample VALUES (2, \"An interesting topic 2\", 12.31, '2017-11-11 03:14:07.999999');";

        executeSqlQuery(talksQuery);
        executeSqlQuery(sampleQuery);
    }

    @Test
    public void testInsertIntoDB() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("title", "Cool API Demo!");
        }};

        verifyQuery("add_table.json", "POST", "/talk", parameters);
    }

    @Test
    public void testMultipleParameters() throws IOException {
        HashMap<String,String> parameters = new HashMap<String, String>(){{
            put("contains", "a");
            put("excludes", "demo");
        }};

        verifyQuery("query_table.json", "GET", "/talk", parameters);
    }

    @Test
    public void testErrors() throws IOException {
        try{
            MyAPIStartup myApi = new MyAPIStartup();
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
        executeSqlQuery("DROP DATABASE talks;");
    }

    private void verifyQuery(String configFile, String httpMethod, String path, HashMap<String, String> params) throws IOException {
        MyAPIStartup myApi = new MyAPIStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "config.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), configFile));
        QueryResult queryResult = myApi.call(httpMethod, path, params);

        Approvals.verify(queryResult.toString());
    }

    private static void executeSqlQuery(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?allowMultiQueries=true", "root", "");
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

}
