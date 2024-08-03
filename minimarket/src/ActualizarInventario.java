import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ActualizarInventario {
    public JPanel UpdateInv;
    private JButton regresarButton;
    private JTextField codigoTxt;
    private JButton actualizarButton;
    private JTextField precioTxt;
    private JTextField cantidadTxt;
    private JButton seleccionarImagenButton;
    private JLabel imgLabel;
    private JFileChooser fileChooser;
    private byte[] imgBytes;

    public ActualizarInventario() {
        imgLabel.setPreferredSize(new Dimension(200, 200));
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoTxt.getText();

                if (precioTxt.getText().isEmpty() || cantidadTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;
                }

                double precio;
                int cantidad;

                try {
                    precio = Double.parseDouble(precioTxt.getText());
                    cantidad = Integer.parseInt(cantidadTxt.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese valores válidos para precio y cantidad.");
                    return;
                }

                try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                    MongoCollection<Document> collection = database.getCollection("productos");

                    Document query = new Document("codigo", codigo);
                    Document update = new Document("$set", new Document("precio", precio).append("cantidad", cantidad));
                    if (imgBytes != null) {
                        update.append("$set", new Document("img", imgBytes));
                    }

                    UpdateResult result = collection.updateOne(query, update);

                    if (result.getMatchedCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Código de producto no encontrado.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage());
                }
            }
        });

        seleccionarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(selectedFile);

                        if (imgLabel.getWidth() == 0 || imgLabel.getHeight() == 0) {
                            imgLabel.setSize(200, 200);
                        }

                        imgLabel.setIcon(new ImageIcon(img.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH)));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(img, "png", baos);
                        imgBytes = baos.toByteArray();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(UpdateInv);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame MenuAdminFrame = new JFrame("Inventario de Productos");
                MenuAdminFrame.setContentPane(new inventario().Inventaio);
                MenuAdminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MenuAdminFrame.pack();
                MenuAdminFrame.setSize(700, 500);
                MenuAdminFrame.setLocationRelativeTo(null);
                MenuAdminFrame.setVisible(true);
            }
        });
    }
}