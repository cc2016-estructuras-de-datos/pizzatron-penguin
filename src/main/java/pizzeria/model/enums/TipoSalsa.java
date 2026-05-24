package pizzeria.model.enums;

/**
 * Define las variantes de salsa disponibles en la pizzería.
 * Usado como atributo diferenciador en Salsa, evitando subclases
 * que solo difieren en datos y no en comportamiento.
 */
public enum TipoSalsa {
    NORMAL("Salsa Normal"),
    PICANTE("Salsa Picante");

    private final String nombre;

    TipoSalsa(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
