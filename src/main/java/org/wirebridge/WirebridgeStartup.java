package org.wirebridge;

import com.google.gson.Gson;
import com.spun.util.io.FileUtils;
import org.wirebridge.models.ConnectionDetails;
import org.wirebridge.models.DatabaseConfig;
import org.wirebridge.models.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WirebridgeStartup {

    private HashMap<String, Command> call = new HashMap<String, Command>();
    private HashMap<String, DatabaseConfig> databases = new HashMap<String, DatabaseConfig>();

    public WirebridgeStartup() {
    }

    public WirebridgeStartup(String pathname) throws IOException {
        File f = new File(pathname);
        File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".json"));

        for(File file : matchingFiles){
            System.out.println("FOUND: " + file.getPath());
            loadJson(FileUtils.readFile(file));
        }
    }

    public QueryResult call(String httpMethod, String path, Map<String, String> params) {
        String pathIdentifier = getPathIdentifier(httpMethod, path);
        Command command = call.get(pathIdentifier);
        if(command == null){
            throw new RuntimeException(String.format("No command found for: %s", pathIdentifier));
        }
        return command.call(params);
    }

    public void loadJson(String json) {
        Gson g = new Gson();
        ConnectionDetails connectionDetails = g.fromJson(json, ConnectionDetails.class);

        Command command = new Command(connectionDetails, databases);
        addDatabases(connectionDetails);
        addRequest(connectionDetails, command);
    }

    private String getPathIdentifier(String httpMethod, String path) {
        return httpMethod + ":" + path;
    }

    private void addRequest(ConnectionDetails connectionDetails, Command command) {
        Request request = connectionDetails.getRequest();

        if(request != null){
            call.put(getPathIdentifier(request.getMethod(), request.getPath()),command);
        }
    }

    private void addDatabases(ConnectionDetails connectionDetails) {
        List<DatabaseConfig> configDatabases = connectionDetails.getDatabases();

        if(configDatabases != null){
            for(int i = 0; i < configDatabases.size(); i++){
                databases.put(configDatabases.get(i).getName(), configDatabases.get(i));
            }
        }
    }
}
