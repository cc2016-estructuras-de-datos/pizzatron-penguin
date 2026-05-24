package pizzeria.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import pizzeria.controller.JuegoController;
import pizzeria.model.enums.EstadoJuego;
import pizzeria.view.components.BandaIngredientes;
import pizzeria.view.components.CocinaPanel;
import pizzeria.view.components.OrdenPanel;
import pizzeria.view.components.PuntajePanel;

public class GameView {

    private final BorderPane root;
    private final JuegoController controller;
    private final Runnable onGameOver;
    private Timeline timeline;

    private final PuntajePanel puntajePanel;
    private final OrdenPanel ordenPanel;
    private final CocinaPanel cocinaPanel;
    private final BandaIngredientes bandaIngredientes;

    public GameView(JuegoController controller, Runnable onGameOver) {
        this.controller = controller;
        this.onGameOver = onGameOver;

        root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        puntajePanel     = new PuntajePanel();
        ordenPanel       = new OrdenPanel();
        cocinaPanel      = new CocinaPanel(controller);
        bandaIngredientes = new BandaIngredientes(controller,
            msg -> cocinaPanel.mostrarFeedback(msg, javafx.scene.paint.Color.web("#ffaa00")));

        root.setTop(puntajePanel.getRoot());
        root.setLeft(ordenPanel.getRoot());
        root.setCenter(cocinaPanel.getRoot());
        root.setBottom(bandaIngredientes.getRoot());

        configurarTimeline();
    }

    private void configurarTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            controller.tick();
            refrescar();
            if (controller.getEstado() == EstadoJuego.GAME_OVER) {
                timeline.stop();
                onGameOver.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /** Llama esto justo antes de mostrar la escena. */
    public void iniciar() {
        refrescar();
        timeline.play();
    }

    public void detener() {
        if (timeline != null) timeline.stop();
    }

    private void refrescar() {
        puntajePanel.actualizar(controller.getPuntaje());
        ordenPanel.actualizar(
            controller.getCaja().getOrdenActiva(),
            controller.getCaja().getOrdenesEnCola()
        );
        cocinaPanel.actualizar(controller.getCocina());
        bandaIngredientes.actualizar(controller.getInventario(), controller.getEstado());
    }

    public BorderPane getRoot() {
        return root;
    }
}
