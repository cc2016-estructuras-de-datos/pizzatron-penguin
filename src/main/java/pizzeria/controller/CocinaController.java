package pizzeria.controller;

import pizzeria.model.Pizza;
import pizzeria.model.Receta;
import pizzeria.model.enums.EstadoCocina;
import pizzeria.model.enums.EstadoPizza;
import pizzeria.model.ingredient.Base;
import pizzeria.model.ingredient.Ingrediente;
import pizzeria.model.ingredient.Salsa;
import pizzeria.model.ingredient.Topping;

import java.util.Stack;

/**
 * Gestiona la preparación física de la pizza.
 *
 * El usuario construye la pizza desde cero: primero elige la base,
 * luego la salsa, luego los toppings. El orden es forzado:
 *   1. Base  (obligatorio primero)
 *   2. Salsa (obligatorio segundo)
 *   3. Toppings (uno o más)
 *
 * Usa una Stack<Ingrediente> para acumular lo que el usuario va
 * clickeando, lo que permite deshacer el último ingrediente agregado
 * mediante un simple pop.
 *
 * Al hornear, construye el objeto Pizza desde la pila y lo valida
 * contra la receta activa.
 */
public class CocinaController {

    private final Stack<Ingrediente> pilaPreparacion;
    private Receta recetaActual;
    private EstadoCocina estado;

    public CocinaController() {
        this.pilaPreparacion = new Stack<>();
        this.estado = EstadoCocina.LIBRE;
    }

    /**
     * Inicia la preparación de una nueva orden.
     * Solo puede iniciarse si la cocina está LIBRE.
     *
     * @param receta la receta que el usuario debe cumplir
     * @throws IllegalStateException si la cocina está ocupada
     */
    public void iniciarPreparacion(Receta receta) {
        if (estado != EstadoCocina.LIBRE) {
            throw new IllegalStateException("La cocina está ocupada. Estado: " + estado);
        }
        recetaActual = receta;
        pilaPreparacion.clear();
        estado = EstadoCocina.PREPARANDO;
    }

    /**
     * Agrega un ingrediente a la pila respetando el orden forzado:
     * Base -> Salsa -> Toppings.
     *
     * Reglas:
     *   - Si la pila está vacía, solo acepta Base
     *   - Si hay Base pero no Salsa, solo acepta Salsa
     *   - Si hay Base y Salsa, solo acepta Toppings
     *   - Solo puede haber una Base y una Salsa
     *
     * @param ingrediente el ingrediente a agregar
     * @return true si se agregó, false si viola el orden
     * @throws IllegalStateException si la cocina no está en PREPARANDO
     */
    public boolean agregarIngrediente(Ingrediente ingrediente) {
        if (estado != EstadoCocina.PREPARANDO) {
            throw new IllegalStateException("No se puede agregar ingredientes. Estado: " + estado);
        }

        if (ingrediente instanceof Base) {
            if (tieneBase()) return false;
            pilaPreparacion.push(ingrediente);
            return true;
        }

        if (ingrediente instanceof Salsa) {
            if (!tieneBase() || tieneSalsa()) return false;
            pilaPreparacion.push(ingrediente);
            return true;
        }

        if (ingrediente instanceof Topping) {
            if (!tieneBase() || !tieneSalsa()) return false;
            pilaPreparacion.push(ingrediente);
            return true;
        }

        return false;
    }

    /**
     * Deshace el último ingrediente agregado (pop de la pila).
     * Permite al usuario corregir errores antes de hornear.
     *
     * @return el ingrediente removido, o null si la pila está vacía
     */
    public Ingrediente retirarUltimoIngrediente() {
        if (estado != EstadoCocina.PREPARANDO || pilaPreparacion.isEmpty()) {
            return null;
        }
        return pilaPreparacion.pop();
    }

    /**
     * Construye el objeto Pizza desde la pila y lo hornea.
     * Requiere que haya al menos Base y Salsa en la pila.
     *
     * @return la pizza construida y horneada
     * @throws IllegalStateException si la cocina no está en PREPARANDO
     *                               o si faltan base o salsa
     */
    public Pizza hornear() {
        if (estado != EstadoCocina.PREPARANDO) {
            throw new IllegalStateException("No se puede hornear. Estado: " + estado);
        }
        if (!tieneBase() || !tieneSalsa()) {
            throw new IllegalStateException("La pizza necesita al menos base y salsa para hornear.");
        }

        estado = EstadoCocina.HORNEANDO;

        Base base = null;
        Salsa salsa = null;
        Stack<Topping> toppings = new Stack<>();

        for (Ingrediente ing : pilaPreparacion) {
            if (ing instanceof Base) base = (Base) ing;
            else if (ing instanceof Salsa) salsa = (Salsa) ing;
            else if (ing instanceof Topping) toppings.push((Topping) ing);
        }

        Pizza pizza = new Pizza(recetaActual.getNombre(), base, salsa);
        pizza.setEstado(EstadoPizza.EN_PREPARACION);
        toppings.forEach(pizza::agregarTopping);
        pizza.cocinar();

        return pizza;
    }

    /**
     * Verifica si la pizza horneada cumple con la receta activa.
     *
     * @param pizza la pizza recién horneada
     * @return true si cumple la receta
     */
    public boolean validarPizzaContraReceta(Pizza pizza) {
        if (pizza == null || recetaActual == null) return false;
        return recetaActual.cumpleReceta(pizza);
    }

    /**
     * Libera la cocina para la siguiente orden.
     * CajaController llama este método después de procesar el resultado.
     */
    public void liberar() {
        pilaPreparacion.clear();
        recetaActual = null;
        estado = EstadoCocina.LIBRE;
    }

    //Queue

    public boolean tieneBase() {
        return pilaPreparacion.stream().anyMatch(i -> i instanceof Base);
    }

    public boolean tieneSalsa() {
        return pilaPreparacion.stream().anyMatch(i -> i instanceof Salsa);
    }

    public boolean estaLibre() {
        return estado == EstadoCocina.LIBRE;
    }

    public EstadoCocina getEstado() {
        return estado;
    }

    public Stack<Ingrediente> getPilaActual() {
        return pilaPreparacion;
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }
}
