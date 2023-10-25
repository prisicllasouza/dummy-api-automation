package util;

import org.json.JSONArray;
import org.json.JSONException;

public class PostUtil {

    public static String[] jsonArrayToStringArray(JSONArray jsonArray) throws JSONException {
        String[] stringArray = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray[i] = jsonArray.getString(i);
        }

        return stringArray;
    }

}


