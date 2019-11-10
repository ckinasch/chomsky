import java.io.*;
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

    // Path to config file
    private static final String CONF_PATH = "./config.cfg";

    private static Properties readConf = new Properties();
    private static Properties writeConf = new Properties();

    /**
     * TODO: de/serialization, git branch PODIUM
     */

    public static Config getConfig()    //returns currently loaded config file or loads/creates file on first pass
    {
        if (config == null) {
            //IF CAN LOAD CONFIG
            try {
                //  CONFIG = LOADCONFIG()
                readConf.load(new FileInputStream(CONF_PATH));
                System.out.println("Reading Config");
            } catch (FileNotFoundException e) {
                System.out.println("Config file not found");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO exception");

            }
            //ELSE

        } else {
            // create new config file TODO
            config = new Config();

        }

        return config;
    }

    // -a / -A : Add Alias
    void addAlias(String args) {

        System.out.println(String.format("Add %s", args));
    }

    // -r / -R : Remove Alias
    void removeAlias(String args) {
        System.out.println(String.format("Add %s", args));

    }

    // -m / -M : Modify Alias
    void modifyAlias(String args) {
        System.out.println(String.format("Add %s", args));

    }

    // -l / -L : List Alias
    String listAliases(String args) {
        return String.format("Add %s", args);
    }

}
