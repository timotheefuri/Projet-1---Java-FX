package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Random;
/**
*  (5.3)
 * Concept du cours de calcul intégral. En
 * probabilité, la probabilité qu'une variable continue X se trouve dans [a, b]
 * est l'aire sous sa courbe f :  P(a ≤ X ≤ b) = fab f(x) dx.
 * (L'animation des dés, elle, applique les equation dif : voir AnimationDe.)
 *
 * Traduction dans le code : on simule 10 000 parties de 6 tours (même règles que
 * le jeu, via la classe Pointage), puis on calcule la moyenne et l'écart-type
 * des scores. On ajuste la densité gaussienne
 *      f(x) = 1/(ec√(2π)) · e^(-(x-m)²/(2ec²))
 * (méthode densite). La fonction e^(-x²) n'a pas de primitive élémentaire : on
 * calcule donc l'intégrale numériquement, par la méthode de Simpson 
 * et par la méthode des trapèzes,qui approchent l'aire sous la courbe par des paraboles ou des trapèzes. Le graphique
 * montre l'histogramme, la courbe ajustée et l'aire intégrée en couleur.
 *
 * Signification du résultat : P(a ≤ score ≤ b) est la chance qu'une partie, jouée
 * avec la stratégie simulée, donne un score entre a et b. Par exemple, un résultat
 * de 40 % pour P(score ≥ 100) signifie qu'environ 4 parties sur 10 atteignent au
 * moins 100 points. 
 *
 * Strat: à chaque tour, 3 lancers en gardant les dés de la valeur la
 * plus fréquente, puis choix de la case disponible qui rapporte le plus de points.
 */
