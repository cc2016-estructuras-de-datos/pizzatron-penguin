package pizzeria.model.enums;

/**
 * Indica el estado operativo de la cocina.
 *
 * Flujo normal:
 *   LIBRE -> PREPARANDO -> HORNEANDO -> LIBRE
 */
public enum EstadoCocina {
    LIBRE,
    PREPARANDO,
    HORNEANDO
}
