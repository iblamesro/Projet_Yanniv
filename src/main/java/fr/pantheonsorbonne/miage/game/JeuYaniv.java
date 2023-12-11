package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.List;

//Classe JeuYaniv, Classe Main , le jeu s'execute ici
public class JeuYaniv {
    private List<Joueur> joueurs;
    private PaquetCartes paquet;
    private Joueur joueurCourant;
    private int sensJeu;
    //Constructeur , ajout dans la liste "joueurs" 2 bots (strategique et pas strategique ), afin de leurs distribuer à chacun les cartes 
    public JeuYaniv(int nombreJoueurs) {
        joueurs = new ArrayList<>();

        for (int i = 1; i < nombreJoueurs; i++) {  // Ici on a une itération qui ajoute un bot strategique et un bot pas strategique
            joueurs.add(new BotPasStrategique("Joueur " + (i))); //Joueur1
            joueurs.add(new BotStrategique("Joueur " + (i + 1))); //Joueur2
            BotStrategique botStrategique = new BotStrategique("Joueur " + i);
            botStrategique.setJoueurs(joueurs);
            int tailleJoueurs = joueurs.size();
            System.out.println("Taille de la liste joueurs : " + tailleJoueurs);

        }

        // Initialisation du joueur courant et du sens du jeu après avoir ajouté les
        // joueurs
        joueurCourant = joueurs.get(0); 
        sensJeu = SensJeu.HORAIRE; 

        paquet = new PaquetCartes();
        paquet.melanger();
        distribuerCartes();

    }
//Methode concernant le sens du jeu (horaire ou antihoraire)
       public int getSensJeu() {
        return this.sensJeu;
    }

    public void setSensJeu(int sens) {
        sensJeu = sens;
    }

    public void changerSensJeu() {
        System.out.println("Le sens du jeu a changé !");
        int sensActuel = getSensJeu();

        if (sensActuel == SensJeu.HORAIRE) {
            setSensJeu(SensJeu.ANTIHORAIRE);
            System.out.println("Le sens du jeu est maintenant anti-horaire.");
        } else {
            setSensJeu(SensJeu.HORAIRE);
            System.out.println("Le sens du jeu est maintenant horaire.");
        }
    }

    //Methode qui permet la distribution des cartes par joueurs 
    private void distribuerCartes() {
        // Mélanger et redistribuer si le paquet n'a pas suffisamment de cartes
        if (paquet.getNombreCartes() < joueurs.size() * 7) {
            paquet.melanger();
        }

        for (Joueur joueur : joueurs) {
            System.out.println();
            joueur.afficherNom();// Affiche le nom du joueur (soit joueur 1 soit joueur 2) avant de recevoir leur cartes dans le Terminal
            for (int i = 0; i < 7; i++) {  // 7 cartes par joueur 
                if (paquet.getNombreCartes() > 0) {
                    joueur.piocherCarte(paquet);
                } else {
                    //Permet de  gérer le cas où le paquet est vide
                    System.out.println("Le paquet est vide. Mélange et redistribue.");
                    paquet.melanger();
                    joueur.piocherCarte(paquet);
                }
            }
            System.out.println();//saut de ligne pour meilleur affichage 
        }
        System.out.println(); // " "
    }

    private List<Carte> creerPioche() {
        // Créer une pioche avec les cartes restantes du paquet initial , soit 54 (avec les 2 Jokers) - 14 (la distrbituon des cartes par joueurs)
        //Le nombre de cartes restantes dans pioche se modifie tout au long de la partie , lorsque un joueur pioche ou jete une carte
        return paquet.creerPioche();
    }

//Methode permettant aux joueurs , chacun leur tour , de jeter une carte puis piocher une nouvelle
    public void jeterEtPiocherCarte(Joueur joueur) {
        if (joueur.doitSauterSonTour) {
            System.out.println("Le " + joueur.getNom() + " doit sauter son tour..."); // Cas de la paire 8 , le tour du joueur suivant doit etre sauter il ne peux donc ni jeter ni piocher 
            joueur.doitSauterSonTour = false;
        } else {  //Tous les autre cas
            System.out.println();
            System.out.println(joueur.getNom());

            // Vérifie la meilleur combinaison , c'est a dire la suite , afin de la jeter 
            List<Carte> combinaison = joueur.trouverMeilleureCombinaison();

            if (combinaison != null && !combinaison.isEmpty()) {
                System.out.println("Combinaison trouvée  " + combinaison);
                joueur.jeterCombinaison(paquet, combinaison);
            } else {
                // Si aucune combinaison n'est trouvée, alors le joueur peut jeter des carte multiple (donc paire , brelan , carre) ou sinon un carte normal
                Carte carteAJeter = joueur.choisirCarteAJeter();
                joueur.jeterCarte(paquet, carteAJeter, true);

            }
            System.out.println();

            joueur.piocherCarteApresJeter(paquet); // Permet de piocher une carte soit de la pioche soit de la défausse
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
        } while (joueurCourant.calculerTotalPoints() >= 7); // Methode qui permet de jeter et piocher les cartes jusqu'a que un des 2 joueurs est obtenu un total de point inferieur strictement à 7 et peut donc dire Yanniv
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
//La partie est terminé on attribue les points en fonction des mains du joueurs 
    private void finirManche() {
        System.out.println("Fin de la manche! Évaluation des mains et attribution des points.");

        Joueur joueurGagnant = null;
        int pointsGagnant = Integer.MAX_VALUE;

        for (Joueur joueur : joueurs) {
            int valeurMain = joueur.calculerTotalPoints();
            joueur.setScore(valeurMain);
            if (valeurMain < pointsGagnant) {
                joueurGagnant = joueur;
                pointsGagnant = valeurMain;
            }
        }

        System.out.println(joueurGagnant.getNom() + " à la main la moins élevée");

        for (Joueur joueur : joueurs) {
            if (joueur != joueurGagnant) {
                joueur.incrementeScore(joueur.getScore());
                System.out.println(joueur.getNom() + " à " + joueur.getScore() + " points.");
            }
        }

        // Déclaration de Yaniv ou Assaf et application des règles spécifiques
        declarerYanivOuAssaf(joueurGagnant); //Appel de la prochaine méthode 
    }


    private void declarerYanivOuAssaf(Joueur joueurGagnant) {

        System.out.println(joueurGagnant.getNom() + " a une main de valeur : "
                + joueurGagnant.calculerTotalPoints() + " points.");

        // Déclaration de Yaniv
        if (joueurGagnant.calculerTotalPoints() < 7) {
            System.out.println(joueurGagnant.getNom() + " déclare Yaniv !");
            // Fin de la partie, marquer la victoire du joueur
            partieTerminee(joueurGagnant);
        } else {
            // Déclaration de Assaf
            boolean declarerAssaf = joueurGagnant.demanderAssaf();

            if (declarerAssaf) {
                System.out.println(joueurGagnant.getNom() + " déclare Assaf !");
                // Le joueur gagnant de la manche prend 30 points de pénalité
                joueurGagnant.incrementeScore(30);
                System.out.println(joueurGagnant.getNom() + " prend 30 points de pénalité.");
            }
        }
    }
// Déclaration du gagnant
    private void partieTerminee(Joueur joueurGagnant) {
        System.out.println("La partie est terminée. " + joueurGagnant.getNom() + " a gagné !");
        System.exit(0);
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
