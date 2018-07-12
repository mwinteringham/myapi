package org.wirebridge.models;

import org.lambda.functions.Function1;

import java.util.ArrayList;
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

    public Query createSqlFor() {
        String regex = "(\\$\\{.*?\\})";

        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        ArrayList<String> ts = new ArrayList<>();
        String queryText = replaceAll(getQuery(), p, m -> {ts.add(substring("${", m.group().toString(), "}")); return "?";});

        return new Query(queryText, ts.toArray(new String[]{}));
    }

    private String substring(String prefix, String string, String postfix) {
        return string.substring(prefix.length(), string.length() - postfix.length());
    }

    public static String replaceAll(String input, Pattern regex, Function1<Matcher, String> callback) {
        StringBuffer resultString = new StringBuffer();
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            regexMatcher.appendReplacement(resultString, callback.call(regexMatcher));
        }
        regexMatcher.appendTail(resultString);

        return resultString.toString();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
