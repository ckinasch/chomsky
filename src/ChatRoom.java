import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionPublicKey;
import net.sf.ntru.encrypt.NtruEncrypt;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ChatRoom implements Runnable
{
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayListExtended<Alias> peers_list;
    ArrayListExtended<Client> connectedClients;
    Boolean serverIsRunning;
    NTRUContext ntru_ctx;

    public ChatRoom(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayListExtended<>();
        this.ntru_ctx = new NTRUContext();
    }

    public ChatRoom(int port, ArrayListExtended<Alias> peers) throws IOException
    {
        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayListExtended<>();
        this.ntru_ctx = new NTRUContext();
        peers_list = new ArrayListExtended<>();
        if (peers != null || peers.size() != 0)
        {
            peers_list = AppendPeers(peers, ntru_ctx);
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
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                int pkeyLength = inputStream.readInt();
                byte[] buff = inputStream.readNBytes(pkeyLength);

                EncryptionPublicKey temp_pub_key = new EncryptionPublicKey(buff);
                Client client;

                if (peers_list != null && arrayContains(peers_list, temp_pub_key))
                {
                    client = new Client(socket, peers_list.get(arrayInstanceIndex(peers_list, temp_pub_key)));
                }
                else if (peers_list != null)
                {
                    System.out.println(String.format("Connection from peer: %s - rejected (publicKey)", socket.getInetAddress().toString()));
                    outputStream.writeUTF("//rej");
                    socket.close();
                    continue outer;
                }
                else
                {
                    NTRUContext tmp = this.ntru_ctx;
                    tmp.setPeer_kp(temp_pub_key);
                    client = new Client(socket, tmp);
                }

                Thread clientHandler = new Thread(client);  //Each client is assigned their own handler
                connectedClients.add(client);
                clientHandler.start();
                sendServerMessage(String.format("Receiving connection from %s", socket.getInetAddress().toString()));
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

    public ArrayListExtended<Alias> AppendPeers(ArrayListExtended<Alias> peers_list, NTRUContext ctx)
    {
        if (peers_list.head() != null)
        {
            NTRUContext ctx_copy = ctx.copy();
            ctx_copy.setPeer_kp(peers_list.head());

            return new ArrayListExtended<>(){
                {
                    add(new Alias(peers_list.head().getAlias(), ctx_copy));
                    append(AppendPeers(peers_list.tail(), ctx));
                }
            };
        }
        else
        {
            return peers_list.tail();
        }
    }


    private Boolean arrayContains(ArrayListExtended<Alias> subject, EncryptionPublicKey comp)
    {
        if (subject == null || subject.size() == 0)
        {
            return false;
        }
        if (subject.last().getNtru_ctx().getPeer_kp().equals(comp))
        {

            return true;
        }
        else
        {

            return arrayContains(subject.body(), comp);
        }
    }

    private int arrayInstanceIndex(ArrayListExtended<Alias> subject, EncryptionPublicKey comp)
    {
        if (subject == null || subject.size() == 0)
        {
            return -1;
        }
        if (comp.equals(subject.last().getNtru_ctx().getPeer_kp()))
        {
            return subject.size()-1;
        }
        else
        {
            return arrayInstanceIndex(subject.body(), comp);
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
            this.ntru_ctx = ctx;

            byte[] randArr = new byte[247];
            new Random().nextBytes(randArr);
            byte[] hs = ntru_ctx.encrypt(randArr);
            outputStream.writeInt(ntru_ctx.getKp_pub().getEncoded().length);
            outputStream.write(ntru_ctx.getKp_pub().getEncoded());
            outputStream.writeInt(hs.length);
            outputStream.write(hs);
            byte[] solution = ntru_ctx.decrypt(inputStream.readNBytes(inputStream.readInt()));

            if (Arrays.equals(solution, randArr))
            {
                outputStream.writeUTF("\\acc");
            }
            else
            {
                outputStream.writeUTF("//rej");
            }
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