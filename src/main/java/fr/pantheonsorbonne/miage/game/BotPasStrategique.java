package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BotPasStrategique extends Joueur {

    private int points;  // Ajout du champ points
    private int sensJeu = SensJeu.HORAIRE;
    private List<Joueur> joueurs;

    public BotPasStrategique(String nom) {
        super(nom, true);
    }

    public BotPasStrategique(String nom, List<Joueur> joueurs) {
        super(nom, true);
        this.joueurs = joueurs;
    }

    @Override
    public boolean demanderYaniv() {
        return false;
        // Implémentez la logique pour décider si le bot stratégique veut déclarer Assaf
        // Retournez true s'il veut déclarer Assaf, sinon false
    }

    @Override
    public boolean demanderAssaf() {
        // Ajoutez des messages de débogage
        System.out.println("Le bot non stratégique envisage de déclarer Assaf.");

        // Votre logique de décision ici

        // Ajoutez un message pour indiquer si Assaf est déclaré
        boolean declarerAssaf = true;
        System.out.println("Le bot non stratégique décide de déclarer Assaf : " + declarerAssaf);

        return declarerAssaf;
        // Implémentez la logique pour décider si le bot non stratégique veut déclarer
        // Assaf
        // Retournez true s'il veut déclarer Assaf, sinon false
    }

    @Override
    public void jouer(PaquetCartes paquet, Joueur prochainJoueur) {
        System.out.println("Tour de " + getNom());
        piocherCarte(paquet);

        // Logique pour choisir une carte à jouer (à implémenter)
        Carte carteAJouer = choisirCarteAJouer(prochainJoueur);

        // Vérifiez si la carte choisie est valide
        if (estCarteValide(carteAJouer, prochainJoueur)) {
            // Retirez la carte de la main du joueur et ajoutez-la à la pile de défausse
            main.remove(carteAJouer);
            paquet.ajouterALaDefausse(carteAJouer);

            // Affichez la carte jouée
            System.out.println(getNom() + " joue : " + carteAJouer);

        } else {
            // Si la carte choisie n'est pas valide, défaussez une carte au hasard
            System.out.println("Carte invalide. " + getNom() + " défausse une carte au hasard.");
            if (prochainJoueur instanceof BotStrategique) {
                defausserCarteAuHasard(paquet, (BotStrategique) prochainJoueur, 3);
            } else {
                defausserCarteAuHasard(paquet, prochainJoueur, 3);
            }
        }
    }

    public int getSensJeu() {
        return sensJeu;
    }

    public void setSensJeu(int sens) {
        sensJeu = sens;
    }

    public Carte choisirCarteAJouer(Joueur prochainJoueur) {
        // Triez la main du joueur par valeur
        Collections.sort(main);

        // Choisissez la première carte de la main
        if (!main.isEmpty()) {
            return main.get(0);
        } else {
            // Gérer le cas où la main est vide, par exemple, piocher une nouvelle carte ou
            // retourner null
            return null;
        }
    }

    public void defausserCarteAuHasard(PaquetCartes paquet, Joueur prochainJoueur, int essaisRestants) {
        if (essaisRestants > 0 && !main.isEmpty()) {
            Random random = new Random();
            int indexCarte = random.nextInt(main.size());
            Carte carteChoisie = main.get(indexCarte);

            if (estCarteValide(carteChoisie, prochainJoueur)) {
                main.remove(carteChoisie);
                paquet.ajouterALaDefausse(carteChoisie);
                System.out.println(getNom() + " défausse une carte au hasard : " + carteChoisie);
            } else {
                // Réduisez le nombre d'essais restants et réessayez
                defausserCarteAuHasard(paquet, prochainJoueur, essaisRestants - 1);
            }
        }
    }

    public boolean estCarteValide(Carte carteChoisie, Joueur prochainJoueur) {
        return false;
    }

    @Override
    public void declarerYanivOuAssaf(Joueur joueurGagnant) {
        System.out.println("Le joueur " + getNom() + " déclare Yaniv !");
        // Le bot non stratégique déclare toujours Yaniv, même s'il a plus de 7 points
        if (getPoints() < 7) {
            System.out.println(getNom() + " a gagné !");
        }
    }

    protected int getPoints() {
        int totalPoints = 0;

        for (Carte carte : main) {
            // Supposez que chaque carte a une méthode getValeur() qui retourne sa valeur en
            // points
            totalPoints += carte.getValeur();
        }

        return totalPoints;
    }

    @Override
    public Carte choisirCarteAJeter() {
        // Triez la main en ordre croissant de points
        main.sort(Comparator.comparingInt(Carte::getValeur));

        // Choisissez la carte avec le moins de points
        Carte carteAJeter = main.get(0);

        System.out.print("Carte jetée : " + carteAJeter);
        return carteAJeter;
    }

    @Override
    public boolean piocherCarteApresJeter(PaquetCartes paquet) {
        // Le BotPasStrategique choisit aléatoirement entre piocher dans la pioche ou
        // dans la défausse
        Random random = new Random();
        boolean prendreDansDefausse = random.nextBoolean();

        if (prendreDansDefausse) {
            // Le bot choisit de piocher dans la défausse
            Carte cartePiochee = paquet.piocherDefausse();
            main.add(cartePiochee);
            System.out.println("Carte piochée dans la défausse : " + cartePiochee);
        } else {
            // Le bot choisit de piocher dans la pioche
            Carte cartePiochee = paquet.piocherCarte();
            main.add(cartePiochee);
            System.out.println("Carte piochée dans la pioche : " + cartePiochee);
        }

        return prendreDansDefausse;
    }

    public void setMain(List<Carte> mainNonVide) {
        this.main = new ArrayList<>(mainNonVide);
    }

    

    public void setPoints(int i) {
        this.points = points;
    }

    public List<Carte> getMain() {
        return main;
    }

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }
}