public class StatistiquesJeu {
    private static final int nb_tours = 6;
    private static final int nb_des = 5;
    private static final int nbcom = 6;
    private static final int LARGEUR_CLASSE = 5; // largeur d'une barre d'histogramme (points)
    private final Random hasard = new Random();
    private final De[] des = new De[nb_des];
    private final Pointage pointage = new Pointage();
    private int[] scores = new int[0];
    private int[] histogramme = new int[1];
    private int sm;
    private double moyenne;
    private double ecartType = 1;
    public StatistiquesJeu() {
        for (int i = 0; i < nb_des; i++) {
            des[i] = new De();
        }
    }
    public void simulerParties(int nbParties) {
        scores = new int[nbParties];
        for (int i = 0; i < nbParties; i++) {
            scores[i] = simulerUnePartie();
        }
        calculerDistribution();
    }
    private int simulerUnePartie() {
        boolean[] utilisee = new boolean[nbcom];
        int total = 0;

        for (int tour = 0; tour < nb_tours; tour++) {
            boolean[] gardes = new boolean[nb_des];
            for (int lancer = 0; lancer < 3; lancer++) {
                for (int d = 0; d < nb_des; d++) {
                    if (!gardes[d]) {
                        des[d].setValeurSimulee(hasard.nextInt(6) + 1);
                    }
                }
                if (lancer < 2) {
                    choisirDesAGarder(gardes);
                }
            }
            int meilleurIndex = -1;
            int meilleurScore = -1;
            for (int idx = nbcom - 1; idx >= 0; idx--) {
                if (!utilisee[idx]) {
                    int s = pointage.calculerScore(CategorieCombinaison.fromIndex(idx), des);
                    if (s > meilleurScore) {
                        meilleurScore = s;
                        meilleurIndex = idx;
                    }
                }
            }
            utilisee[meilleurIndex] = true;
            total += meilleurScore;
        }
        return total;
    }
    private void choisirDesAGarder(boolean[] gardes) {
        int[] compte = new int[7];
        for (De d : des) {
            compte[d.getValeur()]++;
        }
        int meilleureValeur = 1;
        for (int v = 2; v <= 6; v++) {
            if (compte[v] >= compte[meilleureValeur]) {
                meilleureValeur = v;
            }
        }
        for (int i = 0; i < nb_des; i++) {
            gardes[i] = des[i].getValeur() == meilleureValeur;
        }
    }
    private void calculerDistribution() {
        sm = 0;
        double somme = 0;
        for (int s : scores) {
            somme += s;
            sm = Math.max(sm, s);
        }
        moyenne = somme / scores.length;
        double sommeCarres = 0;
        for (int s : scores) {
            sommeCarres += (s - moyenne) * (s - moyenne);
        }
        ecartType = Math.max(Math.sqrt(sommeCarres / scores.length), 1e-9);
        histogramme = new int[sm / LARGEUR_CLASSE + 1];
        for (int s : scores) {
            histogramme[s / LARGEUR_CLASSE]++;
        }
    }
    public double densite(double x) {
        double z = (x - moyenne) / ecartType;
        return Math.exp(-0.5 * z * z) / (ecartType * Math.sqrt(2 * Math.PI));
    }
    public double integrerSimpson(double a, double b, int n) {
        if (n % 2 != 0) {
            n++;
        }
        double h = (b - a) / n;
        double somme = densite(a) + densite(b);
        for (int i = 1; i < n; i++) {
            somme += (i % 2 == 0 ? 2 : 4) * densite(a + i * h);
        }
        return somme * h / 3.0;
    }
    public double integrerTrapezes(double a, double b, int n) {
        double h = (b - a) / n;
        double somme = (densite(a) + densite(b)) / 2.0;
        for (int i = 1; i < n; i++) {
            somme += densite(a + i * h);
        }
        return somme * h;
    }
    public double probabiliteEmpirique(double a, double b) {
        int compte = 0;
        for (int s : scores) {
            if (s >= a && s <= b) {
                compte++;
            }
        }
        return (double) compte / scores.length;
    }
    public double getMoyenne() {
        return moyenne;
    }
    public double getEcartType() {
        return ecartType;
    }
    public static void afficherModule(Stage proprietaire) {
        StatistiquesJeu stats = new StatistiquesJeu();
        TextField tfParties = new TextField("10000");
        TextField tfA = new TextField("100");
        TextField tfB = new TextField("300");
        tfParties.setPrefColumnCount(6);
        tfA.setPrefColumnCount(4);
        tfB.setPrefColumnCount(4);
        Button btnCalculer = new Button("Simuler et calculer");
        HBox commandes = new HBox(8, new Label("Parties :"), tfParties,
                new Label("P(a ≤ score ≤ b)   a :"), tfA, new Label("b :"), tfB, btnCalculer);
        commandes.setAlignment(Pos.CENTER_LEFT);
        commandes.setPadding(new Insets(10));

        Canvas canvas = new Canvas(700, 340);
        Label lblResultats = new Label();
        lblResultats.setFont(Font.font("Arial", 13));
        lblResultats.setWrapText(true);
        VBox bas = new VBox(lblResultats);
        bas.setPadding(new Insets(10));
        Runnable calculer = () -> {
            try {
                int n = Integer.parseInt(tfParties.getText().trim());
                double a = Double.parseDouble(tfA.getText().trim());
                double b = Double.parseDouble(tfB.getText().trim());
                if (n < 100 || a > b) {
                    throw new NumberFormatException();
                }
                stats.simulerParties(n);

                double pSimpson = stats.integrerSimpson(a, b, 1000);
                double pTrapezes = stats.integrerTrapezes(a, b, 1000);
                double pEmpirique = stats.probabiliteEmpirique(a, b);

                lblResultats.setText(String.format(
                        "Parties simulées : %d    Moyenne  = %.2f    Écart-type = %.2f%n"
                                + "P(%.0f ≤ score ≤ %.0f) = f f(x)dx  →  S : %.2f %%   |   T : %.2f %%%n"
                                + "Fréquence observée dans la simulation : %.2f %%%n"
                                + "Interprétation : environ %.1f parties sur 100 donnent un score dans cet intervalle.",
                        n, stats.moyenne, stats.ecartType, a, b,
                        pSimpson * 100, pTrapezes * 100, pEmpirique * 100, pSimpson * 100));
                stats.dessiner(canvas, a, b);
            } catch (NumberFormatException ex) {
                Alert alerte = new Alert(Alert.AlertType.WARNING);
                alerte.setTitle("Valeurs invalides");
                alerte.setHeaderText("Vérifiez les champs");
                alerte.setContentText("Entrez un nombre de parties (≥ 100) et des bornes a ≤ b valides.");
                alerte.showAndWait();
            }
        };
        btnCalculer.setOnAction(e -> calculer.run());
        BorderPane racine = new BorderPane();
        racine.setTop(commandes);
        racine.setCenter(canvas);
        racine.setBottom(bas);
        Stage fenetre = new Stage();
        fenetre.setTitle("Statistiques - Distribution des scores");
        if (proprietaire != null) {
            fenetre.initOwner(proprietaire);
        }
        fenetre.setScene(new Scene(racine, 730, 540));
        fenetre.show();
        calculer.run();
    }

