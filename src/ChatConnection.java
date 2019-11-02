import java.io.*;
import java.net.Socket;

public class ChatConnection
{
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ChatConnection(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(new ClientChatListener()).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            outputStream.flush();
            outputStream.writeUTF(reader.readLine());
        }
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
                    System.out.println("Connection terminated");
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
