package pizzeria.model;

import pizzeria.interfaces.IValidable;
import pizzeria.model.enums.EstadoOrden;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa el pedido que llega a la pizzería.
 * Contiene la pizza a preparar y la receta que debe cumplirse.
 *
 * Implementa IValidable para verificar que sea procesable antes
 * de entrar a la cola de CajaController.
 */
public class Orden implements IValidable {

    private final UUID id;
    private final int numeroOrden;
    private final Pizza pizza;
    private final Receta receta;
    private EstadoOrden estado;
    private final LocalDateTime timestamp;
    private int tiempoRestante;

    private static int contadorOrdenes = 0;

    public Orden(Pizza pizza, Receta receta, int tiempoLimite) {
        this.id = UUID.randomUUID();
        this.numeroOrden = ++contadorOrdenes;
        this.pizza = pizza;
        this.receta = receta;
        this.estado = EstadoOrden.RECIBIDA;
        this.timestamp = LocalDateTime.now();
        this.tiempoRestante = tiempoLimite;
    }

    /**
     * Descuenta un segundo del timer individual de esta orden.
     * JuegoController llama este método en cada tick del juego.
     */
    public void tick() {
        if (tiempoRestante > 0) {
            tiempoRestante--;
        }
    }

    public boolean tiempoAgotado() {
        return tiempoRestante <= 0;
    }

    //IValidable

    @Override
    public boolean validar() {
        return getErrores().isEmpty();
    }

    @Override
    public List<String> getErrores() {
        List<String> errores = new ArrayList<>();

        if (receta == null) {
            errores.add("La orden no tiene receta asociada.");
        }

        if (pizza == null) {
            errores.add("La orden no tiene pizza asociada.");
        } else {
            errores.addAll(pizza.getErrores());
        }

        return errores;
    }

    public UUID getId() {
        return id;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public Receta getReceta() {
        return receta;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public int getRecompensa() {
        return receta != null ? receta.getRecompensaMonedas() : 0;
    }

    @Override
    public String toString() {
        return "Orden{#" + numeroOrden + ", receta='" +
               (receta != null ? receta.getNombre() : "sin receta") +
               "', estado=" + estado + ", tiempo=" + tiempoRestante + "s}";
    }
}
