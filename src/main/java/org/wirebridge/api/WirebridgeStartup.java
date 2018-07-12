package org.wirebridge.api;

import com.google.gson.Gson;
import com.spun.util.io.FileUtils;
import org.wirebridge.db.Command;
import org.wirebridge.db.QueryResult;
import org.wirebridge.models.ConnectionDetails;
import org.wirebridge.models.DatabaseConfig;
import org.wirebridge.models.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WirebridgeStartup {

    private HashMap<String, Command> call = new HashMap<>();
    private HashMap<String, DatabaseConfig> databases = new HashMap<>();

    public WirebridgeStartup(){}

    public void getMappings() throws IOException {
        String p = getMappingsFolderPath();

        File f = new File(p);
        File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".json"));

        if(matchingFiles != null){
            if(matchingFiles.length > 0 ){
                for(File file : matchingFiles){
                    System.out.println("Loading: " + file.getPath());
                    loadJson(FileUtils.readFile(file));
                }
            } else {
                System.out.println("ERROR: Wirebridge unable to find any .json mappings within the mappings folder!");
                throw new RuntimeException();
            }
        } else {
            System.out.println("ERROR: Wirebridge was either unable to find the mappings folder!");
            throw new RuntimeException();
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

    private String getMappingsFolderPath() {
        String path = WirebridgeController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(0, path.length() - 1);

        Pattern pattern = Pattern.compile(":(.*)\\/");
        Matcher matcher = pattern.matcher(path);

        matcher.find();
        return matcher.group(1) + "/mappings/";
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
            for (DatabaseConfig configDatabase : configDatabases) {
                databases.put(configDatabase.getName(), configDatabase);
            }
        }
    }

    private String getPathIdentifier(String httpMethod, String path) {
        return httpMethod + ":" + path;
    }

}
