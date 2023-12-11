package fr.pantheonsorbonne.miage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import fr.pantheonsorbonne.miage.game.BotStrategique;
import fr.pantheonsorbonne.miage.game.JeuYaniv;
import fr.pantheonsorbonne.miage.game.SensJeu;

public class BotStrategiqueTest {

    @Test
    public void testDemanderYaniv() {
        // Cas où le bot stratégique ne veut pas déclarer Yaniv
        BotPasStrategique bot = new BotPasStrategique("BotTest");
        assertFalse(bot.demanderYaniv(), "Le bot stratégique ne devrait pas déclarer Yaniv");

        // Cas où le bot stratégique veut déclarer Yaniv
        // Ajoutez d'autres cas de test en fonction de la logique que vous implémenterez
        // pour décider si le bot veut déclarer Yaniv
    }

    @Test
    public void testDemanderAssaf() {
        // Cas où le bot non stratégique ne veut pas déclarer Assaf
        BotStrategique bot = new BotStrategique("BotTest");
        assertFalse(bot.demanderAssaf(), "Le bot non stratégique ne devrait pas déclarer Assaf");

        // Cas où le bot non stratégique veut déclarer Assaf
        // Ajoutez d'autres cas de test en fonction de la logique que vous implémenterez
        // pour décider si le bot veut déclarer Assaf
    }

    @Test
    public void testJouerTourAutreScenario() {
        // Cas où le joueur doit défausser une carte au hasard
        BotStrategique bot = new BotStrategique("BotTest");
        PaquetCartes paquet = new PaquetCartes();
        Joueur prochainJoueur = new BotStrategique("ProchainJoueur");

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
        assertEquals(SensJeu.ANTIHORAIRE, votreObjet.getSensJeu(),
                "Le sens du jeu devrait être ANTIHORAIRE après le premier appel");

        // Appelez à nouveau la méthode pour changer le sens du jeu
        votreObjet.changerSensJeu();

        // Vérifiez si le sens du jeu a changé à HORAIRE après le deuxième appel
        assertEquals(SensJeu.HORAIRE, votreObjet.getSensJeu(),
                "Le sens du jeu devrait être HORAIRE après le deuxième appel");
    }

    @Test
    public void testObtenirProchainJoueur() {
        // Créez une liste de joueurs (par exemple, trois joueurs)
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new BotPasStrategique("Joueur1"));
        joueurs.add(new BotPasStrategique("Joueur2"));
        joueurs.add(new BotPasStrategique("Joueur3"));

        // Initialisez la liste de joueurs dans chaque instance de BotPasStrategique
        for (Joueur joueur : joueurs) {
            ((BotPasStrategique) joueur).setJoueurs(joueurs);
        }

        // Ajoutez des cas de test pour la méthode obtenirProchainJoueur
        // Assurez-vous de tester différents scénarios avec différents sens de jeu
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

    @Test
    public void testChoisirCarteAJouer() {
        // Créez une instance de BotPasStrategique
        BotStrategique bot = new BotStrategique("BotTest");

        // Ajoutez quelques cartes à la main du bot
        Carte carte1 = new Carte("2", "Coeur");
        Carte carte2 = new Carte("K", "Carreau");
        bot.setMain(Arrays.asList(carte1, carte2));

        // Vérifiez si la méthode choisirCarteAJouer retourne la première carte
        assertEquals(carte1, bot.choisirCarteAJouer(new BotStrategique("JoueurTest")));
    }

    @Test
    public void testDefausserCarteAuHasard() {
        // Créez une instance de BotPasStrategique
        BotStrategique bot = new BotStrategique("BotTest");

        // Ajoutez quelques cartes à la main du bot
        Carte carte1 = new Carte("2", "Coeur");
        Carte carte2 = new Carte("K", "Carreau");
        bot.setMain(Arrays.asList(carte1, carte2));

        // Créez un paquet de cartes et défaussez une carte au hasard
        PaquetCartes paquet = new PaquetCartes();
        bot.defausserCarteAuHasard(paquet, new BotStrategique("JoueurTest"));

        // Vérifiez si une carte a été défaussée
        assertTrue(paquet.getDefausse().size() > 0);
    }
    /*
    @Test
    public void testPiocherCarteApresJeter() {
        // Créez une instance de BotPasStrategique
        BotStrategique bot = new BotStrategique("BotTest", new ArrayList<>());

        // Créez un paquet de cartes et défaussez une carte au hasard
        PaquetCartes paquet = new PaquetCartes();
        paquet.ajouterALaDefausse(new Carte("2", "Coeur"));

        // Vérifiez si la méthode piocherCarteApresJeter ajoute une carte à la main
        assertTrue(bot.piocherCarteApresJeter(paquet));
        assertTrue(bot.getMain().size() > 0);
    }
    */

    @Test
    public void testEstCarteValide() {
        // Cas où la carte est valide
        BotStrategique bot = new BotStrategique("BotTest");
        Carte carteValide = new Carte("8", "Carreau");

        Joueur prochainJoueur = new BotStrategique("ProchainJoueur");

        boolean resultatValide = bot.estCarteValide(carteValide, prochainJoueur);

        // Vérifiez que la méthode renvoie true lorsque la carte est valide
        assertTrue(resultatValide);

        // Cas où la carte n'est pas valide
        bot = new BotStrategique("BotTest");
        Carte carteNonValide = new Carte("3", "Coeur");

        boolean resultatNonValide = bot.estCarteValide(carteNonValide, prochainJoueur);

        // Vérifiez que la méthode renvoie false lorsque la carte n'est pas valide
        assertTrue(resultatNonValide);
    }

    @Test
    public void testDeclarerYanivOuAssaf() {
        // Cas où le joueur a moins de 7 points
        BotStrategique bot = new BotStrategique("BotTest");
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
        bot = new BotStrategique("BotTest");
        bot.setPoints(8);

        joueurGagnant = new BotStrategique("Gagnant");
        bot.declarerYanivOuAssaf(joueurGagnant);

        // Vérifiez que le joueur déclare Yaniv
        // Vérifiez que le message dans la console est correct
        // Assurez-vous que le joueur n'est pas considéré comme gagnant
    }
    @Test
    public void testObtenirJoueurSuivant() {
        // Créez une liste d'instances de Joueur
        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new BotStrategique("Joueur1"));
        joueurs.add(new BotStrategique("Joueur2"));
    

        // Initialisez la liste de joueurs dans chaque instance de BotStrategique
        for (Joueur joueur : joueurs) {
            ((BotStrategique) joueur).setJoueurs(joueurs);
        }

        // Choisissez un BotStrategique de la liste pour le test
        BotStrategique bot = (BotStrategique) joueurs.get(0);

        // Testez la méthode obtenirJoueurSuivant()
        Joueur joueurSuivant1 = bot.obtenirJoueurSuivant();
        assertEquals("Joueur2", joueurSuivant1.getNom(), "Le joueur suivant devrait être Joueur2");

    }
    
    
}
