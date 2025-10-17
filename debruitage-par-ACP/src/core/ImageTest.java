package core;
import javax.imageio.ImageIO;




import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import core.Image.Imagette;
import core.PatchCollection.ResultMoyCovCenter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ImageTest {
    public static void main(String[] args) {
        // Chemin de l'image à traiter
        String imagePath = "./data/lena.jpg";

        try {
            // Chargement de l'image
            BufferedImage image = ImageIO.read(new File(imagePath));

            ArrayList<Imagette> imagettes = Image.decoupeImageEnQuatre(image);

            // Test de la fonction reconstituerImage
            BufferedImage imageReconstituee = Image.reconstituerImage(imagettes, image.getWidth(), image.getHeight());

            // Sauvegarde de l'image reconstituée
            String imageReconstitueePath = "./data/reconstituee.jpg";
            ImageIO.write(imageReconstituee, "jpg", new File(imageReconstitueePath));
            Image.displayImage(imageReconstitueePath);

            System.out.println("Image découpée et reconstituée avec succès.");

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    // Autres méthodes de la classe ImageProcessor
}
