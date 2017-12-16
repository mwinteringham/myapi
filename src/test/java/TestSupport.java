import com.spun.util.io.FileUtils;
import org.approvaltests.Approvals;
import org.myapi.MyAPIStartup;
import org.myapi.QueryResult;

import java.io.IOException;
import java.util.HashMap;

public class TestSupport {

    protected void verifyQuery(String configFile, String httpMethod, String path, HashMap<String, String> params) throws IOException {
        MyAPIStartup myApi = new MyAPIStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "config.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), configFile));
        QueryResult queryResult = myApi.call(httpMethod, path, params);

        Approvals.verify(queryResult.toString());
    }
}
