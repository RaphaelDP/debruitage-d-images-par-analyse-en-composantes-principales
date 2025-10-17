package gui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Image;
import core.Critere;
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


public class Appli extends Application {


		
    public static void main(String[] args) {
        launch(args);
    }
	

	@Override
	public void start(Stage primaryStage) {
	    primaryStage.setTitle("Page d'accueil");

	    Label presa = new Label("Bienvenue sur notre logiciel de bruitage et débruitage d'images");
	    
	    Button go = new Button("Commencer");

	    VBox root = new VBox(20);
	    root.setAlignment(Pos.CENTER);
	    root.setPadding(new Insets(50));
	    root.setStyle("-fx-background-color: linear-gradient(to bottom, #9575cd, #673ab7);");
	    root.getChildren().addAll(presa, go);

	    Scene scene = new Scene(root, 1000, 800);
	    primaryStage.setScene(scene);
	    primaryStage.show();

	   go.setOnAction(event -> {
	        primaryStage.close();
	        try {
                ouvrirChoisirImage();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	  //  ouvrirTest();
	    });
	}


    public void ouvrirChoisirImage() throws IOException {
        
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Choix de l'image d'origine");
		
		ToolBar toolbar = new ToolBar();
		Button retour = new Button("retour");
		toolbar.getItems().addAll(retour);
		
		Label presa = new Label("Voici les images que nous pouvons manipuler, veillez cliquer sur l'image de votre choix");
		
		
		// Créer une grille pour organiser les images
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30); // Espacement horizontal entre les images
        gridPane.setVgap(10); // Espacement vertical entre les images
        gridPane.setPadding(new Insets(10)); // Marge autour de la grille
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        
        // Créer les ImageView pour les images

        BufferedImage bufferedLion = ImageIO.read(new File("./data/lion.jpg"));
        BufferedImage bufferedLena = ImageIO.read(new File("./data/lena.jpg"));
        BufferedImage bufferedfemmes = ImageIO.read(new File("./data/femmes.jpg"));


        ImageView imageView1 = new ImageView("file:./data/lion.jpg");
        ImageView imageView2 = new ImageView("file:./data/lena.jpg");
        ImageView imageView3 = new ImageView("file:./data/femmes.jpg");

        // Définir la taille des ImageView
        imageView1.setFitWidth(500);
        imageView1.setFitHeight(500);
        imageView2.setFitWidth(500);
        imageView2.setFitHeight(500);
        imageView3.setFitWidth(500);
        imageView3.setFitHeight(500);

        // Ajouter les ImageView à la grille
        gridPane.add(imageView1, 0, 0);
        gridPane.add(imageView2, 1, 0);
        gridPane.add(imageView3, 2, 0);
        
        VBox root = new VBox(20);
	    root.setAlignment(Pos.BASELINE_CENTER);
	    root.getChildren().addAll(toolbar, presa, gridPane);

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
	    primaryStage.setScene(scene);
	    primaryStage.show();
    

	    imageView1.setOnMouseClicked(event -> {
            ImageView imageChoisie = imageView1;
            BufferedImage bufferedChoisie = bufferedLion;
            primaryStage.close();
	    	ouvrirChoisirCaracteristiques(imageChoisie, bufferedChoisie);
	    });

        imageView2.setOnMouseClicked(event -> {
            ImageView imageChoisie = imageView2;
            BufferedImage bufferedChoisie = bufferedLena;
            ouvrirChoisirCaracteristiques(imageChoisie, bufferedChoisie);
            primaryStage.close();
	    	/*  
            try {
                ouvrirPageTest();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            */
	    });

        imageView3.setOnMouseClicked(event -> {
            ImageView imageChoisie = imageView3;
            BufferedImage bufferedChoisie = bufferedfemmes;
            primaryStage.close();
	    	ouvrirChoisirCaracteristiques(imageChoisie, bufferedChoisie);
	    });   
	}


