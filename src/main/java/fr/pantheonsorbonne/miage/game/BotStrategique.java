package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
// BotStrategique étend la classe abstraite Joueur 
public class BotStrategique extends Joueur {

    private int points;
    private static List<Joueur> joueurs;
    private int sensJeu = SensJeu.HORAIRE;

    public BotStrategique(String nom) {
        super(nom, true);
    }

    Joueur botProchainJoueur;
    BotStrategique prochainJoueur;

    @Override
    public boolean demanderAssaf() {
        return false;
        //Logique pour décider si le bot stratégique veut déclarer Assaf, il ne déclare pas Assaf
    }

    @Override
    public boolean demanderYaniv() {
        return true;
        //Logique pour décider si le bot stratégique veut déclarer Yanniv , il déclare Yanniv
    }

    @Override
    public void jouer(PaquetCartes paquet, Joueur prochainJoueur) {
        System.out.println("Tour de " + getNom());
        piocherCarte(paquet);

        // Logique pour choisir une carte à jouer (à implémenter)
        Carte carteAJouer = choisirCarteAJouer(prochainJoueur);

        // Vérifie d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Converti prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            // Vérifie si la carte choisie est valide
            if (estCarteValide(carteAJouer, botProchainJoueur)) {
                // Retire la carte de la main du joueur et l'ajoute à la pile de défausse
                main.remove(carteAJouer);
                paquet.ajouterALaDefausse(carteAJouer);

                // Affiche la carte jouée
                System.out.println(getNom() + " joue : " + carteAJouer);
            } else {
                // Si la carte choisie n'est pas valide, il défausse une carte au hasard
                System.out.println("Carte invalide. " + getNom() + " défausse une carte au hasard.");
                defausserCarteAuHasard(paquet, botProchainJoueur);
            }
        }
    }

    public void defausserCarteAuHasard(PaquetCartes paquet, BotStrategique prochainJoueur) {
        // Logique pour défausser une carte au hasard
        if (!main.isEmpty()) {
            //Permet de choisir une carte au hasard
            Random random = new Random();
            int indexCarte = random.nextInt(main.size());
            Carte carteChoisie = main.get(indexCarte);

            // Vérifie si la carte choisie est valide
            if (estCarteValide(carteChoisie, prochainJoueur)) {
                main.remove(carteChoisie);
                paquet.ajouterALaDefausse(carteChoisie);
                System.out.println(getNom() + " défausse une carte au hasard : " + carteChoisie);
            } else {
                // Si la carte choisie n'est pas valide, cela réessaie de défausser une autre carte
                defausserCarteAuHasard(paquet, prochainJoueur);
            }
        }
    }

//Methode qui permet de changer de sens le jeu
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

    public Joueur obtenirJoueurSuivant() {
        int indexJoueurActuel = joueurs.indexOf(this);
        int indexJoueurSuivant = (indexJoueurActuel + 1) % joueurs.size();
        return joueurs.get(indexJoueurSuivant);
    }

//Pour la paire 8 , on veut sauter le tour du joueur suivant , grace à la methode precedente on obtient le joueur suivant puis grace à la méthode setTourSauté()
//qui se trouve dans la classe Joueur on saute le tour du joueur suivant et il ne pourra ni jeter ni piocher de carte 
    private void sauterTourJoueurSuivant() {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Le tour de " + joueurSuivant.getNom() + " est sauté !");
        joueurSuivant.setTourSauté();
    }

// Pour la paire 9 , one veut que le joueur suivant soit forcer de piocher dans la pioche et non dans la défausse. Grace à la methode precedente on obtient le joueur suivant , puis 
// grace à la methode piocherCarte() qui se trouve dans la classe Joueur on lui impose de prendre une carte de la Pioche
    private void forcerPiocheJoueurSuivant(PaquetCartes paquet) {
        Joueur joueurSuivant = obtenirJoueurSuivant();
        System.out.println("Forcer " + joueurSuivant.getNom() + " à piocher une carte de la pioche. Carte punition : ");
        joueurSuivant.piocherCarte(paquet);
    }

    public boolean estCarteValide(Carte carteAJouer, Joueur prochainJoueur) {
        // Vérifie d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            // Converti prochainJoueur en BotStrategique
            BotStrategique botProchainJoueur = (BotStrategique) prochainJoueur;

            //Permet de recuperer la carte du joueur suivant
            Carte carteProchainJoueur = botProchainJoueur.choisirCarteAJouer(null); // null pour le scanner, à remplacer
                                                                                    // si nécessaire

            // Vérifie si la carte à jouer est valide par rapport à la carte du prochain joueur
            if (carteProchainJoueur != null) {
                // Vérifie si la valeur ou la couleur de la carte à jouer correspond à celle de la carte du prochain joueur
                return carteAJouer.getValeur() == carteProchainJoueur.getValeur()
                        || carteAJouer.getCouleur().equals(carteProchainJoueur.getCouleur());
            }
        }

        // Si prochainJoueur n'est pas un BotStrategique ou s'il n'a pas de carte à jouer, toute carte est valide
        return true;
    }

    @Override
    public void declarerYanivOuAssaf(Joueur joueurGagnant) {
        int pointsBotStrategique = getPoints();
        int pointsBotPasStrategique = joueurGagnant.getPoints(); 
        // Logique pour la déclaration de Yaniv ou Assaf
        if (pointsBotStrategique <= 7 && pointsBotStrategique < pointsBotPasStrategique) {
            System.out.println(getNom() + " déclare Yaniv !");
            System.out.println(getNom() + " a gagné !");
        } else {
            System.out.println(getNom() + " déclare Assaf !");
            System.out.println(joueurGagnant.getNom() + " a gagné !");
        }
    }

