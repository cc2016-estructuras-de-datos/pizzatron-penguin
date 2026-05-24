package pizzeria.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pizzeria.controller.PuntajeSnapshot;

public class PuntajePanel {

    private static final int MAXIMO_ERRORES = 5;
    private static final int TIEMPO_TOTAL   = 180;
    private static final int TOTAL_PIZZAS   = 20;

    private final HBox root;
    private final Label lblMonedas;
    private final Label lblPizzas;
    private final Label lblErrores;
    private final Label lblTiempo;
    private final ProgressBar barTiempo;
    private final ProgressBar barErrores;

    public PuntajePanel() {
        root = new HBox(35);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(10, 25, 10, 25));
        root.setStyle(
            "-fx-background-color: #0f3460;" +
            "-fx-border-color: #00d4ff;" +
            "-fx-border-width: 0 0 2 0;"
        );

        lblMonedas = etiqueta("0 monedas");
        lblPizzas  = etiqueta("0/" + TOTAL_PIZZAS);
        lblErrores = etiqueta("0/" + MAXIMO_ERRORES);
        lblTiempo  = etiqueta("3:00");

        barTiempo = new ProgressBar(1.0);
        barTiempo.setPrefWidth(130);
        barTiempo.setStyle("-fx-accent: #00d4ff;");

        barErrores = new ProgressBar(0.0);
        barErrores.setPrefWidth(90);
        barErrores.setStyle("-fx-accent: #ff4444;");

        root.getChildren().addAll(
            bloque("Monedas",  lblMonedas),
            bloque("Pizzas",   lblPizzas),
            bloque("Errores",  lblErrores, barErrores),
            bloque("Tiempo",   lblTiempo, barTiempo)
        );
    }

    public void actualizar(PuntajeSnapshot snap) {
        lblMonedas.setText(snap.monedas + " monedas");
        lblPizzas.setText(snap.pizzasCompletadas + "/" + TOTAL_PIZZAS);
        lblErrores.setText(snap.errores + "/" + MAXIMO_ERRORES);

        int mm = snap.tiempoRestante / 60;
        int ss = snap.tiempoRestante % 60;
        lblTiempo.setText(String.format("%d:%02d", mm, ss));

        double pTiempo = (double) snap.tiempoRestante / TIEMPO_TOTAL;
        barTiempo.setProgress(pTiempo);
        if (pTiempo < 0.25) {
            barTiempo.setStyle("-fx-accent: #ff4444;");
            lblTiempo.setTextFill(Color.web("#ff4444"));
        } else if (pTiempo < 0.5) {
            barTiempo.setStyle("-fx-accent: #ffaa00;");
            lblTiempo.setTextFill(Color.web("#ffaa00"));
        } else {
            barTiempo.setStyle("-fx-accent: #00d4ff;");
            lblTiempo.setTextFill(Color.WHITE);
        }

        double pErrores = (double) snap.errores / MAXIMO_ERRORES;
        barErrores.setProgress(pErrores);
        if (snap.errores >= 4) {
            lblErrores.setTextFill(Color.web("#ff4444"));
        } else if (snap.errores >= 3) {
            lblErrores.setTextFill(Color.web("#ffaa00"));
        } else {
            lblErrores.setTextFill(Color.WHITE);
        }
    }

    public HBox getRoot() {
        return root;
    }

    private Label etiqueta(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lbl.setTextFill(Color.WHITE);
        return lbl;
    }

    private VBox bloque(String titulo, javafx.scene.Node... nodos) {
        Label header = new Label(titulo.toUpperCase());
        header.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        header.setTextFill(Color.web("#8888aa"));
        VBox vbox = new VBox(3, header);
        vbox.getChildren().addAll(nodos);
        return vbox;
    }
}
