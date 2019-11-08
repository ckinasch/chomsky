import java.security.PrivateKey;

public class Identity {
    private String username;
    private String privateKey;
//    private PrivateKey privateKey;

    public Identity(String username) {
        this.username = username;
//        this.privateKey = genPrivateKey();
//        privateKey = GeneratePrivateKey();
    }


    public String getUsername() {
        return username;
    }
    public String getPrivateKey(){return "93W0OIAJDS300943R";}

//    public PrivateKey getPrivateKey() {
//        return privateKey;
//    }

    public String toString(){
        return username + getPrivateKey();
    }

    public void setUsername(String username) {
        this.username = username;
    }



    //ReadPKCS()
}
