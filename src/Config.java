
public class Config
{

    private static Config config;
    //switch to alias post merge
    private Identity[] identities;
    private String userPath;
    private Peer[] addressBook;

    String CONF_PATH = "./config.cfg";

    /**
     * FLOW
     * getConfig
     *      notFound
     *          specify location???
     *          create
     *
     *      found
     *          load
     *              validate settings
     *                  success
     *                      create arrays
     *                  failure
     *                      create
     *
     * METHODS
     * getIdentities -> identities
     *
     * getAddressBook -> addressBook
     *
     * setIdentities : identities.add(vals) -> write config
     *
     * setAddressBook : addressBook.add(vals) -> write config
     *
     */

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