    public VBox VBoxExtraction() {
        // Création des groupes de radio boutons pour chaque caractéristique
        ToggleGroup extractionGroup = new ToggleGroup();
        RadioButton globale = new RadioButton("Globale");
        RadioButton locale = new RadioButton("Locale");
        globale.setToggleGroup(extractionGroup);
        locale.setToggleGroup(extractionGroup);
    
        // Création de l'interface avec les caractéristiques et les options
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
    
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
        RadioButton Doux = new RadioButton("Doux");
        Dur.setToggleGroup(methodeSeuillageGroup);
        Doux.setToggleGroup(methodeSeuillageGroup);

        // Création de l'interface avec les caractéristiques et les options
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.addRow(0, new Label("Méthode de seuillage : "), Dur, Doux);

        VBox vbox = new VBox(20);
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
        gridPane.setPadding(new Insets(20));
        gridPane.addRow(0, new Label("Critère de seuillage : "), SV, SB);
    
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        // Stocker la référence du groupe de boutons radio dans les propriétés de vbox
        vbox.getProperties().put("tg", critereSeuillageGroup);
    
        return vbox;
    }
    

    public void ouvrirChoisirCaracteristiques(ImageView originalImage, BufferedImage bufferedOriginal) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Caractéristiques de débruitage");
    
        ToolBar toolbar = new ToolBar();
        Button retour = new Button("Changer d'image");
        toolbar.getItems().addAll(retour);
    
        Label presa = new Label("Veuillez choisir les caractéristiques de bruitages et de débruitage pour l'image suivante");
    
        VBox vBoxExtractPatch = VBoxExtraction();
        VBox vBoxMethodeSeuillage = VBoxMethodeSeuillage();
        VBox vBoxCritereSeuillage = VBoxCritereSeuillage();
    
        ToggleGroup extractionGroup = (ToggleGroup) vBoxExtractPatch.getProperties().get("tg");
        ToggleGroup methodeSeuillageGroup = (ToggleGroup) vBoxMethodeSeuillage.getProperties().get("tg");
        ToggleGroup critereSeuillageGroup = (ToggleGroup) vBoxCritereSeuillage.getProperties().get("tg");
    
        Label choixSigma = new Label("Veuillez saisir le bruit gaussien sigma que vous voulez affecter à l'image originale :");
        // Création du champ de texte pour l'entrée de l'utilisateur
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        // Création de la ligne contenant l'étiquette et le champ de texte
        HBox HboxSigma = new HBox(10);
        HboxSigma.setAlignment(Pos.CENTER);
        HboxSigma.setPadding(new Insets(10));
        HboxSigma.getChildren().addAll(choixSigma, textField);
    
        Label choixImagettes = new Label("Veuillez saisir le nombre d'imagettes :");
        // Création du champ de texte pour l'entrée de l'utilisateur
        TextField imagettesTextField = new TextField();
        imagettesTextField.setMaxWidth(50);
        // Création de la ligne contenant l'étiquette et le champ de texte
        HBox hboxImagettes = new HBox(10);
        hboxImagettes.setAlignment(Pos.CENTER);
        hboxImagettes.setPadding(new Insets(10));
        hboxImagettes.getChildren().addAll(choixImagettes, imagettesTextField);
    
        Button valider = new Button("Voir le résultat");
    
