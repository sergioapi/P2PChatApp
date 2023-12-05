import java.time.LocalDateTime;

public class Mensaje {

    // Usuario que recibe o al que se le envia el mensaje
    private String destino;

    // Usuario que envia el mensaje
    private String origen;

    // Contenido del mensaje
    private String mensaje;

    // Fecha y hora a la que se envió/recibió el mensaje
    private LocalDateTime fechaHora;

    private boolean leido;

    public Mensaje(String destino, String origen, String mensaje) {
        this.destino = destino;
        this.origen = origen;
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
    }

    public String getDestino() {
        return destino;
    }

    public String getOrigen() {
        return origen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + origen + ": " + mensaje;
    }
}
