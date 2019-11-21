import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.*;

public class AliasHandler {

    static Path ids = Paths.get(String.format("%s/.chomsky/ids/aliases.txt", System.getProperty("user.home")));
    static Path peers = Paths.get(String.format("%s/.chomsky/peers/aliases.txt", System.getProperty("user.home")));


    // -a / -A : Add Alias
    static void addAlias(String args, ArrayList<Alias> list) throws IOException {
        String temp[] = args.split(" ");
        list.add(new Alias(temp[0], temp[1]));
        System.out.println(String.format("Add: %s", temp[0]));
    }

    // -r / -R : Remove Alias
    static void removeAlias(String args, ArrayList<Alias> list) throws IOException {

        System.out.println(String.format("Remove: %s", args));
    }

    // -m / -M : Modify Alias
    static void modifyAlias(String args, ArrayList<Alias> list) throws IOException {

        System.out.println(String.format("Modify: %s", args));
    }

    // -l / -L : List Alias
    static void listAliases(ArrayList<Alias> idsList, ArrayList<Alias> peersList) throws IOException {
        System.out.println(String.format("" +
                "Ids:" +
                "%s\n" +
                "Peers:" +
                "%s", idsList, peersList));
    }

    public static void writeListToFile(String args, ArrayList<Alias> list) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(String.format("%s/.chomsky/%s.mfs", System.getProperty("user.home"), args)));
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Alias> readListFromFile(String args) {
        ArrayList<Alias> list;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(String.format("%s/.chomsky/%s.mfs", System.getProperty("user.home"), args)));
           list = new ArrayList<Alias>(in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }


}
