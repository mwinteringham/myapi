package org.wirebridge.models;

public class DatabaseConfig {

    private String password;
    private String username;
    private String host;
    private String driver;
    private String database;
    private String name;

    public String getUrl() {
        return String.format("%s://%s/%s", getDriver(), getHost(), getDatabase());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
