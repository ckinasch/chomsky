import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Config {

    private static Config config;

    //Test set of keys
    static String[] dummyKeys = {
            "0j:20:4b:88:a7:9t:wd:19:f0:d4:4y:9g:27:cf:97:23",
            "fd:bc:8a:81:58:8f:2c:78:86:a2:cf:02:40:7d:9d:3c",
            "8f:c9:dc:40:ec:9e:dc:65:74:f7:20:c1:29:d1:e8:5a",
            "19:f0:d4:4y:9g:27:cf:97:23:0j:20:4b:88:a7:9t:wd",
            "78:86:a2:cf:02:40:7d:9d:3c:fd:bc:8a:81:58:8f:2c",
            "f0:d4:4y:9g:27:cf:97:23:0j:20:4b:88:a7:9t:wd:19",
    };

    // Address Book
    static ArrayList<Alias> peers = new ArrayList<Alias>();
    // Personal Identifiers
    static ArrayList<Alias> ids = new ArrayList<Alias>();
    // Path to config file
    static final String CONF_PATH = "./config.cfg";

    /**
     * TODO: random gen keys, de/serialization, git branch PODIUM
     * <p>
     * NOTES:
     * -A arg - accept file path / string input
     * <p>
     * fetch config
     * SUCCESS :
     * read file
     * return formatted address book
     * <p>
     * FAILURE:
     * create new
     * options? TODO: define default behaviour
     * alter path
     * create new
     * <p>
     * // (add-peer) chomsky -A
     * <p>
     * // (remove-peer) chomsky -R
     * <p>
     * // (modify-peer) chomsky -M
     */

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

    String readPeers() {
        String out = peers.toString();

        return out;
    }

    String readIds() {
        String out = ids.toString();

        return out;
    }

    static String writeAlias(ArrayList<Alias> args) {
        String out = "";
        for (Alias i :
                args) {
            out += i.toString();
        }

        return out;
    }

    static void writeConf(String k, ArrayList<Alias> v) throws IOException {
        Properties writeConf = new Properties();



        writeConf.setProperty(k, writeAlias(v));
        writeConf.store(new FileOutputStream(CONF_PATH), "");

    }

    static String readConf(String args) throws IOException {
        Properties readConf = new Properties();
        readConf.load(new FileInputStream(CONF_PATH));

        peers.add(new Alias(readConf.getProperty("peers")));

        return "";
    }

    /**
     * Methods for testing implementation : remove after merge
     */
    void addPeer(String id) {
        peers.add(new Alias("Jyhe", dummyKeys[rng()]));
        peers.add(new Alias("So Big", dummyKeys[rng()]));
    }

    void modifyPeer(String args, String moreArgs) {

    }

    //Modifiable if keys are to be read from file
    static String keyFromFile() throws IOException {
        String out = "";

        try (BufferedReader br = new BufferedReader(new FileReader("keys/key" + rng() + ".txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            out = sb.toString();
        }

        System.out.println(out);

        return "";
    }

    static int rng() {
        return (int) (Math.random() * 6);
    }
}
