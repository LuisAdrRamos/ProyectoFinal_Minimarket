import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menuCajeros {
    public JPanel menuCaja;
    private JButton cerrarSesionButton;

    public menuCajeros() {
        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(menuCaja);
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
