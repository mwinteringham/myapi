package org.myapi.actions;

import com.spun.util.ObjectUtils;
import org.myapi.Query;
import org.myapi.QueryResult;
import org.myapi.models.ConnectionDetails;
import org.myapi.models.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MysqlQuery {

    DatabaseConfig databaseConfig;
    ConnectionDetails connectionDetails;
    Map<String, String> params;

    public MysqlQuery(DatabaseConfig databaseConfig, ConnectionDetails connectionDetails, Map<String, String> params) {
        this.databaseConfig = databaseConfig;
        this.connectionDetails = connectionDetails;
        this.params = params;
    }

    public QueryResult call(){
        Query sql = null;

        try {
            Connection connection = DriverManager.getConnection(databaseConfig.getUrl(), databaseConfig.getUsername(), databaseConfig.getPassword());

            sql = connectionDetails.getSql().createSqlFor();

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
}
