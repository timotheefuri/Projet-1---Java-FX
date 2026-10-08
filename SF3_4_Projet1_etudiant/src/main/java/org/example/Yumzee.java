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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Optional;

public class Yumzee extends Application {
	// Initialisation de la base
	public Stage primaryStage = new Stage();
	public BorderPane root = new BorderPane();
	public Scene scene = new Scene(root, 630, 430);
	private int desEnAnimation = 0;
	final int Maxcombos = 6; // 6 combinaisons
	private final boolean[] utilisee = new boolean[Maxcombos];
	private De[] listeDes; // Liste des 5 dés
	private int lancersRestants = 3; // Lancers restants dans le tour
	private int tourActuel = 1; // Numéro de tour
	private final int TOURS_MAX = 6; // Nombre total de tours
	private Label lblLancers; // Affichage des lancers
	private Label lblTour; // Affichage du tour actuel
	private Button btnLancer;
	private final Button[] boutonsCombinaison = new Button[Maxcombos]; // 6 combinaisons
	private final Pointage pointage = new Pointage(); // Gestion du pointage
	Text txtTotal;
	private final AnimationDe animationDe = new AnimationDe();
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			//modification de toutes les zones
			root.setTop(creerBarreMenu());
			root.setCenter(creerZoneDes());
			root.setBottom(creerZoneLancer());
			root.setRight(creerZonePoints());

			// Dernier changement pour la fênetre
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yumzee - Timothée Furi, Rushi Patel !");
			primaryStage.show();
			lancerDes();
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
		btnNP.setOnAction(e -> nouvellePartie());