//Récupere les points pour l'utiliser dans la méthode precedente 
    protected int getPoints() {
        int totalPoints = 0;
        for (Carte carte : main) {
            totalPoints += carte.getValeur();
        }
        return totalPoints;
    }


    public Carte choisirCarteAJouer(Joueur prochainJoueur) {
        // Vérifie d'abord si prochainJoueur est une instance de BotStrategique
        if (prochainJoueur instanceof BotStrategique) {
            Collections.sort(main);

            // Parcoure les cartes dans la main, en commençant par la carte de plus faible valeur
            for (Carte carte : main) {
                // Vérifie si la carte est valide
                if (estCarteValide(carte, prochainJoueur)) {
                    return carte;
                }
            }
        }

        // Si prochainJoueur n'est pas un BotStrategique, retourne null
        return null;
    }

    @Override
    public Carte choisirCarteAJeter() {
        // Trie la main en ordre décroissant de points
        main.sort(Comparator.comparingInt(Carte::getValeur).reversed());

        // Vérifie s'il y a des cartes multiples (paires, brelans ,carrés)
        Map<Integer, List<Carte>> cartesParValeur = main.stream()
                .collect(Collectors.groupingBy(Carte::getValeur));

        // Recherche la première carte multiple (paires, brelans, carrés)
        Optional<Map.Entry<Integer, List<Carte>>> carteMultiples = cartesParValeur.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .findFirst();

        // Si une carte multiple est trouvée, elle est choisi
        if (carteMultiples.isPresent()) {
            Map.Entry<Integer, List<Carte>> entry = carteMultiples.get();
            List<Carte> cartesMultiples = entry.getValue();
            // Détermine le type de multiple (paire, brelan, carré, etc.)
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
            // Vérifie paire de 7
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 7) {
                // Change le sens du jeu
                System.out.println("La paire contient un 7. Le sens du jeu change !");
                sauterTourJoueurSuivant();
            }
            // Vérifie si paire de 8
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 8) {
                // Saute le tour du prochain joueur
                System.out.println("La paire contient un 8. Le prochain joueur sautera son tour !");
                sauterTourJoueurSuivant();
            }
            // Vérifie si paire de 9
            PaquetCartes paquetDeCartes = new PaquetCartes();
            if (typeMultiple.equals("paire") && cartesMultiples.get(0).getValeur() == 9) {
                // Force le joueur suivant a pioche une carte de la pioche
                System.out
                        .println("La paire contient un 9. Le prochain joueur sera obligé de pioché dans la pioche !!");
                forcerPiocheJoueurSuivant(paquetDeCartes);
            }

            // Choisi la carte à jeter parmi les cartes multiples
            Carte carteAJeter = cartesMultiples.get(0);
            System.out.println(getNom() + " a jeté la carte multiple (" + typeMultiple + ") : " + carteAJeter);
            return carteAJeter;
        }
        // Vérifie s'il y a une paire dans la main
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

        // Vérifie s'il y a une suite dans la main
        if (CombinaisonsDeCartes.estSuite(main)) {
            Carte carteSuite = main.get(0);
            return carteSuite;
        }

        // Vérifie s'il y a un brelan dans la main
        if (CombinaisonsDeCartes.estBrelan(main)) {
            Carte carteBrelan = main.stream()
                    .filter(carte -> CombinaisonsDeCartes.nombreOccurences(main, carte.getValeur()) == 3)
                    .findFirst()
                    .orElse(null);

            if (carteBrelan != null) {
                return carteBrelan;
            }
        }

        // Vérifie s'il y a un carré dans la main
        if (CombinaisonsDeCartes.estCarre(main)) {
            Carte carteCarre = main.stream()
                    .filter(carte -> CombinaisonsDeCartes.nombreOccurences(main, carte.getValeur()) == 4)
                    .findFirst()
                    .orElse(null);

            if (carteCarre != null) {
                return carteCarre;
            }
        }

        // S'il n'y a pas de combinaisons spéciales, le bot strategique choisi la carte avec le plus grand nombre de points
        Carte carteAJeter = main.get(0);
        System.out.println("Carte jetée : " + carteAJeter);
        return carteAJeter;
    }

    @Override
    public boolean piocherCarteApresJeter(PaquetCartes paquet) {
        // Le BotStrategique choisit aléatoirement entre piocher dans la pioche ou  dans la défausse
        Random random = new Random();
        boolean prendreDansDefausse = random.nextBoolean();

        // Affiche un message indiquant d'où le joueur a pioché
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