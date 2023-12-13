import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    private JPanel panelListaSolicitudes;


    public GUIChat(UserController controller) throws RemoteException {
        controller.setvChat(this);

        nombres = new ArrayList<>();

        frame = new JFrame("P2P ChatApp - " + controller.getNombreUsuario());
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
        splitPane.setLeftComponent(createNombresPanel(controller));
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
        mainPanel.add(createListaSolicitudes(controller), "ListaNombresPanel");
        mainPanel.add(createAmigosPanel(controller), "AmigosPanel");

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        chatMenuItem.addActionListener(e -> cambiarVista("ChatPanel"));
        listaNombresMenuItem.addActionListener(e -> {
            try {
                actualizarListaSolicitudes(controller);
                cambiarVista("ListaNombresPanel");
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        amigosMenuItem.addActionListener(e -> {
            try {
                actualizarListaAmigos(controller); // Actualizar la lista de amigos
                cambiarVista("AmigosPanel"); // Cambiar la vista para mostrar el panel actualizado
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
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

        sendButton.addActionListener(e -> sendMessage(controller, nombresList.getSelectedValue(), inputField.getText()));
        inputField.addActionListener(e -> sendMessage(controller, nombresList.getSelectedValue(), inputField.getText()));

        // Si se cierra la ventana se cierra la app
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }


    private JPanel createNombresPanel(UserController controller) {
        JPanel nombresPanel = new JPanel(new BorderLayout());
        nombresList = new JList<>();
        actualizarListaNombres(controller); // Llamada para llenar inicialmente la lista

        JScrollPane nombresScrollPane = new JScrollPane(nombresList);
        nombresPanel.add(nombresScrollPane, BorderLayout.CENTER);
        return nombresPanel;
    }

    private void actualizarListaNombres(UserController controller) {
        ArrayList<Usuario> usuariosEnLinea = controller.getUser().getAmigosConectados(); // Método para obtener los nombres de los usuarios en línea
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Usuario usuario : usuariosEnLinea) {
            model.addElement(usuario.getUsername());
        }
        nombresList.setModel(model); // Actualizar el modelo de la lista
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

    private void sendMessage(UserController controller, String destinatario, String mensaje) {
        if (mensaje.isEmpty() || destinatario == null) {
            return; // No enviar mensajes vacíos o si no hay destinatario seleccionado
        }

        try {
            System.out.println("Mensaje de " + destinatario + ": " + mensaje);
            // Envía el mensaje al destinatario a través del UserController
            if (controller.enviarMensaje(destinatario, mensaje)) {
                // Actualiza la interfaz de usuario con el mensaje enviado
                String chatHistory = mensajesPorUsuario.getOrDefault(destinatario, "");
                String messaje = controller.getNombreUsuario() + ": " + mensaje + "\n";

                chatHistory += messaje;
                mensajesPorUsuario.put(destinatario, chatHistory);
                chatArea.setText(chatHistory);
            } else {
                // Manejar el caso en que el mensaje no se pudo enviar
                System.out.println("Error al enviar el mensaje.");
            }

            // Limpia el campo de texto de entrada
            inputField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar error de comunicación aquí
        }
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
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, nombreVista);
    }

    private JPanel createListaSolicitudes(UserController controller) throws RemoteException {
        panelListaSolicitudes = new JPanel();
        panelListaSolicitudes.setLayout(new BoxLayout(panelListaSolicitudes, BoxLayout.Y_AXIS));

        actualizarListaSolicitudes(controller);

        return panelListaSolicitudes;
    }

    private void actualizarListaSolicitudes(UserController controller) throws RemoteException {
        panelListaSolicitudes.removeAll(); // Elimina todos los componentes actuales

        // Obtener las solicitudes pendientes de amistad
        //ArrayList<String> nombres = controller.getServer().obtenerSolicitudes(controller.getNombreUsuario());
        ArrayList<String> nombres = controller.obtenerSolicitudesPendientes();
        if (nombres != null)
            // Crear un panel para cada nombre en la lista de solicitudes
            for (String nombre : nombres) {
                JPanel nombrePanel = new JPanel(new FlowLayout());
                JLabel nombreLabel = new JLabel(nombre);
                JButton aceptarButton = new JButton("Aceptar");
                JButton rechazarButton = new JButton("Rechazar");

                aceptarButton.addActionListener(e -> {
                    try {
                        // Aceptar la solicitud de amistad y actualizar la lista
                        controller.getServer().aceptarAmistad(controller.getUser(), nombre);
                        actualizarListaSolicitudes(controller);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });

                rechazarButton.addActionListener(e -> {
                    // Rechazar la solicitud de amistad y actualizar la lista
                    try {
                        controller.getServer().rechazarAmistad(controller.getNombreUsuario(), nombre);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        actualizarListaSolicitudes(controller);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });

                // Añadir los componentes al panel de nombre
                nombrePanel.add(nombreLabel);
                nombrePanel.add(aceptarButton);
                nombrePanel.add(rechazarButton);

                // Añadir el panel de nombre al panel de lista de solicitudes
                panelListaSolicitudes.add(nombrePanel);
            }

        // Actualizar y refrescar el panel de lista de solicitudes
        panelListaSolicitudes.revalidate();
        panelListaSolicitudes.repaint();
    }

    // Método para crear el panel de amigos
    private JPanel createAmigosPanel(UserController controller) throws RemoteException {
        JPanel amigosPanel = new JPanel();
        amigosPanel.setLayout(new BoxLayout(amigosPanel, BoxLayout.Y_AXIS));

        // Obtiene la lista de amigos del usuario
        ArrayList<String> amigos = controller.getServer().obtenerAmistades(controller.getNombreUsuario());


        if (amigos != null && !amigos.isEmpty()) {
            for (String amigo : amigos) {
                // Crear panel para cada amigo
                JPanel amigoPanel = new JPanel();
                amigoPanel.setLayout(new FlowLayout());

                JLabel nombreAmigo = new JLabel(amigo);
                JButton eliminarAmigoBtn = new JButton("Eliminar Amigo");

                // Acción para eliminar amigo
                eliminarAmigoBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (controller.eliminarAmigo(amigo)) {
                                // Actualizar la lista de amigos del usuario
                                controller.getUser().eliminarAmigo(amigo);

                                // Actualizar la GUI
                                actualizarListaAmigos(controller);
                            }
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                amigoPanel.add(nombreAmigo);
                amigoPanel.add(eliminarAmigoBtn);
                amigosPanel.add(amigoPanel);

            }
        } else {
            // Muestra un mensaje si no hay amigos
            JLabel label = new JLabel("No hay amigos en la lista", JLabel.CENTER);
            amigosPanel.add(label);
        }

        // Agregar un JScrollPane alrededor del panel de amigos para manejar el desplazamiento
        JScrollPane scrollPane = new JScrollPane(amigosPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Crear un panel contenedor para agregar el JScrollPane
        JPanel contenedorPanel = new JPanel(new BorderLayout());
        contenedorPanel.add(scrollPane, BorderLayout.CENTER);

        return contenedorPanel;
    }


    // Método para pedir amistad
    private boolean pedirAmistad(UserController controller) {
        // Mostrar un cuadro de diálogo para ingresar el nombre
        String nombreIngresado = JOptionPane.showInputDialog(frame, "Ingresa el nombre para la solicitud de amistad:");
        if (nombreIngresado != null && !nombreIngresado.trim().isEmpty()) {
            // Lógica adicional con el nombre ingresado
            controller.pedirAmistad(nombreIngresado);
            return true;
        } else {
            return false;
        }
    }

    public void actualizarMensajes(String sender, String message) {
        System.out.println("Mensaje de " + sender + ": " + message);
        if (sender != null) {
            String chatHistory = mensajesPorUsuario.getOrDefault(sender, "");
            chatHistory += sender + ": " + message + "\n";
            mensajesPorUsuario.put(sender, chatHistory);
            chatArea.setText(chatHistory);
        }
    }
    private void actualizarListaAmigos(UserController controller) throws RemoteException {
        // Obtiene la lista actualizada de amigos
        ArrayList<String> amigos = controller.getUser().getAmigos();

        // Limpia el panel actual
        JPanel amigosPanel = (JPanel) mainPanel.getComponent(1); // Asumiendo que es el segundo componente en mainPanel
        amigosPanel.removeAll();

        // Reconstruye la lista de amigos
        for (String amigo : amigos) {
            JPanel amigoPanel = new JPanel(new FlowLayout());
            JLabel nombreAmigo = new JLabel(amigo);
            JButton eliminarAmigoBtn = new JButton("Eliminar Amigo");

            eliminarAmigoBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (controller.eliminarAmigo(amigo)) {
                            // Actualizar la lista de amigos del usuario
                            controller.getUser().eliminarAmigo(amigo);

                            // Actualizar la GUI
                            actualizarListaAmigos(controller);
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            amigoPanel.add(nombreAmigo);
            amigoPanel.add(eliminarAmigoBtn);
            amigosPanel.add(amigoPanel);
        }

        // Refresca la vista
        amigosPanel.revalidate();
        amigosPanel.repaint();
    }
    public void actualizarListaAmigos() {

    }
}
