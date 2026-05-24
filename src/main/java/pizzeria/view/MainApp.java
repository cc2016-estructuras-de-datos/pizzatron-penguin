package pizzeria.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pizzeria.controller.JuegoController;

public class MainApp extends Application {

    private Stage primaryStage;
    private JuegoController controller;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Pizzatron");
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> Platform.exit());
        mostrarMenu();
        stage.show();
    }

    private void mostrarMenu() {
        MenuView menu = new MenuView(this::iniciarJuego);
        primaryStage.setScene(new Scene(menu.getRoot(), 1000, 700));
    }

    private void iniciarJuego() {
        controller = new JuegoController();
        controller.iniciarJuego();
        mostrarJuego();
    }

    private void mostrarJuego() {
        GameView game = new GameView(controller, this::mostrarGameOver);
        primaryStage.setScene(new Scene(game.getRoot(), 1000, 700));
        game.iniciar();
    }

    private void mostrarGameOver() {
        GameOverView go = new GameOverView(
            controller.getPuntaje(),
            this::iniciarJuego,
            () -> Platform.exit()
        );
        primaryStage.setScene(new Scene(go.getRoot(), 1000, 700));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
