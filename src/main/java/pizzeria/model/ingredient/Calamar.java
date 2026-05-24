package pizzeria.model.ingredient;

/**
 * Topping de calamar.
 */
public class Calamar extends Topping {

    public Calamar() {
        super("Calamar");
    }

    @Override
    public void preparar() {
        System.out.println("Colocando calamar sobre la pizza...");
    }
}
