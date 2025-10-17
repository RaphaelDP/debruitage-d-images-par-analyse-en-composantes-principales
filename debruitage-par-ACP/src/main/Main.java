package main;

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

	}

	@Override
    public void start(Stage primaryStage) {
        // Créer une instance de la classe LoginPage
        Accueil accueil = new Accueil();
        // Appeler la méthode start de la classe LoginPage pour afficher la page de connexion
        accueil.start(primaryStage);
    }


}





