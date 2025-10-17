package gui;

import core.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javafx.scene.effect.DropShadow;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Resultat {

    private ImageView originalImage;
    private String adresseImageChoisie;
    private BufferedImage bufferedOriginal;
    private double sigma;
    private String methodeExtraction;
    private String methodeSeuillage;
    private String critereSeuillage;
    private int nbImagettes;

    public Resultat(String adresseImageChoisie, BufferedImage bufferedOriginalChoisie, double sigma,
            String methodeExtraction, String methodeSeuillage, String critereSeuillage, int nbImagettes) {
        this.originalImage = new ImageView("file:"+adresseImageChoisie);
        this.adresseImageChoisie=adresseImageChoisie;
        this.bufferedOriginal = bufferedOriginalChoisie;
        this.sigma = sigma;
        this.methodeExtraction = methodeExtraction;
        this.methodeSeuillage = methodeSeuillage;
        this.critereSeuillage = critereSeuillage;
        this.nbImagettes = nbImagettes;
    }

    public void start(Stage resultatStage) {
        resultatStage.setTitle("Caractéristiques de débruitage");

        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color:  #a27e61;");
        Button retour = new Button("Changer d'image");
        retour.setStyle(
                "-fx-font-size: 12px; -fx-padding: 8px 13px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(retour);
        Button choixCaracterisriquesButton = new Button("Changer les caractéristiques");
        choixCaracterisriquesButton.setStyle(
                "-fx-font-size: 12px; -fx-padding: 8px 13px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(choixCaracterisriquesButton);

        Label presa = new Label(
                "Voici le triplet image d'origine, image bruitée et image reconstruite avec lees caractéristiques choisient");
        presa.setFont(Font.font("Calibri Math", 35));
        presa.setStyle("-fx-text-fill: #3d2b2b;");

        int[][] pixelMatrix = Image.imageToMatrix(bufferedOriginal);
        int[][] noisyPixelMatrix = Image.noising(pixelMatrix, sigma);
        BufferedImage bufferedNoisy = Image.matrixToImage(noisyPixelMatrix);
        Image.saveImage(bufferedNoisy, "./data/noisySigma.jpg");
        ImageView noisyImage = new ImageView("file:./data/noisySigma.jpg");

        // Reconstruction de l'image avec les paramètres choisient
        int width = bufferedOriginal.getWidth();
        int height = bufferedOriginal.getHeight();

        BufferedImage bufferedReconstruite = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        try {
            if (methodeExtraction.equals("Globale")) {
                bufferedReconstruite = Image.imageDEN(bufferedNoisy, sigma, methodeSeuillage, critereSeuillage);
            }
            if (methodeExtraction.equals("Locale")) {
                bufferedReconstruite = Image.imageDENlocale(bufferedNoisy, sigma, methodeSeuillage, critereSeuillage,
                        nbImagettes);
            }
            Image.saveImage(bufferedReconstruite, "./data/reconstructedImage.jpg");
        } catch (IOException e) {
            // Gérer l'exception ici (par exemple, afficher un message d'erreur)
            e.printStackTrace();
        }

        ImageView reconstructedImage = new ImageView("file:./data/reconstructedImage.jpg");

        originalImage.setFitWidth(500);
        originalImage.setFitHeight(500);
        noisyImage.setFitWidth(500);
        noisyImage.setFitHeight(500);
        reconstructedImage.setFitWidth(500);
        reconstructedImage.setFitHeight(500);
        originalImage.setPreserveRatio(true);
        noisyImage.setPreserveRatio(true);
        reconstructedImage.setPreserveRatio(true);

        Label label1 = new Label("Image d'origine");
        Label label2 = new Label("Image affectée d'un bruit gaussien de " + sigma);
        Label label3 = new Label("Image débruitée avec les caractéristiques choisies");

        label1.setFont(Font.font("Calibri Math", 15));
        label1.setStyle("-fx-text-fill: #3d2b2b;");
        label1.setStyle("-fx-alignment: center;");

        label2.setFont(Font.font("Calibri Math", 15));
        label2.setStyle("-fx-text-fill: #3d2b2b;");
        label2.setStyle("-fx-alignment: center;");

        label3.setFont(Font.font("Calibri Math", 15));
        label3.setStyle("-fx-text-fill: #3d2b2b;");
        label3.setStyle("-fx-alignment: center;");

        // Créer une HBox pour contenir les ImageView et les labels
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        hbox.getChildren().addAll(
                new VBox(10, originalImage, label1),
                new VBox(10, noisyImage, label2),
                new VBox(10, reconstructedImage, label3));

        double lambda = 0;
        if (critereSeuillage.equals("SV")) {
            lambda = Critere.seuilVisuShrink(sigma, 512 * 512); // Définit la valeur du seuil lambda7

        } else {
            lambda = Critere.seuilBayesShrink(sigma, Critere.varianceEstimeeSignal(bufferedNoisy));
        }

        Label labelSigma = new Label("Sigma : " + sigma);
        Label labelMethodeExtraction = new Label("Méthode d'extraction de patch : " + methodeExtraction);
        Label labelMethodeSeuillage = new Label("Méthode de seuillage : " + methodeSeuillage);
        Label labelCritereSeuillage = new Label("Critere de seuillage : " + critereSeuillage + " = " + lambda);
        Label labelMSE = new Label("MSE : " + Critere.calculateMSE(bufferedOriginal, bufferedReconstruite));
        Label labelPSNR = new Label("PSNR : " + Critere.calculatePSNR(bufferedOriginal, bufferedReconstruite));

        labelSigma.setFont(Font.font("Calibri Math", FontWeight.BOLD,15));
        labelSigma.setStyle("-fx-text-fill: #3d2b2b;");

        labelMethodeExtraction.setFont(Font.font("Calibri Math",FontWeight.BOLD, 15));
        labelMethodeExtraction.setStyle("-fx-text-fill: #3d2b2b;");

        labelMethodeSeuillage.setFont(Font.font("Calibri Math", FontWeight.BOLD,15));
        labelMethodeSeuillage.setStyle("-fx-text-fill: #3d2b2b;");

        labelCritereSeuillage.setFont(Font.font("Calibri Math", FontWeight.BOLD,15));
        labelCritereSeuillage.setStyle("-fx-text-fill: #3d2b2b;");

        labelMSE.setFont(Font.font("Calibri Math",FontWeight.BOLD, 15));
        labelMSE.setStyle("-fx-text-fill: #3d2b2b;");

        labelPSNR.setFont(Font.font("Calibri Math",FontWeight.BOLD, 15));
        labelPSNR.setStyle("-fx-text-fill: #3d2b2b;");

        VBox vboxCaracteristiques = new VBox(20);
        vboxCaracteristiques.setAlignment(Pos.CENTER);
        vboxCaracteristiques.setPadding(new Insets(-80));
        vboxCaracteristiques.getChildren().addAll(labelSigma, labelMethodeExtraction, labelMethodeSeuillage,
        labelCritereSeuillage, labelMSE, labelPSNR);

        Button telechargerButton = new Button("Télécharger l'image reconstruite");
        telechargerButton.setStyle("-fx-font-size: 12px; -fx-padding: 8px 13px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(telechargerButton);

        toolbar.getItems().addAll(retour, choixCaracterisriquesButton, telechargerButton);
       
        // Créez une variable finale pour stocker la référence à bufferedReconstruite
        final BufferedImage finalBufferedReconstruite = bufferedReconstruite;

        telechargerButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer l'image reconstruite");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
            File file = fileChooser.showSaveDialog(resultatStage);
            if (file != null) {
                try {
                    ImageIO.write(finalBufferedReconstruite, "png", file);
                    System.out.println("Image reconstruite enregistrée avec succès.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #BDA18A, #f5efe6);");

        root.setAlignment(Pos.BASELINE_CENTER);
        root.getChildren().addAll(toolbar, presa, hbox, vboxCaracteristiques);

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
        resultatStage.setScene(scene);
        resultatStage.setResizable(false);
        resultatStage.show();

        retour.setOnAction(e -> {
            ChoixImage choixImage = new ChoixImage();
            Stage choixImageStage = new Stage();
            try {
                choixImage.start(choixImageStage);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            resultatStage.close();

        });

        choixCaracterisriquesButton.setOnAction(e -> {
            ChoixParametres choixParametres = new ChoixParametres(adresseImageChoisie, bufferedOriginal);
            Stage choixParametresStage = new Stage();
            choixParametres.start(choixParametresStage);
            resultatStage.close();

        });

    }

    private void applyButtonEffect(Button button) {
        DropShadow shadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(shadow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }

}