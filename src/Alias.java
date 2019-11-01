import java.security.Key;

public class Alias
{
    private String username;
    private Key key;

    public Alias(String username)
    {
        this.username = username;
    }

    String getUsename()
    {
        return username;
    }
}
