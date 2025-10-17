package core;


import java.awt.image.BufferedImage;	
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;



public class PatchCollection {

    private ArrayList<int[]> positions;
    private ArrayList<Patch> patches = new ArrayList<Patch>();

    public PatchCollection() {
        patches = new ArrayList<>();
        positions = new ArrayList<>();
    }

    public void addPatch(Patch patch, int[] position) {
        patches.add(patch);
        positions.add(position);
    }

    public ArrayList<Patch> getPatches() {
        return patches;
    }

    public ArrayList<int[]> getPatchPositions() {
        return positions;
    }
    
    
    public Patch getPatch(int i, int j) {
        for (int k = 0; k < positions.size(); k++) {
            int[] position = positions.get(k);
            if (position[0] == i && position[1] == j) {
                return patches.get(k);
            }
        }
        return null;
    }
    
    public static PatchCollection convertirListeDoubleEnPatchCollection(ArrayList<double[]> baseVecteurs) {
    	PatchCollection patchCollection = new PatchCollection();
    	for (double[] baseVecteur : baseVecteurs) {
    		double[] pixels = new double[baseVecteur.length];
    		for (int i=0; i< baseVecteur.length;i++) {
    			pixels[i] = (int) baseVecteur[i];
    		}
    		Patch patch = new Patch(pixels);
    		// Définissez les positions des patchs selon vos besoins
    		int[] position = new int[] {0, 0}; // Exemple de position (0, 0)
    		patchCollection.addPatch(patch, position);
    	}
    	return patchCollection;
    }

    public static PatchCollection convertirListeDoubleEnPatchCollection(double[][] baseVecteurs, ArrayList<int[]> positions) {
        PatchCollection patchCollection = new PatchCollection();
        int indice =0;
        for (double[] baseVecteur : baseVecteurs) {
            double[] pixels = new double[baseVecteur.length];
            for (int i = 0; i < baseVecteur.length; i++) {
                pixels[i] = (int) baseVecteur[i];
            }
            Patch patch = new Patch(pixels);
            patchCollection.addPatch(patch, positions.get(indice));
            indice++;
        }
        return patchCollection;
    }

    
    public static PatchCollection extractPatches(BufferedImage image, int s) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int nbPatchLigne = imageWidth - s + 1;    // nombre de patchs sur une ligne
        int nbPatchColonne = imageHeight - s + 1; // nombre de patchs sur une colonne

        PatchCollection patchCollection = new PatchCollection();

        for (int i = 0; i < nbPatchLigne; i++) {
            for (int j = 0; j < nbPatchColonne; j++) {
                double[] patchPixels = new double[s * s];
                for (int y = 0; y < s; y++) {
                    for (int x = 0; x < s; x++) {
                        int pixel = image.getRGB(i + x, j + y); // abcisse, ordonnée du pixel
                        int gray = (pixel >> 16) & 0xFF;
                        patchPixels[y * s + x] = gray;
                    }
                }     
                Patch patch = new Patch(patchPixels);
                int[] patchPosition = { i, j }; // Cet ordre là pour retomber sur l'ordre usuel des matrices M[i = ligne][j = colonne]
                patchCollection.addPatch(patch, patchPosition);
            }
        }