		// Menu bar aide
		Menu menuAide = new Menu("Aide");
		MenuItem btnPropos = new MenuItem("À propos");
		MenuItem btnStats = new MenuItem("Statistiques");  // implement this button
		menuAide.getItems().addAll(btnPropos,btnStats);
		btnStats.setOnAction(e -> StatistiquesJeu.afficherModule(primaryStage));
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
		listeDes = new De[5];
		for (int i = 0 ; i < 5 ; i++) {
			De de = new De();
			de.getAffichage().setFitWidth(50);
			de.getAffichage().setFitHeight(50);
			listeDes[i] = de;
			zonecentrale.getChildren().add(de.getAffichage());
			zonecentrale.setAlignment(Pos.BOTTOM_CENTER);
		}
		return zonecentrale;
	}
	public VBox creerZoneLancer() {
		// Format du Vbox
		VBox zoneLancer = new VBox(5);
		zoneLancer.setPadding(new Insets(10,20,10,20));
		zoneLancer.setAlignment(Pos.CENTER);
		BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY);
		zoneLancer.setBackground(new Background(backgroundFill));
		// Ajout des autres éléments nécessaires
		btnLancer = new Button("Lancer les dés");
		btnLancer.setOnAction(e -> actionLancer());   // implement this button
		lblLancers = new Label("Lancers restants : " + lancersRestants);
		lblTour = new Label("Tour : " + tourActuel + " / " + TOURS_MAX);
		Font font1 = new Font("arial",18);
		Font font2 = new Font("arial",16);
		btnLancer.setFont(font1);
		lblLancers.setFont(font2);
		lblTour.setFont(font2);
		zoneLancer.getChildren().addAll(btnLancer, lblLancers, lblTour);
		return zoneLancer;
	}
	public GridPane creerZonePoints() {
		GridPane zonePoints = new GridPane(2,10);
		zonePoints.setPadding(new Insets(10,10,10,10));
		BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY);
		zonePoints.setBackground(new Background(backgroundFill));

		Label titre = new Label("Combinaisons");
		titre.setFont(Font.font("Tahoma", FontWeight.BOLD,16));
		zonePoints.add(titre,0,0,7,1);

		// ajout des images de dés
		String[] listImages = new String[] {"1.png","2.png","3.png","4.png","5.png","6.png"};
		ImageView[] listImagesViews = new ImageView[listImages.length];
		Label[] combiNom = new Label[] {new Label("Brelan"),new Label("Carré"),new Label("Full"),new Label("Petite Suite"),new Label("Grande Suite"), new Label("5 identiques")};
		ToggleGroup groupe = new ToggleGroup();

		for (int i = 0; i < listImages.length; i++) {
			Image image = new Image(listImages[i]);
			ImageView imageView = new ImageView(image);
			listImagesViews[i] = imageView;
		}
		int[][] listDices = new int[][] {{2,2,2}, {3,3,3,3}, {1,1,4,4,4}, {1,2,3,4}, {0,1,2,3,4}, {5,5,5,5,5} };
		for (int i = 0; i < listDices.length; i++) {
			for (int j = 0 ; j < listDices[i].length; j++) {
				ImageView diceIcon = new ImageView(listImagesViews[listDices[i][j]].getImage());
				diceIcon.setFitWidth(18);
				diceIcon.setFitHeight(18);
				zonePoints.add(diceIcon, 5 - listDices[i].length + j, 1 + i);
			}
			Button btnKeep = new Button("🎯");
			boutonsCombinaison[i] = btnKeep;
			btnKeep.setOnAction(new GestionCombinaisons());
			btnKeep.setStyle("-fx-background-color: royalblue; -fx-text-fill: white;");
			zonePoints.add(btnKeep,5,1+i);
			combiNom[i].setFont(Font.font("Tahoma",FontWeight.BOLD,10));
			zonePoints.add(combiNom[i],6,1+i);
		}
		Label points = new Label("Total points :");
		points.setFont(Font.font("Tahoma",FontWeight.BOLD,10));
		zonePoints.add(points,0,7,4,1);
		txtTotal = new Text(String.valueOf(pointage.getTotalPoints()));
		txtTotal.setFont(Font.font("Tahoma",FontWeight.BOLD,12));
		zonePoints.add(txtTotal,4,7);
		return zonePoints;
	}
	private class GestionCombinaisons implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			for (int i = 0; i < Maxcombos; i++) {
				if (e.getSource() == boutonsCombinaison[i]) {
					CategorieCombinaison categorie = CategorieCombinaison.fromIndex(i);
					int points = pointage.calculerScore(categorie, listeDes.clone());
					pointage.ajoutePoints(points);
					txtTotal.setText(String.valueOf(pointage.getTotalPoints()));
					utilisee[i] = true;
					boutonsCombinaison[i].setDisable(true);
					prochainTour();
					return;
				}// fair la pointage
			}
		}
	}
	private void actionLancer() {
		if (lancersRestants == 0) {
			Alert alerte = new Alert(Alert.AlertType.WARNING);
			alerte.setTitle("Choix obligatoire");
			alerte.setHeaderText("Plus aucun lancer disponible");
			alerte.setContentText("Vous devez choisir une combinaison dans la zone des points.");
			alerte.showAndWait();
			return;
		}
		lancerDes();
	}
	private void lancerDes() {
		lancersRestants--;
		lblLancers.setText("Lancers restants : " + lancersRestants);

		desEnAnimation = 0;
		for (De de : listeDes) {
			if (!de.isGarde()) {
				de.lancer();          // la valeur est tirée ici, une seule fois
				desEnAnimation++;
			}
		}
		if (desEnAnimation > 0) {
			bloquerInterface(true);
			for (De de : listeDes) {
				if (!de.isGarde()) {
					animationDe.lancer(de, this::finAnimationDe);
				}
			}
		}
	}
	private void finAnimationDe() {
		desEnAnimation--;
		if (desEnAnimation == 0) {
			bloquerInterface(false);
		}
	}
	private void bloquerInterface(boolean bloque) {
		btnLancer.setDisable(bloque);
		for (int i = 0; i < Maxcombos; i++) {
			boutonsCombinaison[i].setDisable(bloque || utilisee[i]);
		}
		for (De de : listeDes) {
			de.setInteractif(!bloque);
		}
	}
	private void prochainTour() {
		if (tourActuel < TOURS_MAX) {
			tourActuel++;
			lancersRestants = 3;
			lblTour.setText("Tour : " + tourActuel + " / " + TOURS_MAX);
			for (De de : listeDes) {
				de.liberer();
			}
			lancerDes(); // premier lancer automatique
		} else {
			finDePartie();
		}
	}
	private void finDePartie() {
		btnLancer.setDisable(true);
		ButtonType btnNouvelle = new ButtonType("Nouvelle partie");
		ButtonType btnFermer = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alerte = new Alert(Alert.AlertType.INFORMATION,
				"Votre score total est : " + pointage.getTotalPoints(), btnNouvelle, btnFermer);//fini la partie avec un menu
		alerte.setTitle("Fin de partie");
		alerte.setHeaderText("Partie terminée !");
		Optional<ButtonType> choix = alerte.showAndWait();
		if (choix.isPresent() && choix.get() == btnNouvelle) {
			nouvellePartie();
		}// depend des choix peut fair une nouvelle partie ou non
	}
	private void nouvellePartie() {
		if (desEnAnimation > 0) {
			return; // on ignore Ctrl+N pendant que les dés tournent
		}
		tourActuel = 1;
		lancersRestants = 3;
		lblTour.setText("Tour : " + tourActuel + " / " + TOURS_MAX);
		for (int i = 0; i < Maxcombos; i++) {
			utilisee[i] = false;
			boutonsCombinaison[i].setDisable(false);
		}
		pointage.setTotalPoints(0);
		txtTotal.setText("0");
		for (De de : listeDes) {
			de.liberer();
		}
		btnLancer.setDisable(false);
		lancerDes();
	}

	public static void main(String[] args) {
		launch(args);
	}
}