    /** l'histogramme */
    private void dessiner(Canvas canvas, double a, double b) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double largeur = canvas.getWidth();
        double hauteur = canvas.getHeight();
        double margeG = 45, margeB = 30, margeD = 15, margeH = 15;

        double xMax = Math.ceil((sm + 20) / 10.0) * 10;
        double yMax = densite(moyenne);
        for (int c : histogramme) {
            yMax = Math.max(yMax, c / (double) (scores.length * LARGEUR_CLASSE));
        }
        yMax *= 1.1;
        double zoneL = largeur - margeG - margeD;
        double zoneH = hauteur - margeB - margeH;
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, largeur, hauteur);
        g.setFill(Color.LIGHTSTEELBLUE);
        for (int i = 0; i < histogramme.length; i++) {
            double dens = histogramme[i] / (double) (scores.length * LARGEUR_CLASSE);
            double x0 = margeG + (i * LARGEUR_CLASSE) / xMax * zoneL;
            double w = LARGEUR_CLASSE / xMax * zoneL;
            double h = dens / yMax * zoneH;
            g.fillRect(x0, hauteur - margeB - h, Math.max(w - 1, 1), h);
        }
        double aa = Math.max(a, 0), bb = Math.min(b, xMax);
        if (aa < bb) {
            int pts = 200;
            double[] xs = new double[pts + 3];
            double[] ys = new double[pts + 3];
            xs[0] = margeG + aa / xMax * zoneL;
            ys[0] = hauteur - margeB;
            for (int i = 0; i <= pts; i++) {
                double x = aa + (bb - aa) * i / pts;
                xs[i + 1] = margeG + x / xMax * zoneL;
                ys[i + 1] = hauteur - margeB - densite(x) / yMax * zoneH;
            }
            xs[pts + 2] = margeG + bb / xMax * zoneL;
            ys[pts + 2] = hauteur - margeB;
            g.setFill(Color.rgb(220, 50, 50, 0.45));
            g.fillPolygon(xs, ys, pts + 3);
        }
        g.setStroke(Color.DARKRED);
        g.setLineWidth(2);
        int pas = 300;
        double xPrec = margeG, yPrec = hauteur - margeB - densite(0) / yMax * zoneH;
        for (int i = 1; i <= pas; i++) {
            double x = xMax * i / pas;
            double px = margeG + x / xMax * zoneL;
            double py = hauteur - margeB - densite(x) / yMax * zoneH;
            g.strokeLine(xPrec, yPrec, px, py);
            xPrec = px;
            yPrec = py;
        }
        g.setStroke(Color.BLACK);
        g.setLineWidth(1);
        g.strokeLine(margeG, hauteur - margeB, largeur - margeD, hauteur - margeB);
        g.strokeLine(margeG, margeH, margeG, hauteur - margeB);
        g.setFill(Color.BLACK);
        g.setFont(Font.font("Arial", 11));
        for (int x = 0; x <= xMax; x += 20) {
            double px = margeG + x / xMax * zoneL;
            g.strokeLine(px, hauteur - margeB, px, hauteur - margeB + 4);
            g.fillText(String.valueOf(x), px - 8, hauteur - margeB + 16);
        }
        g.fillText("Score", largeur / 2, hauteur - 4);
        g.fillText("Densité", 2, margeH + 8);
    }
}