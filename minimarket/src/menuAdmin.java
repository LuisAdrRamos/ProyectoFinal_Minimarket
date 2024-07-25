import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menuAdmin {
    private JButton historialDeVentasButton;
    public JPanel menuAdmin;
    private JButton agregarCajerosButton;
    private JButton actualizarInventarioButton;

    public menuAdmin() {


        agregarCajerosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame menuAdmin = new JFrame();
                menuAdmin.setVisible(false);

                JFrame agregarCajerosFrame = new JFrame();
                agregarCajerosFrame.setContentPane(new AgregarCajeros().ACajeros);
                agregarCajerosFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                agregarCajerosFrame.pack();
                agregarCajerosFrame.setSize(400, 400);
                agregarCajerosFrame.setLocationRelativeTo(null);
                agregarCajerosFrame.setVisible(true);
            }
        });
    }
}

