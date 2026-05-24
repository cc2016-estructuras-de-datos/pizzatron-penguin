package pizzeria.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pizzeria.controller.PuntajeSnapshot;

public class GameOverView {

    private final VBox root;

    public GameOverView(PuntajeSnapshot snap, Runnable onReiniciar, Runnable onSalir) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titulo = new Label("GAME OVER");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 68));
        titulo.setTextFill(Color.web("#ff3333"));

        Label subtitulo = new Label("Resultados de la sesion");
        subtitulo.setFont(Font.font("Arial", 20));
        subtitulo.setTextFill(Color.web("#8888aa"));

        VBox stats = panelStats(snap);

        HBox botones = new HBox(20);
        botones.setAlignment(Pos.CENTER);

        Button btnReintentar = boton("JUGAR DE NUEVO", "#00d4ff");
        btnReintentar.setOnAction(e -> onReiniciar.run());

        Button btnSalir = boton("SALIR", "#884444");
        btnSalir.setOnAction(e -> onSalir.run());

        botones.getChildren().addAll(btnReintentar, btnSalir);
        root.getChildren().addAll(titulo, subtitulo, stats, botones);
    }

    private VBox panelStats(PuntajeSnapshot s) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(25, 40, 25, 40));
        panel.setStyle(
            "-fx-background-color: #0f3460;" +
            "-fx-background-radius: 12;"
        );

        int total = s.pizzasCompletadas + s.pizzasRestantes;
        panel.getChildren().addAll(
            fila("Pizzas entregadas",   s.pizzasCompletadas + " de " + total),
            fila("Errores cometidos",   s.errores + " de 5"),
            fila("Monedas acumuladas",  s.monedas + " monedas"),
            fila("Tiempo restante",     formatearTiempo(s.tiempoRestante))
        );
        return panel;
    }

    private HBox fila(String clave, String valor) {
        Label k = new Label(clave + ":");
        k.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
        k.setTextFill(Color.web("#aaaaaa"));
        k.setPrefWidth(220);

        Label v = new Label(valor);
        v.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        v.setTextFill(Color.WHITE);

        HBox fila = new HBox(10, k, v);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    private Button boton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setPrefSize(200, 52);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 9;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }

    private String formatearTiempo(int segundos) {
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }

    public VBox getRoot() {
        return root;
    }
}
