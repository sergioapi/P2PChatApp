import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.*;

public class GUIChat {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Process process;
    private BufferedReader reader;
    private Timer timer;
    private ArrayList<String> nombres;
    private Map<String, String> mensajesPorUsuario;
    private JList<String> nombresList;
    private JMenuBar menuBar;
    private JPanel mainPanel;

    public GUIChat(UserController controller) {
        nombres = new ArrayList<>();

        frame = new JFrame("P2P ChatApp");
        chatArea = new JTextArea("¡Selecciona un chat para empezar a hablar!");
        chatArea.setEditable(false);
        inputField = new JTextField(20);
        sendButton = new JButton("Send");

        mensajesPorUsuario = new HashMap<>();
        for (String nombre : nombres) {
            mensajesPorUsuario.put(nombre, "");
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(100);
        splitPane.setLeftComponent(createNombresPanel());
        splitPane.setRightComponent(createChatPanel());

        menuBar = new JMenuBar();
        JMenu chatMenu = new JMenu("Chat");
        JMenuItem chatMenuItem = new JMenuItem("Abrir Chat");
        JMenu listaNombresMenu = new JMenu("Amigos");
        JMenuItem listaNombresMenuItem = new JMenuItem("Solicitudes pendientes");
        JMenuItem amigosMenuItem = new JMenuItem("Amigos");

        chatMenu.add(chatMenuItem);
        listaNombresMenu.add(listaNombresMenuItem);
        listaNombresMenu.add(amigosMenuItem);
        menuBar.add(chatMenu);
        menuBar.add(listaNombresMenu);

        frame.setJMenuBar(menuBar);

        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(splitPane, "ChatPanel");
        mainPanel.add(createListaNombresPanel(), "ListaNombresPanel");
        mainPanel.add(createAmigosPanel(), "AmigosPanel");

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        chatMenuItem.addActionListener(e -> cambiarVista("ChatPanel"));
        listaNombresMenuItem.addActionListener(e -> cambiarVista("ListaNombresPanel"));
        amigosMenuItem.addActionListener(e -> cambiarVista("AmigosPanel"));

        // Agregar un apartado para enviar solicitud de amistad
        JMenuItem enviarSolicitudAmistadItem = new JMenuItem("Enviar Solicitud de Amistad");
        listaNombresMenu.add(enviarSolicitudAmistadItem);
        enviarSolicitudAmistadItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Mostrar un cuadro de diálogo para ingresar el nombre
                String nombreIngresado = JOptionPane.showInputDialog(frame, "Ingresa el nombre para la solicitud de amistad:");
                if (nombreIngresado != null && !nombreIngresado.trim().isEmpty()) {
                    String nombreSolicitado = nombreIngresado.trim();
                    // Lógica adicional con el nombre ingresado
                    controller.pedirAmistad(nombreIngresado);
                }
            }
        });

        // Desconexión de usuario en caso de que se cierre la ventana
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cierro sesión
                controller.cerrarSesion();
                frame.dispose(); // Opcional, cierra la ventana
            }
        });

        nombresList.clearSelection(); // Asegurarse de que no haya selección inicial
        nombresList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = nombresList.getSelectedValue();
                if (selectedName != null) {
                    chatArea.setText(mensajesPorUsuario.get(selectedName));
                } else {
                    chatArea.setText("¡Selecciona un chat para empezar a hablar!");
                }
            }
        });

        sendButton.addActionListener(e -> sendMessage(controller, inputField.getText()));
        inputField.addActionListener(e -> sendMessage(controller, inputField.getText()));

        // Si se cierra la ventana se cierra la app
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }


    private JPanel createNombresPanel() {
        JPanel nombresPanel = new JPanel(new BorderLayout());
        nombresList = new JList<>(nombres.toArray(new String[0]));
        JScrollPane nombresScrollPane = new JScrollPane(nombresList);
        nombresPanel.add(nombresScrollPane, BorderLayout.CENTER);
        return nombresPanel;
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        return chatPanel;
    }

    private void sendMessage(UserController controller, String message) {
        if (message.isEmpty() || nombresList.getSelectedValue() == null) {
            return;
        }

        String selectedName = nombresList.getSelectedValue();
        String chatHistory = mensajesPorUsuario.getOrDefault(selectedName, "");

        Mensaje mensaje = new Mensaje(selectedName, controller.getNombreUsuario(), message);
        // TODO envío mensaje

        chatHistory += "You: " + message + "\n";
        mensajesPorUsuario.put(selectedName, chatHistory);
        chatArea.setText(chatHistory);

        inputField.setText("");
    }

    private void appendMessage(String sender, String message) {
        String selectedName = nombresList.getSelectedValue();
        if (selectedName != null) {
            String chatHistory = mensajesPorUsuario.getOrDefault(selectedName, "");
            chatHistory += sender + ": " + message + "\n";
            mensajesPorUsuario.put(selectedName, chatHistory);
            chatArea.setText(chatHistory);
        }
    }

    private void cambiarVista(String nombreVista) {
        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, nombreVista);
    }

    private JPanel createListaNombresPanel() {
        // Panel que contendrá todos los nombres
        JPanel listaNombresPanel = new JPanel();
        listaNombresPanel.setLayout(new BoxLayout(listaNombresPanel, BoxLayout.Y_AXIS));

        // Nombres a mostrar
        String[] nombres = new String[]{};

        // Crear un panel para cada nombre
        for (String nombre : nombres) {
            JPanel nombrePanel = new JPanel(new FlowLayout());
            JLabel nombreLabel = new JLabel(nombre);
            JButton aceptarButton = new JButton("Aceptar");
            JButton rechazarButton = new JButton("Rechazar");

            aceptarButton.addActionListener(e -> {
                // Remueve el panel del nombre al presionar "Aceptar"
                listaNombresPanel.remove(nombrePanel);
                listaNombresPanel.revalidate();
                listaNombresPanel.repaint();
            });

            rechazarButton.addActionListener(e -> {
                // Remueve el panel del nombre al presionar "Rechazar"
                listaNombresPanel.remove(nombrePanel);
                listaNombresPanel.revalidate();
                listaNombresPanel.repaint();
            });

            nombrePanel.add(nombreLabel);
            nombrePanel.add(aceptarButton);
            nombrePanel.add(rechazarButton);
            listaNombresPanel.add(nombrePanel);
        }

        JScrollPane scrollPane = new JScrollPane(listaNombresPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel panelConScroll = new JPanel(new BorderLayout());
        panelConScroll.add(scrollPane, BorderLayout.CENTER);
        return panelConScroll;
    }

    // Método para crear el panel de amigos
    private JPanel createAmigosPanel() {
        JPanel amigosPanel = new JPanel();
        amigosPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Lista de Amigos", JLabel.CENTER);
        amigosPanel.add(label, BorderLayout.CENTER);

        return amigosPanel;
    }
}
