import java.io.*;
import java.net.Socket;

public class ChatConnection
{
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public ChatConnection(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(new ConversationListener()).start();
        new ConversationSender();
    }

    public void closeConnection() throws IOException{
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    private class ConversationListener implements Runnable
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConversationSender
    {
        public ConversationSender() throws IOException
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String in;
            System.out.print("Ready to send!");

            while (true)
            {
                in = reader.readLine();
                outputStream.flush();
                outputStream.writeUTF(in);
            }
        }
    }
}
