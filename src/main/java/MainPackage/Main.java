package MainPackage;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;

public class Main
{
    private static Windows windows;
    private static RSA rsa;
    private static DSA dsa;

    private static Queue<String> queue = new LinkedList<>();

    private static int clientServerFlag = 0;

    private static int port = 0;
    private static String adres = "";

    public static void main(String[]args)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //configure
                Main.windows = new Windows();
                Main.windows.initialize();
                Main.windows.zeroScreen("Generowanie klucza...");
                Main.rsa = new RSA();
                Main.rsa.generateKeys();
                Main.dsa = new DSA();
                Main.dsa.generateKeys();
                windows.keysWindow("Wygenerowano klucz publiczny RSA:\n"+Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded())+"\n\nWygenerowano klucz prywatny RSA:\n"+Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded())+"\n\nWygenerowano klucz publiczny DSA:\n"+Base64.getEncoder().encodeToString(dsa.getPublicKey().getEncoded())+"\n\nWygenerowano klucz prywatny DSA:\n"+Base64.getEncoder().encodeToString(dsa.getPrivateKey().getEncoded()));


                //Wybor klient serwer
                Main.windows.firstScreen();
                while (Main.getClientServerFlag()==0)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //konfiguracja sieciowa
                if (Main.getClientServerFlag()==1)
                {
                    windows.thirdScreen();
                    while (Main.getPort()==0)
                    {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    WebCommunication.startClient(Main.getAdres(), Main.getPort());
                }
                else
                {
                    windows.secondScreen();
                    while (Main.getPort()==0)
                    {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    windows.zeroScreen("Oczekiwanie na połączenie...");
                    WebCommunication.startServer(Main.getPort());
                }

                //wymiana kluczy
                windows.zeroScreen("Wymiana kluczy...");
                if (Main.getClientServerFlag()==1)
                {
                    rsa.setPublicKeyOfOtherSide(RSA.returnPublicKeyFromBase64ValueOfThisPublicKey(new String(WebCommunication.receive())));
                    WebCommunication.send("OK".getBytes());
                    dsa.setPublicKeyOfOtherSide(DSA.returnPublicKeyFromBase64ValueOfThisPublicKey(new String(WebCommunication.receive())));
                    WebCommunication.send("OK".getBytes());
                    String base64Hash = Base64.getEncoder().encodeToString(WebCommunication.receive());
                    windows.zeroScreen("Weryfikacja kluczy...");
                    if (dsa.verify(Base64.getDecoder().decode(base64Hash),DigestUtils.sha256(rsa.getPublicKeyOfOtherSide().getEncoded())))
                    {
                        windows.zeroScreen("Weryfikacja zakończona powodzeniem");
                        WebCommunication.send("OK".getBytes());
                    }
                    else
                    {
                        windows.zeroScreen("Weryfikacja zakończona błędem");
                        WebCommunication.send("ERR".getBytes());
                    }
                    WebCommunication.receive();
                    WebCommunication.send(rsa.getPublicKey().getEncoded());
                    windows.keysWindow("Otrzymano klucz publiczny RSA:\n"+Base64.getEncoder().encodeToString(rsa.getPublicKeyOfOtherSide().getEncoded())+"\n\nOtrzymano klucz publiczny DSA:\n"+Base64.getEncoder().encodeToString(dsa.getPublicKeyOfOtherSide().getEncoded())+"\n\nOtrzymano podpis klucza publicznego RSA:\n"+base64Hash);
                }
                else
                {
                    WebCommunication.send(Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded()).getBytes());
                    WebCommunication.receive();
                    WebCommunication.send(Base64.getEncoder().encodeToString(dsa.getPublicKey().getEncoded()).getBytes());
                    WebCommunication.receive();
                    String base64Hash = dsa.sign(DigestUtils.sha256(rsa.getPublicKey().getEncoded()));
                    WebCommunication.send(Base64.getDecoder().decode(base64Hash));
                    windows.zeroScreen("Weryfikacja kluczy...");
                    String response = new String(WebCommunication.receive());
                    if(!response.equals("OK"))
                    {
                        windows.zeroScreen("Weryfikacja zakończona błędem");
                        windows.keysWindow("Weryfikacja zakończona błędem", 100);
                    }
                    else
                    {
                        windows.zeroScreen("Weryfikacja zakończona powodzeniem");
                    }
                    WebCommunication.send("OK".getBytes());
                    rsa.setPublicKeyOfOtherSide(RSA.returnPublicKeyFromBase64ValueOfThisPublicKey(Base64.getEncoder().encodeToString(WebCommunication.receive())));
                    windows.keysWindow("Otrzymano klucz publiczny RSA:\n"+Base64.getEncoder().encodeToString(rsa.getPublicKeyOfOtherSide().getEncoded()));
                }




                //obsluga wiadomosci
                windows.fourthScreen();
                while (true)
                {
                    if (WebCommunication.getAvailable()!=0)
                    {
                        String text = new String(WebCommunication.receive());
                        windows.keysWindow("Otrzymano zaszyfrowaną wiadomość:\n"+text+"\n\nOdszyfrowano:\n"+rsa.decrypt(text),300);
                    }
                    if (Main.getQueue().size()!=0)
                    {
                        String text = rsa.encryptWithKeyOfOtherSide(Main.getQueue().remove());
                        WebCommunication.send(text.getBytes());
                        windows.keysWindow("Wysłano zaszyfrowaną wiadomość:\n"+text,300);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static int getClientServerFlag() {
        return Main.clientServerFlag;
    }

    public static void setClientServerFlag(int clientServerFlag) {
        Main.clientServerFlag = clientServerFlag;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Main.port = port;
    }

    public static String getAdres() {
        return adres;
    }

    public static void setAdres(String adres) {
        Main.adres = adres;
    }

    public static Queue<String> getQueue() {
        return Main.queue;
    }

    public static void setQueue(Queue<String> queue) {
        Main.queue = queue;
    }
}
