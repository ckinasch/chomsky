import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    /**
     * https://stackoverflow.com/questions/24586010/using-environment-variables-in-a-java-file-path
     * https://stackoverflow.com/questions/8627951/java-file-path-in-linux
     * Get OS
     * Check for .chomsky in relevant file path
     * <p>
     * <p>
     * Linux.sh & Windows.bat
     * -> create folder structure in relevant path (/$HOME/.chomsky/) || (\$appData\.chomsky\)
     * <p>
     * TODO: Test on windows
     */

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

    private static String currentOS;

    private static final Path CONF_PATH_LINUX = Paths.get(String.format("%s/.chomsky/", System.getProperty("user.home")));
    private static final Path CONF_PATH_WINDOWS = Paths.get(String.format("%s/.chomsky/", System.getProperty("user.home")));


    //References to properties operations
    private static Properties readConf = new Properties();
    private static Properties writeConf = new Properties();

    public static String pathToConfig;

    public Config() throws IOException {    //returns currently loaded config file or loads/creates file on first pass
        currentOS = System.getProperty("os.name");
        System.out.println(currentOS);

        if (currentOS.equals("Linux")) {
            linuxConfig();

        } else if (currentOS.startsWith("Windows")) {
            windowsConfig();
        }
    }

    private void linuxConfig() {
        System.out.println("Linux Detected");
        findConfig(CONF_PATH_LINUX);
    }

    private void windowsConfig() {
        System.out.println("Windows Detected");
        findConfig(CONF_PATH_WINDOWS);
    }

    private void findConfig(Path path) {
        System.out.println("Finding Config");
        System.out.println(path);

        if (Files.exists(path)) {

        } else {
            createConfigStructure(path);
        }
    }

    private void createConfigStructure(Path path) {
        Path peers = Paths.get(String.format("%s/peers", path));
        Path ids = Paths.get(String.format("%s/ids", path));

        try {
            Files.createDirectories(peers);
            Files.createDirectories(ids);
            System.out.println("Creating Dirs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
