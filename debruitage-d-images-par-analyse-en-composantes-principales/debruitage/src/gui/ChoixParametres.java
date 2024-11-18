package gui;

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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ChoixParametres {

    private ImageView originalImage;
    private BufferedImage bufferedOriginal;
    private String adresseImageChoisie;

    public ChoixParametres(String adresseImageChoisie, BufferedImage bufferedOriginalChoisie) {
        this.bufferedOriginal = bufferedOriginalChoisie;
        this.adresseImageChoisie = adresseImageChoisie;
        this.originalImage = new ImageView("file:"+adresseImageChoisie);
    }

    public VBox VBoxExtraction() {
        // Création des groupes de radio boutons pour chaque caractéristique
        ToggleGroup extractionGroup = new ToggleGroup();
        RadioButton globale = new RadioButton("Globale");
        RadioButton locale = new RadioButton("Locale");

        globale.setFont(Font.font("Calibri Math", 15));
        globale.setStyle("-fx-text-fill: #3d2b2b;");

        locale.setFont(Font.font("Calibri Math", 15));
        locale.setStyle("-fx-text-fill: #3d2b2b;");

        // Ajoutez les RadioButtons au ToggleGroup
        globale.setToggleGroup(extractionGroup);
        locale.setToggleGroup(extractionGroup);

        // Création de l'interface avec les caractéristiques et les options
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));

        // Positionner les éléments dans la grille
        gridPane.addRow(0, new Label("Méthode d'extraction de patch : "), globale, locale);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        // Stocker la référence du groupe de boutons radio dans les propriétés de vbox
        vbox.getProperties().put("tg", extractionGroup);

        return vbox;
    }

    public VBox VBoxMethodeSeuillage() {
        // Création des groupes de radio boutons pour chaque caractéristique
        ToggleGroup methodeSeuillageGroup = new ToggleGroup();
        RadioButton Dur = new RadioButton("Dur");
        Dur.setFont(Font.font("Calibri Math", 15));
        Dur.setStyle("-fx-text-fill: #3d2b2b;");
        RadioButton Doux = new RadioButton("Doux");
        Doux.setFont(Font.font("Calibri Math", 15));
        Doux.setStyle("-fx-text-fill: #3d2b2b;");
        Dur.setToggleGroup(methodeSeuillageGroup);
        Doux.setToggleGroup(methodeSeuillageGroup);

        // Création de l'interface avec les caractéristiques et les options
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        Label labelMethodeSeuillage = new Label("Méthode de seuillage : ");
        labelMethodeSeuillage.setFont(Font.font("Calibri Math", 15));
        labelMethodeSeuillage.setStyle("-fx-text-fill: #3d2b2b;");

        gridPane.addRow(0, labelMethodeSeuillage, Dur, Doux);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        // Stocker la référence du groupe de boutons radio dans les propriétés de vbox
        vbox.getProperties().put("tg", methodeSeuillageGroup);

        return vbox;
    }

    public VBox VBoxCritereSeuillage() {
        // Création des groupes de radio boutons pour chaque caractéristique
        ToggleGroup critereSeuillageGroup = new ToggleGroup();
        RadioButton SV = new RadioButton("SV");
        RadioButton SB = new RadioButton("SB");
        SV.setToggleGroup(critereSeuillageGroup);
        SB.setToggleGroup(critereSeuillageGroup);

        // Création de l'interface avec les caractéristiques et les options
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        Label labelMethodeSeuillage = new Label("Critère de seuillage : ");
        labelMethodeSeuillage.setFont(Font.font("Calibri Math", 15));
        labelMethodeSeuillage.setStyle("-fx-text-fill: #3d2b2b;");

        gridPane.addRow(0, labelMethodeSeuillage, SV, SB);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        // Stocker la référence du groupe de boutons radio dans les propriétés de vbox
        vbox.getProperties().put("tg", critereSeuillageGroup);

        return vbox;
    }

    public void start(Stage choixParametresStage) {
        choixParametresStage.setTitle("Caractéristiques de débruitage");

        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color:  #a27e61;");
        Button retour = new Button("Changer d'image");
        retour.setStyle(
                "-fx-font-size: 12px; -fx-padding: 8px 13px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(retour);
        toolbar.getItems().addAll(retour);

        Label presa = new Label(
                "Veuillez choisir les caractéristiques de bruitages et de débruitage pour l'image suivante");
        presa.setFont(Font.font("Calibri Math", 35));
        presa.setStyle("-fx-text-fill: #3d2b2b;");

        VBox vBoxExtractPatch = VBoxExtraction();
        VBox vBoxMethodeSeuillage = VBoxMethodeSeuillage();
        VBox vBoxCritereSeuillage = VBoxCritereSeuillage();

        ToggleGroup extractionGroup = (ToggleGroup) vBoxExtractPatch.getProperties().get("tg");
        ToggleGroup methodeSeuillageGroup = (ToggleGroup) vBoxMethodeSeuillage.getProperties().get("tg");
        ToggleGroup critereSeuillageGroup = (ToggleGroup) vBoxCritereSeuillage.getProperties().get("tg");

        Label choixSigma = new Label(
                "Veuillez saisir le bruit gaussien sigma que vous voulez affecter à l'image originale :");

        choixSigma.setFont(Font.font("Calibri Math", 15));
        choixSigma.setStyle("-fx-text-fill: #3d2b2b;");
        // Création du champ de texte pour l'entrée de l'utilisateur
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        // Création de la ligne contenant l'étiquette et le champ de texte
        HBox HboxSigma = new HBox(10);
        HboxSigma.setAlignment(Pos.CENTER);
        HboxSigma.setPadding(new Insets(10));
        HboxSigma.getChildren().addAll(choixSigma, textField);

        Label choixImagettes = new Label("Veuillez saisir le nombre d'imagettes :");
        choixImagettes.setFont(Font.font("Calibri Math", 15));
        choixImagettes.setStyle("-fx-text-fill: #3d2b2b;");
        // Création du champ de texte pour l'entrée de l'utilisateur
        TextField imagettesTextField = new TextField();
        imagettesTextField.setMaxWidth(50);
        // Création de la ligne contenant l'étiquette et le champ de texte
        HBox hboxImagettes = new HBox(10);
        hboxImagettes.setAlignment(Pos.CENTER);
        hboxImagettes.setPadding(new Insets(10));
        hboxImagettes.getChildren().addAll(choixImagettes, imagettesTextField);

        Button valider = new Button("Voir le résultat");
        valider.setStyle(
                "-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to bottom, white, #d1ad9c); -fx-text-fill: black;");
        applyButtonEffect(valider);

        VBox root = new VBox(10);
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #BDA18A, #f5efe6);");
        root.getChildren().addAll(toolbar, presa, originalImage, HboxSigma, vBoxExtractPatch, hboxImagettes,
                vBoxMethodeSeuillage, vBoxCritereSeuillage, valider);

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
        choixParametresStage.setScene(scene);
        choixParametresStage.setResizable(false);
        choixParametresStage.show();

        retour.setOnAction(e -> {

            ChoixImage choixImage = new ChoixImage();
            Stage choixImageStage = new Stage();
            try {
                choixImage.start(choixImageStage);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            choixParametresStage.close();
        });

        // Écouteur d'événement pour afficher ou masquer la ligne en fonction de la
        // sélection de l'utilisateur
        extractionGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedExtraction = (RadioButton) extractionGroup.getSelectedToggle();
            String methodeExtraction = selectedExtraction.getText();
            if (methodeExtraction.equals("Locale")) {
                hboxImagettes.setVisible(true);
            } else {
                hboxImagettes.setVisible(false);
            }
        });

        valider.setOnAction(e -> {
            // Récupérer les choix de l'utilisateur
            double sigma = Double.parseDouble(textField.getText());
            int nbImagettes = 1;

            RadioButton selectedExtraction = (RadioButton) extractionGroup.getSelectedToggle();
            String methodeExtraction = selectedExtraction.getText();

            RadioButton selectedMethodeSeuillage = (RadioButton) methodeSeuillageGroup.getSelectedToggle();
            String methodeSeuillage = selectedMethodeSeuillage.getText();

            RadioButton selectedCritereSeuillage = (RadioButton) critereSeuillageGroup.getSelectedToggle();
            String critereSeuillage = selectedCritereSeuillage.getText();

            if (methodeExtraction.equals("Locale")) {
                nbImagettes = Integer.parseInt(imagettesTextField.getText());
                System.out.println("Nombre d'imagettes : " + nbImagettes);
            }

            System.out.println("Valeur de sigma : " + sigma);
            System.out.println("Méthode d'extraction sélectionnée : " + methodeExtraction);
            System.out.println("Méthode de seuillage sélectionnée : " + methodeSeuillage);
            System.out.println("Critère de seuillage sélectionné : " + critereSeuillage);
            System.out.println("nombre d'imagettes : " + nbImagettes);

            // Appeler une autre fonction avec les choix de l'utilisateur

            Resultat resultat = new Resultat(adresseImageChoisie, bufferedOriginal, sigma, methodeExtraction,
                    methodeSeuillage, critereSeuillage, nbImagettes);
            Stage resultatStage = new Stage();
            resultat.start(resultatStage);
            choixParametresStage.close();
        });
    }

    private void applyButtonEffect(Button button) {
        DropShadow shadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(shadow));
        button.setOnMouseExited(event -> button.setEffect(null));
    }

}

//
//
//