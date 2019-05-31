package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.Timer;


public class Controller {
    @FXML
    private Pane root;
    public Circle ball;
    public Rectangle botPaddle;
    public Rectangle playerPaddle;
    public Label scores;
    private double vectorY;
    private double vectorX;
    private boolean game = true;
    private boolean moving = false;
    private int cloresPlayerPaddle = 0;
    private int cloresBotPaddle = 0;
    private final Timer timer = new Timer();
    private int move = 4;
    private StringProperty score = new SimpleStringProperty(cloresPlayerPaddle + ":" + cloresBotPaddle);

    public void keyEventListener(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
            game = true;
            ball.setLayoutY(185);
            ball.setLayoutX(284);
            ball.setVisible(true);
            vectorY = getRandomVectorY(move);
        }
    }


    private void moveBall() {
        if (game) {
            if (botPaddle.getBoundsInParent().intersects(ball.getBoundsInParent()) || playerPaddle.getBoundsInParent().intersects(ball.getBoundsInParent())) {
                vectorX = -vectorX;
            }
            if (ball.getLayoutX() <= 16.5) {
                ++cloresPlayerPaddle;
                score.set(cloresPlayerPaddle + " : " + cloresBotPaddle);
                ball.setVisible(false);
                game = false;
            }
            if (ball.getLayoutX() >= 551.5) {
                ++cloresBotPaddle;
                ball.setVisible(false);
                score.set(cloresPlayerPaddle + " : " + cloresBotPaddle);

                game = false;
            }
            if (ball.getLayoutY() <= 35 || ball.getLayoutY() >= 335) {
                vectorY = -vectorY;
            }
            ball.setLayoutY(ball.getLayoutY() + vectorY);
            ball.setLayoutX(ball.getLayoutX() + vectorX);
            if (botPaddle.getY() < (ball.getLayoutY() - 180 - botPaddle.getHeight() / 2) || botPaddle.getY() > (ball.getLayoutY() - 180 + botPaddle.getHeight() / 2)) {
                botPaddle.setY(botPaddle.getY() + vectorY);
            }
        } else {
        }
    }

    private double getRandomVectorY(int move) {
        double h = Math.sqrt(4 * Math.pow(move * root.getPrefHeight(), 2) / (Math.pow(root.getPrefWidth(), 2) + Math.pow(root.getPrefHeight(), 2)));
        return Math.random() * h - h / 2;
    }

    @FXML
    private void initialize() {
        score.set(scores.textProperty().get());
        scores.textProperty().bind(score);
        vectorY = getRandomVectorY(move);
        Random random = new Random();
        boolean left = random.nextBoolean();
        double h = Math.sqrt(4 * Math.pow(move * root.getPrefHeight(), 2) / (Math.pow(root.getPrefWidth(), 2) + Math.pow(root.getPrefHeight(), 2)));
        vectorY = Math.random() * h - h / 2;
        vectorX = Math.sqrt(Math.pow(move, 2) - Math.pow(vectorY, 2));
        if (left) {
            vectorX = -vectorX;
        }

        Timeline timeline2 = new Timeline();
        Timeline timeline3 = new Timeline();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), ev -> {
            moveBall();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        EventHandler<KeyEvent> handler = (KeyEvent event) -> {
            if (!moving) {
                moving = true;
                if (event.getCode() == KeyCode.UP) {
                    timeline2.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.015), ev -> Platform.runLater(()
                            -> {
                        if (playerPaddle.getY() - playerPaddle.getHeight() / 2 > -160) {
                            playerPaddle.setY(playerPaddle.getY() - 2);
                        }
                    })));
                    timeline2.setCycleCount(Animation.INDEFINITE);
                    timeline2.play();
                }


                if (event.getCode() == KeyCode.DOWN) {
                    timeline3.getKeyFrames().setAll(new KeyFrame(Duration.seconds(0.015), ev -> Platform.runLater(()
                            -> {
                        if (playerPaddle.getY() + playerPaddle.getHeight() / 2 < 160) {
                            playerPaddle.setY(playerPaddle.getY() + 2);
                        }
                    })));
                    timeline3.setCycleCount(Animation.INDEFINITE);
                    timeline3.play();
                }
            }
        };


        javafx.event.EventHandler<javafx.scene.input.KeyEvent> stopHandler = event -> {
            timeline2.stop();
            timeline3.stop();
            timer.purge();
            moving = false;
        };
        if (root.getScene() != null) {
            root.getScene().addEventHandler(KeyEvent.KEY_RELEASED, stopHandler);
            root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
        } else {
            root.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    root.getScene().addEventHandler(KeyEvent.KEY_RELEASED, stopHandler);
                    root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
                }
            });
        }
    }
}