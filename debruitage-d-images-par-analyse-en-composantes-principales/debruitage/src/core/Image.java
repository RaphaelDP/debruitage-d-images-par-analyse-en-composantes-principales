package core;
import javax.imageio.ImageIO;




import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import core.PatchCollection.ResultMoyCovCenter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Image {

    public static int[][] imageToMatrix(BufferedImage bufferedOriginal) {
        int width = bufferedOriginal.getWidth();
        int height = bufferedOriginal.getHeight();
        int[][] pixels = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = bufferedOriginal.getRGB(x, y);
                int gray = (color >> 16) & 0xFF; // Convert to grayscale
                pixels[y][x] = gray;
            }
        }

        return pixels;
    }

    public static BufferedImage matrixToImage(int[][] pixels) {
        
    	int width = pixels[0].length;
        int height = pixels.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = pixels[y][x];
                // Vérification pour s'assurer que la valeur du pixel reste dans la plage attendue (pb si la valeur dépasse 255 a cause du bruit gaussien qu'on rajoute)
                int clampedValue = Math.max(0, Math.min(255, pixelValue));
                Color color = new Color(clampedValue, clampedValue, clampedValue);
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }
    
    
	public static int[][] noising(int[][] image, double sigma) {
        int nb_lignes = image.length;
        int nb_colonnes = image[0].length;
        int[][] noisyImage = new int[nb_lignes][nb_colonnes];

        Random random = new Random();

        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                double noise = random.nextGaussian() * sigma;
                int pixel = image[i][j] + (int) noise;
                noisyImage[i][j] = pixel;
            }
        }

        return noisyImage;
    }
	
	
	public static BufferedImage noising(BufferedImage image, double sigma) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    BufferedImage noisyImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

	    Random random = new Random();

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int pixel = image.getRGB(x, y) & 0xFF;

	            // Ajouter du bruit gaussien au pixel
	            double noise = random.nextGaussian() * sigma;

	            int noisyPixel = clamp((int) (pixel + noise), 0, 255);

	            // Recréer le pixel bruité dans l'image finale
	            noisyImage.setRGB(x, y, (noisyPixel << 16) | (noisyPixel << 8) | noisyPixel);
	        }
	    }

	    return noisyImage;
	}

	// Fonction utilitaire pour limiter la valeur entre une borne inférieure et une borne supérieure
	private static int clamp(int value, int min, int max) {
	    return Math.max(min, Math.min(value, max));
	}

	
	
    public static void saveImage(BufferedImage image, String outputPath) {
        try {
            File output = new File(outputPath);
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void displayImage(String imagePath) {
        // Crée une nouvelle JFrame
        JFrame frame = new JFrame("Affichage d'image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crée un JLabel pour afficher l'image
        JLabel label = new JLabel();
        ImageIcon imageIcon = new ImageIcon(imagePath);
        label.setIcon(imageIcon);

        // Ajoute le JLabel à la JFrame
        frame.getContentPane().add(label, BorderLayout.CENTER);

        // Redimensionne la JFrame en fonction de la taille de l'image
        frame.pack();

        // Centre la JFrame sur l'écran
        frame.setLocationRelativeTo(null);

        // Rend la JFrame visible
        frame.setVisible(true);
    }
    
    
    //Mehtode locale avec les imagettes
    
    public static class Imagette {
        private BufferedImage image;
        private int x;
        private int y;

        public Imagette(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        public BufferedImage getImage() {
            return image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static ArrayList<Imagette> decoupeImage(BufferedImage X, int tailleImagette, int nbImagettes) {
        int imageWidth = X.getWidth();
        int imageHeight = X.getHeight();

        Random random = new Random();
        ArrayList<Imagette> imagettes = new ArrayList<>();

        for (int i = 0; i < nbImagettes; i++) {
            int x = random.nextInt(imageWidth - tailleImagette);
            int y = random.nextInt(imageHeight - tailleImagette);
            BufferedImage imagette = X.getSubimage(x, y, tailleImagette, tailleImagette);
            Imagette imagetteObj = new Imagette(imagette, x, y);
            imagettes.add(imagetteObj);
        }

        return imagettes;
    }
    
    public static ArrayList<Imagette> decoupeImageEnQuatre(BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int tailleImagetteX = imageWidth / 2;
        int tailleImagetteY = imageHeight / 2;

        ArrayList<Imagette> imagettes = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int x = i * tailleImagetteX;
                int y = j * tailleImagetteY;
                BufferedImage imagette = image.getSubimage(x, y, tailleImagetteX, tailleImagetteY);
                Imagette imagetteObj = new Imagette(imagette, x, y);
                imagettes.add(imagetteObj);
            }
        }

        return imagettes;
    }

    public static ArrayList<Imagette> decoupeImageSelonCarreParfait(BufferedImage image, int carreParfait) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
    
        int nbImagettesHauteur = (int) (Math.sqrt(carreParfait));
        //int tailleImagetteY = (int) (imageHeight / (Math.sqrt(carreParfait)));

        int tailleImagetteX = imageWidth / nbImagettesHauteur;
        int tailleImagetteY = imageHeight / nbImagettesHauteur;
    
        ArrayList<Imagette> imagettes = new ArrayList<>();
    
        for (int i = 0; i < nbImagettesHauteur; i++) {
            for (int j = 0; j < nbImagettesHauteur; j++) {
                int x = i * tailleImagetteX;
                int y = j * tailleImagetteY;
                BufferedImage imagette = image.getSubimage(x, y, tailleImagetteX, tailleImagetteY);
                Imagette imagetteObj = new Imagette(imagette, x, y);
                imagettes.add(imagetteObj);
            }
        }
    
        return imagettes;
    }

    
    public static BufferedImage reconstituerImage(ArrayList<Imagette> imagettes, int largeurImage, int hauteurImage) {
        BufferedImage imageReconstituee = new BufferedImage(largeurImage, hauteurImage, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = imageReconstituee.createGraphics();

        for (Imagette imagette : imagettes) {
            BufferedImage imagetteImage = imagette.getImage();
            int x = imagette.getX();
            int y = imagette.getY();

            g2d.drawImage(imagetteImage, x, y, null);
        }

        g2d.dispose();

        return imageReconstituee;
    }

    
    public PatchCollection vectorPatchs(ArrayList<Imagette> imagettes, int s) {
        PatchCollection vectorPatchs = new PatchCollection();

        for (Imagette imagette : imagettes) {
            BufferedImage image = imagette.getImage();
            
            //Position de l'imagette
            int x = imagette.getX();  
            int y = imagette.getY();

            int imagetteWidth = image.getWidth();
            int imagetteHeight = image.getHeight();

            int patchSize = s * s;
            double[] patchPixels = new double[patchSize];

            int patchIndex = 0;
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    int pixelX = x + (j * (imagetteWidth / s));
                    int pixelY = y + (i * (imagetteHeight / s));

                    int rgb = image.getRGB(pixelX, pixelY);
                    int gray = (rgb >> 16) & 0xFF; 

                    patchPixels[patchIndex++] = gray;
                }
            }

            Patch patch = new Patch(patchPixels);
            int[] position = new int[] {x, y};

            vectorPatchs.addPatch(patch, position);
        }

        return vectorPatchs;
    }

	public static PatchCollection reconctructionSeuillage(Patch vecteurMoyen, PatchCollection Vcontrib, PatchCollection baseVecteurs,double sigma, String methodeSeuillage, String critereSeuillage) throws IOException {
	    PatchCollection reconstruction = new PatchCollection(); // Crée une nouvelle collection de patches pour la reconstruction

	    int M = Vcontrib.getPatches().size(); // Récupère le nombre de patches dans Vcontrib
	    int s2= vecteurMoyen.getPatchSize();
	    /*
	     //Affichage vecteur moyen
	    
	    for (int j = 0; j < s2;j++) {
	    	System.out.println(vecteurMoyen.getPixel(j));
	    }
	    */
        BufferedImage bufferedNoisy = ImageIO.read(new File("./data/noisySigma.jpg"));

	    double lambda = 0.0;
        if (critereSeuillage.equals("SV")) {
        	lambda = Critere.seuilVisuShrink(sigma, 512*512); // Définit la valeur du seuil lambda7

        } else {
        	lambda = Critere.seuilBayesShrink(sigma*sigma, Critere.varianceEstimeeSignal(bufferedNoisy)); 

        }

	    
	    for (int k = 0; k<M; k++) {
	    	double[] vecteurPatch=new double[s2];
	        Patch Zpatch = new Patch(vecteurPatch); // Crée un nouveau patch Zpatch égal au vecteurMoyen
	        Patch alphaKpatch = Vcontrib.getPatches().get(k); // Récupère le patch alphaK à l'indice i dans Vcontrib
	        int[] alphaKpos = Vcontrib.getPatchPositions().get(k); // Récupère la position du patch alphaK à l'indice i dans Vcontrib
	        	        
		    for (int i = 0; i < s2; i++) {
		    	Zpatch.setPixel(i, vecteurMoyen.getPixel(i));
	
		        for (int j = 0; j < s2; j++) { // Parcourt les indices j de 0 à 48 (ou toute autre valeur appropriée)
			        Patch uj = baseVecteurs.getPatches().get(j); // Récupère le patch uj à l'indice j dans baseVecteurs

			        if (methodeSeuillage.equals("Dur")) {
			            Zpatch.setPixel(i, alphaKpatch.seuillageDur(j, lambda) * uj.getPixel(i));

			        } else {
			            Zpatch.setPixel(i, alphaKpatch.seuillageDoux(j, lambda) * uj.getPixel(i));

			        }
		            // Calcule la valeur du pixel Zpatch[i] en utilisant la formule : 
		            // vecteurMoyen[i] + somme(j=0;j<s2;j++){alphaKpatch.SeuillageDur(j, lambda) * uj[i]}
		            /*
		            if (k<2) {
		            	System.out.println(alphaKpatch.getPixel(j));
		            	System.out.println(alphaKpatch.SeuillageDur(j, lambda));
		            }
		            if (k<2 & j==2) {
		            	System.out.println("u2 =" + uj.getPixel(i));
		            }
		            */
		            
		        }
		        /*
	            if (k<2) {
		        	System.out.println("Patch" +k+ "pixel"+ i+ ":" + Zpatch.getPixels()[i]);
		        } 
		        */
		    
		    }
	        reconstruction.addPatch(Zpatch, alphaKpos); // Ajoute le patch Zpatch à la reconstruction avec sa position alphaKpos

	    }
	    //System.out.println("lambda =" + lambda);
	    //PatchCollection.displayPatchCollection(reconstruction);
	    return reconstruction; // Retourne la reconstruction complète
	    
	}
	
	public static BufferedImage imageDEN(BufferedImage bufferedSigma, double sigma, String methodeSeuillage, String critereSeuillage) throws IOException {
		int largeur = bufferedSigma.getWidth(); 
		int hauteur = bufferedSigma.getHeight(); 
		PatchCollection patchCollectionTest = PatchCollection.extractPatches(bufferedSigma, 7);
		ResultMoyCovCenter resultTest = PatchCollection.moyCov(patchCollectionTest);
		PatchCollection vecteursCentres = resultTest.getCenteredCollection();
		PatchCollection baseVecteurs = OutilsMathematiques.calculACP(patchCollectionTest);
		PatchCollection collectionProjeted = OutilsMathematiques.projeterContributions(baseVecteurs, vecteursCentres);
		PatchCollection collectionReconstruite = reconctructionSeuillage(resultTest.getMeanVector(), collectionProjeted, baseVecteurs, sigma, methodeSeuillage, critereSeuillage);
		BufferedImage imageReconstruite = PatchCollection.reconstructPatchs2(collectionReconstruite, largeur, hauteur);
		
		return imageReconstruite;
	}
		
	public static BufferedImage imageDENlocale(BufferedImage bufferedSigma, double sigma, String methodeSeuillage, String critereSeuillage, int nbImagettes) throws IOException {
		int largeur = bufferedSigma.getWidth(); 
		int hauteur = bufferedSigma.getHeight(); 

		//ArrayList<Imagette> imagettes = Image.decoupeImageEnQuatre(bufferedSigma);
           		        
        ArrayList<Imagette> imagettes = Image.decoupeImageSelonCarreParfait(bufferedSigma, nbImagettes);
        ArrayList<Imagette> imagettesModifiees = new ArrayList<>();

        // Appliquer la fonction imageDEN à chaque imagette
        for (Imagette imagette : imagettes) {
            // Obtenir l'image de l'imagette
            BufferedImage bufferedSigmaa = imagette.getImage();

            // Appliquer la fonction imageDEN à l'imagette
            BufferedImage imagetteModifiee = Image.imageDEN(bufferedSigmaa, sigma, methodeSeuillage, critereSeuillage);

            // Créer une nouvelle imagette avec l'image modifiée et les mêmes coordonnées
            Imagette imagetteModifieeObj = new Imagette(imagetteModifiee, imagette.getX(), imagette.getY());

            // Ajouter l'imagette modifiée à la liste
            imagettesModifiees.add(imagetteModifieeObj);
        }

        // Utiliser les imagettes modifiées pour la reconstitution de l'image
        BufferedImage imageReconstituee = Image.reconstituerImage(imagettesModifiees, largeur, hauteur);

		return imageReconstituee;

	}
		
		
		
		
		
			



    public static void main(String[] args) throws IOException {
        // Chemin de l'image à traiter
        String imagePath = "./data/lena.jpg";

        BufferedImage image = ImageIO.read(new File(imagePath));

        ArrayList<Imagette> imagettes = decoupeImageEnQuatre(image);

        // Test de la fonction reconstituerImage
        BufferedImage imageReconstituee = reconstituerImage(imagettes, image.getWidth(), image.getHeight());

        // Sauvegarde de l'image reconstituée
        String imageReconstitueePath = "./data/reconstituee.jpg";
            
        saveImage(imageReconstituee, imageReconstitueePath);
    	displayImage(imageReconstitueePath);

        System.out.println("Image découpée et reconstituée avec succès.");

    }























}