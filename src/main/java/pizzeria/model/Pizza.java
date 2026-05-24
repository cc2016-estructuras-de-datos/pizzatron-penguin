package pizzeria.model;

import pizzeria.interfaces.ICocinable;
import pizzeria.interfaces.IValidable;
import pizzeria.model.enums.EstadoPizza;
import pizzeria.model.ingredient.Base;
import pizzeria.model.ingredient.Salsa;
import pizzeria.model.ingredient.Topping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Modelo central del sistema. Representa la pizza física que el usuario
 * construye durante el juego.
 *
 * Implementa ICocinable porque sabe cuánto tarda en cocinarse y
 * gestiona su propia transición a LISTA.
 *
 * Implementa IValidable para verificar su integridad estructural:
 * que tenga base, salsa, y que todos sus ingredientes estén disponibles.
 * Esto es distinto a cumplir una receta específica, que es responsabilidad
 * de Receta.cumpleReceta(pizza).
 */
public class Pizza implements ICocinable, IValidable {

    private final UUID id;
    private final String nombre;
    private final Base base;
    private final Salsa salsa;
    private final List<Topping> toppings;
    private EstadoPizza estado;
    private final int tiempoCoccion;

    public Pizza(String nombre, Base base, Salsa salsa) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.base = base;
        this.salsa = salsa;
        this.toppings = new ArrayList<>();
        this.estado = EstadoPizza.PENDIENTE;
        this.tiempoCoccion = 5;
    }

    public void agregarTopping(Topping topping) {
        if (topping == null) {
            throw new IllegalArgumentException("El topping no puede ser nulo.");
        }
        toppings.add(topping);
    }

    //ICocinable 

    @Override
    public void cocinar() {
        if (estado == EstadoPizza.EN_PREPARACION) {
            estado = EstadoPizza.COCINANDO;
            base.preparar();
            salsa.preparar();
            toppings.forEach(Topping::preparar);
            estado = EstadoPizza.LISTA;
        }
    }

    @Override
    public boolean estaListo() {
        return estado == EstadoPizza.LISTA;
    }

    @Override
    public int getTiempoCoccion() {
        return tiempoCoccion;
    }

    //IValidable

    /**
     * Verifica que la pizza esté bien formada estructuralmente.
     * No valida si cumple una receta, solo si puede ser procesada.
     *
     * @return true si la pizza es válida para ser procesada
     */
    @Override
    public boolean validar() {
        return getErrores().isEmpty();
    }

    @Override
    public List<String> getErrores() {
        List<String> errores = new ArrayList<>();

        if (base == null) {
            errores.add("La pizza no tiene base.");
        } else if (!base.isDisponible()) {
            errores.add("La base '" + base.getNombre() + "' no está disponible.");
        }

        if (salsa == null) {
            errores.add("La pizza no tiene salsa.");
        } else if (!salsa.isDisponible()) {
            errores.add("La salsa '" + salsa.getNombre() + "' no está disponible.");
        }

        for (Topping t : toppings) {
            if (!t.isDisponible()) {
                errores.add("El topping '" + t.getNombre() + "' no está disponible.");
            }
        }

        return errores;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Base getBase() {
        return base;
    }

    public Salsa getSalsa() {
        return salsa;
    }

    public List<Topping> getToppings() {
        return Collections.unmodifiableList(toppings);
    }

    public EstadoPizza getEstado() {
        return estado;
    }

    public void setEstado(EstadoPizza estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pizza{nombre='" + nombre + "', estado=" + estado + "}";
    }
}
