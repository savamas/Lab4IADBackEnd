package interfaces;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface Encodable {
    public String hash(char[] password);
    public boolean authenticate(char[] password, String token);
}