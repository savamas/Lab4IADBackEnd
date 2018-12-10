import interfaces.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class SimpleKeyGenerator implements KeyGenerator {
    @Override
    public Key generateKey() {
        String keyString = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HMACSHA256");
        return key;
    }
}
