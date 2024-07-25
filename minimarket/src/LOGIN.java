import com.mongodb.client.*;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LOGIN {
    public JPanel LOGIN;
    private JButton iniciarSesiónButton;
    private JPasswordField passwordText;
    private JTextField userText;
    private JComboBox modosBox;
    private JLabel img;

    public LOGIN() {
        iniciarSesiónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (modosBox.getSelectedIndex()) {
                    case 1:
                        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                            System.out.println("Conexion establecida");
                            MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                            MongoCollection collection = database.getCollection("administradores");
                            FindIterable<Document> documents = collection.find();

                            boolean valid   = false;
                            String username = userText.getText();
                            String password = passwordText.getText();

                            for (Document document : documents) {
                                String user = document.getString("user");
                                String pass = document.getString("password");

                                if (user.equals(username) && pass.equals(password)) {
                                    valid = true;
                                    break;
                                }
                            }

                            if (valid) {
                                System.out.println("Sesion Administrador validada");
                            } else {
                                System.out.println("Usuario o contraseña incorrecta");
                            }
                        }
                        break;

                    case 2:
                        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
                            System.out.println("Conexion establecida");
                            MongoDatabase database = mongoClient.getDatabase("proyecto_minimarket");
                            MongoCollection collection = database.getCollection("cajeros");
                            FindIterable<Document> documents = collection.find();

                            boolean valid   = false;
                            String username = userText.getText();
                            String password = passwordText.getText();

                            for (Document document : documents) {
                                String user = document.getString("user");
                                String pass = document.getString("password");

                                if (user.equals(username) && pass.equals(password)) {
                                    valid = true;
                                    break;
                                }
                            }

                            if (valid) {
                                System.out.println("Sesion Cajero validada");
                            } else {
                                System.out.println("Usuario o contraseña incorrecta");
                            }
                        }
                        break;
                }
            }
        });
    }

    public Container LOGIN() {
        return LOGIN;
    }
}