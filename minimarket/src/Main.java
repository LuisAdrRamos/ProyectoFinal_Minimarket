import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame login = new JFrame("Login");
        login.setContentPane(new LOGIN().LOGIN());
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setSize(400, 400);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
}