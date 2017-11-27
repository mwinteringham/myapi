import com.spun.util.io.FileUtils;
import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.Test;
import org.myapi.MyAPIStartup;
import org.myapi.QueryResult;

import java.io.IOException;
import java.util.HashMap;


public class SqlQueryTest{

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

    private void verifyQuery(String configFile, String httpMethod, String path, HashMap<String, String> params) throws IOException {
        MyAPIStartup myApi = new MyAPIStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "config.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), configFile));
        QueryResult queryResult = myApi.call(httpMethod, path, params);

        Approvals.verify(queryResult.toString());
    }

}
