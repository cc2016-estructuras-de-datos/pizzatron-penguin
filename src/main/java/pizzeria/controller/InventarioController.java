package pizzeria.controller;

import pizzeria.model.ingredient.Ingrediente;
import pizzeria.model.enums.TipoIngrediente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Administra todos los ingredientes disponibles en la pizzería.
 *
 * Mantiene dos estructuras separadas:
 *   - ingredientes: el catálogo de objetos Ingrediente por nombre
 *   - stock: la cantidad disponible de cada ingrediente
 *
 * El stock es la cantidad puntual en este momento.
 * Ambos deben estar activos para que un ingrediente pueda usarse.
 */
public class InventarioController {

    private final Map<String, Ingrediente> ingredientes;
    private final Map<String, Integer> stock;

    public InventarioController() {
        this.ingredientes = new HashMap<>();
        this.stock = new HashMap<>();
    }

    /**
     * Registra un ingrediente en el inventario con su stock inicial.
     *
     * @param ingrediente el ingrediente a registrar
     * @param cantidadInicial stock con el que arranca
     */
    public void registrarIngrediente(Ingrediente ingrediente, int cantidadInicial) {
        ingredientes.put(ingrediente.getNombre(), ingrediente);
        stock.put(ingrediente.getNombre(), cantidadInicial);
    }

    /**
     * Verifica si un ingrediente puede ser usado en este momento.
     * Requiere que esté registrado, habilitado globalmente y con stock > 0.
     *
     * @param nombre nombre del ingrediente
     * @return true si está disponible para usar
     */
    public boolean hayDisponibilidad(String nombre) {
        Ingrediente ing = ingredientes.get(nombre);
        if (ing == null || !ing.isDisponible()) return false;
        return stock.getOrDefault(nombre, 0) > 0;
    }

    /**
     * Descuenta una unidad del stock del ingrediente.
     * Solo descuenta si hay disponibilidad.
     *
     * @param nombre nombre del ingrediente
     * @return true si se pudo descontar, false si no había stock
     */
    public boolean reducirStock(String nombre) {
        if (!hayDisponibilidad(nombre)) return false;
        stock.put(nombre, stock.get(nombre) - 1);
        return true;
    }

    /**
     * Repone stock de un ingrediente. Usado entre órdenes o por
     * eventos del juego.
     *
     * @param nombre nombre del ingrediente
     * @param cantidad unidades a reponer
     */
    public void reponerStock(String nombre, int cantidad) {
        if (!ingredientes.containsKey(nombre) || cantidad <= 0) return;
        stock.put(nombre, stock.getOrDefault(nombre, 0) + cantidad);
    }

    /**
     * Retorna todos los ingredientes de un tipo específico,
     * independientemente de su disponibilidad actual.
     * La vista usa esto para construir la banda de ingredientes.
     *
     * @param tipo BASE, SALSA o TOPPING
     * @return lista de ingredientes del tipo solicitado
     */
    public List<Ingrediente> getIngredientesPorTipo(TipoIngrediente tipo) {
        List<Ingrediente> resultado = new ArrayList<>();
        for (Ingrediente ing : ingredientes.values()) {
            if (ing.getTipo() == tipo) {
                resultado.add(ing);
            }
        }
        return resultado;
    }

    public Ingrediente getIngrediente(String nombre) {
        return ingredientes.get(nombre);
    }

    public int getStock(String nombre) {
        return stock.getOrDefault(nombre, 0);
    }

    public Map<String, Integer> getStockCompleto() {
        return new HashMap<>(stock);
    }
}
