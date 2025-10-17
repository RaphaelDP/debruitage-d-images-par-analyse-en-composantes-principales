package core;

import java.awt.image.BufferedImage;

public class Critere {
	
	//Paramètres de seuillage
	
	public static double seuilVisuShrink(double sigma, int nbPixels) {
        return(sigma*Math.sqrt(2*Math.log10(nbPixels)));
}
 
public static double varianceEstimeeSignal(BufferedImage bufferedNoisy) {
    int width = bufferedNoisy.getWidth();
    int height = bufferedNoisy.getHeight();
    double sum = 0;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int pixelValue = bufferedNoisy.getRGB(x, y) & 0xFF; // Extraire la valeur du pixel (composante de luminosité)
            sum += pixelValue;
        }
    }
    double pixelMoyen = sum / (width * height);

    double sum2 = 0.0;
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int pixelValue = bufferedNoisy.getRGB(x, y) & 0xFF; // Extraire la valeur du pixel (composante de luminosité)

            double difference = pixelValue - pixelMoyen;
            sum2 += difference * difference;
        }
    }
    double res = sum2 / (width * height);
    System.out.println("le pixel moyen vaut " + pixelMoyen);
    System.out.println("le sum2 vaut " + sum2);
    System.out.println("la variance estimee vaut " + res);

    return res;
}


public static double seuilBayesShrink(double variance, double varianceEstimeeSignal) {
    double ecartTypeEstimeSignal=Math.sqrt(Math.max(varianceEstimeeSignal-variance,0));
    
    return(variance/ecartTypeEstimeSignal);
}
	


    public static double calculateMSE(BufferedImage originalImage, BufferedImage denoisedImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        double mse = 0.0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalPixel = originalImage.getRGB(x, y) & 0xFF;
                int denoisedPixel = denoisedImage.getRGB(x, y) & 0xFF;

                double diff = originalPixel - denoisedPixel;
                mse += diff*diff;
            }
        }
        mse /= (width * height);
        System.out.println("mse =" + mse);
        return mse;
    }

    public static double calculatePSNR(BufferedImage originalImage, BufferedImage denoisedImage) {
        double mse = calculateMSE(originalImage, denoisedImage);

        double psnr = 10 * Math.log10(255 / mse);
        return psnr;
    }
    
    

}