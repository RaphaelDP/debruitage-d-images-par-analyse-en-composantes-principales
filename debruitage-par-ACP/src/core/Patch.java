package core;

public class Patch {
    
	private double[] pixels;
    
    public Patch(double[] pixels) {
        
    	this.pixels = pixels;
    }
    
    
	public double getPixel(int i) {
        return pixels[i];
    }
	
	public void setPixel(int i, double valeur) {
        pixels[i]+=valeur;
    }
    
	public double[] getPixels() {
        return pixels;
    }
    public int getSize() {
    	return pixels.length;
    }
    
    public int getPatchSize() {
        return pixels.length;
    }
    
    public Patch transpose() {
        int patchSize = getPatchSize();
        double[] transposedPixels = new double[patchSize];
        for (int i = 0; i < patchSize; i++) {
            transposedPixels[i] = getPixels()[i];
        }
        return new Patch(transposedPixels);
    }
    
// Surement inutiles

    public Patch multiply(double d) {
        double[] multipliedPixels = new double[getPixels().length];
        for (int i = 0; i < getPixels().length; i++) {
            multipliedPixels[i] = d * getPixels()[i];
        }
        return new Patch(multipliedPixels);
    }

    public Patch add(Patch other) {
        double[] pixels1 = getPixels();
        double[] pixels2 = other.getPixels();

        if (pixels1.length != pixels2.length) {
            throw new IllegalArgumentException("Les patches doivent avoir la même longueur pour l'addition.");
        }

        double[] addedPixels = new double[pixels1.length];

        for (int i = 0; i < addedPixels.length; i++) {
            addedPixels[i] = pixels1[i] + pixels2[i];
        }

        return new Patch(addedPixels);
    }
    
    public void displayPatch(Patch patch) {
        double[] pixels = patch.getPixels();
        int patchSize = patch.getPatchSize();

        System.out.println("Patch:");
        for (int i = 0; i < patchSize; i++) {
            System.out.print(pixels[i] + " ");
        }
        System.out.println();
    }




//FONCTIONS DE SEUILLAGES (modifie un patch selon le sueillage choisi)

	public double seuillageDur(int i, double lambda) {		
			if (Math.abs(pixels[i]) <= lambda) {
				return 0;
			}
			else {
				return pixels[i];
		}	
	}
	
	public double seuillageDoux(int i, double lambda) {

			
			if (Math.abs(pixels[i]) < lambda) {
				return 0;
			}
			if  (pixels[i] > lambda) {
				return pixels[i] - lambda;
			}
			else {
				return pixels[i] + lambda;
			}
		}
	
//Paramètres de seuillage
	
	public double seuilVisuShrink(double variance) {
		return(Math.sqrt(variance*Math.log(pixels.length)));
	 }
	 
	public double seuilBayesShrink(double variance, float varianceEstimeeSignal) {
		double ecartTypeEstimeSignal=Math.sqrt(Math.max(varianceEstimeeSignal-variance,0));
		 
		return(variance/ecartTypeEstimeSignal);
	 }
	
	
}