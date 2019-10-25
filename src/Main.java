import java.util.ArrayList;

public class Main
{
    private static ArrayList<ArrayList<String>> splitArgs(String[] args)
    {
        ArrayList<ArrayList<String>> out = new ArrayList<>();

        for (int i = 0; i < args.length; i++)
        {
            if (args[i].charAt(0) == '-')   //if string begins with -, is a command
            {
                out.add(new ArrayList<>());
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

        return true;
    }

    public static void main(String[] args)
    {
        ArrayList<ArrayList<String>> argsArray = splitArgs(args);    //Split args into [arg][param] array

        if (validate(argsArray))
        {
            //TODO
            //init()
            //IT'S PROGRAM TIME
        }
        else
        {
            System.out.println("Syntax error! Check help for details (-h)");
        }
    }
}
