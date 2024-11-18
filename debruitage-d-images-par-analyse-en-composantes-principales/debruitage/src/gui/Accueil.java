package gui;

import java.io.IOException;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Accueil extends Application {

  
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Page d'accueil");

        Label welcomeLabel = new Label("Bienvenue sur notre logiciel de bruitage et débruitage d'images");
        welcomeLabel.setFont(Font.font("Calibri Math", 20));
        welcomeLabel.setStyle("-fx-text-fill: #3d2b2b;");

        Button startButton = new Button("Commencer");
        startButton.setStyle(
                "-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(startButton);

        Label creditsLabel = new Label("Logiciel développé par Mohamed, Lisa, Hugo, Paul et Raphael");
        creditsLabel.setFont(Font.font("Calibri Math", 12));
        creditsLabel.setStyle("-fx-text-fill: #3d2b2b;");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #BDA18A, #f5efe6);");
        root.getChildren().addAll(welcomeLabel, startButton, creditsLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        startButton.setOnAction(event -> {
            // Rediriger vers la page ChoixImage.java
            ChoixImage choixImage = new ChoixImage();
            Stage choixImageStage = new Stage();
            try {
                choixImage.start(choixImageStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.close();
        });
    }

    private void applyButtonEffect(Button button) {
        DropShadow shadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(shadow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }
}
