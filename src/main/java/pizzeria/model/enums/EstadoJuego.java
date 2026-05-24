package pizzeria.model.enums;

/**
 * Controla el flujo general del juego desde JuegoController.
 * La vista escucha este estado para saber qué pantalla mostrar
 * en cada momento.
 */
public enum EstadoJuego {
    MENU,
    JUGANDO,
    PAUSADO,
    GAME_OVER
}
