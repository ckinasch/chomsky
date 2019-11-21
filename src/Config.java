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
     * TODO: Test on Windows
     * TODO: Create .cfg && I/O
     * TODO: outward facing properties access.
     */

    private static Config config;

    // Public attributes, access in main
    final Path CONF_PATH_LINUX = Paths.get(String.format("%s/.chomsky/", System.getProperty("user.home")));
    final Path CONF_PATH_WINDOWS = Paths.get(String.format("%s/.chomsky/", System.getProperty("user.home")));


    public Config() throws IOException {    //returns currently loaded config file or loads/creates file on first pass
        String currentOS;

        // Environment information
        //      OS
        //      user home

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