        VBox root = new VBox(20);
        root.setAlignment(Pos.BASELINE_CENTER);
        root.getChildren().addAll(toolbar, presa, originalImage, HboxSigma, vBoxExtractPatch, hboxImagettes, vBoxMethodeSeuillage, vBoxCritereSeuillage, valider);
    
        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    
        retour.setOnAction(e -> {
            primaryStage.close();
            try {
                ouvrirChoisirImage();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
        // Écouteur d'événement pour afficher ou masquer la ligne en fonction de la sélection de l'utilisateur
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
            try {
                primaryStage.close();
                ouvrirPageResultat(originalImage, bufferedOriginal, sigma, methodeExtraction, methodeSeuillage, critereSeuillage, nbImagettes);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }
        

    public void ouvrirPageResultat(ImageView originalImage, BufferedImage bufferedOriginal, double sigma, String methodeExtraction, String methodeSeuillage, String critereSeuillage, int nbImagettes) throws IOException {
        
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Caractéristiques de débruitage");    

        ToolBar toolbar = new ToolBar();
		Button retour = new Button("Changer d'image");
        Button choixCaracterisriquesButton = new Button("Changer les caractéristiques");
		toolbar.getItems().addAll(retour, choixCaracterisriquesButton);

        Label presa = new Label("Voici le triplet image d'origine, image bruitée et image reconstruite avec lees caractéristiques choisient");

        int[][] pixelMatrix = Image.imageToMatrix(bufferedOriginal);
        int[][] noisyPixelMatrix = Image.noising(pixelMatrix, sigma);
        BufferedImage bufferedNoisy = Image.matrixToImage(noisyPixelMatrix);
        Image.saveImage(bufferedNoisy, "./data/noisySigma.jpg");
	    ImageView noisyImage = new ImageView("file:./data/noisySigma.jpg");

        
        //Reconstruction de l'image avec les paramètres choisient
        int width = bufferedOriginal.getWidth();
        int height = bufferedOriginal.getHeight();

        BufferedImage bufferedReconstruite = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        if (methodeExtraction.equals("Globale")) {
			bufferedReconstruite = Image.imageDEN(bufferedNoisy, sigma, methodeSeuillage, critereSeuillage);
		}
        if (methodeExtraction.equals("Locale")) {
			bufferedReconstruite = Image.imageDENlocale(bufferedNoisy, sigma, methodeSeuillage, critereSeuillage,25);
		}        Image.saveImage(bufferedReconstruite, "./data/reconstructedImage.jpg");
        ImageView reconstructedImage = new ImageView("file:./data/reconstructedImage.jpg");

        originalImage.setFitWidth(500);
        originalImage.setFitHeight(500);
        noisyImage.setFitWidth(500);
        noisyImage.setFitHeight(500);
        reconstructedImage.setFitWidth(500);
        reconstructedImage.setFitHeight(500);


        Label label1 = new Label("Image d'origine");
        Label label2 = new Label("Image affecter d'un bruit gaussien de " + sigma);
        Label label3 = new Label("Image débruitée avec les caractéristiques choisies");

        // Créer une HBox pour contenir les ImageView et les labels
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        hbox.getChildren().addAll(
                new VBox(10, originalImage, label1),
                new VBox(10, noisyImage, label2),
                new VBox(10, reconstructedImage, label3)
        );


        double lambda = 0;
        if (critereSeuillage.equals("SV")) {
        	lambda = Critere.seuilVisuShrink(sigma, 512*512); // Définit la valeur du seuil lambda7

        } else {
        	lambda = Critere.seuilBayesShrink(sigma, Critere.varianceEstimeeSignal(bufferedNoisy));
        }

        Label labelSigma = new Label("Sigma : " + sigma); 
        Label labelMethodeExtraction = new Label("Méthode d'extraction de patch : " + methodeExtraction);
        Label labelMethodeSeuillage = new Label("Méthode de seuillage : " + methodeSeuillage);
        Label labelCritereSeuillage = new Label("Critere de seuillage : " + critereSeuillage + " = " + lambda);
        Label labelMSE = new Label("MSE : " + Critere.calculateMSE(bufferedOriginal, bufferedReconstruite));
        Label labelPSNR = new Label("PSNR : " + Critere.calculatePSNR(bufferedOriginal, bufferedReconstruite));


        VBox vboxCaracteristiques = new VBox(10);
        vboxCaracteristiques.setAlignment(Pos.CENTER);
        vboxCaracteristiques.setPadding(new Insets(20));
        vboxCaracteristiques.getChildren().addAll(labelSigma, labelMethodeExtraction, labelMethodeSeuillage, labelCritereSeuillage, labelMSE, labelPSNR);
        

        VBox root = new VBox(10);
	    root.setAlignment(Pos.BASELINE_CENTER);
	    root.getChildren().addAll(toolbar, presa, hbox, vboxCaracteristiques);

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
	    primaryStage.setScene(scene);
	    primaryStage.show();

        
        retour.setOnAction(e -> {
            primaryStage.close();
            try {
                ouvrirChoisirImage();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });


        choixCaracterisriquesButton.setOnAction(e -> {
            primaryStage.close();
            ouvrirChoisirCaracteristiques(originalImage, bufferedOriginal);
        });
    }



    public void ouvrirPageTest() throws IOException {

        Stage primaryStage = new Stage();
        primaryStage.setTitle("OUI");
    
        VBox vBoxExtractPatch = VBoxExtraction();
    
        VBox root = new VBox(20);
        root.setAlignment(Pos.BASELINE_CENTER);
        root.getChildren().addAll(vBoxExtractPatch);
    
        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(root, 1700, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    


    }
    
    



}
