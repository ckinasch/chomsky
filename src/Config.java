import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Config {

    private static Config config;

    // Address Book
    static ArrayList<Alias> peers = new ArrayList<Alias>();
    // Personal Identifiers
    static ArrayList<Alias> ids = new ArrayList<Alias>();
    // Path to config file
    static final String CONF_PATH = "./config.cfg";

    /**
     * TODO: random gen keys, de/serialization, git branch PODIUM
     *
     * NOTES:
     *      -A arg - accept file path / string input
     */

    public static void main(String[] args) throws IOException {
        Properties writeConf = new Properties();

        // (add-peer) chomsky -A
        peers.add(new Alias("Jyhe"));
        peers.add(new Alias("will"));
        peers.add(new Alias("Taz"));

        // (remove-peer) chomsky -R

        // (modify-peer) chomsky -M



        writeConf.setProperty("peers", peers.toString());
        writeConf.store(new FileOutputStream(CONF_PATH), "");
        getStuff();
//        getConfig();

    }

    void addPeer(String id) {

    }

    void modifyPeer(String args, String moreArgs){

    }

    String readPeers() {
        String out = peers.toString();

        return out;
    }

    void alterConf(String args){

    }

    String readConf(){

        return "";
    }

    public static void getStuff() throws IOException {
        ArrayList<Alias> idIn = new ArrayList<Alias>();
        Properties readConf = new Properties();
        readConf.load(new FileInputStream(CONF_PATH));
        idIn.add(new Alias(readConf.getProperty("peers")));
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
