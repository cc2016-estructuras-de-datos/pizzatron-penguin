package pizzeria.controller;

/**
 * Objeto inmutable que encapsula el estado del puntaje en un momento dado.
 * PuntajeController lo genera bajo demanda para que la vista lea
 * el estado sin poder modificarlo directamente.
 */
public class PuntajeSnapshot {

    public final int pizzasCompletadas;
    public final int pizzasRestantes;
    public final int errores;
    public final int monedas;
    public final int tiempoRestante;

    public PuntajeSnapshot(int pizzasCompletadas, int pizzasRestantes,
                           int errores, int monedas, int tiempoRestante) {
        this.pizzasCompletadas = pizzasCompletadas;
        this.pizzasRestantes = pizzasRestantes;
        this.errores = errores;
        this.monedas = monedas;
        this.tiempoRestante = tiempoRestante;
    }

    @Override
    public String toString() {
        return "Puntaje{completadas=" + pizzasCompletadas +
               ", restantes=" + pizzasRestantes +
               ", errores=" + errores +
               ", monedas=" + monedas +
               ", tiempo=" + tiempoRestante + "s}";
    }
}