        return patchCollection;
    }


    public static void displayPatchCollection(PatchCollection patchCollection) {
        ArrayList<Patch> patches = patchCollection.getPatches();
        ArrayList<int[]> positions = patchCollection.getPatchPositions();

        for (int i = 0; i < 50; i++) {
            Patch patch = patches.get(i);
            int[] position = positions.get(i);

            System.out.println("Patch Position: (" + position[0] + ", " + position[1] + ")");
            System.out.println("Patch Pixels:");
            double[] pixels = patch.getPixels();
            for (int y = 0; y < pixels.length; y++) {
                    System.out.print(pixels[y] + " ");
                }
                System.out.println();
            }
       }
    
    
    
    public static BufferedImage reconstructPatchs(PatchCollection patchCollection, int imageWidth, int imageHeight) {
    	int patchSize = patchCollection.getPatches().get(0).getPatchSize();
        int s = (int)Math.sqrt(patchSize);
    	int nbPatchLigne = (imageWidth - s + 1);
        int nbPatchColonne = (imageHeight - s + 1);

        BufferedImage reconstructedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = reconstructedImage.getRaster();
        
        for (int i = 0; i < nbPatchLigne; i++) {
            for (int j = 0; j < nbPatchColonne; j++) {
                Patch patch = patchCollection.getPatch(i, j); //on récupère le patch qui est à la posisition i,j
                int[] position = {i, j};

                double[] patchPixels = patch.getPixels(); //on récupère les pixels de ce patch

                for (int z = 0; z < s; z++) {
                    double pixelValue = patchPixels[z];
                    double[] pixel = { pixelValue };
                    int posPixelDansImageX = position[0] + Math.floorMod(z, s); //dessin
                    int posPixelDansImageY = position[1] + Math.floorDiv(z, s); //dessin
                    raster.setPixel(posPixelDansImageX, posPixelDansImageY, pixel);
                }
            }
        }
        

        return reconstructedImage;
    } 
    
    public static BufferedImage reconstructPatchs2(PatchCollection patchCollection, int imageWidth, int imageHeight) {
        int patchSize = patchCollection.getPatches().get(0).getPatchSize();
        int s = (int) Math.sqrt(patchSize);
        int nbPatchLigne = (imageWidth - s + 1);
        int nbPatchColonne = (imageHeight - s + 1);
    
        BufferedImage reconstructedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = reconstructedImage.getRaster();
        int[][] count = new int[imageWidth][imageHeight];
        double[][] sommePixels = new double[imageWidth][imageHeight];
    
        for (int i = 0; i < nbPatchLigne; i++) {
            for (int j = 0; j < nbPatchColonne; j++) {
                Patch patch = patchCollection.getPatch(i, j);
                int[] position = {i, j};
    
                double[] patchPixels = patch.getPixels();
    
                for (int z = 0; z < patchSize; z++) {
                    double pixelValue = patchPixels[z];
                    int posPixelDansImageX = i + Math.floorMod(z, s);
                    int posPixelDansImageY = j + Math.floorDiv(z, s);
    
                    sommePixels[posPixelDansImageX][posPixelDansImageY] += pixelValue;
                    count[posPixelDansImageX][posPixelDansImageY]++;
                }
            }
        }
    
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                double sample = sommePixels[i][j];
                double pixelValuee = sample / (count[i][j]);
                if (pixelValuee < 0) {
                    pixelValuee = 0;
                }
    
                raster.setSample(i, j, 0, pixelValuee);
            }
        }
    
        return reconstructedImage;
    }
    
   

    //Calcul de moyenne et covariance d'une collection de patchs
    
    public static class ResultMoyCovCenter {
        
    	private Patch meanVector;
        private RealMatrix covarianceMatrix;
        private PatchCollection centeredCollection;
        
        public ResultMoyCovCenter(Patch meanVector, RealMatrix covarianceMatrix, PatchCollection centeredCollection) {
            this.meanVector = meanVector;
            this.covarianceMatrix = covarianceMatrix;
            this.centeredCollection = centeredCollection;
        }

		public Patch getMeanVector() {
			return meanVector;
		}

		public void setMeanVector(Patch meanVector) {
			this.meanVector = meanVector;
		}

		public RealMatrix getCovarianceMatrix() {
			return covarianceMatrix;
		}

		public void setCovarianceMatrix(RealMatrix covarianceMatrix) {
			this.covarianceMatrix = covarianceMatrix;
		}

		public PatchCollection getCenteredCollection() {
			return centeredCollection;
		}

		public void setCenteredCollection(PatchCollection centeredCollection) {
			this.centeredCollection = centeredCollection;
		}

    }
    public static ResultMoyCovCenter moyCov(PatchCollection patchCollection) {
        int nbPatchs = patchCollection.getPatches().size();
        int patchSize = patchCollection.getPatches().get(0).getPatchSize();
        Patch meanVector = new Patch(new double[patchSize]);
        PatchCollection centeredCollection = new PatchCollection();
        RealMatrix varCovMatrice = new Array2DRowRealMatrix(patchSize, patchSize);

        // Calcul du vecteur moyen
        // Etape 1 : on additionne tous les vecteurs
        for (Patch patch : patchCollection.getPatches()) { //Boucle for sur tous les patchs de la colloction
            double[] patchPixels = patch.getPixels();
            for (int i = 0; i < patchSize; i++) {
                meanVector.getPixels()[i] += patchPixels[i];
            }
        }
        // Etape 2 : on divise chaque pixel par le nombre de patchs qu'on a additionné pour avoir la moyenne  
        for (int i = 0; i < patchSize; i++) {
            meanVector.getPixels()[i] /= nbPatchs;
        }

        // Calcul de la collection centrée
        for (int i = 0; i < nbPatchs; i++) {
            Patch patch = patchCollection.getPatches().get(i);
            double[] centeredPixels = new double[patchSize];
            double[] patchPixels = patch.getPixels();
            for (int j = 0; j < patchSize; j++) {
                centeredPixels[j] = patchPixels[j] - meanVector.getPixel(j);
            }
            centeredCollection.addPatch(new Patch(centeredPixels), patchCollection.getPatchPositions().get(i));
        }

        // Calcul de la matrice de covariance
        for (int k = 0; k < nbPatchs; k++) {
            Patch centeredPatch = centeredCollection.getPatches().get(k);
            double[] centeredVector = centeredPatch.getPixels();
            
            RealMatrix Vck = new Array2DRowRealMatrix(patchSize,1);
            for (int i = 0; i <patchSize; i++) {
                Vck.setEntry(i, 0, centeredVector[i]);
            }
     
            varCovMatrice = varCovMatrice.add(Vck.multiply(Vck.transpose()));
        }

        varCovMatrice = varCovMatrice.scalarMultiply(1.0 / nbPatchs);
        
        return new ResultMoyCovCenter(meanVector, varCovMatrice, centeredCollection);
    }

    
    public static void afficherResultat(ResultMoyCovCenter resultat) {
        System.out.println("Vecteur moyen : " );
        resultat.getMeanVector().displayPatch(resultat.getMeanVector());
        
        System.out.println("Collection centrée :");
        PatchCollection centeredCollection = resultat.getCenteredCollection();
        for (int i = 0; i < centeredCollection.getPatches().size(); i++) {
            Patch patch = centeredCollection.getPatches().get(i);
            int[] position = centeredCollection.getPatchPositions().get(i);
            System.out.println("Patch " + i + " - Position (" + position[0] + ", " + position[1] + ")");
            patch.displayPatch(patch);
        }
        
        System.out.println("Matrice de covariance :");
        RealMatrix covarianceMatrix = resultat.getCovarianceMatrix();
        double[][] covarianceData = covarianceMatrix.getData();
/*
        for (int i = 0; i < covarianceData.length; i++) {
            for (int j = 0; j < covarianceData[i].length; j++) {
                System.out.print(covarianceData[i][j] + " ");
            }
            System.out.println();
        }
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < covarianceData[i].length; j++) {
                System.out.print(covarianceData[i][j] + " ");
            }
            System.out.println();
        }
        */
    }

}