package org.wirebridge.models;

import java.util.List;

public class ConnectionDetails {

    private Request request;
    private SqlDetails sql;
    private List<DatabaseConfig> databases;

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

    public List<DatabaseConfig> getDatabases() {
        return databases;
    }

    public void setDatabase(List<DatabaseConfig> database) {
        this.databases = database;
    }
}
