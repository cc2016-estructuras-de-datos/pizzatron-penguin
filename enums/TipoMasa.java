package pizzeria.model.enums;

/**
 * Define las variantes de masa disponibles en la pizzería.
 * Usado como atributo diferenciador en Base, evitando subclases
 * que solo difieren en datos y no en comportamiento.
 */
public enum TipoMasa {
    CLASICA("Masa Clásica"),
    INTEGRAL("Masa Integral");

    private final String nombre;

    TipoMasa(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
