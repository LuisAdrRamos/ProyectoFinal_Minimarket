import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

    private List<Document> productos;
    private int currentIndex;

    public inventario() {

        try {
            String imagePath = "imagenes/inventario1.png";
            ImageIcon imageIcon = new ImageIcon(imagePath);
            productImg.setIcon(imageIcon);

            String imagePath2 = "imagenes/ventas.png";
            ImageIcon imageIcon2 = new ImageIcon(imagePath2);
            productImg.setIcon(imageIcon2);

            String imagePath3 = "imagenes/cajero.png";
            ImageIcon imageIcon3 = new ImageIcon(imagePath3);
            productImg.setIcon(imageIcon3);


        } catch (Exception e) {
            e.printStackTrace();
        }

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

                JFrame MenuAdminFrame = new JFrame("Agregar Cajeros");
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
        }
    }
}
