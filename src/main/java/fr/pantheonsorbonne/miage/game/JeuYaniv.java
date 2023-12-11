package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.List;

public class JeuYaniv {
    private List<Joueur> joueurs;
    private PaquetCartes paquet;
    private Joueur joueurCourant;
    private int sensJeu;

    // private static int sensJeu = PaquetCartes.SENS_HORAIRE;

    public JeuYaniv(int nombreJoueurs) {
        joueurs = new ArrayList<>();

        for (int i = 1; i < nombreJoueurs; i++) { // ici on a une iteration qui ajoute un bot strategique et un bot pas
                                                  // strategique
            joueurs.add(new BotPasStrategique("Joueur " + (i)));
            joueurs.add(new BotStrategique("Joueur " + (i + 1)));
            BotStrategique botStrategique = new BotStrategique("Joueur " + i);
            botStrategique.setJoueurs(joueurs);
            int tailleJoueurs = joueurs.size();
            System.out.println("Taille de la liste joueurs : " + tailleJoueurs);

        }

        // Initialisation du joueur courant et du sens du jeu après avoir ajouté les
        // joueurs
        joueurCourant = joueurs.get(0); // Vous pouvez choisir un joueur initial ici
        sensJeu = SensJeu.HORAIRE; // Vous pouvez choisir le sens initial ici

        paquet = new PaquetCartes();
        paquet.melanger();
        distribuerCartes();

    }

    private void distribuerCartes() {
        // Mélanger et redistribuer si le paquet n'a pas suffisamment de cartes
        if (paquet.getNombreCartes() < joueurs.size() * 7) {
            paquet.melanger();
        }

        for (Joueur joueur : joueurs) {
            System.out.println();
            joueur.afficherNom();
            for (int i = 0; i < 7; i++) {
                if (paquet.getNombreCartes() > 0) {
                    joueur.piocherCarte(paquet);
                } else {
                    // Gérer le cas où le paquet est vide (vous pouvez ajouter une logique
                    // supplémentaire ici)
                    System.out.println("Le paquet est vide. Mélange et redistribue.");
                    paquet.melanger();
                    joueur.piocherCarte(paquet);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private List<Carte> creerPioche() {
        // Créer une pioche avec les cartes restantes du paquet initial
        return paquet.creerPioche();
    }

    public void jeterEtPiocherCarte(Joueur joueur) {
        if (joueur.doitSauterSonTour) {
            System.out.println("Le " + joueur.getNom() + " doit sauter son tour...");
            joueur.doitSauterSonTour = false;
        } else {
            System.out.println();
            System.out.println(joueur.getNom());

            // Vérifiez les combinaisons et choisissez la carte à jeter en conséquence
            List<Carte> combinaison = joueur.trouverMeilleureCombinaison();

            if (combinaison != null && !combinaison.isEmpty()) {
                System.out.println("Combinaison trouvée  " + combinaison);
                joueur.jeterCombinaison(paquet, combinaison);
            } else {
                // Si aucune combinaison n'est trouvée, choisissez une carte à jeter normalement
                Carte carteAJeter = joueur.choisirCarteAJeter();
                joueur.jeterCarte(paquet, carteAJeter, true);

            }
            System.out.println();

            joueur.piocherCarteApresJeter(paquet);
        }
        System.out.println();

    }

    public void jeterEtPiocher() {
        do {
            joueurCourant = obtenirProchainJoueur(joueurCourant, joueurs, SensJeu.HORAIRE);
            jeterEtPiocherCarte(joueurCourant);
            // Mettreclea à jour le
            // joueur
            // courant
        } while (joueurCourant.calculerTotalPoints() >= 7);
    }

    private Joueur obtenirProchainJoueur(Joueur joueurCourant, List<Joueur> joueurs, int sensJeu) {
        int indexJoueurCourant = joueurs.indexOf(joueurCourant);

        if (indexJoueurCourant != -1) {
            // Calculer l'index du prochain joueur en fonction du sens du jeu
            int indexProchainJoueur = (sensJeu == SensJeu.ANTIHORAIRE)
                    ? (indexJoueurCourant + 1) % joueurs.size()
                    : (indexJoueurCourant - 1 + joueurs.size()) % joueurs.size();

            return joueurs.get(indexProchainJoueur);
        }

        return null;
    }

    private void finirManche() {
        System.out.println("Fin de la manche! Évaluation des mains et attribution des points.");

        Joueur joueurGagnant = null;
        int pointsGagnant = Integer.MAX_VALUE;

        for (Joueur joueur : joueurs) {
            int valeurMain = joueur.calculerTotalPoints();
            joueur.setScore(valeurMain);

            System.out.println(joueur.getNom() + " a une main de valeur : " + valeurMain + " points.");

            if (valeurMain < pointsGagnant) {
                joueurGagnant = joueur;
                pointsGagnant = valeurMain;
            }
        }

        System.out.println(
                "Le joueur " + joueurGagnant.getNom() + " a la main la moins élevée et ne marque aucun point.");

        for (Joueur joueur : joueurs) {
            if (joueur != joueurGagnant) {
                joueur.incrementeScore(joueur.getScore());
                System.out.println(joueur.getNom() + " marque " + joueur.getScore() + " points.");
            }
        }

        // Déclaration de Yaniv ou Assaf et application des règles spécifiques
        declarerYanivOuAssaf(joueurGagnant);
    }

    private void declarerYanivOuAssaf(Joueur joueurGagnant) {

        System.out.println("Le joueur " + joueurGagnant.getNom() + " a une main de valeur : "
                + joueurGagnant.calculerTotalPoints() + " points.");

        // Déclaration de Yaniv
        if (joueurGagnant.calculerTotalPoints() < 7) {
            System.out.println("Le joueur " + joueurGagnant.getNom() + " déclare Yaniv !");
            // Fin de la partie, marquer la victoire du joueur
            partieTerminee(joueurGagnant);
        } else {
            // Déclaration de Assaf
            boolean declarerAssaf = joueurGagnant.demanderAssaf();

            if (declarerAssaf) {
                System.out.println("Le joueur " + joueurGagnant.getNom() + " déclare Assaf !");
                // Le joueur gagnant de la manche prend 30 points de pénalité
                joueurGagnant.incrementeScore(30);
                System.out.println("Le joueur " + joueurGagnant.getNom() + " prend 30 points de pénalité.");
            }
        }
    }

    private void partieTerminee(Joueur joueurGagnant) {
        System.out.println("La partie est terminée. " + joueurGagnant.getNom() + " a gagné !");
        // Ajoutez ici toute logique supplémentaire à exécuter à la fin de la partie
        System.exit(0); // Ceci est optionnel, cela ferme l'application
    }

    public static void main(String[] args) {
        JeuYaniv jeu = new JeuYaniv(2);

        // Jeter les cartes avant le début de la partie
        jeu.jeterEtPiocher();

        // Créer une pioche avec les cartes restantes
        List<Carte> cartesRestantes = jeu.creerPioche();

        // Afficher les cartes dans la pioche
        System.out.println("Cartes dans la pioche après distribution :");
        for (Carte carte : cartesRestantes) {
            System.out.println(carte);
        }

        // Afficher le nombre de cartes dans la pioche
        System.out.println("Nombre de cartes dans la pioche : " + cartesRestantes.size());
        jeu.finirManche();

    }
}

// Classe Joueur et les autres classes restent inchangées
