import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom
{
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<Client> clientList;

    public ChatRoom(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);

        clientList = new ArrayList<>(); //List of connected clients

        System.out.println("Waiting for ChatConnection...");

        Thread listenThread = new Thread()
        {
            private Boolean isRunning;

            public void setRunning(Boolean running)
            {
                isRunning = running;
            }

            @Override
            public void run()
            {
                super.run();
                isRunning = true;
                while(isRunning)
                {
                    try
                    {
                        socket = serverSocket.accept();
                        clientList.add(new Client(socket, new Alias("ConnectingUser")));   //TODO Create a new alias object with connecting users information
                        Thread chatThread = new Thread(clientList.get(clientList.size()-1));
                        chatThread.start();

                        sendServerMessage("New connection from " + clientList.get(clientList.size() - 1).getAddress());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                try
                {
                    socket.close();
                    serverSocket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        listenThread.start();
    }

    private void sendServerMessage(String message) throws IOException
    {
        for (Client c : clientList)
        {
            c.sendMessage(message);
        }
    }

    private void sendMessage(String message, Client origin) throws IOException
    {
        for (Client c : clientList)
        {
            c.sendMessage(String.format("%s: %s", origin.getClientAlias().getUsername(), message));
        }
    }

    private class Client implements Runnable
    {
        private Alias clientAlias;
        private String address;
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        private Boolean isConnected;

        public Client(Socket socket, Alias alias) throws IOException
        {
            this.clientAlias = alias;
            this.socket = socket;
            this.address = socket.getInetAddress().toString();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            isConnected = true;
        }

        public void sendMessage(String message) throws IOException
        {
            outputStream.flush();
            outputStream.writeUTF(message);
        }

        public Alias getClientAlias()
        {
            return clientAlias;
        }

        public String getAddress()
        {
            return address;
        }

        @Override
        public void run()
        {
            //todo
            while (isConnected)
            {

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

        public void closeConnection()
        {
            isConnected = false;
        }
    }
}
