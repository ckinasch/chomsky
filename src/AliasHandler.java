import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;

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
    static void listAliases(ArrayList<Alias> idsList, ArrayList<Alias>peersList) throws IOException {
        System.out.println(String.format("" +
                "Ids:" +
                "%s\n" +
                "Peers:" +
                "%s", idsList, peersList));
    }

    public static ArrayListExtended<Alias> readAlias(String fileContents)
    {
        if (fileContents == null)
        {
            return new ArrayListExtended<>();
        }

        ArrayListExtended<String> l = new ArrayListExtended<>(Arrays.asList(fileContents.split("\\s")));

        ArrayListExtended<ArrayListExtended<String>> parsedL = splitArray(l);

        return new ArrayListExtended<>(){
            {
                add(new Alias(parsedL.head().get(0), parsedL.head().get(1)));
                append(readAlias(parsedL.tail()));
            }
        };
    }

    private static ArrayListExtended<Alias> readAlias(ArrayListExtended<ArrayListExtended<String>>  parsedL)
    {
        if (parsedL == null || parsedL.head() == null)
        {
            return new ArrayListExtended<>();
        }
        return new ArrayListExtended<>(){
            {
                add(new Alias(parsedL.head().get(0), parsedL.head().get(1)));
                append(readAlias(parsedL.tail()));
            }
        };
    }

    private static ArrayListExtended<ArrayListExtended<String>> splitArray(ArrayListExtended<String> array)
    {
        if (array.head() == null || array == null)
        {
            return new ArrayListExtended<>();
        }
        return new ArrayListExtended<>(){
            {
                add(new ArrayListExtended<>(Arrays.asList(array.head().split(","))));
                append(splitArray(array.tail()));
            }
        };
    }

    public static String fileToString(String filePath) {
        try
        {
            String s = "";
            String buff = "";
            BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            while ((buff=file.readLine()) != null)
            {
                s += buff;
            }
            return s;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }
}
