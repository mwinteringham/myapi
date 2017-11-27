import com.spun.util.io.FileUtils;
import org.approvaltests.Approvals;
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

        verifyQuery("addtable.json", "POST", "/talk", parameters);
    }

    private void verifyQuery(String configFile, String httpMethod, String path, HashMap<String, String> params) throws IOException {
        MyAPIStartup myApi = new MyAPIStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "config.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), configFile));
        QueryResult queryResult = myApi.call(httpMethod, path, params);

        Approvals.verify(queryResult.toString());
    }

}
