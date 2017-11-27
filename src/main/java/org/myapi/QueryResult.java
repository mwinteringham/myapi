package org.myapi;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult {

    public final ResultSet resultSet;
    public final int updateCount;

    public QueryResult(ResultSet resultSet, int updateCount) {

        this.resultSet = resultSet;
        this.updateCount = updateCount;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                " updateCount=" + updateCount +
                '}' + printResultSet();
    }

    private String printResultSet() {
        if(resultSet == null){
            return "";
        }

        try {
            return "\n" + com.spun.util.database.ResultSetWriter.toString(resultSet);
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
}
