
import java.io.*;
import java.lang.reflect.Array;
import java.net.BindException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.sf.ntru.encrypt.NtruEncrypt;

import javax.xml.crypto.Data;

public class Main {
    private static HashMap<String, Executable> commandMap;

    private static Config config;

    private enum listTypes { ids, peers };

    private static final String IDS = "IDS";
    private static final String PEERS = "PEERS";

    private static ArrayListExtended<Alias> ids = new ArrayListExtended<>();
    private static ArrayListExtended<Alias> peers = new ArrayListExtended<>();

    //TODO: a,r,m,l & A,R,M,L command operations.

    private static void loadCommandMap() {
        commandMap = new HashMap(); //HashMap contains the CLI identifier and command as a key value pair

        //Define commands below
        commandMap.put("i", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                //TODO
            }
        });

        commandMap.put("a", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.addAlias(IDS, ids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("r", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.removeAlias(IDS, ids);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("m", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.modifyAlias(IDS, ids);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("l", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.listAliases(ids,peers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("A", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.addAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("R", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.removeAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("M", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.modifyAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("L", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    AliasHandler.listAliases(ids, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("K", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                String aliasPath = String.format("%s/.chomsky/ids/%s.alias", System.getProperty("user.home"), args.get(0));

                String keyPath = String.format("%s/.chomsky/ids/%s_private.key", System.getProperty("user.home"), args.get(0));
                String keyPubPath = String.format("%s/.chomsky/ids/%s_public.key", System.getProperty("user.home"), args.get(0));

                try
                {
                    File f = new File(aliasPath);

                    if (f.exists())
                    {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        System.out.print(String.format("File -> %s : already exists. Overwrite alias and associated keys? ", aliasPath));
                        if ((char) reader.read() != 'y')
                        {
                            System.out.println("Okie dokes, nm then...");
                            System.exit(1);
                        }
                    }
                    FileOutputStream s = new FileOutputStream(f);

                    s.flush();
                    s.write(String.format("%s,%s,%s", args.get(0), keyPath, keyPubPath).getBytes());
                    s.close();
                } catch (Exception e)
                {
                    System.out.println(String.format("Error writing to file %s: %s", aliasPath, e.toString()));
                }


                NTRUContext ctx = new NTRUContext();

                ctx.writeKeyPair(keyPath);
                ctx.writePublicKey(keyPubPath);
                System.out.println(String.format("New alias and keys created for %s -> %s", args.get(0), aliasPath));
            }
        });

        commandMap.put("c", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    new ChatConnection(args.get(0), Integer.parseInt(args.get(1)), ids.get(0));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (ConnectException e) {
                    System.out.println("Connection Error: " + e.getLocalizedMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("C", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                //TODO
            }
        });

        commandMap.put("o", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
                    if (args.size() == 1)
                    {
                        new Thread(new ChatRoom(Integer.parseInt(args.get(0)))).start();
                    }
                    else
                    {
                        new Thread(new ChatRoom(Integer.parseInt(args.get(0)), getAliasArray(args.get(1)).append(ids.get(0)))).start();
                    }

                    new ChatConnection("127.0.0.1", Integer.parseInt(args.get(0)), ids.get(0));
                }
                catch (ConnectException e) {
                    System.out.println("Connection Error: " + e.getLocalizedMessage());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (BindException e) {
                    System.out.println("Unable to bind to port " + args.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("ddd", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                //TODO
            }
        });

        commandMap.put("v", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                //TODO output '-h line 1'
            }
        });

        commandMap.put("h", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                FileInputStream in = null;

                try {
                    in = new FileInputStream(new File("help.txt"));
                    int buffer;
                    while ((buffer = in.read()) != -1) {
                        System.out.print((char) buffer);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Help.txt not found!");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static ArrayListExtended<Alias> getAliasArray(String list)
    {
        ArrayListExtended tmp = new ArrayListExtended();
        ArrayListExtended<String> listArray = new ArrayListExtended<String>(Arrays.asList(list .split(",")));
        // Need alias not found error handling
        if (listArray.head() != null && instanceOf(peers, listArray.head()) != -1)
        {
            tmp.add(peers.get(instanceOf(peers, listArray.head())));
            return tmp.append(getAliasArray(listArray.tail()));
        }
        else if (listArray.head() != null)
        {
            System.out.println(String.format("Peer: %s - not found", listArray.head()));
            return tmp.append(getAliasArray(listArray.tail()));
        }
        else
        {
            return new ArrayListExtended<>();
        }
    }

    private static ArrayListExtended<Alias> getAliasArray(ArrayListExtended<String> listArray)
    {
        ArrayListExtended tmp = new ArrayListExtended();
            // Need alias not found error handling
        if (listArray.head() != null && instanceOf(peers, listArray.head()) != -1)
        {
            tmp.add(peers.get(instanceOf(peers, listArray.head())));
            return tmp.append(getAliasArray(listArray.tail()));
        }
        else if (listArray.head() != null)
        {
            System.out.println(String.format("Peer: %s - not found", listArray.head()));
            return tmp.append(getAliasArray(listArray.tail()));
        }
        else
        {
            return new ArrayListExtended<>();
        }
    }

    private static int instanceOf(ArrayListExtended<Alias> subject, String comp)
    {
        if (subject == null || subject.size() == 0)
        {
            return -1;
        }
        else if (subject.last().equals(comp))
        {
            return subject.size()-1;
        }
        else
        {
            return instanceOf(subject.body(), comp);
        }
    }

    private static ArrayList<ArrayList<String>> splitArgs(String[] args) {
        ArrayList<ArrayList<String>> out = new ArrayList();

        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-')   //if string begins with -, is a command
            {
                out.add(new ArrayList());
                out.get(out.size() - 1).add(args[i].substring(1));    //add to [x][0] position of array
            } else    //otherwise is an argument
            {
                try {
                    if (!out.get(out.size() - 1).isEmpty())
                        out.get(out.size() - 1).add(args[i]); //add to [x][y] position of array
                } catch (ArrayIndexOutOfBoundsException e)    //Throws if arguments come before command
                {
                    System.out.println("Syntax error! Check help for details (-h)");
                    break;
                }
            }
        }
        return out;
    }

    private static boolean validate(ArrayList<ArrayList<String>> argsArray) {
        //TODO
        //All argument validation happens here
        return true;
    }

    private static void initialize() throws IOException    //Initialization happens here
    {
        System.out.println("Initializing");
        //TODO: Temp try catch. Make good
        //TODO: wrap & route traffic in NTRUContext

        config = new Config(); //initialise config file
        //TODO: any forward facing methods from conf???
        loadCommandMap();

        ids.add(new Alias("OldMate", String.format("%s/.chomsky/ids/default.key", System.getProperty("user.home"))));
        peers.add(new Alias("OldMate", String.format("%s/.chomsky/ids/default.key", System.getProperty("user.home"))));
        peers.add(new Alias("john", String.format("%s/.chomsky/peers/john.key", System.getProperty("user.home"))));
    }

    private static ArrayListExtended<Alias> readAlias(listTypes T)
    {
        if (T == )
    }

    private static void parseCommands(ArrayList<ArrayList<String>> argsArray)   //Goes through argsArray and runs each command with given arguments
    {
        for (ArrayList<String> e : argsArray)    //For each command given
        {
            if (commandMap.containsKey(e.get(0)))   //If the command exists in the hashmap
            {
                ArrayList<String> args = new ArrayList(e);
                args.remove(0); //create a new array containing the arguments without the identifier
                commandMap.get(e.get(0)).execute(args);     //execute the command with relevant arguments in an array
            } else
                System.out.println(String.format("Unrecognised command -%s! Check help for details (-h)",
                        e.get(0)));
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList<String>> argsArray = splitArgs(args);    //Split args into [arg][param] array

        //FOR TESTING PURPOSES
        NTRUContext tmp = new NTRUContext();

        tmp.writeKeyPair(String.format("%s/.chomsky/ids/default.key", System.getProperty("user.home")));


        if (validate(argsArray)) {
            initialize();
            parseCommands(argsArray);
        } else {
            System.out.println("Syntax error! Check help for details (-h)");
        }
        System.exit(0);
    }
}