package application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas {
    private static final double UNIT_SIZE = 25;
    private static final double FIELD_SIZE = 16;
    private static final double TOP_MARGIN = 30;
    private static final double WIDTH = FIELD_SIZE * UNIT_SIZE;
    private static final double HEIGHT = FIELD_SIZE * UNIT_SIZE + TOP_MARGIN;

    private final GraphicsContext gc;

    private ArrayList<Vector2> sectionPos = new ArrayList<Vector2>();
    private Vector2 currentDir = new Vector2(0, 1);
    private Vector2 applePos = new Vector2(0, 0);
    private long timePassed = 0;
    private int score = 0;
    private boolean gameOver = false;

    public Game(Stage stage) {
        super(WIDTH, HEIGHT);
        gc = getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        setOnKeyPressed(this::onKeyPressed);

        sectionPos.add(new Vector2(0, 0));
        placeApple();
    }

    public void update(long deltaTime) {
        if (gameOver) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            gc.setFill(Color.WHITE);
            gc.fillText("GAME OVER", WIDTH / 2 - 80, HEIGHT / 2 - 30);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gc.fillText("Score: " + score, WIDTH / 2 - 30, HEIGHT / 2);
            return;
        }
        if (timePassed > 0) {
            timePassed -= deltaTime;
            return;
        }

        Vector2 prevTailPos = new Vector2(sectionPos.getLast());
        move();
        if (applePos.equals(sectionPos.getFirst())) {
            score++;
            sectionPos.add(new Vector2(prevTailPos));
            System.out.println(score);
            placeApple();
        }
        draw();

        if (hitWall() || hitSelf()) {
            setOnKeyPressed(this::onKeyPressedGameOver);
            gameOver = true;
        }

        timePassed = 250;
    }

    private void onKeyPressedGameOver(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            score = 0;
            setOnKeyPressed(this::onKeyPressed);
            sectionPos.clear();
            sectionPos.add(new Vector2(0, 0));
            currentDir = new Vector2(0, 1);
            placeApple();
            gameOver = false;
        }
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawSnake();
        drawApple();

        gc.setFont(new Font("Arial", 20));
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 20, 20);
    }

    private void move() {
        for (int i = sectionPos.size() - 1; i > 0; i--) {
            sectionPos.set(i, new Vector2(sectionPos.get(i - 1)));
        }
        sectionPos.getFirst().setX(sectionPos.getFirst().getX() + currentDir.getX());
        sectionPos.getFirst().setY(sectionPos.getFirst().getY() + currentDir.getY());
    }

    private void onKeyPressed(KeyEvent event) {
        Vector2 newDir;
        switch (event.getCode()) {
            case KeyCode.LEFT:
                newDir = new Vector2(-1, 0);
                break;
            case KeyCode.RIGHT:
                newDir = new Vector2(1, 0);
                break;
            case KeyCode.UP:
                newDir = new Vector2(0, -1);
                break;
            case KeyCode.DOWN:
                newDir = new Vector2(0, 1);
                break;
            default:
                return;
        }
        currentDir = newDir;
    }

    private void placeApple() {
        Random rand = new Random();
        applePos.setX(rand.nextInt(16));
        applePos.setY(rand.nextInt(16));
        draw();
    }

    private void drawApple() {
        gc.setFill(Color.RED);
        double headX = applePos.getX() * UNIT_SIZE;
        double headY = applePos.getY() * UNIT_SIZE + TOP_MARGIN;
        gc.fillRect(headX, headY, UNIT_SIZE, UNIT_SIZE);
    }

    private void drawSnake() {
        gc.setFill(Color.DARKGREEN);
        double headX = sectionPos.getFirst().getX() * UNIT_SIZE;
        double headY = sectionPos.getFirst().getY() * UNIT_SIZE + TOP_MARGIN;
        gc.fillRect(headX, headY, UNIT_SIZE, UNIT_SIZE);

        gc.setFill(Color.GREEN);
        for (int i = 1; i < sectionPos.size(); i++) {
            double bodyX = sectionPos.get(i).getX() * UNIT_SIZE;
            double bodyY = sectionPos.get(i).getY() * UNIT_SIZE + TOP_MARGIN;
            gc.fillRect(bodyX, bodyY, UNIT_SIZE, UNIT_SIZE);
        }
    }

    private boolean hitWall() {
        double headX = sectionPos.getFirst().getX();
        double headY = sectionPos.getFirst().getY();
        return headX < 0 || headX > 15 || headY < 0 || headY > 15;
    }

    private boolean hitSelf() {
        for (int i = 1;i < sectionPos.size();i++) {
            if (sectionPos.getFirst().equals(sectionPos.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static double getWIDTH() {
        return WIDTH;
    }

    public static double getHEIGHT() {
        return HEIGHT;
    }
}
