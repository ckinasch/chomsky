import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom
{
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean isRunning;
    private ArrayList<Client> clientList;

    public ChatRoom(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        clientList = new ArrayList<Client>();
        clientList.add(new Client(new Alias("Username"), "Address"));
        isRunning = true;

        try
        {
            System.out.println("Waiting for Connection...");
            socket = serverSocket.accept();

            clientList.add(new Client(new Alias("ConnectingUser"),
                    socket.getInetAddress().toString()));

            System.out.println("Connection from "+clientList.get(1).getAddress());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    private class Client
    {
        Alias clientAlias;
        String address;

        public Client(Alias alias, String address)
        {
            this.clientAlias = alias;
            this.address = address;
        }

        public String getAddress()
        {
            return address;
        }
    }

    private class ChatThread implements Runnable
    {
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public ChatThread(Socket socket) throws IOException
        {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        }

        @Override
        public void run()
        {

        }
    }
}
