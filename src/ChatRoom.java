import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionPublicKey;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoom implements Runnable
{
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<Alias> peers_list;
    ArrayList<Client> connectedClients;
    Boolean serverIsRunning;
    NTRUContext ntru_ctx = new NTRUContext();

    public ChatRoom(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayList<>();
        this.ntru_ctx = new NTRUContext();
    }

    public ChatRoom(int port, Alias[] peers) throws IOException
    {
        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayList<>();
        this.ntru_ctx = new NTRUContext();
        peers_list = AppendPeer(peers, this.ntru_ctx);
    }

    public ArrayList<Alias> AppendPeer(Alias[] peers_list, NTRUContext ctx)
    {
         if (peers_list != null)
         {
             ArrayList<Alias> temp_list = new ArrayList<Alias>();
             ctx.setPeer_kp(peers_list[0]);
             temp_list.add(new Alias(peers_list[0].getAlias(), ctx));
             temp_list.addAll(AppendPeer(Arrays.copyOfRange(peers_list, 1, peers_list.length), ctx));
             return temp_list;
        }
         else
         {
             return null;
         }
    }

    @Override
    public void run()   //Thread listens for client connections
    {
        System.out.println("Chat Room Opened");
        serverIsRunning = true;

        outer: while(serverIsRunning)
        {
            try
            {

                socket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                byte[] buff = inputStream.readNBytes(2066);

                System.out.println(buff.length);

                EncryptionPublicKey temp_pub_key = new EncryptionPublicKey(buff);
                System.out.println("HERE CR");

                if (peers_list != null)
                {
                    for (int p = 0; p < peers_list.size() ; p++)
                    {
                        if(temp_pub_key.equals(peers_list.get(p).getNtru_ctx().getPeer_kp()))
                        {
                            Client client = new Client(socket, peers_list.get(p));
                            Thread clientHandler = new Thread(client);  //Each client is assigned their own handler
                            connectedClients.add(client);
                            clientHandler.start();
                            sendServerMessage(String.format("Receiving connection from %s", socket.getInetAddress().toString()));
                            continue outer;
                        }
                    }
                }
                else
                {
                    NTRUContext tmp = this.ntru_ctx;
                    tmp.setPeer_kp(temp_pub_key);
                    Client client = new Client(socket, tmp);
                    Thread clientHandler = new Thread(client);  //Each client is assigned their own handler
                    connectedClients.add(client);
                    clientHandler.start();
                    sendServerMessage(String.format("Receiving connection from %s", socket.getInetAddress().toString()));
                    continue outer;
                }


                System.out.println(String.format("Connection from peer: %s : rejected", socket.getInetAddress().toString()));
                socket.getOutputStream().write("//rej".getBytes());
                continue outer;
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
        private NTRUContext ntru_ctx;
        private String name;

        public Client(Socket clientSocket, Alias peer) throws IOException
        {
            socket = clientSocket;
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            address = socket.getInetAddress().toString();
            isLoggedIn = true;
            outputStream.writeUTF("\\acc");
        }

        public Client(Socket clientSocket, NTRUContext ctx) throws IOException
        {

            socket = clientSocket;
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            address = socket.getInetAddress().toString();
            isLoggedIn = true;
            outputStream.writeUTF("\\acc");
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
                                    System.out.println("Shutting down chatroom");
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