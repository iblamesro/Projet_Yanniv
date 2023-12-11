package fr.pantheonsorbonne.miage;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Carte;
import fr.pantheonsorbonne.miage.game.PaquetCartes;

class PaquetCartesTest {

   @Test
    void testPiocherCarte() {
        PaquetCartes paquet = new PaquetCartes();
        int nombreCartesAvant = paquet.getNombreCartes();

        Carte cartePiochee = paquet.piocherCarte();

        int nombreCartesApres = paquet.getNombreCartes();

        assertNotNull(cartePiochee);
        assertEquals(nombreCartesAvant - 1, nombreCartesApres);
    }


    @Test
    void testAjouterALaDefausse() {
        PaquetCartes paquet = new PaquetCartes();
        Carte carte = new Carte("A", "Coeur");

        paquet.ajouterALaDefausse(carte);

        assertFalse(paquet.estVide());
    }

    @Test
    void testMelanger() {
        PaquetCartes paquet = new PaquetCartes();
        List<Carte> cartesAvantMelange = paquet.creerPioche();
        paquet.melanger();
        List<Carte> cartesApresMelange = paquet.creerPioche();

        assertNotEquals(cartesAvantMelange, cartesApresMelange);
    }

    @Test
    void testRetirerCarte() {
        PaquetCartes paquet = new PaquetCartes();
        int nombreCartesAvant = paquet.getNombreCartes();

        Carte carteRetiree = paquet.retirerCarte();

        int nombreCartesApres = paquet.getNombreCartes();

        assertNotNull(carteRetiree);
        assertEquals(nombreCartesAvant - 1, nombreCartesApres);
    }

        @Test
        void testPiocherCarteApresJeter() {
            PaquetCartes paquet = new PaquetCartes();
            paquet.ajouterALaDefausse(paquet.piocherCarte()); // Assurez-vous que la défausse n'est pas vide
        
            assertTrue(paquet.piocherCarteApresJeter()); // On s'attend à ce que la méthode renvoie true
        }     
         @Test
        void testAjouterDefausse() {
            PaquetCartes paquet = new PaquetCartes();
            Carte carte = new Carte("A", "Coeur");

            // Assurez-vous que la défausse est initialement vide
            assertTrue(paquet.getDefausse().isEmpty());

            // Ajoutez la carte à la défausse
            paquet.ajouterDefausse(carte);

            // Assurez-vous que la défausse n'est plus vide après l'ajout
            assertFalse(paquet.getDefausse().isEmpty());

            // Assurez-vous que la carte ajoutée est effectivement présente dans la défausse
            assertTrue(paquet.getDefausse().contains(carte));
        }
        @Test
        void testAjouterCartes() {
            PaquetCartes paquet = new PaquetCartes();
            List<Carte> nouvellesCartes = new ArrayList<>();
            nouvellesCartes.add(new Carte("10", "Coeur"));
            nouvellesCartes.add(new Carte("J", "Carreau"));

            int nombreCartesAvant = paquet.getNombreCartes();

            // Ajoutez les nouvelles cartes au paquet
            paquet.ajouterCartes(nouvellesCartes);

            int nombreCartesApres = paquet.getNombreCartes();

            // Vérifiez que le nombre de cartes a augmenté du bon montant
            assertEquals(nombreCartesAvant + nouvellesCartes.size(), nombreCartesApres);
    }
    @Test
    void testPiocherDefausseVide() {
        PaquetCartes paquet = new PaquetCartes();

        // Assurez-vous que la défausse est vide
        assertTrue(paquet.getDefausse().isEmpty());

        // Piochez depuis la défausse vide, cela devrait revenir à piocher depuis la pioche
        Carte cartePiochee = paquet.piocherDefausse();

        // Assurez-vous que la carte piochée n'est pas null et que la défausse reste vide
        assertNotNull(cartePiochee);
        assertTrue(paquet.getDefausse().isEmpty());
    }
    @Test
void testPiocherCarteApresMelange() {
    PaquetCartes paquet = new PaquetCartes();
    paquet.melanger();
    
    int nombreCartesAvant = paquet.getNombreCartes();
    Carte cartePiochee = paquet.piocherCarte();
    int nombreCartesApres = paquet.getNombreCartes();
    
    assertNotNull(cartePiochee);
    assertEquals(nombreCartesAvant - 1, nombreCartesApres);
}
@Test
void testCreerPiocheApresAjout() {
    PaquetCartes paquet = new PaquetCartes();
    List<Carte> piocheAvantAjout = paquet.creerPioche();
    
    List<Carte> nouvellesCartes = Arrays.asList(new Carte("8", "Pique"), new Carte("9", "Coeur"));
    paquet.ajouterCartes(nouvellesCartes);
    
    List<Carte> piocheApresAjout = paquet.creerPioche();
    
    assertNotEquals(piocheAvantAjout, piocheApresAjout);
}
@Test
void testSetSensJeu() {
    // Assurez-vous que le sens du jeu est initialisé à 0 (par exemple)
    assertEquals(0, PaquetCartes.getSensJeu());

    // Définissez un nouveau sens du jeu
    int nouveauSens = 1;
    PaquetCartes.setSensJeu(nouveauSens);

    // Assurez-vous que le sens du jeu a été correctement modifié
    assertEquals(nouveauSens, PaquetCartes.getSensJeu());
}

    @Test
    void testPiocherDefausse() {
        // Créer un paquet avec des cartes dans la défausse
        PaquetCartes paquet = new PaquetCartes();
        Carte carte = new Carte("A", "Coeur");
        paquet.ajouterDefausse(carte);

        // Appeler la méthode pour piocher depuis la défausse
        Carte cartePiochee = paquet.piocherDefausse();

        // Vérifier que la carte piochée est celle ajoutée précédemment
        assertEquals(carte, cartePiochee);
    }
    @Test
void testPiocherCarteApresJeterToutesLesCartes() {
    PaquetCartes paquet = new PaquetCartes();
    while (paquet.getNombreCartes() > 0) {
        paquet.piocherCarte();
    }

    assertFalse(paquet.piocherCarteApresJeter());
}

    @Test
void testPiocherApresAjoutEtMelange() {
    PaquetCartes paquet = new PaquetCartes();
    List<Carte> nouvellesCartes = Arrays.asList(new Carte("8", "Pique"), new Carte("9", "Coeur"));
    paquet.ajouterCartes(nouvellesCartes);
    paquet.melanger();

    int nombreCartesAvantPioche = paquet.getNombreCartes();
    paquet.piocherCarte();
    int nombreCartesApresPioche = paquet.getNombreCartes();

    assertEquals(nombreCartesAvantPioche - 1, nombreCartesApresPioche);
}
@Test
    void testGenererCarteAleatoire() {
        PaquetCartes paquet = new PaquetCartes();
        Carte carte = paquet.genererCarteAleatoire();

        // Vérifiez si la carte générée est valide
        assertNotNull(carte);
        assertNotNull(carte.getValeur());
        assertNotNull(carte.getCouleur());
        assertTrue(!carte.getCouleur().isEmpty());

        // Ajoutez des assertions supplémentaires selon les contraintes de votre application
        // Par exemple, vérifiez si la valeur est dans la plage attendue, si la couleur est valide, etc.
    }


    
}