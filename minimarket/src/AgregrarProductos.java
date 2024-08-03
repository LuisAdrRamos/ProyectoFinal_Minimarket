import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

public class AgregrarProductos {
    private JButton regresarButton;
    private JTextField codigoTxt;
    private JTextField nameTxt;
    private JTextField cantidadTxt;
    private JTextField precioTxt;
    private JLabel imgLabel;
    private JButton agregarProductoButton;
    public JPanel AgreProd;
    private JButton seleccionarImagenButton;
    private JFileChooser fileChooser;
    private byte[] imgBytes;

    public AgregrarProductos() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(AgreProd);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame MenuAdminFrame = new JFrame("Inventario");
                MenuAdminFrame.setContentPane(new inventario().Inventaio);
                MenuAdminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MenuAdminFrame.pack();
                MenuAdminFrame.setSize(700, 500);
                MenuAdminFrame.setLocationRelativeTo(null);
                MenuAdminFrame.setVisible(true);
            }
        });

        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoTxt.getText();
                String nombre = nameTxt.getText();
                String cantidad = cantidadTxt.getText();
                String precio = precioTxt.getText();

                if (codigo.isEmpty() || nombre.isEmpty() || cantidad.isEmpty() || precio.isEmpty() || imgBytes == null) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos y seleccione una imagen.");
                    return;
                }

                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                    MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                    MongoCollection<Document> collection = database.getCollection("productos");

                    Document query = new Document("codigo", codigo);
                    if (collection.find(query).first() != null) {
                        JOptionPane.showMessageDialog(null, "El código de producto ya existe.");
                        return;
                    }

                    Document document = new Document("codigo", codigo)
                            .append("nombre", nombre)
                            .append("cantidad", Integer.parseInt(cantidad))
                            .append("precio", Double.parseDouble(precio))
                            .append("img", imgBytes);
                    collection.insertOne(document);

                    JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese valores válidos para precio y cantidad.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al agregar el producto.");
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
    }
}
