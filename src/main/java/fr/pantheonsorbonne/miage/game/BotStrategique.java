package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
// Implémentation du BotStrategique
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class BotStrategique extends Joueur {

    private int points;  // Ajout du champ points
    private static List<Joueur> joueurs;
    private int sensJeu = SensJeu.HORAIRE;

    public BotStrategique(String nom) {
        super(nom, true);
    }

    Joueur botProchainJoueur;
    BotStrategique prochainJoueur;

    public BotStrategique(String nom, List<Joueur> joueurs) {
        super(nom, true);
        this.joueurs = joueurs;
    }

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

    @Override
    public void jouer(PaquetCartes paquet, Joueur prochainJoueur) {
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
            } else {
                // Si la carte choisie n'est pas valide, défaussez une carte au hasard
                System.out.println("Carte invalide. " + getNom() + " défausse une carte au hasard.");
                defausserCarteAuHasard(paquet, botProchainJoueur);
            }
        }
    }

    public void defausserCarteAuHasard(PaquetCartes paquet, BotStrategique prochainJoueur) {
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

    private Joueur obtenirJoueurSuivant() {
        int indexJoueurActuel = joueurs.indexOf(this);
        int indexJoueurSuivant = (indexJoueurActuel + 1) % joueurs.size();
        return joueurs.get(indexJoueurSuivant);
    }

    private void sauterTourJoueurSuivant() {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Le tour de " + joueurSuivant.getNom() + " est sauté !");
        joueurSuivant.setTourSauté();
    }

    private void forcerPiocheJoueurSuivant(PaquetCartes paquet) {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Forcer " + joueurSuivant.getNom() + " à piocher une carte de la pioche.");
        joueurSuivant.piocherCarte(paquet);
    }

    public boolean estCarteValide(Carte carteAJouer, Joueur prochainJoueur) {
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

    public Carte choisirCarteAJouer(Joueur prochainJoueur) {
        // Vérifiez d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
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

        // Vérifiez s'il y a des cartes multiples (paires, triples, etc.)
        Map<Integer, List<Carte>> cartesParValeur = main.stream()
                .collect(Collectors.groupingBy(Carte::getValeur));

        // Recherchez la première carte multiple (paires, triples, etc.)
        Optional<Map.Entry<Integer, List<Carte>>> carteMultiples = cartesParValeur.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .findFirst();

        // Si une carte multiple est trouvée, choisissez-la
        if (carteMultiples.isPresent()) {
            Map.Entry<Integer, List<Carte>> entry = carteMultiples.get();
            List<Carte> cartesMultiples = entry.getValue();
            // Déterminez le type de multiple (paire, brelan, carré, etc.)
            String typeMultiple;
            switch (cartesMultiples.size()) {
                case 2:
                    typeMultiple = "paire";
                    break;
                case 3:
                    typeMultiple = "brelan";
                    break;
                case 4:
                    typeMultiple = "carré";
                    break;
                default:
                    typeMultiple = "inconnu";
                    break;
            }
            // Vérifiez si la carte de la paire est 7
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 7) {
                // Changez le sens du jeu
                System.out.println("La paire contient un 7. Le sens du jeu change !");
                changerSensJeu();
            }
            // Vérifiez si la carte de la paire est 8
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 8) {
                // Sauter le tour du prochain joueur
                System.out.println("La paire contient un 8. Le prochain joueur sautera son tour !");
                sauterTourJoueurSuivant();
            }
            // Vérifiez si la carte de la paire est 9
            PaquetCartes paquetDeCartes = new PaquetCartes();
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 9) {
                // forcer le joueur suivant a pioche une carte de la pioche
                System.out
                        .println("La paire contient un 9. Le prochain joueur sera obligé de pioché dans la pioche !!");
                forcerPiocheJoueurSuivant(paquetDeCartes);
            }

            // Choisissez la carte à jeter parmi les cartes multiples
            Carte carteAJeter = cartesMultiples.get(0);
            System.out.println(getNom() + " a jeté la carte multiple (" + typeMultiple + ") : " + carteAJeter);
            return carteAJeter;
        }
        // Vérifiez s'il y a une paire dans la main

        if (CombinaisonsDeCartes.estDouble(main)) {
            // Choisissez la carte avec la plus grande valeur dans la paire
            Carte carteDouble = main.stream()
                    .filter(carte -> Collections.frequency(main, carte) == 2)
                    .findFirst()
                    .orElse(null);

            if (carteDouble != null) {
                return carteDouble;
            }
        }

        // Vérifiez s'il y a une suite dans la main
        if (CombinaisonsDeCartes.estSuite(main)) {
            // Choisissez la carte avec la plus grande valeur dans la suite
            Carte carteSuite = main.get(0);
            return carteSuite;
        }

        // Vérifiez s'il y a un brelan dans la main
        if (CombinaisonsDeCartes.estBrelan(main)) {
            // Choisissez la carte avec la plus grande valeur dans le brelan
            Carte carteBrelan = main.stream()
                    .filter(carte -> CombinaisonsDeCartes.nombreOccurences(main, carte.getValeur()) == 3)
                    .findFirst()
                    .orElse(null);

            if (carteBrelan != null) {
                return carteBrelan;
            }
        }

        // Vérifiez s'il y a un carré dans la main
        if (CombinaisonsDeCartes.estCarre(main)) {
            // Choisissez la carte avec la plus grande valeur dans le carré
            Carte carteCarre = main.stream()
                    .filter(carte -> CombinaisonsDeCartes.nombreOccurences(main, carte.getValeur()) == 4)
                    .findFirst()
                    .orElse(null);

            if (carteCarre != null) {
                return carteCarre;
            }
        }

        // S'il n'y a pas de combinaisons spéciales, choisissez la carte avec le plus
        // grand nombre de points
        Carte carteAJeter = main.get(0);
        System.out.println("Carte jetée : " + carteAJeter);
        return carteAJeter;
    }

    @Override
    public boolean piocherCarteApresJeter(PaquetCartes paquet) {
        // Le BotPasStrategique choisit aléatoirement entre piocher dans la pioche ou
        // dans la défausse
        Random random = new Random();
        boolean prendreDansDefausse = random.nextBoolean();

        // Affichez un message indiquant d'où le joueur a pioché
        System.out.println("Il a choisi de piocher dans " + (prendreDansDefausse ? "la défausse." : "la pioche.")); // message
                                                                                                                    // d'affiche
                                                                                                                    // de
                                                                                                                    // joueur
                                                                                                                    // 2
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