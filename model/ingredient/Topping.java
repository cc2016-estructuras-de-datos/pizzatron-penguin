package pizzeria.model.ingredient;

import pizzeria.model.enums.TipoIngrediente;

/**
 * Representa cualquier topping que va sobre la salsa de la pizza.
 * Implementa getTipo() retornando TOPPING para todas las subclases.
 *
 * Sigue siendo abstracta porque preparar() depende del tipo
 * específico de topping.
 */
public abstract class Topping extends Ingrediente {

    protected Topping(String nombre) {
        super(nombre);
    }

    @Override
    public TipoIngrediente getTipo() {
        return TipoIngrediente.TOPPING;
    }
}
