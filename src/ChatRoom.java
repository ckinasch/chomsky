import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoom implements Runnable
{
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean isRunning;

    public ChatRoom(int port) throws IOException
    {
        super();
        serverSocket = new ServerSocket(port);
        isRunning = false;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void run()
    {
        isRunning = true;

        while(isRunning)
        {
            try
            {
                socket = serverSocket.accept();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
