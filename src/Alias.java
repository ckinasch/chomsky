import java.security.PublicKey;

public class Peer
{
    private String alias;
    private PublicKey publicKey;

    public Peer(String alias, PublicKey publicKey)
    {
        this.alias = alias;
        this.publicKey = publicKey;
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
}
