import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionPublicKey;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AliasHandler {

    /**
     * Home for 'arml' switch operations, and 'id / peer' arraylist's population at run time
     */

    // -a / -A : Add Alias
    static void addAlias(String args, ArrayList<Alias> list, String type) {
        String[] temp = args.split(" ");
        list.add(new Alias(temp[0], temp[1]));
        writeAliasesToFile(args, list, type);
        System.out.println(String.format("Add: %s", temp[0]));
    }

    // -r / -R : Remove Alias
    static void removeAlias(String args, ArrayList<Alias> list, String type) {
        list.removeIf(s -> s.getAlias().equals(args));
        writeAliasesToFile(args, list, type);

    }

    // -m / -M : Modify Alias
    static void modifyAlias(String args, ArrayList<Alias> list) {
        String[] temp = args.split(" ");
        int theIndex = 0;

        //TODO

        list.add(theIndex, new Alias(temp[0], temp[1]));
        System.out.println(String.format("Modify: %s", args));
    }

    // -l / -L : List Alias
    static void listAliases(String args, ArrayList<Alias> list) {
        System.out.println(String.format("" +
                "%s:" +
                "%s\n", args, list));
    }

    static void writeAliasesToFile(String args, ArrayList<Alias> list, String type) {

        String filePath = String.format("%s/.chomsky/%s/%s.alias", System.getProperty("user.home"), args, args);
        for (int index = 0; index < list.size(); index++) {

            String keyPath;
            if (args.equals("ids"))
            {
                keyPath = String.format("%s/.chomsky/%s/%s_private.key", System.getProperty("user.home"), args, list.get(index).getAlias());
            }
            else
            {
                keyPath = String.format("%s/.chomsky/%s/%s_pubkey.key", System.getProperty("user.home"), args, list.get(index).getAlias());
            }

            try {
                if (args.equals("ids")) {
                    InputStream pub_inpt = new FileInputStream(list.get(index).getPublicKeyUrl());
                    EncryptionKeyPair ctx = new EncryptionKeyPair(pub_inpt);
                    ctx.writeTo(new FileOutputStream(keyPath));
                }
                else
                {
                    InputStream pub_inpt = new FileInputStream(list.get(index).getPublicKeyUrl());
                    EncryptionPublicKey ctx = new EncryptionPublicKey(pub_inpt);
                    ctx.writeTo(new FileOutputStream(keyPath));
                }
            } catch (FileNotFoundException e) {
                System.out.println(String.format("Key file not found for %s -> %s", list.get(index).getAlias(), e));
                continue;
            } catch (IOException e) {
                System.out.println(String.format("Unable to write to %s -> %s", list.get(index).getPublicKeyUrl(), e));
                continue;
            }
            try {
                File f = new File(filePath);
                if (index > 0) {
                    FileWriter s = new FileWriter(f, true);
                    s.flush();
                    s.write(String.format("\n%s,%s", list.get(index).getAlias(), keyPath));
                    s.close();
                } else {
                    FileOutputStream s = new FileOutputStream(f);
                    s.flush();
                    s.write(String.format("%s,%s", list.get(index).getAlias(), keyPath).getBytes());
                    s.close();
                }
            } catch (Exception e) {
                System.out.println(String.format("Unable to write to -> %s: %s", keyPath, e.getMessage()));
            }
            System.out.println(String.format("New alias and keys created for %s -> %s", list.get(index), keyPath));
        }
    }

    static ArrayListExtended<Alias> readAlias(String fileContents) {
        if (fileContents == null || fileContents.isEmpty()) {
            return new ArrayListExtended<>();
        }

        ArrayListExtended<String> l = new ArrayListExtended<>(Arrays.asList(fileContents.split("\\s")));

        ArrayListExtended<ArrayListExtended<String>> parsedL = splitArray(l);
        return new ArrayListExtended<>() {
            {
                add(new Alias(parsedL.head().get(0), parsedL.head().get(1)));
                append(readAlias(parsedL.tail()));
            }
        };
    }

    private static ArrayListExtended<Alias> readAlias(ArrayListExtended<ArrayListExtended<String>> parsedL) {
        if (parsedL == null || parsedL.head() == null) {
            return new ArrayListExtended<>();
        }
        return new ArrayListExtended<>() {
            {
                add(new Alias(parsedL.head().get(0), parsedL.head().get(1)));
                append(readAlias(parsedL.tail()));
            }
        };
    }

    private static ArrayListExtended<ArrayListExtended<String>> splitArray(ArrayListExtended<String> array) {
        if (array.head() == null || array == null) {
            return new ArrayListExtended<>();
        }
        return new ArrayListExtended<>() {
            {
                add(new ArrayListExtended<>(Arrays.asList(array.head().split(","))));
                append(splitArray(array.tail()));
            }
        };
    }

    static String fileToString(String filePath) {
        String s = "";
        try {
            String buff;
            BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            while ((buff = file.readLine()) != null) {
                s += buff;
                s += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return s;
    }
}
