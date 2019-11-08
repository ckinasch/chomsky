import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ChatConnection
{
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Boolean isLoggedIn;
    public ChatConnection(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(new ClientChatListener()).start();
        isLoggedIn = true;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (isLoggedIn)
        {
            if (System.in.available() > 0)
            {
                outputStream.flush();
                outputStream.writeUTF(reader.readLine());
            }
        }

        outputStream.close();
        inputStream.close();
        socket.close();
    }

    private class ClientChatListener implements Runnable    //Listens for messages sent from chat server, then prints
    {
        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    System.out.println(inputStream.readUTF());
                }
                catch (EOFException e)
                {
                    isLoggedIn = false;
                    break;
                }
                catch (SocketException e)
                {
                    System.out.println("Session was ended remotely");
                    break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
