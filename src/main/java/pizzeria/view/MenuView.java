package pizzeria.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MenuView {

    private final VBox root;

    public MenuView(Runnable onJugar) {
        root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titulo = new Label("PIZZATRON");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 76));
        titulo.setTextFill(Color.web("#00d4ff"));

        Label subtitulo = new Label("La Pizzeria del Pinguino");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        subtitulo.setTextFill(Color.web("#8888cc"));

        Label instrucciones = new Label(
            "Prepara pizzas siguiendo las ordenes de los clientes\n" +
            "Orden obligatorio:  Base  →  Salsa  →  Toppings\n\n" +
            "Tienes 3 minutos  ·  20 pizzas  ·  maximo 5 errores"
        );
        instrucciones.setFont(Font.font("Arial", 15));
        instrucciones.setTextFill(Color.web("#aaaaaa"));
        instrucciones.setTextAlignment(TextAlignment.CENTER);

        Button btnJugar = boton("JUGAR", "#00d4ff", "#003366");
        btnJugar.setOnAction(e -> onJugar.run());

        root.getChildren().addAll(titulo, subtitulo, instrucciones, btnJugar);
    }

    private Button boton(String texto, String bg, String fg) {
        Button btn = new Button(texto);
        btn.setPrefSize(220, 58);
        String base = "-fx-background-color:" + bg + ";-fx-text-fill:" + fg +
                      ";-fx-font-size:20px;-fx-font-weight:bold;" +
                      "-fx-background-radius:10;-fx-cursor:hand;";
        String hover = "-fx-background-color:#00b8dd;-fx-text-fill:" + fg +
                       ";-fx-font-size:20px;-fx-font-weight:bold;" +
                       "-fx-background-radius:10;-fx-cursor:hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    public VBox getRoot() {
        return root;
    }
}
