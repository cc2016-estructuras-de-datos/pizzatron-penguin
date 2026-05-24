package pizzeria.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pizzeria.controller.CocinaController;
import pizzeria.controller.InventarioController;
import pizzeria.controller.JuegoController;
import pizzeria.model.enums.EstadoJuego;
import pizzeria.model.enums.TipoIngrediente;
import pizzeria.model.ingredient.Ingrediente;
import pizzeria.view.assets.AssetManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BandaIngredientes {

    private static final String ESTILO_ACTIVO   = "-fx-background-color:#1a3a5c;-fx-background-radius:8;-fx-cursor:hand;";
    private static final String ESTILO_HOVER    = "-fx-background-color:#1a4a7c;-fx-background-radius:8;-fx-cursor:hand;";
    private static final String ESTILO_INACTIVO = "-fx-background-color:#111122;-fx-background-radius:8;-fx-opacity:0.45;";

    private final HBox root;
    private final JuegoController controller;
    private final Consumer<String> feedbackCallback;
    private final HBox bandaBases;
    private final HBox bandaSalsas;
    private final HBox bandaToppings;

    // Referencias persistentes — se crean una vez, se actualizan en cada refresh
    private final Map<String, Label> mapaStockLabels = new HashMap<>();
    private final Map<String, VBox>  mapaFichas      = new HashMap<>();
    private boolean fichasCreadas = false;

    public BandaIngredientes(JuegoController controller, Consumer<String> feedbackCallback) {
        this.controller       = controller;
        this.feedbackCallback = feedbackCallback;

        root = new HBox(18);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setStyle(
            "-fx-background-color:#0f3460;" +
            "-fx-border-color:#00d4ff;" +
            "-fx-border-width:2 0 0 0;"
        );

        bandaBases    = new HBox(8);
        bandaSalsas   = new HBox(8);
        bandaToppings = new HBox(8);

        root.getChildren().addAll(
            seccion("BASES",    bandaBases),
            separador(),
            seccion("SALSAS",   bandaSalsas),
            separador(),
            seccion("TOPPINGS", bandaToppings)
        );
    }

    /**
     * Primer llamado: construye las fichas.
     * Llamados siguientes: solo actualiza stock y estado visual.
     */
    public void actualizar(InventarioController inventario, EstadoJuego estado) {
        if (!fichasCreadas) {
            crearFichas(inventario, TipoIngrediente.BASE,    bandaBases);
            crearFichas(inventario, TipoIngrediente.SALSA,   bandaSalsas);
            crearFichas(inventario, TipoIngrediente.TOPPING, bandaToppings);
            fichasCreadas = true;
        }
        boolean jugando = estado == EstadoJuego.JUGANDO;
        actualizarEstados(inventario, jugando);
    }

    private void crearFichas(InventarioController inventario,
                             TipoIngrediente tipo, HBox banda) {
        List<Ingrediente> lista = inventario.getIngredientesPorTipo(tipo);
        for (Ingrediente ing : lista) {
            String nombre = ing.getNombre();
            int stock = inventario.getStock(nombre);

            Node icono = AssetManager.getIngredienteNode(nombre, 44);

            String nombreCorto = nombre.length() > 10 ? nombre.replace(" ", "\n") : nombre;
            Label lblNombre = new Label(nombreCorto);
            lblNombre.setFont(Font.font("Arial", 9));
            lblNombre.setTextFill(Color.web("#cccccc"));
            lblNombre.setTextAlignment(TextAlignment.CENTER);

            Label lblStock = new Label("x" + stock);
            lblStock.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            lblStock.setTextFill(Color.WHITE);

            VBox ficha = new VBox(3, icono, lblNombre, lblStock);
            ficha.setAlignment(Pos.CENTER);
            ficha.setPadding(new Insets(6));
            ficha.setPrefWidth(72);
            ficha.setPrefHeight(95);
            ficha.setStyle(ESTILO_ACTIVO);

            // Handler persistente: el estado se verifica en tiempo de click
            ficha.setOnMouseClicked(e -> manejarClick(nombre));
            ficha.setOnMouseEntered(e -> {
                if (!ESTILO_INACTIVO.equals(ficha.getStyle())) ficha.setStyle(ESTILO_HOVER);
            });
            ficha.setOnMouseExited(e -> {
                if (!ESTILO_INACTIVO.equals(ficha.getStyle())) ficha.setStyle(ESTILO_ACTIVO);
            });

            mapaFichas.put(nombre, ficha);
            mapaStockLabels.put(nombre, lblStock);
            banda.getChildren().add(ficha);
        }
    }

    private void actualizarEstados(InventarioController inventario, boolean jugando) {
        for (Map.Entry<String, VBox> entry : mapaFichas.entrySet()) {
            String nombre  = entry.getKey();
            VBox   ficha   = entry.getValue();
            int    stock   = inventario.getStock(nombre);
            boolean disponible = jugando && inventario.hayDisponibilidad(nombre);

            Label lblStock = mapaStockLabels.get(nombre);
            lblStock.setText("x" + stock);
            lblStock.setTextFill(stock > 3 ? Color.WHITE : Color.web("#ff6644"));

            ficha.setStyle(disponible ? ESTILO_ACTIVO : ESTILO_INACTIVO);
        }
    }

    private void manejarClick(String nombre) {
        boolean ok = controller.agregarIngrediente(nombre);
        if (!ok && feedbackCallback != null) {
            feedbackCallback.accept(determinarMotivo(nombre));
        }
    }

    private String determinarMotivo(String nombre) {
        CocinaController cocina = controller.getCocina();
        if (cocina.estaLibre()) {
            return "No hay orden activa en este momento";
        }
        if (!cocina.tieneBase()) {
            return "Primero agrega la base";
        }
        if (!cocina.tieneSalsa()) {
            return "Primero agrega la salsa antes de los toppings";
        }
        return "Sin disponibilidad de " + nombre;
    }

    private VBox seccion(String titulo, HBox banda) {
        Label lbl = new Label(titulo);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setTextFill(Color.web("#8888aa"));
        VBox sec = new VBox(5, lbl, banda);
        sec.setAlignment(Pos.CENTER_LEFT);
        return sec;
    }

    private Line separador() {
        Line sep = new Line(0, 0, 0, 80);
        sep.setStroke(Color.web("#334466"));
        sep.setStrokeWidth(1);
        return sep;
    }

    public HBox getRoot() {
        return root;
    }
}
