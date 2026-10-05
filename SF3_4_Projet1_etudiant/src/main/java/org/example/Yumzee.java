package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Yumzee extends Application {
	// Initialisation de la base
	public Stage primaryStage = new Stage();
	public BorderPane root = new BorderPane();
	public Scene scene = new Scene(root, 800, 500);


	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;

			// Dernier changement pour la fênetre
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yumzee - Timothée Furi, Rushi Patel !");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void creerBarreMenu() {
		// Initialisation de MenuBar
		MenuBar mb = new MenuBar();

		// Menu bar jeu
		Menu menuJeu = new Menu("Jeu");
		MenuItem btnNP = new MenuItem("Nouvelle Partie");  // implement this button
		MenuItem btnQuitter = new MenuItem("Quitter");
		menuJeu.getItems().addAll(btnNP,btnQuitter);
		menuJeu.setAccelerator(KeyCombination.keyCombination("Alt+J"));
		btnNP.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		btnQuitter.setOnAction(event -> {primaryStage.close();});
		mb.getMenus().addAll(menuJeu);

		// Menu bar aide
		Menu menuAide = new Menu("Aide");
		MenuItem btnPropos = new MenuItem("À propos");
		MenuItem btnStats = new MenuItem("Statistiques");  // implement this button
		menuAide.getItems().addAll(btnPropos,btnStats);
		menuAide.setAccelerator(KeyCombination.keyCombination("Alt+A"));
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("À propos de Yumzee");
		alert.setHeaderText("Yumzee - Projet 1 du cours 420-SF3-RE réalisé par Timothée Furi et Rushi Patel");
		alert.setContentText("Yumzee est un jeu de chance et de stratégie. Amusez-vous Bien!");
		btnPropos.setOnAction(event -> alert.showAndWait());
		mb.getMenus().addAll(menuAide);

		// Position du MenuBar
		root.setTop(mb);

	}

	// not finished
	public HBox creerZoneDes() {
		// Format du Hbox
		HBox zonecentrale = new HBox(10);
		zonecentrale.setPadding(new Insets(0,20,10,20));

		// Ajout des dées
		for (int i = 0 ; i < 6 ; i++) {
			De de = new De();
			// zonecentrale.getChildren().add(de.getAffichage()); 		implement the method getAffichage
			zonecentrale.setAlignment(Pos.BOTTOM_CENTER);
		}

		return zonecentrale;
	}

	public VBox creerZoneLancer() {
		// Format du Vbox
		VBox zoneLancer = new VBox(5);
		zoneLancer.setPadding(new Insets(0,20,10,20));
		zoneLancer.setAlignment(Pos.CENTER);
		BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY);
		zoneLancer.setBackground(new Background(backgroundFill));

		// Ajout des autres éléments nécessaires
        Button lancer = new Button("Lancer les dés");   // implement this button
		Label lancerRestant = new Label("Lancer restants :");
		Label tour = new Label("Tour :");

		Font font1 = new Font("arial",18);
		Font font2 = new Font("arial",16);

		lancer.setFont(font1);
		lancerRestant.setFont(font2);
		tour.setFont(font2);

		return zoneLancer;
	}

	public GridPane creerZonePoints() {
		GridPane zonePoints = new GridPane(20,10);
		zonePoints.setPadding(new Insets(0,20,10,20));
		BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
		zonePoints.setBackground(new Background(backgroundFill));

		Label titre = new Label("Combinaisons : ");
		titre.setFont(Font.font("Tahoma", FontWeight.BOLD,16));

		// ajout des images de dés
		Image image1 = new Image("1.png");
		Image image2 = new Image("2.png");
		Image image3 = new Image("3.png");
		Image image4 = new Image("4.png");
		Image image5 = new Image("5.png");
		Image image6 = new Image("6.png");

		ImageView imageView1 = new ImageView(image1);
		ImageView imageView2 = new ImageView(image2);
		ImageView imageView3 = new ImageView(image3);
		ImageView imageView4 = new ImageView(image4);
		ImageView imageView5 = new ImageView(image5);
		ImageView imageView6 = new ImageView(image6);

		return zonePoints;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
