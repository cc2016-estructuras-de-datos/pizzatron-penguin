package pizzeria.model.ingredient;

import pizzeria.model.enums.TipoIngrediente;
import pizzeria.model.enums.TipoSalsa;

/**
 * Representa cualquier salsa aplicable sobre la base de la pizza.
 * Usa TipoSalsa como atributo diferenciador entre variantes,
 * evitando subclases que solo difieren en datos.
 *
 * Instanciación desde RecetaFactory:
 *   new Salsa(TipoSalsa.NORMAL)
 *   new Salsa(TipoSalsa.PICANTE)
 */
public class Salsa extends Ingrediente {

    private final TipoSalsa tipoSalsa;

    public Salsa(TipoSalsa tipoSalsa) {
        super(tipoSalsa.getNombre());
        this.tipoSalsa = tipoSalsa;
    }

    @Override
    public void preparar() {
        System.out.println("Extendiendo " + getNombre() + " sobre la base...");
    }

    @Override
    public TipoIngrediente getTipo() {
        return TipoIngrediente.SALSA;
    }

    public TipoSalsa getTipoSalsa() {
        return tipoSalsa;
    }
}
