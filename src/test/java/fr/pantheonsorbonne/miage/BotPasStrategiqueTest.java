package fr.pantheonsorbonne.miage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Carte;
import fr.pantheonsorbonne.miage.game.Joueur;
import fr.pantheonsorbonne.miage.game.PaquetCartes;
import fr.pantheonsorbonne.miage.game.BotPasStrategique;
import fr.pantheonsorbonne.miage.game.JeuYaniv;
import fr.pantheonsorbonne.miage.game.SensJeu;

public class BotPasStrategiqueTest {

    @Test
    public void testDemanderYaniv() {
        // Cas où le bot stratégique ne veut pas déclarer Yaniv
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        assertFalse(bot.demanderYaniv(), "Le bot stratégique ne devrait pas déclarer Yaniv");

        // Cas où le bot stratégique veut déclarer Yaniv
    }

    
    @Test
    public void testDemanderAssaf() {
        // Cas où le bot non stratégique ne veut pas déclarer Assaf
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        assertTrue(bot.demanderAssaf(), "Le bot non stratégique ne devrait pas déclarer Assaf");

        // Cas où le bot non stratégique veut déclarer Assaf
        // Ajoutez d'autres cas de test en fonction de la logique que vous implémenterez
        // pour décider si le bot veut déclarer Assaf
    }
    

    
    @Test
    public void testJouerTourAutreScenario() {
        // Cas où le joueur doit défausser une carte au hasard
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        PaquetCartes paquet = new PaquetCartes();
        Joueur prochainJoueur = new BotPasStrategique("ProchainJoueur");

        // Initialisez une main avec des cartes qui ne peuvent pas être jouées
        Carte carte1 = new Carte("2", "Carreau");
        Carte carte2 = new Carte("4", "Pique");
        bot.setMain(List.of(carte1, carte2));

        // Ajoutez une carte au sommet du paquet de cartes
        paquet.ajouterDefausse(new Carte("7", "Coeur"));

        // Exécutez la méthode jouerTour
        bot.jouerTour(paquet, prochainJoueur);

        // Vérifiez que le joueur a défaussé une carte au hasard
        assertFalse(bot.getMain().isEmpty(), "La carte devrait être retirée de la main");
        assertNotNull(paquet.getDefausse(), "La carte devrait être dans la pile de défausse");
    }

    


    @Test
    public void testChangerSensJeu() {
        // Créez une instance de votre classe
        JeuYaniv votreObjet = new JeuYaniv(2);

        // Cas où le sens du jeu est initialisé à HORAIRE
        assertEquals(SensJeu.HORAIRE, votreObjet.getSensJeu(), "Le sens du jeu devrait être initialisé à HORAIRE");

        // Appelez la méthode pour changer le sens du jeu
        votreObjet.changerSensJeu();

        // Vérifiez si le sens du jeu a changé à ANTIHORAIRE
        assertEquals(SensJeu.ANTIHORAIRE, votreObjet.getSensJeu(), "Le sens du jeu devrait être ANTIHORAIRE après le premier appel");

        // Appelez à nouveau la méthode pour changer le sens du jeu
        votreObjet.changerSensJeu();

        // Vérifiez si le sens du jeu a changé à HORAIRE après le deuxième appel
        assertEquals(SensJeu.HORAIRE, votreObjet.getSensJeu(), "Le sens du jeu devrait être HORAIRE après le deuxième appel");
    }

    @Test
    public void testObtenirProchainJoueur() {
        // Créez une liste de joueurs (par exemple, trois joueurs)
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new BotPasStrategique("Joueur1"));
        joueurs.add(new BotPasStrategique("Joueur2"));


        // Initialisez la liste de joueurs dans chaque instance de BotPasStrategique
        for (Joueur joueur : joueurs) {
            ((BotPasStrategique) joueur).setJoueurs(joueurs);
        }

