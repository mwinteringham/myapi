package org.myapi;

public class Query {

    private final String queryText;
    private final String[] parameterNames;

    public Query(String queryText, String[] parameterNames) {

        this.queryText = queryText;
        this.parameterNames = parameterNames;
    }

    public String getQueryText() {
        return queryText;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }
}
