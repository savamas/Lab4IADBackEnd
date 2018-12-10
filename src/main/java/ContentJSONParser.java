import interfaces.Parsabale;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class ContentJSONParser implements Parsabale {
    public Map<String, String> extractParams(String content) {
        Map<String, String> params = new HashMap<String, String>();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        params.put("username", (String) jsonObject.get("username"));
        params.put("password", (String) jsonObject.get("password"));

        return params;
    }
}
