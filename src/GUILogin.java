import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class GUILogin {
    private JDialog dialog;
    private JPanel panel1;
    private JTextField CUsuario;
    private JLabel Contraseña;
    private JLabel Usuario;
    private JPasswordField passwordField1;
    private JButton entrarButton;
    private JButton registrarButton;
    private String usuario;
    private String contrasenha;

    // Constante de intentos máximos de login antes de que se cierre la aplicación
    private int intentos = 3;

    public GUILogin(UserController controller) {
        dialog = new JDialog();
        dialog.setTitle("P2P - ChatApp");
        dialog.setModal(true); // Hace que el diálogo sea modal

        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuario = CUsuario.getText();
                contrasenha = new String(passwordField1.getPassword());

                if (!controller.iniciarSesion(usuario, contrasenha)) {
                    intentos--;
                    if (intentos > 0) {
                        JOptionPane.showMessageDialog(dialog, "Usuario no encontrado o contraseña incorrecta. Intentos restantes: " + intentos, "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                        CUsuario.setText("");
                        passwordField1.setText("");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Se agotaron los intentos. La aplicación se cerrará.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                } else {
                    dialog.setVisible(false); // Cierra el diálogo
                    dialog.dispose();
                }
            }
        });

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuario = CUsuario.getText();
                contrasenha = new String(passwordField1.getPassword());

                if (!controller.registrarse(usuario, contrasenha)) {
                    JOptionPane.showMessageDialog(dialog, "El usuario ya existe.", "Error de registro", JOptionPane.ERROR_MESSAGE);
                    CUsuario.setText("");
                    passwordField1.setText("");
                } else {
                    dialog.setVisible(false); // Cierra el diálogo
                    dialog.dispose();                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        dialog.getContentPane().add(panel1);
        dialog.pack();
        dialog.setVisible(true);
    }

}
