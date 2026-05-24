package pizzeria.model.enums;

/**
 * Representa el ciclo de vida completo de una pizza dentro del sistema.
 *
 * Flujo normal:
 *   PENDIENTE -> EN_PREPARACION -> COCINANDO -> LISTA -> ENTREGADA
 *
 * Flujo de error:
 *   Cualquier estado -> CANCELADA (cuando el timer de la orden llega a cero)
 */
public enum EstadoPizza {
    PENDIENTE,
    EN_PREPARACION,
    COCINANDO,
    LISTA,
    ENTREGADA,
    CANCELADA
}
