package application;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        Game game = new Game(stage);
        game.setFocusTraversable(true);
        root.getChildren().add(game);

        Scene scene = new Scene(root, Game.getWIDTH(), Game.getHEIGHT());

        stage.setTitle("Snake Test");
        stage.setScene(scene);
        stage.show();

        game.requestFocus();

        AnimationTimer timer = new AnimationTimer() {
            long lastTimeMillis = System.currentTimeMillis();

            @Override
            public void handle(long now) {
                long currentTimeMillis = System.currentTimeMillis();
                long deltaTimeMillis = currentTimeMillis - lastTimeMillis;
                game.update(deltaTimeMillis);
                game.render();
                lastTimeMillis = currentTimeMillis;
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
