package fr.pantheonsorbonne.miage.game;

// Implémentation du BotStrategique
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class BotStrategique extends Joueur {
    private static List<Joueur> joueurs;
    private int sensJeu = SensJeu.HORAIRE;

    public BotStrategique(String nom) {
        super(nom, true);
    }
    Joueur botProchainJoueur;
    BotStrategique prochainJoueur;

    public void setJoueurs(List<Joueur> joueurs) {
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
                sauterTourJoueurSuivant();
            }

            // Gestion du double 9-9
            if (carteAJouer.getValeur() == 9 && carteAJouer.estJusteApres(carteAJouer)) {
                // Forcer le prochain joueur à piocher une carte de la pioche
                forcerPiocheJoueurSuivant(null);
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

    private Joueur obtenirJoueurSuivant() {
        int indexJoueurActuel = joueurs.indexOf(this);
        int indexJoueurSuivant = (indexJoueurActuel + 1) % joueurs.size();
        return joueurs.get(indexJoueurSuivant);
    }

    private void sauterTourJoueurSuivant() {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Le tour de " + joueurSuivant.getNom() + " est sauté !");
        joueurSuivant.setTourSauté(true);
    }


   
    private void forcerPiocheJoueurSuivant(PaquetCartes paquet) {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Forcer " + joueurSuivant.getNom() + " à piocher une carte de la pioche.");
        joueurSuivant.piocherCarte(paquet);
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
            forcerPiocheJoueurSuivant(paquet);
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
            int valeurMultiple = entry.getKey();

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
                System.out.println("La paire contient un 9. Le prochain joueur sera obligé de pioché dans la pioche !!");
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
                System.out.println(getNom() + " a jeté la carte de la paire : " + carteDouble);
                return carteDouble;
            }
        }

        // Vérifiez s'il y a une suite dans la main
        if (CombinaisonsDeCartes.estSuite(main)) {
            // Choisissez la carte avec la plus grande valeur dans la suite
            Carte carteSuite = main.get(0);
            System.out.println(getNom() + " a jeté la carte de la suite : " + carteSuite);
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
                System.out.println(getNom() + " a jeté la carte du brelan : " + carteBrelan);
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
                System.out.println(getNom() + " a jeté la carte du carré : " + carteCarre);
                return carteCarre;
            }
        }

        // S'il n'y a pas de combinaisons spéciales, choisissez la carte avec le plus
        // grand nombre de points
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
