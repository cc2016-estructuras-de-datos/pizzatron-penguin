package pizzeria.model.enums;

/**
 * Representa el ciclo de vida de una orden dentro del sistema.
 *
 * Flujo normal:
 *   RECIBIDA -> EN_COLA -> PROCESANDO -> COMPLETADA
 *
 * Flujo de error:
 *   RECIBIDA -> FALLIDA  (si no pasa validación o no hay stock)
 *   PROCESANDO -> FALLIDA (si el timer llega a cero)
 *  
 */
public enum EstadoOrden {
    RECIBIDA,
    EN_COLA,
    PROCESANDO,
    COMPLETADA,
    FALLIDA
}
