package pizzeria.view.assets;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Capa de abstracción para los assets visuales.
 *
 * Intenta cargar una imagen PNG desde /assets/ingredientes/<nombre>.png.
 * Si no existe, devuelve un placeholder con forma geométrica y color
 * representativo del ingrediente.
 *
 * Cuando lleguen imágenes reales, colocarlas en:
 *   src/main/resources/assets/ingredientes/<nombre>.png
 * donde <nombre> es el nombre en minúsculas con espacios reemplazados por "_".
 * Ej: "Masa Clásica" → masa_clásica.png
 */
public class AssetManager {

    private static final Map<String, Color> COLORES = new HashMap<>();
    private static final Map<String, String> ABREVIACIONES = new HashMap<>();

    static {
        COLORES.put("Masa Clásica",  Color.web("#F5DEB3"));
        COLORES.put("Masa Integral", Color.web("#8B6347"));
        COLORES.put("Salsa Normal",  Color.web("#CC2200"));
        COLORES.put("Salsa Picante", Color.web("#FF4500"));
        COLORES.put("Pescado",       Color.web("#4682B4"));
        COLORES.put("Camarones",     Color.web("#FF69B4"));
        COLORES.put("Alga",          Color.web("#228B22"));
        COLORES.put("Calamar",       Color.web("#9370DB"));
        COLORES.put("Hielo",         Color.web("#B0E0FF"));

        ABREVIACIONES.put("Masa Clásica",  "MC");
        ABREVIACIONES.put("Masa Integral", "MI");
        ABREVIACIONES.put("Salsa Normal",  "SN");
        ABREVIACIONES.put("Salsa Picante", "SP");
        ABREVIACIONES.put("Pescado",       "Pe");
        ABREVIACIONES.put("Camarones",     "Ca");
        ABREVIACIONES.put("Alga",          "Al");
        ABREVIACIONES.put("Calamar",       "Sq");
        ABREVIACIONES.put("Hielo",         "Hi");
    }

    /**
     * Devuelve un nodo cuadrado (icono) para usar en la banda de ingredientes.
     * Intenta cargar la imagen; si falla, devuelve un círculo de color.
     */
    public static Node getIngredienteNode(String nombre, double tamanio) {
        String ruta = "/assets/ingredientes/" +
                nombre.toLowerCase().replace(" ", "_").replace("á","a")
                      .replace("é","e").replace("í","i")
                      .replace("ó","o").replace("ú","u") + ".png";
        InputStream stream = AssetManager.class.getResourceAsStream(ruta);
        if (stream != null) {
            ImageView iv = new ImageView(new Image(stream));
            iv.setFitWidth(tamanio);
            iv.setFitHeight(tamanio);
            iv.setPreserveRatio(true);
            return iv;
        }
        return crearIconoPlaceholder(nombre, tamanio);
    }

    /**
     * Devuelve un nodo elíptico para representar una capa de pizza en la cocina.
     */
    public static StackPane getIngredienteCapaNode(String nombre, double ancho) {
        Color color = COLORES.getOrDefault(nombre, Color.GRAY);

        Ellipse capa = new Ellipse(ancho / 2.0, 14);
        capa.setFill(color);
        capa.setStroke(color.darker());
        capa.setStrokeWidth(2);

        Label etiqueta = new Label(nombre);
        etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        etiqueta.setTextFill(color.getBrightness() > 0.65 ? Color.BLACK : Color.WHITE);
        etiqueta.setTextAlignment(TextAlignment.CENTER);

        StackPane pane = new StackPane(capa, etiqueta);
        pane.setPrefWidth(ancho);
        pane.setPrefHeight(28);
        return pane;
    }

    public static Color getColor(String nombre) {
        return COLORES.getOrDefault(nombre, Color.GRAY);
    }

    private static StackPane crearIconoPlaceholder(String nombre, double tamanio) {
        Color color = COLORES.getOrDefault(nombre, Color.GRAY);
        String abrev = ABREVIACIONES.getOrDefault(nombre, "?");

        Circle fondo = new Circle(tamanio / 2.0, color);
        fondo.setStroke(color.darker());
        fondo.setStrokeWidth(2);

        Label texto = new Label(abrev);
        texto.setFont(Font.font("Arial", FontWeight.BOLD, tamanio * 0.40));
        texto.setTextFill(color.getBrightness() > 0.65 ? Color.BLACK : Color.WHITE);

        StackPane pane = new StackPane(fondo, texto);
        pane.setPrefSize(tamanio, tamanio);
        return pane;
    }
}
