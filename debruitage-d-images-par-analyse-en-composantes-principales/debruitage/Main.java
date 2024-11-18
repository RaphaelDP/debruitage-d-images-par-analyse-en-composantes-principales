import gui.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import core.*;
import core.PatchCollection.ResultMoyCovCenter;


public class Main extends Application {

	public static void main(String[] args) throws IOException {

    launch(args);


		
		/*
		String LenaOriginal = "./data/lena.jpg" ;
        String outputDirectory = "./data/";
		String Lena20 = "./data/noisy_lena_20.0.jpg" ;
		
		BufferedImage bufferedOriginal = ImageIO.read(new File(LenaOriginal));
		
		
        int[][] pixelMatrix = Image.imageToMatrix(bufferedOriginal);
        
        int[][] noisyPixelMatrix = Image.noising(pixelMatrix, 20.0);
        BufferedImage noisyImage = Image.matrixToImage(noisyPixelMatrix);
        

        String outputFilename = "noisy_lena_" + 20.0 + ".jpg";
        String outputPath = outputDirectory + outputFilename;
        Image.saveImage(noisyImage, outputPath);
		
		BufferedImage buffered20 = ImageIO.read(new File(Lena20));
		
		//Affichage des images
		//Image.displayImage(LenaOriginal);
		//Image.displayImage(Lena20);
		
		//AFFICHAGE DES PATCHS		
		PatchCollection patchCollectionTest = PatchCollection.extractPatches(buffered20, 7);
		System.out.println("L'extraction est faite");
		
		ArrayList<Patch> patchTest=patchCollectionTest.getPatches();
		 for (int i=0; i<2;i++) {
		 
			patchTest.get(i).displayPatch(patchTest.get(i));
		}
		
		//PatchCollection.displayPatchCollection(patchCollectiontest);
	    
		
		//Test MoyCov
		ResultMoyCovCenter resultTest = PatchCollection.moyCov(patchCollectionTest);
		System.out.println("Moy Cov est calculée");
		
		PatchCollection vecteursCentres = resultTest.getCenteredCollection();
		System.out.println("Vecteurs centrés récupérés");
		
		//PatchCollection.afficherResultat(resultTest);
		
		
		
		
		// ACP
		
		PatchCollection baseVecteurs = OutilsMathematiques.calculACP(patchCollectionTest);
		System.out.println("ACP calculée");
		 //Test orthonormalisation de la base
		double somme;
		for (int i=0; i<baseVecteurs.getPatches().get(0).getSize();i++) {
			for (int j=0; j<baseVecteurs.getPatches().get(0).getSize();j++) {
				somme = 0;
				for (int k=0; k<baseVecteurs.getPatches().get(0).getSize();k++) {
					somme+=baseVecteurs.getPatches().get(i).getPixel(k)*baseVecteurs.getPatches().get(j).getPixel(k);
				}
				System.out.println(somme);
			}
			
		}
		
		//utils.displayVectorsFromACP(baseVecteurs);
		
		//PatchCollection.displayPatchCollection(baseVecteurs);

		
		// Test projection
		

		PatchCollection collectionProjeted = OutilsMathematiques.projeterContributions(baseVecteurs, vecteursCentres);
		System.out.println("Projection faite");
		
		
		Patch patchTest=patchCollectionTest.getPatch(0,0);
		Patch patchTest2=resultTest.getMeanVector();
		for(int i=0; i<patchTest.getSize(); i++) {
			patchTest2=patchTest2.add(baseVecteurs.getPatches().get(i).multiply(collectionProjeted.getPatch(0, 0).getPixel(i)));
			System.out.println(" ajout "+i);
			patchTest.displayPatch(baseVecteurs.getPatches().get(i).multiply(collectionProjeted.getPatch(0, 0).getPixel(i)));
		}
	
		
		patchTest2.displayPatch(patchTest2);
		
		patchTest.displayPatch(patchTest);

		
		patchTest2.displayPatch(resultTest.getMeanVector());
		
		
		
		
		//double pixel = collectionProjeted.getPatches().get(156).getPixel(30);
		// Récupère la valeur du pixel à la position (156, 30) dans la collection "collectionProjeted"
	//	System.out.println("taille collectionprojeted = " + pixel);

		
		//PatchCollection.displayPatchCollection(collectionProjeted);

		
		double sigma = 20.0;
		
		//PatchCollection collectionReconstruite = Image.reconctructionSeuillage(resultTest.getMeanVector(), collectionProjeted, baseVecteurs, sigma, "Dur", "PV");
		PatchCollection collectionReconstruite = Image.reconctructionSeuillage(resultTest.getMeanVector(), collectionProjeted, baseVecteurs, sigma, "Dur", "PV");
		System.out.println("Reconstruction des patchs avec seuillage faite");
		
		//PatchCollection.displayPatchCollection(collectionReconstruite);

		BufferedImage imageReconstruite = PatchCollection.reconstructPatchs2(collectionReconstruite, 512, 512);
		System.out.println("Reconstruction image avec nv patchs fait");

		Image.saveImage(imageReconstruite, "./data/imageReconstruiteee.jpg");
		// Sauvegarde l'image reconstruite dans un fichier JPEG avec le chemin "./data/imageReconstruiteee.jpg"

		Image.displayImage("./data/imageReconstruiteee.jpg");
		// Affiche l'image reconstruite à l'écran
	        
		//Reconstruction d'une image a partir des patchs    
		
		BufferedImage imageFromPatches = PatchCollection.reconstructPatchs(collectionReconstruite,512, 512);
		Image.saveImage(imageFromPatches, outputDirectory+"/testRebuildImage.jpg");
		Image.displayImage(outputDirectory+"/testRebuildImage.jpg");	
	 
		//Test du Critère
		
		double mse = Critere.calculateMSE(bufferedOriginal, imageReconstruite);
		double psrn = Critere.calculatePSNR(bufferedOriginal, imageReconstruite);
		System.out.println("psnr = " + psrn + "\n mse = "+mse); //Pb car MSE négatif ??
	
	
		
		double[] sigmas = {10.0, 20.0, 30.0, 50.0};
        
        try {
            BufferedImage originalImage = ImageIO.read(new File(LenaOriginal));
            int[][] pixelMatrix = Image.ImageToMatrix(originalImage);

            for (double sigma : sigmas) {
                int[][] noisyPixelMatrix = Image.noising(pixelMatrix, sigma);
                BufferedImage noisyImage = Image.MatrixToImage(noisyPixelMatrix);

                String outputFilename = "noisy_lena_" + sigma + ".jpg";
                String outputPath = outputDirectory + outputFilename;
                Image.saveImage(noisyImage, outputPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        

	   */
	}

	@Override
    public void start(Stage primaryStage) {
        // Créer une instance de la classe LoginPage
        Accueil accueil = new Accueil();
        // Appeler la méthode start de la classe LoginPage pour afficher la page de connexion
        accueil.start(primaryStage);
    }


}





