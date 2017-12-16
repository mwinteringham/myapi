package org.myapi;

import com.spun.util.ObjectUtils;
import org.myapi.actions.MysqlQuery;
import org.myapi.models.ConnectionDetails;
import org.myapi.models.DatabaseConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Command {

    private ConnectionDetails config;
    private HashMap<String, DatabaseConfig> databases;

    public Command(ConnectionDetails connectionDetails, HashMap<String, DatabaseConfig> databases) {
        config = connectionDetails;
        this.databases = databases;
    }

    public QueryResult call(Map<String, String> params) {
        DatabaseConfig database = getDatabaseFor(config.getSql().getDatabase());

        switch(config.getSql().getDatabase()){
            case "mysql":
                MysqlQuery query = new MysqlQuery(database, config, params);
                return query.call();
        }

        return null;
    }

    private DatabaseConfig getDatabaseFor(String database) {
        return databases.get(database);
    }
}
