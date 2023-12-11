import java.time.LocalDateTime;

public class Mensaje {

    // Usuario que recibe o al que se le envía el mensaje
    private String destino;

    // Usuario que envía el mensaje
    private String origen;

    // Contenido del mensaje
    private String mensaje;

    // Fecha y hora a la que se envió/recibió el mensaje
    private LocalDateTime fechaHora;

    // Estado que indica si el mensaje ha sido leído
    private boolean leido;

    // Constructor para crear un nuevo mensaje con destino, origen y contenido
    public Mensaje(String destino, String origen, String mensaje) {
        this.destino = destino;
        this.origen = origen;
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
    }

    // Método para obtener el destinatario del mensaje
    public String getDestino() {
        return destino;
    }

    // Método para obtener el remitente del mensaje
    public String getOrigen() {
        return origen;
    }

    // Método para obtener el contenido del mensaje
    public String getMensaje() {
        return mensaje;
    }

    // Método para obtener la fecha y hora del mensaje
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    // Método para verificar si el mensaje ha sido leído
    public boolean isLeido() {
        return leido;
    }

    // Método para establecer el estado de lectura del mensaje
    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    // Método toString para representar el mensaje como una cadena
    @Override
    public String toString() {
        return "[" + fechaHora + "] " + origen + ": " + mensaje;
    }
}
