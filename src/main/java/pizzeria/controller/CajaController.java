package pizzeria.controller;

import pizzeria.model.Orden;
import pizzeria.model.Pizza;
import pizzeria.model.Receta;
import pizzeria.model.enums.EstadoOrden;
import pizzeria.model.enums.EstadoPizza;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Punto de entrada de las órdenes y responsable del cierre de las mismas.
 *
 * Usa una Queue<Orden> (FIFO) para garantizar que las órdenes se
 * atiendan en el orden en que llegaron.
 *
 * Responsabilidades:
 *   - Recibir y validar nuevas órdenes antes de encolarlas
 *   - Despachar la siguiente orden a CocinaController cuando esté libre
 *   - Cerrar la orden (COMPLETADA o FALLIDA) y notificar a PuntajeController
 *
 * Es el único controlador que toca PuntajeController al cierre de una orden:
 * cocina cocina, caja entrega y contabiliza.
 */
public class CajaController {

    private final Queue<Orden> colaOrdenes;
    private final CocinaController cocina;
    private final PuntajeController puntaje;
    private Orden ordenActiva;

    public CajaController(CocinaController cocina, PuntajeController puntaje) {
        this.colaOrdenes = new LinkedList<>();
        this.cocina = cocina;
        this.puntaje = puntaje;
        this.ordenActiva = null;
    }

    /**
     * Recibe una nueva orden, la valida y la encola si es procesable.
     *
     * @param receta       receta que debe cumplirse
     * @param tiempoLimite segundos disponibles para completar la orden
     * @return la orden creada
     */
    public Orden recibirOrden(Receta receta, int tiempoLimite) {
        Orden orden = new Orden(receta, tiempoLimite);
        if (orden.validar()) {
            orden.setEstado(EstadoOrden.EN_COLA);
            colaOrdenes.add(orden);
        }
        return orden;
    }

    /**
     * Despacha la siguiente orden de la cola a la cocina si está libre.
     */
    public void procesarSiguienteOrden() {
        if (!cocina.estaLibre() || colaOrdenes.isEmpty()) return;

        ordenActiva = colaOrdenes.poll();
        ordenActiva.setEstado(EstadoOrden.PROCESANDO);
        cocina.iniciarPreparacion(ordenActiva.getReceta());
    }

    /**
     * Cierra la orden activa una vez que el usuario presionó hornear.
     * Recibe la pizza ya construida y horneada por CocinaController.
     *
     * Este es el traspaso de responsabilidad entre cocina y caja:
     * la cocina hornea, la caja valida el resultado y contabiliza.
     *
     * @param pizza la pizza horneada por CocinaController
     */
    public void cerrarOrdenActiva(Pizza pizza) {
        if (ordenActiva == null) return;

        boolean exitosa = cocina.validarPizzaContraReceta(pizza);
        cocina.liberar();

        if (exitosa) {
            pizza.setEstado(EstadoPizza.ENTREGADA);
            ordenActiva.setEstado(EstadoOrden.COMPLETADA);
            puntaje.registrarExito(ordenActiva.getRecompensa());
        } else {
            pizza.setEstado(EstadoPizza.CANCELADA);
            ordenActiva.setEstado(EstadoOrden.FALLIDA);
            puntaje.registrarError();
        }

        ordenActiva = null;
    }

    /**
     * Cancela la orden activa por timer agotado.
     */
    public void cancelarOrdenActiva() {
        if (ordenActiva == null) return;

        if (!cocina.estaLibre()) {
            cocina.liberar();
        }

        ordenActiva.setEstado(EstadoOrden.FALLIDA);
        puntaje.registrarError();
        ordenActiva = null;
    }

    /**
     * Cancela todas las órdenes en cola. Usado al terminar el juego.
     */
    public void vaciarCola() {
        while (!colaOrdenes.isEmpty()) {
            colaOrdenes.poll().setEstado(EstadoOrden.FALLIDA);
        }
    }

    public boolean hayOrdenesPendientes() {
        return !colaOrdenes.isEmpty();
    }

    public int getTamañoCola() {
        return colaOrdenes.size();
    }

    public Orden getOrdenActiva() {
        return ordenActiva;
    }

    public List<Orden> getOrdenesEnCola() {
        return new ArrayList<>(colaOrdenes);
    }
}
