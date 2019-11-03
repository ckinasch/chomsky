import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom implements Runnable
{
    private Socket socket;
    private ServerSocket serverSocket;
    ArrayList<Client> connectedClients;
    Boolean serverIsRunning;

    public ChatRoom(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayList<>();
    }

    @Override
    public void run()   //Thread listens for client connections
    {
        System.out.println("Chat Room Opened");
        serverIsRunning = true;

        while(serverIsRunning)
        {
            try
            {
                socket = serverSocket.accept();
                Client client = new Client(socket);
                Thread clientHandler = new Thread(client);  //Each client is assigned their own handler
                connectedClients.add(client);
                clientHandler.start();
                sendServerMessage(String.format("Receiving connection from %s", socket.getInetAddress().toString()));

                //TODO Identity Verification
                //if identity is verified
                //sendServerMessage(String.format("%s verified", username));

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            serverSocket.close();
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendServerMessage(String message) throws IOException   //Send message from server
    {
        for (Client c : connectedClients)
        {
            c.getOutputStream().flush();
            c.getOutputStream().writeUTF(message);
        }
    }

    private void sendUserMessage(String message, Client origin) throws IOException  //Send message from user
    {
        for (Client c : connectedClients)
        {
            c.getOutputStream().flush();
            c.getOutputStream().writeUTF(String.format("%s: %s", origin.getAddress(), message));
        }
    }

    private class Client implements Runnable
    {
        private Socket socket;
        private DataOutputStream outputStream;
        private DataInputStream inputStream;
        private String address;
        private Boolean isLoggedIn;

        public Client(Socket clientSocket) throws IOException
        {
            socket = clientSocket;
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            address = socket.getInetAddress().toString();
            isLoggedIn = true;
        }

        public String getAddress()
        {
            return address;
        }

        public DataOutputStream getOutputStream()
        {
            return outputStream;
        }

        @Override
        public void run()   //Checks clients input stream for any inbound messaged from client to server
        {
            String in;

            while (isLoggedIn)
            {
                try
                {
                    //user command logic here
                    in = inputStream.readUTF();

                    if (in.charAt(0) == '\\')
                    {
                        //TODO process commands
                        switch(in)
                        {
                            case "\\exit":
                                logOut();
                                if (connectedClients.isEmpty())
                                {
                                    System.out.println("All clients have disconnected");
                                    serverIsRunning = false;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    else
                        sendUserMessage(in, this);   //broadcasts messages to users on server
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                inputStream.close();
                outputStream.close();
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void logOut() throws IOException
        {
            isLoggedIn = false;

            sendServerMessage(String.format("%s has disconnected", address));
            connectedClients.remove(connectedClients.indexOf(this));
        }

        public Boolean getLoggedIn()
        {
            return isLoggedIn;
        }
    }
}
