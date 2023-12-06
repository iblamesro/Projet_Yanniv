package fr.pantheonsorbonne.miage.game;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class BotPasStrategique extends Joueur {
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

    private static List<Joueur> joueurs; // Assurez-vous que cette liste est correctement initialisée
    private int sensJeu = SensJeu.HORAIRE;

    public BotPasStrategique(String nom) {
        super(nom, true);
    }

    @Override
    public void jouerTour(PaquetCartes paquet, Joueur prochainJoueur) {
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

            // Appliquez les règles spécifiques du jeu Yaniv ici
            appliquerReglesYaniv(carteAJouer, prochainJoueur);
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

    private void appliquerReglesYaniv(Carte carteAJouer, Joueur prochainJoueur) {
        // Gestion du double 7-7
        if (carteAJouer.getValeur() == 7 && carteAJouer.estJusteApres(carteAJouer)) {
            // Changer de sens : À implémenter
            changerSensJeu();
        }

        // Gestion du double 8-8
        if (carteAJouer.getValeur() == 8 && carteAJouer.estJusteApres(carteAJouer)) {
            // Sauter le tour du prochain joueur : À implémenter
            sauterTourProchainJoueur();
        }

        // Gestion du double 9-9
        if (carteAJouer.getValeur() == 9 && carteAJouer.estJusteApres(carteAJouer)) {
            // Forcer le prochain joueur à piocher une carte de la pioche : À implémenter
            forcerPiocheProchainJoueur(null, prochainJoueur);
        }

        // Gestion du double 10-10
        if (carteAJouer.getValeur() == 10 && carteAJouer.estJusteApres(carteAJouer)) {
            // Piocher au hasard une carte chez le prochain joueur
            // et éventuellement l'échanger avec l'une de ses cartes : À implémenter
            piocherEchangerProchainJoueur(prochainJoueur);
        }

        // Gestion de la suite 10-J-Q
        if (carteAJouer.getValeur() == 10 && carteAJouer.estJusteApres(carteAJouer)) {
            // Le prochain joueur est obligé de mettre une carte qui continue la suite
            // (un K de la même couleur) sinon pioche une carte : À implémenter
            gererSuite10JQ(prochainJoueur, null);
        }

        // Ajoutez d'autres règles spécifiques ici en fonction de vos besoins
    }

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
        int indexJoueurCourant = joueurs.indexOf(this); // Assurez-vous que "this" se réfère à l'instance actuelle de
                                                        // Joueur

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

    private void sauterTourProchainJoueur() {
        Joueur prochainJoueur = obtenirProchainJoueur();

        if (prochainJoueur != null) {
            System.out.println("Le prochain joueur, " + prochainJoueur.getNom() + ", a son tour sauté !");
            prochainJoueur.sauterTour();
        } else {
            System.out.println("Erreur : Impossible de sauter le tour du prochain joueur.");
        }
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

    private Carte choisirCarteAJouer(Joueur prochainJoueur) {
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

    private void defausserCarteAuHasard(PaquetCartes paquet, Joueur prochainJoueur, int essaisRestants) {
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

    private boolean estCarteValide(Carte carteChoisie, Joueur prochainJoueur) {
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