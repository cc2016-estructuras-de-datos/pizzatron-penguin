package pizzeria.model.ingredient;

import pizzeria.model.enums.TipoIngrediente;

/**
 * Clase abstracta raíz de toda la jerarquía de ingredientes.
 * Contiene los atributos y comportamientos comunes a cualquier
 * ingrediente de la pizzería.
 *
 * Nadie instancia Ingrediente directamente. Las subclases abstractas
 * (Base, Salsa, Topping) añaden atributos propios de su categoría,
 * y las clases concretas finales implementan preparar().
 */
public abstract class Ingrediente {

    private final String nombre;
    private boolean disponible;

    protected Ingrediente(String nombre) {
        this.nombre = nombre;
        this.disponible = true;
    }

    /**
     * Define cómo se prepara este ingrediente específico.
     * Cada clase concreta implementa su propia lógica.
     */
    public abstract void preparar();

    /**
     * Retorna la categoría a la que pertenece este ingrediente.
     * Implementado en Base, Salsa y Topping.
     *
     * @return TipoIngrediente correspondiente a la categoría
     */
    public abstract TipoIngrediente getTipo();

    public String getNombre() {
        return nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return nombre + " (" + getTipo() + ")";
    }
}
