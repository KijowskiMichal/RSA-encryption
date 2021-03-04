package MainPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Windows
{
    private JFrame frame;

    public void initialize() {
        frame = new JFrame("RSA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new GridLayout(2, 2));
        frame.setVisible(true);
    }

    public void zeroScreen(String status)
    {
        frame.setLayout(new GridLayout(1, 1));
        this.clear();
        frame.add(new JLabel("<html>"+status+"</html>", SwingConstants.CENTER));
        frame.setVisible(true);
    }

    public void firstScreen()
    {
        frame.setLayout(new GridLayout(2, 2));
        this.clear();
        JButton button1 = new JButton("Server");
        JButton button2 = new JButton("Klient");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.setClientServerFlag(2);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.setClientServerFlag(1);
            }
        });

        frame.add(button1);
        frame.add(button2);
        frame.setVisible(true);
    }

    public void secondScreen()
    {
        frame.setLayout(new GridLayout(3, 3));
        this.clear();
        JTextArea jTextArea = new JTextArea("Podaj port na którym ma nasłuchiwać serwer:");
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);

        JTextField jTextField = new JTextField();

        JButton jButton = new JButton("Zatwierdź");

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.setPort(Integer.valueOf(jTextField.getText()));
            }
        });

        frame.add(jTextArea);
        frame.add(jTextField);
        frame.add(jButton);
        frame.setVisible(true);
    }

    public void thirdScreen()
    {
        frame.setLayout(new GridLayout(4, 4));
        this.clear();
        JTextArea jTextArea = new JTextArea("Podaj adres (pierwsze pole) oraz port (drugie pole) na którym oczekuje serwer:");
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);

        JTextField jTextField = new JTextField();
        JTextField jTextField2 = new JTextField();

        JButton jButton = new JButton("Zatwierdź");

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.setAdres(jTextField.getText());
                Main.setPort(Integer.valueOf(jTextField2.getText()));
            }
        });

        frame.add(jTextArea);
        frame.add(jTextField);
        frame.add(jTextField2);
        frame.add(jButton);
        frame.setVisible(true);
    }

    public void fourthScreen()
    {
        frame.setLayout(new GridLayout(3, 3));
        this.clear();
        JTextArea jTextArea = new JTextArea("Podaj wiadomość którą chcesz przesłać:");
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);

        JTextField jTextField = new JTextField();

        JButton jButton = new JButton("Wyślij");

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Main.getQueue().add(jTextField.getText());
                jTextField.setText("");
            }
        });

        frame.add(jTextArea);
        frame.add(jTextField);
        frame.add(jButton);
        frame.setVisible(true);
    }

    public void clear()
    {
        frame.getContentPane().removeAll();
        frame.repaint();
    }

    public void keysWindow(String text)
    {
        keysWindow(text, 500);
    }

    public void keysWindow(String text, int height)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JTextArea msg = new JTextArea(text);
                msg.setLineWrap(true);
                msg.setWrapStyleWord(true);

                JScrollPane scrollPane = new JScrollPane(msg);
                UIManager.put("OptionPane.minimumSize",new Dimension(500,height));
                JOptionPane.showMessageDialog(null, scrollPane);
            }
        }).start();
    }
}