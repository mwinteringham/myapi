package org.myapi.models;

public class ConnectionDetails {

    private Request request;
    private SqlDetails sql;
    private DatabaseConfig database;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public SqlDetails getSql() {
        return sql;
    }

    public void setSql(SqlDetails sql) {
        this.sql = sql;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }
}
