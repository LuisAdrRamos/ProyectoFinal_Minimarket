package ADMINISTRADOR;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa el historial de ventas en el minimarket.
 */
public class historial {
    public JPanel HistVentas;
    private JButton menuPrincipalButton;
    private JLabel NumVentas;
    private JButton anteriorButton;
    private JButton siguienteButton;
    private JLabel ventaIDtxt;
    private JLabel fechatxt;
    private JLabel totaltxt;
    private JButton abrirReciboButton;

    private List<Document> recibos;
    private int currentIndex;

    /**
     * Constructor de la clase historial que inicializa los componentes y establece las acciones de los botones.
     */
    public historial() {
        currentIndex = 0;
        recibos = fetchVentasFromDB();
        if (!recibos.isEmpty()) {
            displayRecibo(currentIndex);
        }

        menuPrincipalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(HistVentas);
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

        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayRecibo(currentIndex);
                }
            }
        });

        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < recibos.size() - 1) {
                    currentIndex++;
                    displayRecibo(currentIndex);
                }
            }
        });

        abrirReciboButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex >= 0 && currentIndex < recibos.size()) {
                    Document recibo = recibos.get(currentIndex);
                    String pdfPath = recibo.getString("pdf"); // Aquí asumiendo que el campo es 'pdf'
                    if (pdfPath != null) {
                        try {
                            File pdfFile = new File(pdfPath);
                            Desktop desktop = Desktop.getDesktop();
                            if (desktop.isSupported(Desktop.Action.OPEN)) {
                                desktop.open(pdfFile);
                            } else {
                                JOptionPane.showMessageDialog(HistVentas, "Abrir archivos no es compatible en este sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(HistVentas, "No se pudo abrir el archivo PDF.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    /**
     * Obtiene una lista de ventas desde la base de datos.
     *
     * @return La lista de ventas.
     */
    private List<Document> fetchVentasFromDB() {
        List<Document> ventasList = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
            MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
            MongoCollection<Document> collection = database.getCollection("ventas");

            for (Document doc : collection.find()) {
                ventasList.add(doc);
            }
        }
        return ventasList;
    }

    /**
     * Muestra los datos del recibo en el índice especificado.
     *
     * @param index El índice del recibo a mostrar.
     */
    private void displayRecibo(int index) {
        if (index >= 0 && index < recibos.size()) {
            Document recibo = recibos.get(index);

            ventaIDtxt.setText(String.valueOf(recibo.getInteger("ventaID")));

            Object fechaObj = recibo.get("fecha");
            if (fechaObj instanceof Date) {
                Date fecha = (Date) fechaObj;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fechatxt.setText(sdf.format(fecha));
            } else {
                fechatxt.setText("Fecha no disponible");
            }

            totaltxt.setText(String.format("Total: %.2f", recibo.getDouble("total")));
        }
    }
}

