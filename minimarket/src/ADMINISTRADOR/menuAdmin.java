package ADMINISTRADOR;

import LOGIN.LOGIN;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menuAdmin {
    private JButton historialDeVentasButton;
    public JPanel menuAdmin;
    private JButton agregarCajerosButton;
    private JButton actualizarInventarioButton;
    private JButton cerrarSesionButton;
    private JLabel imgInventario;
    private JLabel imgHistorial;
    private JLabel imgACajeros;

    public menuAdmin() {
        try {
            String imagePath = "imagenes/inventario1.png";
            ImageIcon imageIcon = new ImageIcon(imagePath);
            imgInventario.setIcon(imageIcon);

            String imagePath2 = "imagenes/ventas.png";
            ImageIcon imageIcon2 = new ImageIcon(imagePath2);
            imgHistorial.setIcon(imageIcon2);

            String imagePath3 = "imagenes/cajero.png";
            ImageIcon imageIcon3 = new ImageIcon(imagePath3);
            imgACajeros.setIcon(imageIcon3);


        } catch (Exception e) {
            e.printStackTrace();
        }
        actualizarInventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(menuAdmin);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame inventarioFrame = new JFrame("Inventario");
                inventarioFrame.setContentPane(new inventario().Inventaio);
                inventarioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                inventarioFrame.pack();
                inventarioFrame.setSize(600, 500);
                inventarioFrame.setLocationRelativeTo(null);
                inventarioFrame.setVisible(true);
            }
        });

        historialDeVentasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(menuAdmin);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame historialFrame = new JFrame("Historial de Ventas");
                historialFrame.setContentPane(new historial().HistVentas);
                historialFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                historialFrame.pack();
                historialFrame.setSize(600, 500);
                historialFrame.setLocationRelativeTo(null);
                historialFrame.setVisible(true);
            }
        });

        agregarCajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(menuAdmin);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame agregarCajerosFrame = new JFrame("Agregar Cajeros");
                agregarCajerosFrame.setContentPane(new AgregarCajeros().ACajeros);
                agregarCajerosFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                agregarCajerosFrame.pack();
                agregarCajerosFrame.setSize(600, 500);
                agregarCajerosFrame.setLocationRelativeTo(null);
                agregarCajerosFrame.setVisible(true);
            }
        });

        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(menuAdmin);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame loginFrame = new JFrame("Login");
                loginFrame.setContentPane(new LOGIN().LOGIN());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setSize(400, 500);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });
    }
}

