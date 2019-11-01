import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main
{
    private static HashMap<String, Executable> commandMap;

    private static void loadCommandMap()
    {
        commandMap = new HashMap(); //HashMap contains the CLI identifier and command as a key value pair

        //Define commands below
        commandMap.put("i", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("a", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("r", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("m", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("l", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("A", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("R", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("M", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("L", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("K", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("c", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("C", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("o", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
                try
                {
                    new Thread(new ChatRoom(Integer.parseInt(args.get(0)))).start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        });

        commandMap.put("ddd", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("v", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });

        commandMap.put("h", new Executable()
        {
            @Override
            public void execute(ArrayList<String> args)
            {
                //TODO
            }
        });
    }

    private static ArrayList<ArrayList<String>> splitArgs(String[] args)
    {
        ArrayList<ArrayList<String>> out = new ArrayList();

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].charAt(0) == '-')   //if string begins with -, is a command
            {
                out.add(new ArrayList());
                out.get(out.size()-1).add(args[i].substring(1));    //add to [x][0] position of array
            }
            else    //otherwise is an argument
            {
                try
                {
                    if (!out.get(out.size() - 1).isEmpty())
                        out.get(out.size() - 1).add(args[i]); //add to [x][y] position of array
                }
                catch (ArrayIndexOutOfBoundsException e)    //Throws if arguments come before command
                {
                    System.out.println("Syntax error! Check help for details (-h)");
                    break;
                }
            }

        }

        return out;
    }

    private static boolean validate(ArrayList<ArrayList<String>> argsArray)
    {
        //TODO
        //All argument validation happens here
        return true;
    }

    private static void initialize()    //Initialization happens here
    {
        Config.getConfig(); //initialise config file
        loadCommandMap();
    }

    private static void parseCommands(ArrayList<ArrayList<String>> argsArray)   //Goes through argsArray and runs each command with given arguments
    {
        for (ArrayList<String> e: argsArray)    //For each command given
        {
            if (commandMap.containsKey(e.get(0)))   //If the command exists in the hashmap
            {
                ArrayList<String> args = new ArrayList(e);
                args.remove(0); //create a new array containing the arguments without the identifier
                commandMap.get(e.get(0)).execute(args);     //execute the command with relevant arguments in an array
            }
            else
                System.out.println(String.format("Unrecognised command -%s! Check help for details (-h)",
                        e.get(0)));
        }
    }

    public static void main(String[] args)
    {
        ArrayList<ArrayList<String>> argsArray = splitArgs(args);    //Split args into [arg][param] array

        if (validate(argsArray))
        {
            initialize();
            parseCommands(argsArray);
        }
        else
        {
            System.out.println("Syntax error! Check help for details (-h)");
        }
    }
}