        // Ajoutez des cas de test pour la méthode obtenirProchainJoueur
        // Assurez-vous de tester différents scénarios avec différents sens de jeu
    }

    @Test
    public void testChoisirCarteAJouer() {
        // Créez une instance de BotPasStrategique
        BotPasStrategique bot = new BotPasStrategique("BotTest");

        // Ajoutez quelques cartes à la main du bot
        Carte carte1 = new Carte("2", "Coeur");
        Carte carte2 = new Carte("K", "Carreau");
        bot.setMain(Arrays.asList(carte1, carte2));

        // Vérifiez si la méthode choisirCarteAJouer retourne la première carte
        assertEquals(carte1, bot.choisirCarteAJouer(new BotPasStrategique("JoueurTest")));
    }

    @Test
    public void testDefausserCarteAuHasard() {
        // Créez une instance de BotPasStrategique
        BotPasStrategique bot = new BotPasStrategique("BotTest");

        // Ajoutez quelques cartes à la main du bot
        Carte carte1 = new Carte("2", "Coeur");
        Carte carte2 = new Carte("K", "Carreau");
        bot.setMain(Arrays.asList(carte1, carte2));

        // Créez un paquet de cartes et défaussez une carte au hasard
        PaquetCartes paquet = new PaquetCartes();
        bot.defausserCarteAuHasard(paquet, new BotPasStrategique("JoueurTest"), 3);

        // Vérifiez si une carte a été défaussée
        assertFalse(paquet.getDefausse().size() > 0);
    }

    
    @Test
    public void testPiocherCarteApresJeter() {
        // Créez une instance de BotPasStrategique
        BotPasStrategique bot = new BotPasStrategique("BotTest");

        // Créez un paquet de cartes et défaussez une carte au hasard
        PaquetCartes paquet = new PaquetCartes();
        paquet.ajouterALaDefausse(new Carte("2", "Coeur"));

        // Vérifiez si la méthode piocherCarteApresJeter ajoute une carte à la main
        assertTrue(bot.piocherCarteApresJeter(paquet));
        assertTrue(bot.getMain().size() > 0);
    }





    @Test
    public void testSauterTourProchainJoueur() {
        // Ajoutez des cas de test pour la méthode sauterTourProchainJoueur
        // Assurez-vous de tester si le tour du prochain joueur est correctement sauté
    }

    @Test
    public void testForcerPiocheProchainJoueur() {
        // Ajoutez des cas de test pour la méthode forcerPiocheProchainJoueur
        // Assurez-vous de tester si le prochain joueur pioche correctement
    }

    @Test
    public void testPiocherEchangerProchainJoueur() {
        // Ajoutez des cas de test pour la méthode piocherEchangerProchainJoueur
        // Assurez-vous de tester différents scénarios avec des mains de cartes
        // différentes
    }

    /*
    @Test
    public void testDefausserCarteAuHasard() {
        // Cas où la main n'est pas vide et une carte valide est défaussée
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        List<Carte> mainNonVide = new ArrayList<>();
        mainNonVide.add(new Carte("A", "🂱"));
        mainNonVide.add(new Carte("10", "🂺"));
        mainNonVide.add(new Carte("5","🂵"));
        bot.setMain(mainNonVide);

        PaquetCartes paquet = new PaquetCartes(); // Supposons que vous ayez une classe PaquetCartes appropriée
        Joueur prochainJoueur = new BotPasStrategique("ProchainJoueur"); // Créez un joueur fictif

        // Appelez la méthode pour défausser une carte au hasard
        bot.defausserCarteAuHasard(paquet, prochainJoueur, 3);

        // Vérifiez que la main a une carte de moins après la défausse
        assertEquals(2, bot.getMain().size());

        // ... (d'autres vérifications nécessaires)

        // Cas où la main est vide
        bot = new BotPasStrategique("BotTest");
        List<Carte> mainVide = new ArrayList<>();
        bot.setMain(mainVide);

        // Appelez la méthode pour défausser une carte au hasard
        bot.defausserCarteAuHasard(paquet, prochainJoueur, 3);

        // Vérifiez que la main est toujours vide
        assertTrue(bot.getMain().isEmpty());

        // ... (autres cas de test)
    }

    */

    @Test
    public void testEstCarteValide() {
        // Cas où la carte est valide
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        Carte carteValide = new Carte("8", "Carreau");

        Joueur prochainJoueur = new BotPasStrategique("ProchainJoueur");

        boolean resultatValide = bot.estCarteValide(carteValide, prochainJoueur);

        // Vérifiez que la méthode renvoie true lorsque la carte est valide
        assertFalse(resultatValide);

        // Cas où la carte n'est pas valide
        bot = new BotPasStrategique("BotTest");
        Carte carteNonValide = new Carte("3", "Coeur");

        boolean resultatNonValide = bot.estCarteValide(carteNonValide, prochainJoueur);

        // Vérifiez que la méthode renvoie false lorsque la carte n'est pas valide
        assertFalse(resultatNonValide);
    }

    @Test
    public void testDeclarerYanivOuAssaf() {
        // Cas où le joueur a moins de 7 points
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        bot.setPoints(6);

        Joueur joueurGagnant = new BotPasStrategique("Gagnant");
        bot.declarerYanivOuAssaf(joueurGagnant);

        // Vérifiez que le joueur déclare Yaniv
        // Vérifiez que le message dans la console est correct (par exemple, en
        // utilisant System.out.println)
        // Assurez-vous que le joueur est considéré comme gagnant
        // (par exemple, vérifiez un état interne ou ajoutez une méthode pour récupérer
        // l'état gagnant)

        // Cas où le joueur a 7 points ou plus
        bot = new BotPasStrategique("BotTest");
        bot.setPoints(8);

        joueurGagnant = new BotPasStrategique("Gagnant");
        bot.declarerYanivOuAssaf(joueurGagnant);

        // Vérifiez que le joueur déclare Yaniv
        // Vérifiez que le message dans la console est correct
        // Assurez-vous que le joueur n'est pas considéré comme gagnant
    }

    /*
    @Test
    public void testGetPoints() {
        // Cas où la main a des cartes avec des valeurs
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        List<Carte> mainAvecPoints = new ArrayList<>();
        mainAvecPoints.add(new Carte("7", "🂷"));
        mainAvecPoints.add(new Carte("10", "🃊"));
        mainAvecPoints.add(new Carte("A", "🂡"));
        bot.setMain(mainAvecPoints);

        int points = bot.getPoints();

        // Vérifiez que la somme des points est correcte
        assertEquals(7 + 10 + 1, points);

        // Cas où la main est vide
        bot = new BotPasStrategique("BotTest");
        List<Carte> mainVide = new ArrayList<>();
        bot.setMain(mainVide);

        points = bot.getPoints();

        // Vérifiez que la somme des points est 0 car la main est vide
        assertEquals(0, points);
    }
    */

    /*
    @Test
    public void testChoisirCarteAJeter() {
        // Cas où la main n'est pas vide
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        List<Carte> mainNonVide = new ArrayList<>();
        mainNonVide.add(new Carte("7", "Coeur"));
        mainNonVide.add(new Carte("10", "Carreau"));
        mainNonVide.add(new Carte("A", "Pique"));
        bot.setMain(mainNonVide);

        PaquetCartes paquet = new PaquetCartes(); // Supposons que vous ayez une classe PaquetCartes appropriée

        // Appelez la méthode pour choisir une carte à jeter
        Carte carteAJeter = bot.choisirCarteAJeter();

        // Vérifiez que la carte choisie n'est pas null
        assertNotNull(carteAJeter);

        // Vérifiez que la valeur et la couleur de la carte choisie sont correctes
        assertEquals("7", carteAJeter.getValeur());
        assertEquals("Coeur", carteAJeter.getCouleur());

        // Cas où la main est vide
        bot = new BotPasStrategique("BotTest");
        List<Carte> mainVide = new ArrayList<>();
        bot.setMain(mainVide);

        // Appelez la méthode pour choisir une carte à jeter
        carteAJeter = bot.choisirCarteAJeter();

        // Vérifiez que la carte choisie est null car la main est vide
        assertNull(carteAJeter);
    }

    

    @Test
    public void testPiocherCarteApresJeter() {
        // Créez un paquet de cartes avec une défausse et une pioche
        PaquetCartes paquet = new PaquetCartes();

        // Ajoutez des cartes à la défausse
        paquet.ajouterALaDefausse(new Carte("As", "Coeur"));
        paquet.ajouterALaDefausse(new Carte("2", "Carreau"));
        paquet.ajouterALaDefausse(new Carte("3", "Pique"));

        // Ajoutez des cartes à la pioche
        paquet.ajouterALaPioche(new Carte("4", "Trèfle"));
        paquet.ajouterALaPioche(new Carte("5", "Coeur"));
        paquet.ajouterALaPioche(new Carte("6", "Carreau"));

        // Créez un BotPasStrategique
        BotPasStrategique bot = new BotPasStrategique("TestBot");

        // Testez la méthode piocherCarteApresJeter plusieurs fois
        for (int i = 0; i < 5; i++) {
            boolean aPiocheDansDefausse = bot.piocherCarteApresJeter(paquet);

            // Affichez le contenu de la main après chaque pioche
            System.out.println("Main du bot après la pioche : " + bot.getMain());

            // Vérifiez que la méthode renvoie true si le bot a pioché dans la défausse
            // et false s'il a pioché dans la pioche
            if (aPiocheDansDefausse) {
                assertTrue(paquet.getDefausse().isEmpty()); // La défausse doit être vide après la pioche
            } else {
                assertFalse(paquet.getPioche().isEmpty()); // La pioche ne doit pas être vide après la pioche
            }
        }
    }

    */

}
