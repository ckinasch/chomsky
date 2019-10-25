import java.security.PrivateKey;

public class Identity
{
    private String username;
    private PrivateKey privateKey;

    public Identity(String username)
    {
        this.username = username;
        //privateKey = GeneratePrivateKey();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public PrivateKey getPrivateKey()
    {
        return privateKey;
    }

    //ReadPKCS()
}
