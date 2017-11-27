package org.myapi;

import java.sql.ResultSet;

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
                "resultSet=" + resultSet +
                ", updateCount=" + updateCount +
                '}';
    }
}
