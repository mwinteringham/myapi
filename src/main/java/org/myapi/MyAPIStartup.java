package org.myapi;

import com.google.gson.Gson;
import com.spun.util.io.FileUtils;
import org.myapi.models.ConnectionDetails;
import org.myapi.models.DatabaseConfig;
import org.myapi.models.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAPIStartup {
    private HashMap<String, Command> call = new HashMap<String, Command>();
    private HashMap<String, DatabaseConfig> databases = new HashMap<String, DatabaseConfig>();

    public QueryResult call(String httpMethod, String path, Map<String, String> params) {
        String pathIdentifier = getPathIdentifier(httpMethod, path);
        Command command = call.get(pathIdentifier);
        if(command == null){
            throw new RuntimeException(String.format("No command found for: %s", pathIdentifier));
        }
        return command.call(params);
    }

    private String getPathIdentifier(String httpMethod, String path) {
        return httpMethod + ":" + path;
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
            call.put(getPathIdentifier(request.getMethod(), request.getPath()),command);
        }
    }

    private void addDatabases(ConnectionDetails connectionDetails) {
        DatabaseConfig database = connectionDetails.getDatabase();

        if(database != null){
            databases.put(database.getName(), database);
        }
    }
}
