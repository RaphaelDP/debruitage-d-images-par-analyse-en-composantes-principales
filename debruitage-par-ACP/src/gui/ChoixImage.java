package gui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import core.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ChoixImage {

    public void start(Stage choixImageStage) throws IOException {
        choixImageStage.setTitle("Choix de l'image d'origine");

        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color:  #a27e61;");
        Button retour = new Button("Retour");
        retour.setStyle(
                "-fx-font-size: 12px; -fx-padding: 8px 13px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(retour);
        retour.getStyleClass().add("button-primary");
        toolbar.getItems().addAll(retour);

        Label presa = new Label(
                "Voici les images que nous pouvons manipuler, veuillez cliquer sur l'image de votre choix");

        presa.setFont(Font.font("Calibri Math", 35));
        presa.setStyle("-fx-text-fill: #3d2b2b;");

        // Créer une grille pour organiser les images
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30); // Espacement horizontal entre les images
        gridPane.setVgap(10); // Espacement vertical entre les images
        gridPane.setPadding(new Insets(10)); // Marge autour de la grille
        gridPane.setAlignment(Pos.BASELINE_CENTER);

        // Créer les ImageView pour les images
        String adresseLion = "./data/lion.jpg" ;
        String adresseLena = "./data/lena.jpg" ;
        String adresseFemmes = "./data/femmes.jpg" ;


        BufferedImage bufferedLion = ImageIO.read(new File(adresseLion));
        BufferedImage bufferedLena = ImageIO.read(new File(adresseLena));
        BufferedImage bufferedFemmes = ImageIO.read(new File(adresseFemmes));


        ImageView imageView1 = new ImageView("file:"+adresseLion);
        ImageView imageView2 = new ImageView("file:"+adresseLena);
        ImageView imageView3 = new ImageView("file:"+adresseFemmes);

        // Définir la taille des ImageView
        imageView1.setFitWidth(500);
        imageView1.setFitHeight(500);
        imageView2.setFitWidth(500);
        imageView2.setFitHeight(500);
        imageView3.setFitWidth(525);
        imageView3.setFitHeight(500);

        // Ajouter les ImageView à la grille
        gridPane.add(imageView1, 0, 0);
        gridPane.add(imageView2, 1, 0);
        gridPane.add(imageView3, 2, 0);

        // Création de l'ImageView pour afficher l'image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);
        imageView.setEffect(new DropShadow(10, 5, 5, javafx.scene.paint.Color.GRAY));

        // Création du bouton pour importer une image
        Button importerImageButton = new Button("Importer une image");
        importerImageButton.setStyle(
                "-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(importerImageButton);
        importerImageButton.getStyleClass().add("button-primary");
        importerImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(choixImageStage);
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);

                // Création de la boîte de dialogue de confirmation
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Voulez-vous utiliser cette image ?");
                confirmationDialog.setGraphic(new ImageView(image));

                // Ajout des boutons "Oui" et "Non" à la boîte de dialogue
                confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                // Affichage de la boîte de dialogue et attente de la réponse
                ButtonType selectedOption = confirmationDialog.showAndWait().orElse(ButtonType.NO);

                if (selectedOption == ButtonType.YES) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(selectedFile);
                        System.out.println("L'utilisateur a choisi d'utiliser son image :" + selectedFile.toURI().toString().substring("file:".length()));
                        
                        ChoixParametres choixParametres = new ChoixParametres(selectedFile.toURI().toString().substring("file:".length()), bufferedImage);
                        Stage choixParametresStage = new Stage();
                        choixParametres.start(choixParametresStage);
                        choixImageStage.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    System.out.println("L'utilisateur a choisi de ne pas importer d'image");
                    // Faire autre chose ou ne rien faire
                }
            }
        });

        VBox root = new VBox(20);
        root.setAlignment(Pos.BASELINE_CENTER);
        root.getChildren().addAll(toolbar, presa, gridPane, importerImageButton);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #BDA18A, #f5efe6);");

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
     //   scene.getStylesheets().add("file:./data/fichier.css"); // Ajouter le fichier CSS
        choixImageStage.setScene(scene);
        choixImageStage.setResizable(false);
        choixImageStage.show();

        imageView1.setOnMouseClicked(event -> {
            ChoixParametres choixParametres = new ChoixParametres(adresseLion, bufferedLion);
            Stage choixParametresStage = new Stage();
            choixParametres.start(choixParametresStage);
            choixImageStage.close();
        });

        imageView2.setOnMouseClicked(event -> {
            ChoixParametres choixParametres = new ChoixParametres(adresseLena, bufferedLena);
            Stage choixParametresStage = new Stage();
            choixParametres.start(choixParametresStage);
            choixImageStage.close();
        });

        imageView3.setOnMouseClicked(event -> {
            ChoixParametres choixParametres = new ChoixParametres(adresseFemmes, bufferedFemmes);
            Stage choixParametresStage = new Stage();
            choixParametres.start(choixParametresStage);
            choixImageStage.close();
        });

        retour.setOnAction(event -> {
            Accueil accueil = new Accueil();
            Stage accueilStage = new Stage();
            accueil.start(accueilStage);
            choixImageStage.close();
        });
    }

    private void applyButtonEffect(Button button) {
        DropShadow shadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(shadow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }
}
