package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
		MenuItem btnNP = new MenuItem("Nouvelle Partie");
		MenuItem btnQuitter = new MenuItem("Quitter");
		menuJeu.getItems().addAll(btnNP,btnQuitter);
		menuJeu.setAccelerator(KeyCombination.keyCombination("Alt+J"));
		btnNP.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		btnQuitter.setOnAction(event -> {primaryStage.close();});
		mb.getMenus().addAll(menuJeu);

		// Menu bar aide
		Menu menuAide = new Menu("Aide");
		MenuItem btnPropos = new MenuItem("À propos");
		MenuItem btnStats = new MenuItem("Statistiques");
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
		for (int i = 0 ; i < 5 ; i++) {
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
        Button lancer = new Button("Lancer les dés");
		Label lancerRestant = new Label("Lancer restants :");
		Label tour = new Label("Tour :");

		return zoneLancer;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
