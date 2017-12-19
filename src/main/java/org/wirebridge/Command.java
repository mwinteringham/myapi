package org.wirebridge;

import com.spun.util.ObjectUtils;
import org.wirebridge.models.ConnectionDetails;
import org.wirebridge.models.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        Query sql = null;

        try {
            Connection connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());

            sql = config.getSql().createSqlFor();

            System.out.println("Prepared SQL: " + sql.getQueryText());

            PreparedStatement preparedStatement = connection.prepareStatement(sql.getQueryText());

            String[] parameters = sql.getParameterNames();

            for(int i = 0; i < parameters.length; i++){
                System.out.println("Parameter name: " + parameters[i]);
                preparedStatement.setString(i + 1, params.get(parameters[i]));
            }

            preparedStatement.execute();

            return new QueryResult(preparedStatement.getResultSet(), preparedStatement.getUpdateCount());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + sql);
            ObjectUtils.throwAsError(e);
        }

        return null;
    }

    private DatabaseConfig getDatabaseFor(String database) {
        return databases.get(database);
    }
}
