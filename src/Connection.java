import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection
{
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public Connection(String address, int port) throws IOException
    {
        socket = new Socket(address, port);
    }

    public void closeConnection() throws IOException{
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
