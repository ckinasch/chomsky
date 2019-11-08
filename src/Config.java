import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Config {

    private static Config config;
    //switch to alias post merge

    private String userPath;
    private Peer[] addressBook;

    static final String CONF_PATH = "./config.cfg";

    /**
     * FLOW
     * getConfig
     * notFound
     * specify location???
     * create
     * <p>
     * found
     * load
     * validate settings
     * success
     * create arrays
     * failure
     * create
     * <p>
     * METHODS
     * getIdentities -> identities
     * <p>
     * getAddressBook -> addressBook
     * <p>
     * setIdentities : identities.add(vals) -> write config
     * <p>
     * setAddressBook : addressBook.add(vals) -> write config
     *
     * TODO: random gen keys, de/serialization
     */

    public static void main(String[] args) throws IOException {
        Properties writeConf = new Properties();
        ArrayList<Identity> identities = new ArrayList<Identity>();

        identities.add(new Identity("Jyhe"));
        identities.add(new Identity("will"));
        identities.add(new Identity("Taz"));

        writeConf.setProperty("identities", identities.toString());
        writeConf.store(new FileOutputStream(CONF_PATH), "");
        getStuff();
//        getConfig();

    }

    public static void getStuff() throws IOException {
        ArrayList<Identity> idIn = new ArrayList<Identity>();
        Properties readConf = new Properties();
        readConf.load(new FileInputStream(CONF_PATH));
        idIn.add(new Identity(readConf.getProperty("identities")));
        System.out.println(idIn);
    }

    public static Config getConfig() throws IOException    //returns currently loaded config file or loads/creates file on first pass
    {


        if (config == null) {
            //IF CAN LOAD CONFIG
            //  CONFIG = LOADCONFIG()
            //ELSE
            config = new Config();
        }

        return config;
    }
}
