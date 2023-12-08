import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class GUILogin {
    private JFrame frame;
    private JPanel panel1;
    private JTextField CUsuario;
    private JLabel Contraseña;
    private JLabel Usuaro;
    private JPasswordField passwordField1;
    private JButton entrarButton;
    private JButton registrarButton;
    private String usuario;
    private String contrasenha;

    // Constante de intentos máximos de login antes de que se cierre la aplicación
    private int intentos = 3;

    public GUILogin(CallbackServerInterface servidor, CallbackClientInterface cliente) {

        frame = new JFrame("CHATAPP"); // Inicializa el JFrame

        entrarButton.addActionListener(new ActionListener() {

            // Logging usuario
            @Override
            public void actionPerformed(ActionEvent e) {

                // Guardamos usuario y contraseña
                usuario = CUsuario.getText();
                contrasenha = new String(passwordField1.getPassword());

                // Comprobamos si existe el usuario en la base de datos del servidor
                System.out.println("Usuario: " + usuario + "\nContraseña: " + contrasenha);


                // Si no existe sacamos una ventana emergente
                if (!servidor.iniciarSesion(cliente, usuario, contrasenha)) {

                    // Disminuimos los intentos
                    intentos--;

                    // Calcular los intentos restantes
                    if (intentos > 0) {
                        // Si hay intentos restantes, mostrar el mensaje con el número de intentos restantes
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado o contraseña incorrecta. Intentos restantes: " + intentos, "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);

                        // Borrar el contenido de los campos
                        CUsuario.setText("");
                        passwordField1.setText("");
                    } else {
                        // Si se agotaron los intentos, mostrar un mensaje y cerrar la aplicación
                        JOptionPane.showMessageDialog(null, "Se agotaron los intentos. La aplicación se cerrará.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                        // Cerrar la aplicación
                        System.exit(0);
                    }
                } else { // Si el Logging se hizo satisfactoriamente

                    // logica de la continuacion del logging
                    frame.setVisible(false); // Oculta la ventana de login
                    new GUIChat(); // Abre la nueva GUI
                }
            }
        });


        // Registrar usuario
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Guardamos usuario y contraseña
                usuario = CUsuario.getText();
                contrasenha = new String(passwordField1.getPassword());

                // Comprobamos si existe el usuario en la base de datos del servidor
                System.out.println("Usuario: " + usuario + "\nContraseña: " + contrasenha);


                try {
                    // Si el usuario ya existe en la base de datos sacamos una ventana emergente
                    if (!servidor.registrarCliente(cliente, usuario, contrasenha)) {
                        JOptionPane.showMessageDialog(null, "El usuario ya existe.", "Error de registro", JOptionPane.ERROR_MESSAGE);

                        // Borrar el contenido de los campos
                        CUsuario.setText("");
                        passwordField1.setText("");
                    } else { // Si el registro se hizo correctamente
                        // Logica de la continuacion del registro
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Agrega un WindowListener para detectar el cierre de la ventana
        JFrame frame = new JFrame("CHATAPP");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Llama a la función que de offlineCliente antes de cerrar la aplicación
                try {
                    GUIcierre(servidor, cliente, usuario, contrasenha);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Configurar el cierre de la ventana
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Agregar el panel de GUILogin a la ventana
        frame.getContentPane().add(panel1);

        // Configurar el tamaño de la ventana
        frame.pack();

        // Hacer visible la ventana
        frame.setVisible(true);
    }

    // La función que deseas ejecutar antes de cerrar la aplicación
    private void GUIcierre(CallbackServerInterface servidor, CallbackClientInterface cliente, String usuario, String contraseña) throws RemoteException {

        if (servidor != null && usuario != null && contraseña != null) {
            // Desconectamos al usuario si se cierra la app
            servidor.cerrarSesion(cliente, usuario);
        }

        // Cerramos la app
        System.exit(0);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
