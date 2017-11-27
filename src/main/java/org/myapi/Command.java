package org.myapi;

import com.spun.util.ObjectUtils;
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

    public QueryResult call(HashMap<String, String> params) {
//        Create DB connection
        DatabaseConfig database = getDatabaseFor(config.getSql().getDatabase());

        Query sql = null;

        try {
            Connection connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());

//        Mix params in SQL
            sql = config.getSql().createSqlFor(params);
            PreparedStatement preparedStatement = connection.prepareStatement(sql.getQueryText());
            preparedStatement.setString(1, params.get(sql.getParameterNames()[0]));
            boolean returnResultSet = preparedStatement.execute();

            return new QueryResult(preparedStatement.getResultSet(), preparedStatement.getUpdateCount());
        } catch (SQLException e) {
//            Call SQL
            System.out.println("SQL Error: " + sql);
            ObjectUtils.throwAsError(e);
        }

        return null;
    }

    private DatabaseConfig getDatabaseFor(String database) {
        return databases.get(database);
    }
}
