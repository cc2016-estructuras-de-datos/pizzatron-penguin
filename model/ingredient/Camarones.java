package pizzeria.model.ingredient;

/**
 * Topping de camarones frescos.
 */
public class Camarones extends Topping {

    public Camarones() {
        super("Camarones");
    }

    @Override
    public void preparar() {
        System.out.println("Distribuyendo camarones sobre la pizza...");
    }
}
