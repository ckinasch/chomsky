
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.ntru.encrypt.NtruEncrypt;

public class Main {
    private static HashMap<String, Executable> commandMap;

    private static Config config;

    private static final String IDS = "IDS";
    private static final String PEERS = "PEERS";

    private static ArrayList<Alias> ids = new ArrayList<>();
    private static ArrayList<Alias> peers = new ArrayList<>();

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
//                    config.addAlias(IDS, ids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("r", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    config.removeAlias(IDS, ids);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("m", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    config.modifyAlias(IDS, ids);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("l", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    System.out.println(config.listAliases(IDS, ids));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("A", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    config.addAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("R", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    config.removeAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("M", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    config.modifyAlias(PEERS, peers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("L", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                try {
//                    System.out.println(config.listAliases(PEERS, peers));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("K", new Executable() {
            @Override
            public void execute(ArrayList<String> args) {
                //TODO
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
                    new Thread(new ChatRoom(Integer.parseInt(args.get(0)))).start();
                    new ChatConnection("127.0.0.1", Integer.parseInt(args.get(0)), ids.get(0));
                } catch (ConnectException e) {
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