package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.List;

// Classe abstraite Joueur
public abstract class Joueur {
    protected String nom;
    public ArrayList<Carte> main;
    protected int score;
    public boolean doitSauterSonTour = false;

    public Joueur(String nom, boolean estBot) {
        this.nom = nom;
        this.main = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void incrementeScore(int points) {
        this.score += points;
    }

    public void afficherNom() {
        System.out.println("Main du " + nom + " :");
    }

    public void piocherCarte(PaquetCartes paquet) {
        Carte cartePiochee = paquet.piocherCarte();
        main.add(cartePiochee);
        System.out.print(cartePiochee + " "); //Affichage des carte dans la main du joueur
    }

    //Méthode qui permet lors de la paire 8 de sauter le tour du prochain joueur 
    public void jouerTour(PaquetCartes paquet, Joueur prochainJoueur) {
        if (this.doitSauterSonTour) {
            this.doitSauterSonTour = false;
        } else {
            this.jouer(paquet, prochainJoueur);
        }
    }

    public abstract void jouer(PaquetCartes paquet, Joueur prochainJoueur);

    public abstract boolean demanderAssaf();

    public abstract boolean demanderYaniv();

    public abstract void declarerYanivOuAssaf(Joueur joueurGagnant);

    public int calculerTotalPoints() {
        int totalPoints = 0;
        int nombreJokers = 0;
        for (Carte carte : main) {
            totalPoints += attribuerPointsSelonValeur(carte, totalPoints);
            // Compter le nombre de Jokers dans la main
            if (carte.getValeur() == 14) {
                nombreJokers++;
            }
        }
        // Applique la règle spécifique au Joker
        totalPoints = ajusterPointsJoker(totalPoints, nombreJokers);
        return totalPoints;
    }

    private int attribuerPointsSelonValeur(Carte carte, int totalPoints) {
        int valeurCarte = carte.getValeur();
        switch (valeurCarte) {
            case 1:
                return 1; // As : 1 point
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return valeurCarte; // 2 au 10 : la valeur inscrite sur la carte
            case 11:
                return 11; // Valet : 11 points
            case 12:
            case 13:
                return 10; // Dame et Roi : 10 points
            case 14:
                return 2; // Joker : 2 points
            default:
                return 0; // Valeurs non prises en compte
        }
    }

    public int ajusterPointsJoker(int totalPoints, int nombreJokers) {
        // Si le nombre de Jokers est supérieur à 1, ajuster le total des points
        if (nombreJokers > 1) {
            totalPoints += (nombreJokers - 1) * 5; // Chaque Joker supplémentaire après le premier ajoute 5 points
        }
        return totalPoints;
    }

    public boolean aGagne() {
        // Défini le seuil de points pour la victoire
        int seuilVictoire = 7;
        // Évalue la valeur de la main du joueur
        int totalPoints = calculerTotalPoints();
        // Retourne true si le joueur a atteint ou dépassé le seuil de points
        return totalPoints <= seuilVictoire;

    }

    public void sauterTour() {
    }

    protected int getPoints() {
        int totalPoints = 0;
        for (Carte carte : main) {
            totalPoints += carte.getValeur();
        }
        return totalPoints;
    }

    public int getNombreCartes() {
        return main.size();
    }

    public List<Carte> getMain() {
        return null;
    }

    public Carte choisirCarteAJeter() {
        // Choisi automatiquement la première carte de la main à jeter
        return main.get(0);
    }

    public void jeterCarte(PaquetCartes paquet, Carte carte, boolean dansDefausse) {
        // Enleve la carte de la main du joueur
        main.remove(carte);
        // Ajoutez la carte à la défausse si nécessaire
        if (dansDefausse) {
            paquet.ajouterDefausse(carte);
        }
    }

    public boolean piocherCarteApresJeter(PaquetCartes paquet) {
        // Utilise la méthode piocherCarteApresJeter du paquet
        boolean prendreDansDefausse = paquet.piocherCarteApresJeter();
        return prendreDansDefausse;
    }

    public List<Carte> trouverMeilleureCombinaison() {
        // Vérifier si le joueur a une suite
        if (CombinaisonsDeCartes.estSuite(main)) {
            // Retourner la suite trouvée
            return main;
        }
        // Si aucune autre combinaison n'est trouvée
        return null;
    }

    public void jeterCombinaison(PaquetCartes paquet, List<Carte> combinaison) {
        // S'assure que les cartes de la combinaison sont présentes dans la main du joueur
        if (main.containsAll(combinaison)) {
            // Retire les cartes de la combinaison de la main du joueur
            main.removeAll(combinaison);
            // Ajoute ces cartes au paquet 
            paquet.ajouterCartes(combinaison);
            if (CombinaisonsDeCartes.estSuite(combinaison)) {
                System.out.println("A jeté une suite : " + combinaison);
            } else {
                System.out.println(getNom() + " a jeté une combinaison non spécifiée : " + combinaison);
            }
        } else {
            System.out.println("Erreur : la combinaison n'est pas valide pour le joueur " + getNom());
        }
    }
//on a setTourSauté() dans la classe BotStrategique quand doitSauterSonTour est true
    public void setTourSauté() {
        this.doitSauterSonTour = true;
    }

}
