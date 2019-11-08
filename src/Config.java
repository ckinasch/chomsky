public class Config
{
    private static Config config;
    private String userPath;
    private Alias[] addressBook;

    public static Config getConfig()    //returns currently loaded config file or loads/creates file on first pass
    {
        if (config == null)
        {
            //IF CAN LOAD CONFIG
            //  CONFIG = LOADCONFIG()
            //ELSE
            config = new Config();
        }

        return config;
    }
}
