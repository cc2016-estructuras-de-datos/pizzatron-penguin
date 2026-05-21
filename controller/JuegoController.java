package pizzeria.controller;

import pizzeria.model.Orden;
import pizzeria.model.Pizza;
import pizzeria.model.Receta;
import pizzeria.model.enums.EstadoJuego;
import pizzeria.model.ingredient.Ingrediente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Orquestador principal del juego.
 *
 * Es el único punto de contacto que la vista necesita conocer.
 * Coordina todos los controladores y gestiona el ciclo principal del juego.
 *
 * La vista llama los métodos de acción del usuario:
 *   - agregarIngrediente()
 *   - deshacerIngrediente()
 *   - hornear()
 */
public class JuegoController {

    private final CocinaController cocina;
    private final PuntajeController puntaje;
    private final CajaController caja;
    private final InventarioController inventario;
    private GeneradorOrdenes generador;

    private final List<Receta> recetas;
    private EstadoJuego estado;

    private static final int TIEMPO_GLOBAL = 180;
    private static final int TOTAL_PIZZAS = 20;
    private static final int TIEMPO_POR_ORDEN = 60;
    private static final int INTERVALO_NUEVA_ORDEN = 15;

    private int ticksDesdeUltimaOrden;
    private final Random random;

    public JuegoController() {
        this.cocina = new CocinaController();
        this.puntaje = new PuntajeController(TOTAL_PIZZAS, TIEMPO_GLOBAL);
        this.caja = new CajaController(cocina, puntaje);
        this.inventario = new InventarioController();
        this.recetas = new ArrayList<>();
        this.estado = EstadoJuego.MENU;
        this.ticksDesdeUltimaOrden = 0;
        this.random = new Random();
    }

    /**
     * Inicializa y arranca el juego.
     */
    public void iniciarJuego() {
        RecetaFactory.todasLasRecetas().forEach(this::agregarReceta);
        this.generador = new GeneradorOrdenes(recetas, caja, TIEMPO_POR_ORDEN);
        estado = EstadoJuego.JUGANDO;
        ticksDesdeUltimaOrden = INTERVALO_NUEVA_ORDEN;
        tick();
    }

    public void pausarJuego() {
        if (estado == EstadoJuego.JUGANDO) estado = EstadoJuego.PAUSADO;
        else if (estado == EstadoJuego.PAUSADO) estado = EstadoJuego.JUGANDO;
    }

    public void terminarJuego() {
        estado = EstadoJuego.GAME_OVER;
        caja.vaciarCola();
    }

    /**
     * Ciclo principal del juego. El Timeline de JavaFX llama este
     * método cada segundo mientras el estado sea JUGANDO.
     *
     * Orden de operaciones por tick:
     *   1. Descontar tiempo global
     *   2. Descontar timers de órdenes activas
     *   3. Cancelar órdenes con timer agotado
     *   4. Generar nueva orden si corresponde
     *   5. Despachar siguiente orden a cocina si está libre
     *   6. Verificar game over
     */
    public void tick() {
        if (estado != EstadoJuego.JUGANDO) return;

        puntaje.tick();
        ticksDesdeUltimaOrden++;

        Orden ordenActiva = caja.getOrdenActiva();
        if (ordenActiva != null) {
            ordenActiva.tick();
            if (ordenActiva.tiempoAgotado()) {
                caja.cancelarOrdenActiva();
            }
        }

        for (Orden orden : caja.getOrdenesEnCola()) {
            orden.tick();
        }

        if (ticksDesdeUltimaOrden >= INTERVALO_NUEVA_ORDEN) {
            generador.generarOrden();
            ticksDesdeUltimaOrden = 0;
        }

        caja.procesarSiguienteOrden();

        if (puntaje.isGameOver()) {
            terminarJuego();
        }
    }

    /**
     * El usuario hace click en un ingrediente de la banda.
     * Verifica disponibilidad en inventario antes de agregarlo.
     * La cocina se encarga de validar el orden (base → salsa → topping).
     *
     * @param nombreIngrediente nombre del ingrediente clickeado
     * @return true si se agregó correctamente
     */
    public boolean agregarIngrediente(String nombreIngrediente) {
        if (estado != EstadoJuego.JUGANDO) return false;
        if (!inventario.hayDisponibilidad(nombreIngrediente)) return false;

        Ingrediente ing = inventario.getIngrediente(nombreIngrediente);
        boolean agregado = cocina.agregarIngrediente(ing);

        if (agregado) {
            inventario.reducirStock(nombreIngrediente);
        }

        return agregado;
    }

    /**
     * El usuario presiona el botón Deshacer.
     * Retira el último ingrediente de la pila y devuelve stock al inventario.
     *
     * @return true si se deshizo algún ingrediente
     */
    public boolean deshacerIngrediente() {
        if (estado != EstadoJuego.JUGANDO) return false;

        Ingrediente retirado = cocina.retirarUltimoIngrediente();
        if (retirado != null) {
            inventario.reponerStock(retirado.getNombre(), 1);
            return true;
        }
        return false;
    }

    /**
     * El usuario presiona el botón Hornear.
     * CocinaController construye la pizza desde la pila y la hornea.
     * CajaController recibe la pizza y cierra la orden.
     *
     * @return true si se horneó correctamente
     */
    public boolean hornear() {
        if (estado != EstadoJuego.JUGANDO) return false;

        try {
            Pizza pizza = cocina.hornear();
            caja.cerrarOrdenActiva(pizza);
            caja.procesarSiguienteOrden();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Genera una orden aleatoria a partir de las recetas disponibles
     * y la encola en CajaController.
     */
    public void generarOrdenAleatoria() {
        if (recetas.isEmpty()) return;
        Receta receta = recetas.get(random.nextInt(recetas.size()));
        caja.recibirOrden(receta, TIEMPO_POR_ORDEN);
    }

    // Registro de recetas

    public void agregarReceta(Receta receta) {
        recetas.add(receta);
    }

    // Getters para la vista

    public EstadoJuego getEstado() {
        return estado;
    }

    public PuntajeSnapshot getPuntaje() {
        return puntaje.getPuntaje();
    }

    public CocinaController getCocina() {
        return cocina;
    }

    public CajaController getCaja() {
        return caja;
    }

    public InventarioController getInventario() {
        return inventario;
    }

    public List<Receta> getRecetas() {
        return new ArrayList<>(recetas);
    }
}
