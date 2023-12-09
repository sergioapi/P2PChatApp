import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIChat {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton; // Agregamos un botón de envío
    private Process process;
    private BufferedReader reader;
    private Timer timer;
    private JPanel panel1;
    private ArrayList<String> nombres;
    private Map<String, String> mensajesPorUsuario;
    private JList<String> nombresList;

    public GUIChat(UserController user) {
        nombres = new ArrayList<>();
        nombres.add("Fran");
        nombres.add("Alicia");
        nombres.add("Mauro");

        frame = new JFrame("Terminal Chat");
        chatArea = new JTextArea();
        inputField = new JTextField();
        sendButton = new JButton("Send"); // Creamos el botón

        mensajesPorUsuario = new HashMap<>();
        for (String nombre : nombres) {
            mensajesPorUsuario.put(nombre, ""); // Inicializa cada chat vacío
        }
        chatArea.setEditable(false);

        // Utilizamos un JSplitPane para dividir la ventana en dos partes
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(100); // Ancho de la lista de nombres
        splitPane.setLeftComponent(createNombresPanel());
        splitPane.setRightComponent(createChatPanel());

        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        // Agregar un ListSelectionListener a nombresList
        nombresList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = nombresList.getSelectedValue();
                chatArea.setText(mensajesPorUsuario.get(selectedName)); // Actualiza el chatArea
            }
        });

        // Agregamos un ActionListener al botón de envío
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(inputField.getText());
            }
        });

        // Agregamos un ActionListener al inputField para manejar la tecla Enter
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(inputField.getText());
            }
        });


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300); // Ajusta el tamaño según tus preferencias
        frame.setVisible(true);

        startTerminalReader();
    }

    private JPanel createNombresPanel() {
        JPanel nombresPanel = new JPanel(new BorderLayout());
        nombresList = new JList<>(nombres.toArray(new String[0])); // Usa la variable de instancia
        JScrollPane nombresScrollPane = new JScrollPane(nombresList);
        nombresPanel.add(nombresScrollPane, BorderLayout.CENTER);
        return nombresPanel;
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        // Creamos un JPanel para el campo de entrada y el botón
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        return chatPanel;
    }

    private void startTerminalReader() {
        try {
            process = Runtime.getRuntime().exec("/bin/bash");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Leer continuamente la salida de la terminal
            timer = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (reader.ready()) {
                            String message = reader.readLine();
                            appendMessage("terminal: ", message);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        // Verifica si hay un mensaje para enviar y si hay un usuario seleccionado
        if (message.isEmpty() || nombresList.getSelectedValue() == null) {
            return;
        }

        // Obtiene el nombre del usuario seleccionado
        String selectedName = nombresList.getSelectedValue();

        // Obtiene el historial de chat actual para el usuario seleccionado
        String chatHistory = mensajesPorUsuario.getOrDefault(selectedName, "");

        // Añade el nuevo mensaje al historial de chat
        chatHistory += "You: " + message + "\n";

        // Actualiza el historial de chat en el HashMap
        mensajesPorUsuario.put(selectedName, chatHistory);

        // Actualiza el área de chat en la GUI
        chatArea.setText(chatHistory);

        // Limpia el campo de entrada de texto
        inputField.setText("");

        // Aquí puedes agregar la lógica para enviar el mensaje a un servidor o a otro proceso, si es necesario
    }


    private void appendMessage(String sender, String message) {
        // Obtiene el nombre del usuario seleccionado actualmente en la lista
        String selectedName = nombresList.getSelectedValue();
        if (selectedName != null) {
            // Obtiene el historial de chat actual para el usuario seleccionado
            String chatHistory = mensajesPorUsuario.getOrDefault(selectedName, "");

            // Añade el nuevo mensaje al historial de chat
            chatHistory += sender + ": " + message + "\n";

            // Actualiza el historial de chat en el HashMap
            mensajesPorUsuario.put(selectedName, chatHistory);

            // Actualiza el área de chat en la GUI solo si el mensaje es del usuario actualmente seleccionado
            chatArea.setText(chatHistory);
        }
    }
}
