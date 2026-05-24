package pizzeria.view.components;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import pizzeria.controller.CocinaController;
import pizzeria.controller.JuegoController;
import pizzeria.model.ingredient.Ingrediente;
import pizzeria.view.assets.AssetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CocinaPanel {

    private final VBox root;
    private final VBox vistaIngredientes;
    private final Label lblReceta;
    private final Label lblMensaje;
    private final JuegoController controller;
    private PauseTransition limpiezaMensaje;

    public CocinaPanel(JuegoController controller) {
        this.controller = controller;

        root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(18, 20, 18, 20));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titulo = new Label("COCINA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.web("#00d4ff"));

        lblReceta = new Label("Esperando orden...");
        lblReceta.setFont(Font.font("Arial", 13));
        lblReceta.setTextFill(Color.web("#aaaaaa"));
        lblReceta.setWrapText(true);

        vistaIngredientes = new VBox(3);
        vistaIngredientes.setAlignment(Pos.CENTER);
        vistaIngredientes.setPrefHeight(260);
        vistaIngredientes.setPrefWidth(280);
        vistaIngredientes.setStyle(
            "-fx-background-color: #0f3460;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;"
        );

        Label pizzaVacia = new Label("Agrega ingredientes desde abajo");
        pizzaVacia.setTextFill(Color.web("#445566"));
        pizzaVacia.setFont(Font.font("Arial", 12));
        vistaIngredientes.getChildren().add(pizzaVacia);

        lblMensaje = new Label("");
        lblMensaje.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblMensaje.setTextFill(Color.web("#ffaa00"));
        lblMensaje.setPrefHeight(20);

        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER);

        Button btnDeshacer = boton("DESHACER", "#556677");
        btnDeshacer.setOnAction(e -> {
            boolean ok = controller.deshacerIngrediente();
            if (!ok) mostrarFeedback("Nada que deshacer", Color.web("#888888"));
        });

        Button btnHornear = boton("HORNEAR", "#cc5500");
        btnHornear.setOnAction(e -> {
            boolean ok = controller.hornear();
            if (!ok) mostrarFeedback("Necesitas base y salsa para hornear", Color.web("#ff4444"));
        });

        botones.getChildren().addAll(btnDeshacer, btnHornear);
        root.getChildren().addAll(titulo, lblReceta, vistaIngredientes, lblMensaje, botones);
    }

    public void actualizar(CocinaController cocina) {
        vistaIngredientes.getChildren().clear();

        if (cocina.getRecetaActual() != null) {
            lblReceta.setText("Preparando: " + cocina.getRecetaActual().getNombre());
            lblReceta.setTextFill(Color.web("#e0e0e0"));
        } else {
            lblReceta.setText("Esperando orden...");
            lblReceta.setTextFill(Color.web("#aaaaaa"));
        }

        Stack<Ingrediente> pila = cocina.getPilaActual();

        if (pila.isEmpty()) {
            Label vacio = new Label("Pizza vacia");
            vacio.setTextFill(Color.web("#445566"));
            vacio.setFont(Font.font("Arial", 12));
            vistaIngredientes.getChildren().add(vacio);
        } else {
            // Capas de la pizza: ingrediente más reciente arriba, base abajo
            List<Ingrediente> lista = new ArrayList<>(pila);
            for (int i = lista.size() - 1; i >= 0; i--) {
                Node capa = AssetManager.getIngredienteCapaNode(lista.get(i).getNombre(), 220);
                vistaIngredientes.getChildren().add(capa);
            }
        }
    }

    /**
     * Muestra un aviso temporal en el panel de cocina.
     * Se limpia automaticamente despues de 2.5 segundos.
     * Llamado desde BandaIngredientes cuando un click falla.
     */
    public void mostrarFeedback(String msg, Color color) {
        lblMensaje.setText(msg);
        lblMensaje.setTextFill(color);

        if (limpiezaMensaje != null) limpiezaMensaje.stop();
        limpiezaMensaje = new PauseTransition(Duration.seconds(2.5));
        limpiezaMensaje.setOnFinished(e -> lblMensaje.setText(""));
        limpiezaMensaje.play();
    }

    private Button boton(String texto, String colorHex) {
        Button btn = new Button(texto);
        btn.setPrefSize(130, 44);
        btn.setStyle(
            "-fx-background-color: " + colorHex + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }

    public VBox getRoot() {
        return root;
    }
}
