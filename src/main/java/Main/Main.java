package Main;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static void main(String[] args){
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("PAC-MAN");

        Panel panel = new Panel();
        KeyHandler keyHandler = new KeyHandler(panel);
        MouseHandler mouseHandler = new MouseHandler(panel);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(keyHandler);
        panel.addMouseListener(mouseHandler);

        window.add(panel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        panel.start();
    }
}
