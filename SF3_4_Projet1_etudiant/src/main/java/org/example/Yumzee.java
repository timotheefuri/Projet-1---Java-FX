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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Yumzee extends Application {
	// Initialisation de la base
	public Stage primaryStage = new Stage();
	public BorderPane root = new BorderPane();
	public Scene scene = new Scene(root, 800, 500);

	final int MAX_COMBINAISONS = 6; // 6 combinaisons
	private De[] listeDes; // Liste des 5 dés
	private int lancersRestants = 3; // Lancers restants dans le tour
	private int tourActuel = 1; // Numéro de tour
	private final int TOURS_MAX = 6; // Nombre total de tours
	private Label lblLancers; // Affichage des lancers
	private Label lblTour; // Affichage du tour actuel
	private Button[] boutonsCombinaison = new Button[MAX_COMBINAISONS]; // 6 combinaisons
	private Pointage pointage = new Pointage(); // Gestion du pointage
	Text txtTotal;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;

			root.setTop(creerBarreMenu());
			root.setCenter(creerZoneDes());
			root.setBottom(creerZoneLancer());
			root.setRight(creerZonePoints());


			// Dernier changement pour la fênetre
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yumzee - Timothée Furi, Rushi Patel !");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MenuBar creerBarreMenu() {
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

		return mb;
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
        Button lancer = new Button("Lancer les dés" + lancersRestants);   // implement this button
		Label lancerRestant = new Label("Lancer restants :" + tourActuel);
		Label tour = new Label("Tour :");

		Font font1 = new Font("arial",18);
		Font font2 = new Font("arial",16);

		lancer.setFont(font1);
		lancerRestant.setFont(font2);
		tour.setFont(font2);

		return zoneLancer;
	}

	public GridPane creerZonePoints() {
		GridPane zonePoints = new GridPane(10,10);
		zonePoints.setPadding(new Insets(0,20,10,20));
		BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
		zonePoints.setBackground(new Background(backgroundFill));

		Label titre = new Label("Combinaisons : ");
		titre.setFont(Font.font("Tahoma", FontWeight.BOLD,16));
		zonePoints.add(titre,0,0,3,1);

// ajout des images de dés
		String[] listImages = new String[] {"1.png","2.png","3.png","4.png","5.png","6.png"};
		ImageView[] listImagesViews = new ImageView[listImages.length];
		Label[] combiNom = new Label[] {new Label("Brelan"),new Label("Carré"),new Label("Full"),new Label("Petite Suite"),new Label("Grande Suite"), new Label("5 Identiques")};
		Button[] buttonBleuListe = new Button[6];

		for (int i = 0; i < listImages.length; i++) {
			Image image = new Image(listImages[i]);
			ImageView imageView = new ImageView(image);
			listImagesViews[i] = imageView;
		}

		int[][] listDices = new int[][] {{2,2,2}, {3,3,3,3}, {1,1,4,4,4}, {1,2,3,4}, {0,1,2,3,4}, {5,5,5,5,5} };

		for (int i = 0; i < listDices.length; i++) {
			for (int j = 0 ; j < listDices[i].length; j++) {
				ImageView diceIcon = new ImageView(listImagesViews[listDices[i][j]].getImage());
				diceIcon.setFitWidth(20); // Optional: adjust size if needed
				diceIcon.setFitHeight(20);
				zonePoints.add(diceIcon, 1 + j, 1 + i);
			}
			Button btnKeep = new Button(" ");   // implement this button
			btnKeep.setTextFill(Color.ROYALBLUE);
			buttonBleuListe[i] = btnKeep;
			zonePoints.add(btnKeep,6,1+i); // Moved column to 6 so it doesn't overlap dice columns (0 to 5)
			combiNom[i].setFont(Font.font("Tahoma",FontWeight.BOLD,10));
			zonePoints.add(combiNom[i],7,1+i); // Moved column to 7 to match row index fix
		}
		Label points = new Label("Total des points :");
		points.setFont(Font.font("Tahoma",FontWeight.BOLD,10));
		zonePoints.add(points,1,8,3,1);
		return zonePoints;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
