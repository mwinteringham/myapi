package org.wirebridge;

import com.google.gson.GsonBuilder;
import com.spun.util.ObjectUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryResult {

    public final ResultSet resultSet;
    public final int updateCount;

    public QueryResult(ResultSet resultSet, int updateCount) {

        this.resultSet = resultSet;
        this.updateCount = updateCount;
    }

    @Override
    public String toString() {
        if(resultSet == null){
            HashMap<String, Object> count = new HashMap<String, Object>();
            count.put("updateCount", updateCount);

            return printJson(count);
        } else {
            return getFormattedResult(resultSet);
        }
    }

    public static String getFormattedResult(ResultSet rs){
        HashMap<String, Object> resultSet = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        resultSet.put("results", list);

        try{
            ResultSetMetaData meta = rs.getMetaData();
            int cc = meta.getColumnCount();

            while (rs.next()) {
                HashMap<String, Object> row = new HashMap<>();

                for (int i = 1; i <= cc; ++i) {
                    Object value = rs.getObject(i);

                    row.put(meta.getColumnName(i), value);
                }
                list.add(row);
            }
        } catch (Exception e) {
            throw ObjectUtils.throwAsError(e);
        }

        return printJson(resultSet);
    }

    private static String printJson(HashMap<String, Object> resultSet) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create().toJson(resultSet);
    }
}
