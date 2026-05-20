package pizzeria.model.ingredient;

/**
 * Topping de pescado. El ingrediente más icónico
 * de la pizzería pingüino.
 */
public class Pescado extends Topping {

    public Pescado() {
        super("Pescado");
    }

    @Override
    public void preparar() {
        System.out.println("Colocando pescado sobre la pizza...");
    }
}
