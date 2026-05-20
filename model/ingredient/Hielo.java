package pizzeria.model.ingredient;

/**
 * Topping de hielo. Sí, los pingüinos le ponen hielo a la pizza.
 */
public class Hielo extends Topping {

    public Hielo() {
        super("Hielo");
    }

    @Override
    public void preparar() {
        System.out.println("Colocando hielo sobre la pizza...");
    }
}
