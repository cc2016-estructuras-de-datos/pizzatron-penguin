package pizzeria.model;

import pizzeria.model.ingredient.Base;
import pizzeria.model.ingredient.Salsa;
import pizzeria.model.ingredient.Topping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Define los requisitos exactos que debe cumplir una pizza para
 * considerarse correctamente preparada.
 *
 * Receta está separada de Pizza: agregar una nueva
 * pizza al menú es solo crear una nueva Receta en RecetaFactory,
 * sin tocar ninguna otra clase del sistema.
 *
 * La validación de si una pizza cumple la receta se encuentra aquí, no en Pizza.
 * Pizza sabe si está bien formada (IValidable), pero no sabe si
 * coincide con lo que se le pidió.
 */
public class Receta {

    private final UUID id;
    private final String nombre;
    private final Base baseRequerida;
    private final Salsa salsaRequerida;
    private final List<Topping> toppingsRequeridos;
    private final int recompensaMonedas;
    private final int tiempoCoccion;

    public Receta(String nombre, Base baseRequerida, Salsa salsaRequerida,
                  List<Topping> toppingsRequeridos, int recompensaMonedas, int tiempoCoccion) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.baseRequerida = baseRequerida;
        this.salsaRequerida = salsaRequerida;
        this.toppingsRequeridos = new ArrayList<>(toppingsRequeridos);
        this.recompensaMonedas = recompensaMonedas;
        this.tiempoCoccion = tiempoCoccion;
    }

    /**
     * Verifica si la pizza construida por el usuario coincide exactamente
     * con los requisitos de esta receta.
     *
     * Compara por nombre de ingrediente para desacoplar la validación
     * de las instancias concretas, ya que cada click del usuario
     * genera una nueva instancia del mismo ingrediente.
     *
     * @param pizza la pizza construida por el usuario
     * @return true si la pizza cumple todos los requisitos de la receta
     */
    public boolean cumpleReceta(Pizza pizza) {
        if (pizza == null) return false;

        if (!pizza.getBase().getNombre().equals(baseRequerida.getNombre())) {
            return false;
        }

        if (!pizza.getSalsa().getNombre().equals(salsaRequerida.getNombre())) {
            return false;
        }

        if (pizza.getToppings().size() != toppingsRequeridos.size()) {
            return false;
        }

        List<String> toppingsPizza = new ArrayList<>();
        pizza.getToppings().forEach(t -> toppingsPizza.add(t.getNombre()));

        List<String> toppingsReceta = new ArrayList<>();
        toppingsRequeridos.forEach(t -> toppingsReceta.add(t.getNombre()));

        Collections.sort(toppingsPizza);
        Collections.sort(toppingsReceta);

        return toppingsPizza.equals(toppingsReceta);
    }

    /**
     * Construye una pizza base usando los ingredientes de esta receta,
     * para que CocinaController la tome como punto de partida.
     *
     * @return pizza con base y salsa listos, sin toppings
     */
    public Pizza construirPizzaBase() {
        return new Pizza(nombre, baseRequerida, salsaRequerida);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Base getBaseRequerida() {
        return baseRequerida;
    }

    public Salsa getSalsaRequerida() {
        return salsaRequerida;
    }

    public List<Topping> getToppingsRequeridos() {
        return Collections.unmodifiableList(toppingsRequeridos);
    }

    public int getRecompensaMonedas() {
        return recompensaMonedas;
    }

    public int getTiempoCoccion() {
        return tiempoCoccion;
    }

    @Override
    public String toString() {
        return "Receta{nombre='" + nombre + "', recompensa=" + recompensaMonedas + " monedas}";
    }
}
