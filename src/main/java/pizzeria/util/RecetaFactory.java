package pizzeria.util;

import pizzeria.model.Receta;
import pizzeria.model.enums.TipoMasa;
import pizzeria.model.enums.TipoSalsa;
import pizzeria.model.ingredient.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Centraliza la creación de todas las recetas del menú de la pizzería.
 *
 * Agregar una nueva pizza al menú es añadir un método aquí,
 * sin tocar ninguna otra clase del sistema.
 */
public class RecetaFactory {

    /**
     * Retorna todas las recetas disponibles en el menú.
     *
     * @return lista con todas las recetas del juego
     */
    public static List<Receta> todasLasRecetas() {
        List<Receta> recetas = new ArrayList<>();
        recetas.add(recetaClasicaPinguino());
        recetas.add(recetaEspecialDelMar());
        recetas.add(recetaDobleCalamares());
        recetas.add(recetaMarHelado());
        recetas.add(recetaAlgaFusion());
        return recetas;
    }

    /**
     * Masa clásica + salsa normal + pescado.
     */
    public static Receta recetaClasicaPinguino() {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(new Pescado());

        return new Receta(
            "Clásica Pingüino",
            new Base(TipoMasa.CLASICA),
            new Salsa(TipoSalsa.NORMAL),
            toppings,
            40,
            5
        );
    }

    /**
     * Masa clásica + salsa picante + camarones + alga.
     */
    public static Receta recetaEspecialDelMar() {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(new Camarones());
        toppings.add(new Alga());

        return new Receta(
            "Especial del Mar",
            new Base(TipoMasa.CLASICA),
            new Salsa(TipoSalsa.PICANTE),
            toppings,
            60,
            5
        );
    }

    /**
     * Masa integral + salsa normal + calamar + calamar.
     */
    public static Receta recetaDobleCalamares() {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(new Calamar());
        toppings.add(new Calamar());

        return new Receta(
            "Doble Calamar",
            new Base(TipoMasa.INTEGRAL),
            new Salsa(TipoSalsa.NORMAL),
            toppings,
            55,
            5
        );
    }

    /**
     * Masa clásica + salsa picante + pescado + hielo.
     */
    public static Receta recetaMarHelado() {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(new Pescado());
        toppings.add(new Hielo());

        return new Receta(
            "Mar Helado",
            new Base(TipoMasa.CLASICA),
            new Salsa(TipoSalsa.PICANTE),
            toppings,
            65,
            5
        );
    }

    /**
     * Masa integral + salsa normal + alga + camarones + hielo.
     */
    public static Receta recetaAlgaFusion() {
        List<Topping> toppings = new ArrayList<>();
        toppings.add(new Alga());
        toppings.add(new Camarones());
        toppings.add(new Hielo());

        return new Receta(
            "Alga Fusión",
            new Base(TipoMasa.INTEGRAL),
            new Salsa(TipoSalsa.NORMAL),
            toppings,
            75,
            5
        );
    }
}
