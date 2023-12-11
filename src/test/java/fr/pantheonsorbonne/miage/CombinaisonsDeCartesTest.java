package fr.pantheonsorbonne.miage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import fr.pantheonsorbonne.miage.game.Carte;  // Ajoutez l'importation de la classe Carte
import fr.pantheonsorbonne.miage.game.CombinaisonsDeCartes;  // Ajoutez l'importation de la classe CombinaisonsDeCartes

public class CombinaisonsDeCartesTest {

    @Test
    void testEstSuite() {
        // Créer une liste de cartes pour tester la suite
        List<Carte> cartesSuite = Arrays.asList(
            new Carte("7", "Coeur"),
            new Carte("8", "Carreau"),
            new Carte("9", "Pique")
        );

        // Vérifier si c'est une suite
        assertTrue(CombinaisonsDeCartes.estSuite(cartesSuite));
    }

    @Test
        void testEstDouble() {
            // Créer une liste de cartes pour tester le double
            List<Carte> cartesDouble = Arrays.asList(
                new Carte("7", "Coeur"),
                new Carte("7", "Carreau"),
                new Carte("9", "Pique")
            );

            // Vérifier si c'est un double
            assertTrue(CombinaisonsDeCartes.estDouble(cartesDouble));
        }

        @Test
        void testEstBrelan() {
            // Créer une liste de cartes pour tester le brelan
            List<Carte> cartesBrelan = Arrays.asList(
                new Carte("7", "Coeur"),
                new Carte("7", "Carreau"),
                new Carte("7", "Pique")
            );
    
            // Vérifier si c'est un brelan
            assertTrue(CombinaisonsDeCartes.estBrelan(cartesBrelan));
        }

        @Test
            void testEstCarre() {
                // Créer une liste de cartes pour tester le carré
                List<Carte> cartesCarre = Arrays.asList(
                    new Carte("7", "Coeur"),
                    new Carte("7", "Carreau"),
                    new Carte("7", "Pique"),
                    new Carte("7", "Trèfle")
                );
        
                // Vérifier si c'est un carré
                assertTrue(CombinaisonsDeCartes.estCarre(cartesCarre));
            }

    @Test
    void testNombreOccurences() {
        // Créer une liste de cartes pour tester le nombre d'occurrences
        List<Carte> cartes = Arrays.asList(
            new Carte("7", "Coeur"),
            new Carte("7", "Carreau"),
            new Carte("9", "Pique")
        );

        // Vérifier le nombre d'occurrences de la valeur 7
        assertEquals(2, CombinaisonsDeCartes.nombreOccurences(cartes, 7));
    }
}
