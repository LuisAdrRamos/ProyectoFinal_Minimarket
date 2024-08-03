import CAJERO.menuCajeros;
import LOGIN.LOGIN;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*JFrame login = new JFrame("Login");
        login.setContentPane(new LOGIN().LOGIN());
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setSize(400, 500);
        login.setLocationRelativeTo(null);
        login.setVisible(true);*/

        /*JFrame menu = new JFrame("Menu Administrador");
        menu.setContentPane(new menuAdmin().menuAdmin);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.pack();
        menu.setSize(700, 500);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        new menuAdmin();*/

        JFrame menu = new JFrame("Menu Cajero");
        menu.setContentPane(new menuCajeros().menuCaja);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.pack();
        menu.setSize(700, 500);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }
}