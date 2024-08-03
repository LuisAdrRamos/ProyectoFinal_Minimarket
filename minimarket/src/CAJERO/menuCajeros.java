package CAJERO;

import LOGIN.LOGIN;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class menuCajeros {
    public JPanel menuCaja;
    private JButton anteriorButton;
    private JButton siguienteButton;
    private JLabel nombretxt;
    private JLabel preciotxt;
    private JLabel img;
    private JTextField codigoTxt;
    private JTextField cantidadTxt;
    private JButton buscarButton;
    private JButton cerrarSesionButton;
    private JButton AgregarCarritoButton;
    private JButton finalizarCompraButton;
    private JLabel totalTxt;

    private List<Document> productos;
    private int currentIndex;
    private double total;
    private Map<String, Integer> carrito;

    public menuCajeros() {
        productos = fetchProductosFromDB();
        currentIndex = 0;
        total = 0.0;
        carrito = new HashMap<>();
        cantidadTxt.setText("1");
        displayProducto(currentIndex);

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

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoTxt.getText();
                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese el código del producto.");
                    return;
                }
                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                    MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                    MongoCollection<Document> collection = database.getCollection("productos");

                    Document query = new Document("codigo", codigo);
                    Document producto = collection.find(query).first();

                    if (producto != null) {
                        mostrarDatosProducto(producto);
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                    }
                }
            }
        });

        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayProducto(currentIndex);
                }
            }
        });

        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < productos.size() - 1) {
                    currentIndex++;
                    displayProducto(currentIndex);
                }
            }
        });

        AgregarCarritoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoTxt.getText();
                int cantidad = Integer.parseInt(cantidadTxt.getText());

                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                    MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                    MongoCollection<Document> collection = database.getCollection("productos");

                    Document query = new Document("codigo", codigo);
                    Document producto = collection.find(query).first();

                    if (producto != null) {
                        int stockActual = producto.getInteger("cantidad");
                        if (stockActual < cantidad) {
                            JOptionPane.showMessageDialog(null, "Stock insuficiente.");
                            return;
                        }

                        double precio = producto.getDouble("precio");
                        total += precio * cantidad;
                        totalTxt.setText(String.format("Total: %.2f", total));

                        carrito.put(codigo, carrito.getOrDefault(codigo, 0) + cantidad);
                        JOptionPane.showMessageDialog(null, "Producto agregado al carrito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                    }
                }
            }
        });

        finalizarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (total == 0) {
                    JOptionPane.showMessageDialog(null, "No hay productos en el carrito.");
                } else {
                    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                        MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                        MongoCollection<Document> collection = database.getCollection("productos");

                        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
                            String codigo = entry.getKey();
                            int cantidad = entry.getValue();

                            Document query = new Document("codigo", codigo);
                            Document producto = collection.find(query).first();

                            if (producto != null) {
                                int stockActual = producto.getInteger("cantidad");
                                collection.updateOne(query, new Document("$set", new Document("cantidad", stockActual - cantidad)));
                            }
                        }

                        carrito.clear();
                        total = 0.0;
                        totalTxt.setText("Total: 0.00");

                        JOptionPane.showMessageDialog(null, "Compra realizada con éxito.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al finalizar la compra.");
                    }
                }
            }
        });
    }

    private List<Document> fetchProductosFromDB() {
        List<Document> productosList = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
            MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
            MongoCollection<Document> collection = database.getCollection("productos");

            for (Document doc : collection.find()) {
                productosList.add(doc);
            }
        }
        return productosList;
    }

    private void displayProducto(int index) {
        if (index >= 0 && index < productos.size()) {
            Document producto = productos.get(index);
            mostrarDatosProducto(producto);
        }
    }

    private void mostrarDatosProducto(Document producto) {
        codigoTxt.setText(producto.getString("codigo"));
        nombretxt.setText(producto.getString("nombre"));
        cantidadTxt.setText("1");
        preciotxt.setText(String.valueOf(producto.getDouble("precio")));

        Object imgField = producto.get("img");
        if (imgField instanceof Binary) {
            Binary imgBinary = (Binary) imgField;
            byte[] imgBytes = imgBinary.getData();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes)) {
                BufferedImage bufferedImage = ImageIO.read(bais);
                ImageIcon imageIcon = new ImageIcon(bufferedImage);
                img.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            img.setIcon(null);
        }
    }
}
