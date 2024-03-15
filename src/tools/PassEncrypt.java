
package tools;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PassEncrypt {
    public String getEncryptPassword(String password, String salt){
         KeySpec spec = new PBEKeySpec(password.toCharArray(),salt.getBytes(),65536,128);
         SecretKeyFactory factory;
        try {
             factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
             byte[] hash = factory.generateSecret(spec).getEncoded();
             return new BigInteger(hash).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PassEncrypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PassEncrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public String getSalt(){
        return "SecretWOWfosOGO";
    }
    
    
}