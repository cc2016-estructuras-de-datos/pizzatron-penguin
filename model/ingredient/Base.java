package pizzeria.model.ingredient;

import pizzeria.model.enums.TipoIngrediente;
import pizzeria.model.enums.TipoMasa;

/**
 * Representa la capa base de cualquier pizza: la masa.
 * Usa TipoMasa como atributo diferenciador entre variantes,
 * evitando subclases que solo difieren en datos.
 *
 * Instanciación desde RecetaFactory:
 *   new Base(TipoMasa.CLASICA)
 *   new Base(TipoMasa.INTEGRAL)
 */
public class Base extends Ingrediente {

    private final TipoMasa tipoMasa;

    public Base(TipoMasa tipoMasa) {
        super(tipoMasa.getNombre());
        this.tipoMasa = tipoMasa;
    }

    @Override
    public void preparar() {
        System.out.println("Preparando " + getNombre() + "...");
    }

    @Override
    public TipoIngrediente getTipo() {
        return TipoIngrediente.BASE;
    }

    public TipoMasa getTipoMasa() {
        return tipoMasa;
    }
}
