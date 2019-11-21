import net.sf.ntru.encrypt.EncryptionPublicKey;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AliasHandler {

    static String ids = String.format("%s/.chomsky/ids/ids.alias", System.getProperty("user.home"));
    static String peers = String.format("%s/.chomsky/peers/peers.alias", System.getProperty("user.home"));


    // -a / -A : Add Alias
    static void addAlias(String args, ArrayList<Alias> list) {
        String temp[] = args.split(" ");
        list.add(new Alias(temp[0], temp[1]));
        System.out.println(String.format("Add: %s", temp[0]));
    }

    // -r / -R : Remove Alias
    static void removeAlias(String args, ArrayList<Alias> list) {
        list.removeIf(s -> s.getAlias().equals(args));
    }

    // -m / -M : Modify Alias
    static void modifyAlias(String args, ArrayList<Alias> list) {
        String temp[] = args.split(" ");
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

    static void writePeerAliases(ArrayListExtended<Alias> l) {
        for (int index = 0; index < l.size(); index++) {
            try {
                InputStream peer_pub_inpt = new FileInputStream(l.get(index).getPublicKeyUrl());
                EncryptionPublicKey ctx = new EncryptionPublicKey(peer_pub_inpt);

                ctx.writeTo(new FileOutputStream(String.format("%s/.chomsky/peers/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())));

            } catch (FileNotFoundException e) {
                System.out.println(String.format("Key file not found for %s", l.get(index).getAlias()));
                continue;
            } catch (IOException e) {
                System.out.println(String.format("Unable to write to %s", l.get(index).getPublicKeyUrl()));
                continue;
            }
            try {
                File f = new File(peers);

                if (index > 0) {
                    FileWriter s = new FileWriter(f, true);

                    s.flush();
                    s.write(String.format("\n%s,%s", l.get(index).getAlias(), String.format("%s/.chomsky/peers/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())));
                    s.close();
                } else {
                    FileOutputStream s = new FileOutputStream(f);

                    s.flush();
                    s.write(String.format("%s,%s", l.get(index).getAlias(), String.format("%s/.chomsky/peers/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())).getBytes());
                    s.close();
                }
            } catch (Exception e) {
                System.out.println(String.format("Unable to write to -> %s/.chomsky/ids/%s_pubkey.key: %s", System.getProperty("user.home"), l.get(index).getAlias(), e.getMessage()));
            }
            System.out.println(String.format("New alias and keys created for %s -> %s", l.get(index), String.format("%s/.chomsky/peers/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())));
        }
    }

    static void writeIdsAliases(ArrayListExtended<Alias> l) {
        for (int index = 0; index < l.size(); index++) {
            try {
                File f = new File(ids);

                if (index > 0) {
                    FileWriter s = new FileWriter(f, true);

                    s.flush();
                    s.write(String.format("\n%s,%s", l.get(index).getAlias(), String.format("%s/.chomsky/ids/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())));
                    s.close();
                } else {
                    FileOutputStream s = new FileOutputStream(f);

                    s.flush();
                    s.write(String.format("%s,%s", l.get(index).getAlias(), String.format("%s/.chomsky/ids/%s_pubkey.key", System.getProperty("user.home"), l.get(index).getAlias())).getBytes());
                    s.close();
                }
            } catch (Exception e) {
                System.out.println(String.format("Unable to write to -> %s/.chomsky/ids/%s_pubkey.key: %s", System.getProperty("user.home"), l.get(index).getAlias(), e.getMessage()));
            }
            System.out.println(String.format("New alias and keys created for %s -> %s", l.get(index), String.format("%s/.chomsky/ids/%s_private.key", System.getProperty("user.home"), l.get(index).getAlias())));
        }
    }

    public static ArrayListExtended<Alias> readAlias(String fileContents) {
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

    public static String fileToString(String filePath) {
        try {
            String s = "";
            String buff = "";
            BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            while ((buff = file.readLine()) != null) {

                s += buff;
                s += "\n";

            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }
}
