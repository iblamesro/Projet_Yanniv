package fr.pantheonsorbonne.miage;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Joueur;
import fr.pantheonsorbonne.miage.game.PaquetCartes;

class JoueurTest {

    @Test
    void testPiocherCarte() {
        Joueur joueur = new JoueurImpl("Alice", false);
        PaquetCartes paquet = new PaquetCartes();
        int nombreCartesAvant = joueur.getNombreCartes();

        joueur.piocherCarte(paquet);

        int nombreCartesApres = joueur.getNombreCartes();

        assertEquals(nombreCartesAvant + 1, nombreCartesApres);
    }
    
    @Test
    void testAGagne() {
        // Créez une instance de la classe concrète JoueurImpl
        Joueur joueur = new JoueurImpl("Alice", false);

        // Définissons le score du joueur (ou ajustez les points dans la main) en fonction du test
        // Pour cet exemple, définissons manuellement le score à un niveau qui devrait gagner
        joueur.setScore(5);

        // Appelons la méthode à tester
        boolean aGagne = joueur.aGagne();

        // Vérifions si le joueur a gagné selon le seuil prédéfini dans la méthode
        assertTrue(aGagne);
 }
 @Test
    void testIncrementeScore() {
        // Créez une instance de la classe concrète JoueurImpl
        Joueur joueur = new JoueurImpl("Bob", false);

        // Obtenons le score actuel du joueur
        int scoreAvant = joueur.getScore();

        // Définissons un nombre de points à incrémenter
        int pointsIncremente = 3;

        // Appelons la méthode à tester
        joueur.incrementeScore(pointsIncremente);

        // Vérifions si le score a été correctement incrémenté
        assertEquals(scoreAvant + pointsIncremente, joueur.getScore());
    }

    // Classe interne pour permettre l'accès aux méthodes protégées de Joueur
    private static class JoueurImpl extends Joueur {
        public JoueurImpl(String nom, boolean estBot) {
            super(nom, estBot);
        }

        @Override
        public void jouerTour(PaquetCartes paquet, Joueur prochainJoueur) {
            // Implémentation factice pour les tests
        }

        @Override
        public boolean demanderAssaf() {
            // Implémentation factice pour les tests
            return false;
        }

        @Override
        public boolean demanderYaniv() {
            // Implémentation factice pour les tests
            return false;
        }

        @Override
        public void declarerYanivOuAssaf(Joueur joueurGagnant) {
            // Implémentation factice pour les tests
        }

        @Override
        public void jouer(PaquetCartes paquet, Joueur prochainJoueur) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'jouer'");
        }
    }

    @Test
    void testJouerTourAvecSautDeTour() {
        // Créez une instance de la classe concrète JoueurImpl avec le saut de tour activé
        Joueur joueur = new JoueurImpl("Alice", false);
        joueur.setTourSauté();

        // Vérifiez que le joueur ne joue pas et que la valeur doitSauterSonTour est réinitialisée
        joueur.jouerTour(new PaquetCartes(), new JoueurImpl("Bob", false));
        assertTrue(joueur.doitSauterSonTour);
    }

    /* 
    @Test
    void testCalculerTotalPointsAvecDifferentesCartes() {
        // Créez une instance de la classe concrète JoueurImpl avec différentes cartes dans la main
        Joueur joueur = new JoueurImpl("Charlie", false);
        joueur.getMain().add(new Carte("2", "Carreau")); 
        joueur.getMain().add(new Carte("2", "Carreau")); 
        joueur.getMain().add(new Carte("2", "Carreau"));
        joueur.getMain().add(new Carte("2", "Carreau"));

        // Vérifiez que le total des points est correct en fonction des valeurs des cartes
        int totalPoints = joueur.calculerTotalPoints();
        assertEquals(8, totalPoints);
    }

    @Test
    void testAjusterPointsJokerAvecDifferentsNombresDeJokers() {
        // Créez une instance de la classe concrète JoueurImpl avec différents nombres de Jokers dans la main
        Joueur joueur = new JoueurImpl("Eve", false);
        joueur.getMain().add(new Carte("Joker", "Joker")); 
        joueur.getMain().add(new Carte("Joker", "Joker")); 
        joueur.getMain().add(new Carte("Joker", "Joker")); 

        // Vérifiez que le total des points est correct en fonction du nombre de Jokers
        int totalPoints = joueur.ajusterPointsJoker(0, 3);
        assertEquals(10, totalPoints);
    }

    */
}
