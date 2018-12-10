package interfaces;

import java.util.Map;

public interface Parsabale {
    Map<String, String> extractParams(String content);
}
