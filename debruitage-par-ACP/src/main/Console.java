package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import core.Critere;
import core.Image;
import core.Image.Imagette;

public class Console {

	public static void main(String[] args) throws IOException {		
		
		String LenaOriginal = "./data/lena.jpg" ;
        String outputDirectory = "./data/";
        BufferedImage bufferedOriginal = ImageIO.read(new File(LenaOriginal));
        
		System.out.println("Voila l'image originale qu'on va modifier");
		Image.displayImage(LenaOriginal);

		
		// Choix du bruit
		Scanner scanner1 = new Scanner(System.in);
		System.out.println("Veuillez choisir le bruit gaussien qu'on va affecter à l'image originale : ");
		double sigma = 0.0;
		try {
			sigma = scanner1.nextDouble();
		    // Faites quelque chose avec la valeur de l'entier
		} catch (InputMismatchException e) {
			System.out.println("Erreur de saisie. Veuillez entrer une valeur valide.");
		}
		
		//Bruitage et affichage de l'image bruitée
		
		int[][] matrixImage = Image.imageToMatrix(bufferedOriginal);
		int[][] matrixNoisy = Image.noising(matrixImage, sigma);
		BufferedImage bufferedSigma = Image.matrixToImage(matrixNoisy);
		
		//BufferedImage bufferedSigma = Image.noising(bufferedOriginal, sigma); NE MARCHE PAS DONC ON PASSSE EN MATRICE
        
        String outputFilename = "noisy_lena_" + sigma + ".jpg";
        String outputPath = outputDirectory + outputFilename; 
        Image.saveImage(bufferedSigma, outputPath);
		String LenaSigma = outputPath;
		Image.displayImage(LenaSigma);
		
		//Choix des critères pour reconstruire l'image bruitée
		
		//Choix de la méthode d'extraction de patch
		Scanner scanner2 = new Scanner(System.in);
		System.out.println("Veuillez choisir la méthode d'extraction des patchs :\n1 - Globale\n2 - Locale");
		int choixExtraction = 0;
		try {
			choixExtraction = scanner2.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Erreur de saisie. Veuillez entrer un entier valdie qui est proposé");
		}
		switch (choixExtraction) {
	    case 1:
	        System.out.println("Vous avez choisi l'extraction globale.");
	        break;
	    case 2:
	        System.out.println("Vous avez choisi l'extraction locale.");
	        break;
	    default:
	        System.out.println("Choix invalide. Veuillez choisir une option valide.");
	        break;
	}
			
		//Choix de la méthode d'extraction de patch
		Scanner scanner3 = new Scanner(System.in);
		System.out.println("Veuillez choisir la méthode de seuillage :\n1 - Seuillage dur\n2 - Seuillage doux");
		int choixMethodeSeuillage = 0;
		try {
			choixMethodeSeuillage = scanner3.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Erreur de saisie. Veuillez entrer un entier valdie qui est proposé");
		}
		switch (choixMethodeSeuillage) {
	    case 1:
	        System.out.println("Vous avez choisi le seuillage dur.");
	        break;
	    case 2:
	        System.out.println("Vous avez choisi le seuillage doux.");
	        break;
	    default:
	        System.out.println("Choix invalide. Veuillez choisir une option valide.");
	        break;
		}
		
		String methodeSeuillage = "";
		if (choixMethodeSeuillage == 1) {
			methodeSeuillage = "Dur";
		}
		if (choixMethodeSeuillage == 2) {
			methodeSeuillage = "Doux";
		}
		
		//Choix du critere pour le seuillage entre SV ET SB
		Scanner scanner4 = new Scanner(System.in);
		System.out.println("Veuillez choisir la méthode de seuillage :\n1 - SV\n2 - SB");
		int choixCritereSeuillage = 0;
		try {
			choixCritereSeuillage = scanner4.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Erreur de saisie. Veuillez entrer un entier valdie qui est proposé");
		}
		switch (choixCritereSeuillage) {
		    case 1:
		        System.out.println("Vous avez choisi le seuillage SV.");
		        break;
		    case 2:
		        System.out.println("Vous avez choisi le seuillage SB.");
		        break;
		    default:
		        System.out.println("Choix invalide. Veuillez choisir une option valide.");
		        break;
		}
		String critereSeuillage = "";
		if (choixMethodeSeuillage == 1) {
			critereSeuillage = "SV";
		}
		if (choixMethodeSeuillage == 2) {
			critereSeuillage = "SB";
		}
						
		int width = bufferedOriginal.getWidth();
		int height = bufferedOriginal.getHeight();

	    BufferedImage bufferedReconstruite = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		//Reconstruction de l'image avec les paramètre de l'utilisateur
		if ( choixExtraction == 1 ) {
			bufferedReconstruite = Image.imageDEN(bufferedSigma, sigma, methodeSeuillage, critereSeuillage);
		}
		if ( choixExtraction == 2 ) {
			Scanner scanner5 = new Scanner(System.in);
			System.out.println("En combien d'imagettes voulez vous découper l'image : ");
			int nbImagettes = 1;
			try {
				nbImagettes = scanner1.nextInt();
				// Faites quelque chose avec la valeur de l'entier
			} catch (InputMismatchException e) {
				System.out.println("Erreur de saisie. Veuillez entrer une valeur valide.");
			}
			bufferedReconstruite = Image.imageDENlocale(bufferedSigma, sigma, methodeSeuillage, critereSeuillage, nbImagettes);
		}
		/*
		else {
			bufferedReconstruite = Image.imageDENlocale(bufferedSigma, sigma, methodeSeuillage, critereSeuillage);
		}
		*/
		
		Image.saveImage(bufferedReconstruite, "./data/rebuildImage");
		Image.displayImage("./data/rebuildImage");
		
		
		//Affichage de l'erreur
		double mse = Critere.calculateMSE(bufferedOriginal, bufferedReconstruite);
		double psrn = Critere.calculatePSNR(bufferedOriginal, bufferedReconstruite);
		System.out.println("psnr = " + psrn + "\n mse = "+mse); 	
	}
	
}
