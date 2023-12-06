package fr.pantheonsorbonne.miage.game;
// Implémentation du BotStrategique
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BotStrategique extends Joueur {
    @Override
    public boolean demanderAssaf() {
        return false;
        // Implémentez la logique pour décider si le bot stratégique veut déclarer Assaf
        // Retournez true s'il veut déclarer Assaf, sinon false
    }

    @Override
    public boolean demanderYaniv() {
        return true;
        // Implémentez la logique pour décider si le bot stratégique veut déclarer Assaf
        // Retournez true s'il veut déclarer Assaf, sinon false
    }

    private static List<Joueur> joueurs;
    private int sensJeu = SensJeu.HORAIRE;

    public BotStrategique(String nom) {
        super(nom, true);
    }

    @Override
    public void jouerTour(PaquetCartes paquet, Joueur prochainJoueur) {
        System.out.println("Tour de " + getNom());
        piocherCarte(paquet);

        // Logique pour choisir une carte à jouer (à implémenter)
        Carte carteAJouer = choisirCarteAJouer(prochainJoueur);

        // Vérifiez d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Convertissez prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            // Vérifiez si la carte choisie est valide
            if (estCarteValide(carteAJouer, botProchainJoueur)) {
                // Retirez la carte de la main du joueur et ajoutez-la à la pile de défausse
                main.remove(carteAJouer);
                paquet.ajouterALaDefausse(carteAJouer);

                // Affichez la carte jouée
                System.out.println(getNom() + " joue : " + carteAJouer);

                // Appliquez les règles spécifiques du jeu Yaniv ici
                appliquerReglesYaniv(carteAJouer, botProchainJoueur);
            } else {
                // Si la carte choisie n'est pas valide, défaussez une carte au hasard
                System.out.println("Carte invalide. " + getNom() + " défausse une carte au hasard.");
                defausserCarteAuHasard(paquet, botProchainJoueur);
            }
        }
    }

    private void defausserCarteAuHasard(PaquetCartes paquet, BotStrategique prochainJoueur) {
        // Logique pour défausser une carte au hasard
        if (!main.isEmpty()) {
            // Choisissez une carte au hasard
            Random random = new Random();
            int indexCarte = random.nextInt(main.size());
            Carte carteChoisie = main.get(indexCarte);

            // Vérifiez si la carte choisie est valide
            if (estCarteValide(carteChoisie, prochainJoueur)) {
                main.remove(carteChoisie);
                paquet.ajouterALaDefausse(carteChoisie);
                System.out.println(getNom() + " défausse une carte au hasard : " + carteChoisie);
            } else {
                // Si la carte choisie n'est pas valide, réessayez de défausser une autre carte
                defausserCarteAuHasard(paquet, prochainJoueur);
            }
        }
    }

    private void appliquerReglesYaniv(Carte carteAJouer, Joueur prochainJoueur) {
        // Vérifiez d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Convertissez prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            // Gestion du double 7-7
            if (carteAJouer.getValeur() == 7 && carteAJouer.estJusteApres(carteAJouer)) {
                // Changer de sens
                changerSensJeu();
            }

            // Gestion du double 8-8
            if (carteAJouer.getValeur() == 8 && carteAJouer.estJusteApres(carteAJouer)) {
                // Sauter le tour du prochain joueur
                sauterTourProchainJoueur(botProchainJoueur);
            }

            // Gestion du double 9-9
            if (carteAJouer.getValeur() == 9 && carteAJouer.estJusteApres(carteAJouer)) {
                // Forcer le prochain joueur à piocher une carte de la pioche
                forcerPiocheProchainJoueur(null, botProchainJoueur);
            }

            // Gestion du double 10-10
            if (carteAJouer.getValeur() == 10 && carteAJouer.estJusteApres(carteAJouer)) {
                // Piocher au hasard une carte chez le prochain joueur
                // et éventuellement l'échanger avec l'une de ses cartes
                piocherEchangerProchainJoueur(botProchainJoueur);
            }

            // Gestion de la suite 10-J-Q
            if (carteAJouer.getValeur() == 10 && carteAJouer.estJusteApres(carteAJouer)) {
                // Le prochain joueur est obligé de mettre une carte qui continue la suite
                // (un K de la même couleur) sinon pioche une carte
                gererSuite10JQ(botProchainJoueur, null);
            }
        }
    }

    // Les autres méthodes restent inchangées

    private void changerSensJeu() {
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

    public int getSensJeu() {
        return sensJeu;
    }

    public void setSensJeu(int sens) {
        sensJeu = sens;
    }

    // Les méthodes de gestion des règles spécifiques à implémenter
    public class JeuYaniv {
        // ...
        private int sensJeu = SensJeu.HORAIRE;

        private void changerSensJeu() {
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

        private Joueur obtenirProchainJoueur() {
            int indexJoueurCourant = joueurs.indexOf(this); // Assurez-vous que "this" se réfère à l'instance actuelle
                                                            // de Joueur

            if (indexJoueurCourant != -1) {
                // Calculer l'index du prochain joueur en fonction du sens du jeu
                int indexProchainJoueur = (sensJeu == SensJeu.ANTIHORAIRE)
                        ? (indexJoueurCourant + 1) % joueurs.size()
                        : (indexJoueurCourant - 1 + joueurs.size()) % joueurs.size();

                return joueurs.get(indexProchainJoueur);
            }

            return null;
        }

        public int getSensJeu() {
            return sensJeu;
        }

        public void setSensJeu(int sens) {
            sensJeu = sens;
        }
        // ...
    }

    private void sauterTourProchainJoueur(Joueur prochainJoueur) {
        System.out.println("Le prochain joueur, " + prochainJoueur.getNom() + ", a son tour sauté !");
        prochainJoueur.sauterTour();
    }

    private void forcerPiocheProchainJoueur(PaquetCartes paquet, Joueur prochainJoueur) {
        Carte cartePiochee = paquet.piocherCarte();
        prochainJoueur.main.add(cartePiochee);
        System.out.println(prochainJoueur.getNom() + " doit piocher une carte !");
    }

    private void piocherEchangerProchainJoueur(Joueur prochainJoueur) {
        // Piocher au hasard une carte chez le prochain joueur
        Collections.shuffle(prochainJoueur.main);
        Carte cartePiochee = prochainJoueur.main.get(0);

        // Éventuellement l'échanger avec l'une de ses cartes
        if (!main.isEmpty()) {
            Collections.shuffle(main);
            Carte carteEchangee = main.get(0);

            // Afficher les cartes échangées
            System.out.println(getNom() + " pioche et échange avec " + prochainJoueur.getNom() +
                    ": " + cartePiochee + " vs " + carteEchangee);

            // Effectuer l'échange
            prochainJoueur.main.remove(cartePiochee);
            main.remove(carteEchangee);
            prochainJoueur.main.add(carteEchangee);
            main.add(cartePiochee);
        }
    }

    private void gererSuite10JQ(Joueur prochainJoueur, PaquetCartes paquet) {
        // Logique pour gérer la suite 10-J-Q
        Carte carteK = new Carte("K", "Pique"); // Supposons que le bot choisit le K de Pique

        if (main.contains(carteK)) {
            System.out.println(getNom() + " joue la suite 10-J-Q avec le K de Pique !");
            main.remove(carteK);
        } else {
            forcerPiocheProchainJoueur(paquet, prochainJoueur);
        }
    }

    private boolean estCarteValide(Carte carteAJouer, Joueur prochainJoueur) {
        // Vérifiez d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Convertissez prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            // Récupérez la carte du joueur suivant
            Carte carteProchainJoueur = botProchainJoueur.choisirCarteAJouer(null); // null pour le scanner, à remplacer
                                                                                    // si nécessaire

            // Vérifiez si la carte à jouer est valide par rapport à la carte du prochain
            // joueur
            if (carteProchainJoueur != null) {
                // Vérifiez si la valeur ou la couleur de la carte à jouer correspond à celle de
                // la carte du prochain joueur
                return carteAJouer.getValeur() == carteProchainJoueur.getValeur()
                        || carteAJouer.getCouleur().equals(carteProchainJoueur.getCouleur());
            }
        }

        // Si prochainJoueur n'est pas un BotStrategique ou s'il n'a pas de carte à
        // jouer, toute carte est valide
        return true;
    }

    @Override
    public void declarerYanivOuAssaf(Joueur joueurGagnant) {
        int pointsBotStrategique = getPoints();
        int pointsBotPasStrategique = joueurGagnant.getPoints(); // Supposons que la méthode getPoints existe dans la
                                                                 // classe Joueur

        // Logique pour la déclaration de Yaniv ou Assaf
        if (pointsBotStrategique <= 7 && pointsBotStrategique < pointsBotPasStrategique) {
            System.out.println(getNom() + " déclare Yaniv !");
            System.out.println(getNom() + " a gagné !");
        } else {
            System.out.println(getNom() + " déclare Assaf !");
            System.out.println(joueurGagnant.getNom() + " a gagné !");
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

    // Autres méthodes nécessaires pour la logique du bot stratégique (à
    // implémenter)

    private Carte choisirCarteAJouer(Joueur prochainJoueur) {
        // Vérifiez d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Convertissez prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            // Triez la main du BotStrategique par valeur (ou selon une stratégie
            // spécifique)
            Collections.sort(main);

            // Parcourez les cartes dans la main, en commençant par la carte de plus faible
            // valeur
            for (Carte carte : main) {
                // Vérifiez si la carte est valide (utilisez votre logique spécifique)
                if (estCarteValide(carte, prochainJoueur)) {
                    return carte;
                }
            }
        }

        // Si prochainJoueur n'est pas un BotStrategique, retournez null ou une autre
        return null;
    }

    @Override
    public Carte choisirCarteAJeter() {
        // Triez la main en ordre décroissant de points
        main.sort(Comparator.comparingInt(Carte::getValeur).reversed());

        // Choisissez la carte avec le plus grand nombre de points
        Carte carteAJeter = main.get(0);

        System.out.println(getNom() + " a jeté la carte : " + carteAJeter);
        return carteAJeter;
    }

    @Override
    public boolean piocherCarteApresJeter(PaquetCartes paquet) {
        // Le BotPasStrategique choisit aléatoirement entre piocher dans la pioche ou
        // dans la défausse
        Random random = new Random();
        boolean prendreDansDefausse = random.nextBoolean();

        // Affichez un message indiquant d'où le joueur a pioché
        System.out.println(
                getNom() + " a choisi de piocher dans " + (prendreDansDefausse ? "la défausse." : "la pioche."));

        if (prendreDansDefausse) {
            // Le bot choisit de piocher dans la défausse
            Carte cartePiochee = paquet.piocherDefausse();
            main.add(cartePiochee);
            System.out.println(getNom() + " a pioché dans la défausse : " + cartePiochee);
        } else {
            // Le bot choisit de piocher dans la pioche
            Carte cartePiochee = paquet.piocherCarte();
            main.add(cartePiochee);
            System.out.println(getNom() + " a pioché dans la pioche : " + cartePiochee);
        }

        return prendreDansDefausse;
    }

}
