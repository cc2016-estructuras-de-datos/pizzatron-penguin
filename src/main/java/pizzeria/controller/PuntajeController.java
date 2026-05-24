package pizzeria.controller;

/**
 * Lleva el marcador completo del juego.
 * Es el único lugar donde se modifican monedas, errores y tiempo global.
 */
public class PuntajeController {

    private int pizzasCompletadas;
    private int pizzasRestantes;
    private int errores;
    private int monedas;
    private int tiempoRestante;

    private static final int PENALIZACION_ERROR = 10;
    private static final int MAXIMO_ERRORES = 5;

    public PuntajeController(int totalPizzas, int tiempoInicial) {
        this.pizzasCompletadas = 0;
        this.pizzasRestantes = totalPizzas;
        this.errores = 0;
        this.monedas = 0;
        this.tiempoRestante = tiempoInicial;
    }

    /**
     * Registra una pizza completada correctamente.
     * Suma la recompensa de la receta y actualiza contadores.
     *
     * @param recompensa monedas que otorga la receta completada
     */
    public void registrarExito(int recompensa) {
        pizzasCompletadas++;
        pizzasRestantes = Math.max(0, pizzasRestantes - 1);
        monedas += recompensa;
    }

    /**
     * Registra un error: pizza incorrecta o timer de orden agotado.
     * Aplica penalización de monedas sin bajar de cero.
     */
    public void registrarError() {
        errores++;
        monedas = Math.max(0, monedas - PENALIZACION_ERROR);
    }

    /**
     * Descuenta un segundo del timer global del juego.
     * JuegoController llama este método en cada tick.
     */
    public void tick() {
        if (tiempoRestante > 0) {
            tiempoRestante--;
        }
    }

    /**
     * El juego termina si se agota el tiempo global, se completaron
     * todas las pizzas, o se alcanzó el máximo de errores permitidos.
     *
     * @return true si el juego debe terminar
     */
    public boolean isGameOver() {
        return tiempoRestante <= 0
            || pizzasRestantes <= 0
            || errores >= MAXIMO_ERRORES;
    }

    /**
     * Genera un snapshot del estado actual del puntaje.
     * La vista siempre usa este método para leer el marcador.
     *
     * @return PuntajeSnapshot con el estado actual
     */
    public PuntajeSnapshot getPuntaje() {
        return new PuntajeSnapshot(
            pizzasCompletadas,
            pizzasRestantes,
            errores,
            monedas,
            tiempoRestante
        );
    }

    public int getMaximoErrores() {
        return MAXIMO_ERRORES;
    }
}
