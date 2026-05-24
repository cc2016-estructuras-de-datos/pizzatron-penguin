package pizzeria.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pizzeria.model.Orden;
import pizzeria.model.Receta;
import pizzeria.model.ingredient.Topping;

import java.util.List;

public class OrdenPanel {

    private static final int TIEMPO_MAX_ORDEN = 60;

    private final VBox root;
    private final VBox contenido;

    public OrdenPanel() {
        root = new VBox(10);
        root.setPrefWidth(230);
        root.setPadding(new Insets(15, 12, 15, 12));
        root.setStyle(
            "-fx-background-color: #16213e;" +
            "-fx-border-color: #00d4ff;" +
            "-fx-border-width: 0 2 0 0;"
        );

        Label titulo = new Label("ORDENES");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        titulo.setTextFill(Color.web("#00d4ff"));

        contenido = new VBox(8);
        root.getChildren().addAll(titulo, contenido);
    }

    public void actualizar(Orden activa, List<Orden> enCola) {
        contenido.getChildren().clear();

        if (activa != null) {
            Label lblActiva = new Label("EN PREPARACION");
            lblActiva.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            lblActiva.setTextFill(Color.web("#00d4ff"));
            contenido.getChildren().addAll(lblActiva, tarjetaOrden(activa, true));
        } else {
            Label espera = new Label("Sin orden activa");
            espera.setTextFill(Color.web("#555577"));
            espera.setFont(Font.font("Arial", 12));
            contenido.getChildren().add(espera);
        }

        if (!enCola.isEmpty()) {
            Label lblCola = new Label("EN COLA (" + enCola.size() + ")");
            lblCola.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            lblCola.setTextFill(Color.web("#8888aa"));
            contenido.getChildren().add(lblCola);
            for (Orden o : enCola) {
                contenido.getChildren().add(tarjetaOrden(o, false));
            }
        }
    }

    private VBox tarjetaOrden(Orden orden, boolean activa) {
        VBox tarjeta = new VBox(4);
        tarjeta.setPadding(new Insets(8));
        String borde = activa ? "#00d4ff" : "#334466";
        tarjeta.setStyle(
            "-fx-background-color: #0f3460;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + borde + ";" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;"
        );

        Label lblNombre = new Label("#" + orden.getNumeroOrden() + "  " + orden.getReceta().getNombre());
        lblNombre.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lblNombre.setTextFill(activa ? Color.web("#00d4ff") : Color.WHITE);
        lblNombre.setWrapText(true);

        Label lblRecompensa = new Label(orden.getRecompensa() + " monedas");
        lblRecompensa.setFont(Font.font("Arial", 11));
        lblRecompensa.setTextFill(Color.web("#ffaa00"));

        tarjeta.getChildren().addAll(lblNombre, lblRecompensa);

        if (activa) {
            int tiempo = orden.getTiempoRestante();
            double progreso = Math.max(0.0, (double) tiempo / TIEMPO_MAX_ORDEN);

            ProgressBar barra = new ProgressBar(progreso);
            barra.setPrefWidth(195);
            barra.setStyle(progreso < 0.3 ? "-fx-accent: #ff4444;" :
                           progreso < 0.6 ? "-fx-accent: #ffaa00;" :
                                            "-fx-accent: #00d4ff;");

            Label lblTiempo = new Label(tiempo + "s restantes");
            lblTiempo.setFont(Font.font("Arial", 10));
            lblTiempo.setTextFill(progreso < 0.3 ? Color.web("#ff4444") : Color.web("#aaaaaa"));

            tarjeta.getChildren().addAll(barra, lblTiempo);
        }

        // Siempre mostrar la receta (activa y en cola) para poder planificar
        tarjeta.getChildren().add(detalleReceta(orden.getReceta()));

        return tarjeta;
    }

    private VBox detalleReceta(Receta receta) {
        VBox detalles = new VBox(2);
        detalles.setPadding(new Insets(4, 0, 0, 0));

        detalles.getChildren().add(lineaReceta("Base", receta.getBaseRequerida().getNombre()));
        detalles.getChildren().add(lineaReceta("Salsa", receta.getSalsaRequerida().getNombre()));

        if (!receta.getToppingsRequeridos().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Topping t : receta.getToppingsRequeridos()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(t.getNombre());
            }
            detalles.getChildren().add(lineaReceta("Tops", sb.toString()));
        }

        return detalles;
    }

    private Label lineaReceta(String clave, String valor) {
        Label lbl = new Label(clave + ": " + valor);
        lbl.setFont(Font.font("Arial", 10));
        lbl.setTextFill(Color.web("#cccccc"));
        lbl.setWrapText(true);
        return lbl;
    }

    public VBox getRoot() {
        return root;
    }
}
