package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class OutilsMathematiques {
	
	public double[][] convertirCollectionEnMatrice(PatchCollection collection) {
	    ArrayList<Patch> patches = collection.getPatches();
	    int patchSize = patches.get(0).getPatchSize();
	    int numPatches = patches.size();

	    double[][] matrix = new double[numPatches][patchSize];
	    for (int i = 0; i < numPatches; i++) {
	        Patch patch = patches.get(i);
	        double[] pixels = patch.getPixels();
	        for (int j = 0; j < patchSize; j++) {
	            matrix[i][j] = (double) pixels[j];
	        }
	    }

	    return matrix;
	} 
	
	public static PatchCollection convertirMatriceEnCollection(double[][] matrix, ArrayList<int[]> positionsListe ) {
	    PatchCollection collection = new PatchCollection();

	    int numPatches = matrix.length;
	    int patchSize = matrix[0].length;

	    for (int i = 0; i < numPatches; i++) {
	        double[] patchData = matrix[i];
	        double[] pixels = new double[patchSize];
	        
	        for (int j = 0; j < patchSize; j++) {
	            pixels[j] = patchData[j];
	        }
	        
	        Patch patch = new Patch(pixels); // Créez un nouvel objet Patch en utilisant les pixels
	    	collection.addPatch(patch, positionsListe.get(i));
	    }
	   // PatchCollection.displayPatchCollection(collection);
	    return collection;
	}

	
	
	public static double[] convertirTableauIntEnTableauDouble(int[] listeEntiers) {
	    double[] listeDouble = new double[listeEntiers.length];
	    int index = 0;
	    for (int entier : listeEntiers) {
	        listeDouble[index] = (double) entier;
	        index++;
	    }
	    return listeDouble;
	}

	public int[] convertirTableauDoubleEnTableauInt(double[] listeDoubles) {
	    int[] listeEntiers = new int[listeDoubles.length];
	    int index = 0;
	    for (double nombreDouble : listeDoubles) {
	        listeEntiers[index] = (int) nombreDouble;
	        index++;
	    }
	    return listeEntiers;
	}



    // Méthode pour calculer les vecteurs propres

    public static double[][] calculerVecteursPropres(RealMatrix matrice) {

    	EigenDecomposition eigenDecomposition = new EigenDecomposition(matrice);
        RealMatrix eigenvectors = eigenDecomposition.getV();
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();

        // Classer les vecteurs propres par ordre décroissant selon les valeurs propres
        TreeMap<Double, double[]> sortedEigenpairs = new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < eigenvalues.length; i++) {
            sortedEigenpairs.put(eigenvalues[i], eigenvectors.getColumn(i));
        }
        // Récupérer les vecteurs propres classés
        double[][] sortedEigenvectors = new double[matrice.getColumnDimension()][matrice.getColumnDimension()];
        int index = 0;
        for (double[] vector : sortedEigenpairs.values()) {
            sortedEigenvectors[index++] = vector;
        }
        
        // Après avoir récupéré les vecteurs propres triés, on libère la mémoire
        sortedEigenpairs.clear();

        // Convertir le résultat en une instance de la classe Matrice
        return (sortedEigenvectors);
    }
    
    
    public static void displayVectors(double[][] eigenvectors) {
        System.out.println("Vecteurs propres de l'ACP:");
        for (int i = 0; i < eigenvectors.length; i++) {
            System.out.print("Vecteur " + (i + 1) + ": ");
            double[] vector = eigenvectors[i];
            for (int j = 0; j < vector.length; j++) {
                System.out.print(vector[j] + " ");
            }
            System.out.println();
        }
    }
    
    public static PatchCollection calculACP(PatchCollection collectionV) {

        // Appeler la méthode MoyCov sur l'instance de PatchCollection
        PatchCollection.ResultMoyCovCenter resultMoyCov = PatchCollection.moyCov(collectionV);

        // Calcul des vecteurs propres
        double[][] eigenvectors = calculerVecteursPropres(resultMoyCov.getCovarianceMatrix());
        
       // displayVectors(eigenvectors);

        PatchCollection baseVecteurs =  convertirMatriceEnCollection(eigenvectors,resultMoyCov.getCenteredCollection().getPatchPositions());
        
        
        
        return(baseVecteurs);
    }


    
    public static PatchCollection projeterContributions(PatchCollection baseVecteurs, PatchCollection vecteursCentres) {

        ArrayList<Patch> basePatches = baseVecteurs.getPatches();
        ArrayList<Patch> centrePatches = vecteursCentres.getPatches();
        int nbPatch = vecteursCentres.getPatchPositions().size();
        int nbPixel= baseVecteurs.getPatches().get(1).getSize();
        
        System.out.println("Taille de patchPos :" + nbPatch + "\nNombre pixels : " + nbPixel);

        double [][] contributionVect = new double[nbPatch][nbPixel];
        
        for (int i =0 ; i< nbPixel ; i++) {
            double[] uiPixels = basePatches.get(i).getPixels();
           
            /*  
            
            for(int x : basePatches.get(i).getPixels()) {
                System.out.println(x);

            }
            
            */
            
            
            
            for (int k =0 ; k< nbPatch ; k++) {
            	double dotProduct = 0;     
            	
                double[] vcPixels = centrePatches.get(k).getPixels();
                for (int j = 0; j < nbPixel; j++) {
                    dotProduct += uiPixels[j] * vcPixels[j];
                //	if (k==10) {
                //		System.out.println(uiPixels[j]+" * " +vcPixels[j]+ " = " + uiPixels[j] * vcPixels[j]);            		
                //	}
                }
                
                contributionVect[k][i] = dotProduct;
            	
            	//System.out.println("le pixel projeté vaut :" + dotProduct);            		
            	
            }
            
        }

        return(PatchCollection.convertirListeDoubleEnPatchCollection(contributionVect,vecteursCentres.getPatchPositions()));
/*
        PatchCollection contributionCollection = new PatchCollection();
        ArrayList<int[]> patchPositions = vecteursCentres.getPatchPositions();
        System.out.println("Taille de patchPos :" + patchPositions.size() + "\nTaille de contributions : " + contributions.size());
        for (int i = 0; i < contributions.size(); i++) {
            Patch contribution = contributions.get(i);
            int[] patchPosition = patchPositions.get(i);
            contributionCollection.addPatch(contribution, patchPosition);
        }

        return contributionCollection;
*/
    
    }
    
   
}



