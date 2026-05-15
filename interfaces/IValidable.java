package pizzeria.interfaces;

import java.util.List;

/**
 * Contrato de validación para objetos que deben verificar su integridad
 * antes de ser procesados por el sistema.
 *
 * Esta interfaz valida que el objeto esté bien formado,
 * no si cumple con una receta específica.
 */
public interface IValidable {

    /**
     * Verifica que el objeto cumpla todas las condiciones necesarias
     * para ser procesado.
     *
     * @return true si el objeto es válido y puede ser procesado
     */
    boolean validar();

    /**
     * Retorna la lista de problemas encontrados durante la validación.
     * Si validar() retorna true, esta lista debe estar vacía.
     *
     * @return lista de mensajes de error, vacía si no hay problemas
     */
    List<String> getErrores();
}
