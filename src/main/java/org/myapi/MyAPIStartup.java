package org.myapi;

import com.google.gson.Gson;
import com.spun.util.io.FileUtils;
import org.myapi.models.ConnectionDetails;
import org.myapi.models.DatabaseConfig;
import org.myapi.models.Request;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

public class MyAPIStartup {
    private HashMap<String, Command> call = new HashMap<String, Command>();
    private HashMap<String, DatabaseConfig> databases = new HashMap<String, DatabaseConfig>();

    public QueryResult call(String httpMethod, String path, HashMap<String, String> params) {
        Command command = call.get(httpMethod + ":" + path);
        return command.call(params);
    }

    public void loadJsonFromFile(String configFile) throws IOException {
        loadJson(FileUtils.readFile(configFile));
    }

    public void loadJson(String json) {
        Gson g = new Gson();
        ConnectionDetails connectionDetails = g.fromJson(json, ConnectionDetails.class);

        Command command = new Command(connectionDetails, databases);
        addDatabases(connectionDetails);
        addRequest(connectionDetails, command);
    }

    private void addRequest(ConnectionDetails connectionDetails, Command command) {
        Request request = connectionDetails.getRequest();

        if(request != null){
            call.put(request.getMethod() + ":" + request.getPath(),command);
        }
    }

    private void addDatabases(ConnectionDetails connectionDetails) {
        DatabaseConfig database = connectionDetails.getDatabase();

        if(database != null){
            databases.put(database.getName(), database);
        }
    }
}
