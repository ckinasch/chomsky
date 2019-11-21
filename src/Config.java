import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private static final Path CONF_PATH = Paths.get(String.format("%s/.chomsky/", System.getProperty("user.home")));

    public Config() throws IOException {
        if (!Files.exists(CONF_PATH)) {
            createConfigStructure();
        }
    }

    private void createConfigStructure() {

        Path peers = Paths.get(String.format("%s/peers", CONF_PATH));
        Path ids = Paths.get(String.format("%s/ids", CONF_PATH));

        try {
            System.out.println(String.format("Creating file structure in -> %s", CONF_PATH));
            Files.createDirectories(peers);
            Files.createDirectories(ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
