package pizzeria.util;

import pizzeria.controller.CajaController;
import pizzeria.model.Receta;

import java.util.List;
import java.util.Random;

/**
 * Genera órdenes aleatorias para el juego.
 *
 * Está separado de JuegoController para mantener responsabilidad única:
 * JuegoController orquesta, GeneradorOrdenes genera.
 * Si en el futuro se quiere dificultad progresiva, patrones de órdenes
 * o niveles, solo se modifica esta clase sin tocar el orquestador.
 */
public class GeneradorOrdenes {

    private final List<Receta> recetas;
    private final CajaController caja;
    private final Random random;

    private int tiempoPorOrden;
    private static final int TIEMPO_MINIMO = 20;

    public GeneradorOrdenes(List<Receta> recetas, CajaController caja, int tiempoPorOrden) {
        this.recetas = recetas;
        this.caja = caja;
        this.random = new Random();
        this.tiempoPorOrden = tiempoPorOrden;
    }

    /**
     * Genera y encola una orden aleatoria.
     *
     * @return true si la orden fue generada y encolada exitosamente
     */
    public boolean generarOrden() {
        if (recetas.isEmpty()) return false;

        Receta receta = recetas.get(random.nextInt(recetas.size()));
        caja.recibirOrden(receta, tiempoPorOrden);
        return true;
    }

    /**
     * Genera una orden de una receta específica.
     *
     * @param receta receta específica a generar
     * @return true si la orden fue encolada exitosamente
     */
    public boolean generarOrdenEspecifica(Receta receta) {
        if (receta == null) return false;
        caja.recibirOrden(receta, tiempoPorOrden);
        return true;
    }

    /**
     * Aumenta la dificultad reduciendo el tiempo disponible por orden.
     * No baja del mínimo definido para que siempre sea posible completarla.
     *
     * @param reduccion segundos a reducir del tiempo por orden
     */
    public void aumentarDificultad(int reduccion) {
        tiempoPorOrden = Math.max(TIEMPO_MINIMO, tiempoPorOrden - reduccion);
    }

    public int getTiempoPorOrden() {
        return tiempoPorOrden;
    }
}
