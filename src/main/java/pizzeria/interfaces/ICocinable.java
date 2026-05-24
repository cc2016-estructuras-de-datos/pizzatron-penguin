package pizzeria.interfaces;

/**
 * Contrato para cualquier objeto que pueda ser cocinado dentro del sistema.
 * Separa la capacidad de cocinarse de la jerarquía de herencia principal,
 * permitiendo que en el futuro otros objetos sean cocinables sin pertenecer
 * a la jerarquía de Ingrediente.
 */
public interface ICocinable {

    /**
     * Ejecuta el proceso de cocción del objeto.
     * Cambia el estado interno hacia COCINANDO y posteriormente LISTA.
     */
    void cocinar();

    /**
     * Indica si el objeto ya completó su proceso de cocción.
     *
     * @return true si está listo para ser entregado
     */
    boolean estaListo();

    /**
     * Retorna el tiempo estimado de cocción en segundos.
     *
     * @return tiempo de cocción en segundos
     */
    int getTiempoCoccion();
}
