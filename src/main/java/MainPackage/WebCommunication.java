package MainPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebCommunication
{
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void startClient(final String ip, final int port)
    {
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer(final int port)
    {
        try {
            ServerSocket welcomeSocket = new ServerSocket(port);
            socket = welcomeSocket.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(final byte[]bytes)
    {
        try {
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] receive()
    {
        try {
            while (in.available()==0)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            byte[]bytes = new byte[in.available()];
            in.read(bytes, 0, in.available());
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAvailable()
    {
        try {
            return in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void shutItDown()
    {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
