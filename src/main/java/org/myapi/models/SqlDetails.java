package org.myapi.models;

import org.myapi.Query;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlDetails {
    private String database;
    private String query;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }


    public Query createSqlFor(HashMap<String, String> params) {
        String regex = "(\\$\\{.*?\\})";

        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(getQuery());
        String[] ts = params.keySet().toArray(new String[]{});
        return new Query(m.replaceAll("?"), ts);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
