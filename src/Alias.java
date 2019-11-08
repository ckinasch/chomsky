import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class Alias
{
    private String alias;
    private PublicKey publicKey;
    private KeyGenerator pkey;
    private PrivateKey privateKey;

    public Alias(String alias, PublicKey publicKey, PrivateKey privateKey)
    {
        this.alias = alias;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey()
    {
        return privateKey;
    }

    public void setPrivateKey(KeyGenerator pKey) {
        KeyGenerator privateKey = new KeyGenerator.getInstance("AES");
        privateKey = pkey;
        SecureRandom randomKey = new SecureRandom();
        privateKey.init(randomKey);
        Key key = privateKey.generateKey();
    }
}
