package ADMINISTRADOR;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class inventario {
    public JPanel Inventaio;
    private JButton button1;
    private JLabel idTxt;
    private JLabel nombreTxt;
    private JLabel cantidadTxt;
    private JLabel precioTxt;
    private JButton anteriorProductoButton;
    private JButton siguienteProductoButton;
    private JLabel productImg;
    private JButton actualizarInventarioButton;
    private JButton agrergarProductosButton;

    private List<Document> productos;
    private int currentIndex;

    public inventario() {
        productos = fetchProductosFromDB();
        currentIndex = 0;
        displayProducto(currentIndex);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(Inventaio);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame MenuAdminFrame = new JFrame("Menu Administrador");
                MenuAdminFrame.setContentPane(new menuAdmin().menuAdmin);
                MenuAdminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MenuAdminFrame.pack();
                MenuAdminFrame.setSize(700, 500);
                MenuAdminFrame.setLocationRelativeTo(null);
                MenuAdminFrame.setVisible(true);
            }
        });

        siguienteProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < productos.size() - 1) {
                    currentIndex++;
                    displayProducto(currentIndex);
                }
            }
        });

        anteriorProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayProducto(currentIndex);
                }
            }
        });
        actualizarInventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(Inventaio);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame inventarioFrame = new JFrame("Actualizar Inventario");
                inventarioFrame.setContentPane(new ActualizarInventario().UpdateInv);
                inventarioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                inventarioFrame.pack();
                inventarioFrame.setSize(600, 500);
                inventarioFrame.setLocationRelativeTo(null);
                inventarioFrame.setVisible(true);
            }
        });
        agrergarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(Inventaio);
                if (currentFrame != null) {
                    currentFrame.setVisible(false);
                }

                JFrame inventarioFrame = new JFrame("Agregar Productos");
                inventarioFrame.setContentPane(new AgregrarProductos().AgreProd);
                inventarioFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                inventarioFrame.pack();
                inventarioFrame.setSize(600, 500);
                inventarioFrame.setLocationRelativeTo(null);
                inventarioFrame.setVisible(true);
            }
        });
    }

    private List<Document> fetchProductosFromDB() {
        List<Document> productosList = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
            System.out.println("Conexion establecida");
            MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
            MongoCollection collection = database.getCollection("productos");

            for (Object doc : collection.find()) {
                productosList.add((Document) doc);
            }
        }
        return productosList;
    }

    private void displayProducto(int index) {
        if (index >= 0 && index < productos.size()) {
            Document producto = productos.get(index);
            idTxt.setText(producto.getString("codigo"));
            nombreTxt.setText(producto.getString("nombre"));
            cantidadTxt.setText(String.valueOf(producto.getInteger("cantidad")));
            precioTxt.setText(String.valueOf(producto.getDouble("precio")));

            Object imgField = producto.get("img");
            if (imgField instanceof Binary) {
                Binary imgBinary = (Binary) imgField;
                byte[] imgBytes = imgBinary.getData();

                try (ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes)) {
                    BufferedImage img = ImageIO.read(bais);
                    ImageIcon imageIcon = new ImageIcon(img);
                    productImg.setIcon(imageIcon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                productImg.setIcon(null);
            }
        }
    }
}
