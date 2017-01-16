package com.jayfeng.lesscode.debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DebugUtils {

    private static final int JSON_INDENT = 4;

    /**
     * 格式化Body内容为Json格式
     */
    public static String formatBodyToJson(String body) {
        String result;
        try {
            if (body.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(body);
                result = jsonObject.toString(JSON_INDENT);
            } else if (body.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(body);
                result = jsonArray.toString(JSON_INDENT);
            } else {
                result = body;
            }
        } catch (JSONException e) {
            result = body;
        }

        return result;
    }
}
