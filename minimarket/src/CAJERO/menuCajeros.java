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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.imageio.ImageIO;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

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
                        MongoCollection<Document> ventasCollection = database.getCollection("ventas");

                        int numeroVenta = getNextVentaId(database);

                        StringBuilder recibo = new StringBuilder();
                        recibo.append("Minimarket La T U C A\n\n");
                        recibo.append("Venta No: ").append(numeroVenta).append("\n\n");
                        recibo.append("\tProductos comprados:\n");

                        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
                            String codigo = entry.getKey();
                            int cantidad = entry.getValue();

                            Document query = new Document("codigo", codigo);
                            Document producto = collection.find(query).first();

                            if (producto != null) {
                                int stockActual = producto.getInteger("cantidad");
                                collection.updateOne(query, new Document("$set", new Document("cantidad", stockActual - cantidad)));

                                String nombre = producto.getString("nombre");
                                double precio = producto.getDouble("precio");

                                recibo.append(String.format("\tProducto: %s\t\n Cantidad: %d\t\n Precio: $ %.2f\n\n", nombre, cantidad, precio * cantidad));
                            }
                        }

                        recibo.append(String.format("\nTotal: $ %.2f", total));
                        String pdfPath = createPDF("recibo.pdf", recibo.toString());

                        Document venta = new Document("ventaID", numeroVenta)
                                .append("productos", carrito)
                                .append("total", total)
                                .append("pdf", pdfPath)
                                .append("fecha", new Date());
                        ventasCollection.insertOne(venta);

                        carrito.clear();
                        total = 0.0;
                        totalTxt.setText("Total: 0.00");

                        JOptionPane.showMessageDialog(null, "Compra realizada con éxito. El recibo se ha guardado como 'recibo.pdf'.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al finalizar la compra.");
                    }
                }
            }
        });
    }

    private int getNextVentaId(MongoDatabase database) {
        MongoCollection<Document> contadorCollection = database.getCollection("contadorVentas");
        Document query = new Document("_id", "ventaId");
        Document update = new Document("$inc", new Document("seq", 1));
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true);
        Document result = contadorCollection.findOneAndUpdate(query, update, options);
        return result == null ? 1 : result.getInteger("seq");
    }

    private String generateUniqueFilename(String baseFilename) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        return baseFilename.replace(".pdf", "_" + timestamp + ".pdf");
    }

    private String createPDF(String dest, String text) {
        try {
            String uniqueDest = generateUniqueFilename(dest);

            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream(uniqueDest));

            document.open();

            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph(text, font));

            document.close();

            System.out.println("PDF creado: " + uniqueDest);
            return uniqueDest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
