package pizzeria.model.ingredient;

/**
 * Topping de alga marina, ingrediente característico
 * del menú antártico de la pizzería.
 */
public class Alga extends Topping {

    public Alga() {
        super("Alga");
    }

    @Override
    public void preparar() {
        System.out.println("Esparciendo alga sobre la pizza...");
    }
}
