import java.security.PublicKey;

public class Alias
{
    private String alias;
    private String publicKeyUrl;

    public Alias(String alias, String pub_key_url)
    {
        this.alias = alias;
        this.publicKeyUrl = pub_key_url;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public String getPublicKeyUrl()
    {
        return this.publicKeyUrl;
    }

    public void setPublicKeyUrl(String pub_key)
    {
        this.publicKeyUrl = pub_key;
    }
}
