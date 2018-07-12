import com.spun.util.io.FileUtils;
import org.approvaltests.Approvals;
import org.wirebridge.api.WirebridgeStartup;
import org.wirebridge.db.QueryResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TestSupport {

    protected void verifyQuery(String configFile, String httpMethod, String path, HashMap<String, String> params) throws IOException {
        WirebridgeStartup myApi = new WirebridgeStartup();
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), "config.json"));
        myApi.loadJson(FileUtils.readFromClassPath(getClass(), configFile));
        QueryResult queryResult = myApi.call(httpMethod, path, params);

        Approvals.verify(queryResult.toString());
    }

    protected static void executeSqlQuery(Map<String, String> connectionDetails, String sql) throws SQLException {
        Connection connection = DriverManager.getConnection(connectionDetails.get("connection"), connectionDetails.get("username"), connectionDetails.get("password"));
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }
}
