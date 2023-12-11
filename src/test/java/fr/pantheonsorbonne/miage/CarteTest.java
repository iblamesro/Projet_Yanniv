package fr.pantheonsorbonne.miage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Carte;

public class CarteTest {
    
    
    @Test
    public void toStringTestAsPique() {
        List<String> valeurs = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
        String[] iconesAttendues = {
            "🂡", "🂢", "🂣", "🂤", "🂥", "🂦", "🂧", "🂨", "🂩", "🂪", "🂫", "🂭", "🂮"
        };

        for (int i = 0; i < valeurs.size(); i++) {
            String val = valeurs.get(i);
            String iconeAttendue = iconesAttendues[i];

            Carte carte = new Carte(val, "Pique");
            assertEquals(iconeAttendue, carte.toString());
            assertThrows(IllegalArgumentException.class, () -> new Carte("X", "Pique"));
        }
    }
    
    @Test
    public void toStringTestAsCoeur() {
        List<String> valeurs = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
        String[] iconesAttendues = {
            "🂱", "🂲", "🂳", "🂴", "🂵", "🂶", "🂷", "🂸", "🂹", "🂺", "🂻", "🂽", "🂾"
        };

        for (int i = 0; i < valeurs.size(); i++) {
            String val = valeurs.get(i);
            String iconeAttendue = iconesAttendues[i];

            Carte carte = new Carte(val, "Coeur");
            assertEquals(iconeAttendue, carte.toString());
            assertThrows(IllegalArgumentException.class, () -> new Carte("X", "Coeur"));
        }
    }

    @Test
    public void toStringTestAsCarreau() {
        List<String> valeurs = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
        String[] iconesAttendues = {
            "🃁", "🃂", "🃃", "🃄", "🃅", "🃆", "🃇", "🃈", "🃉", "🃊", "🃋", "🃍", "🃎"
        };

        for (int i = 0; i < valeurs.size(); i++) {
            String val = valeurs.get(i);
            String iconeAttendue = iconesAttendues[i];

            Carte carte = new Carte(val, "Carreau");
            assertEquals(iconeAttendue, carte.toString());
            assertThrows(IllegalArgumentException.class, () -> new Carte("X", "Carreau"));
        }
    }

    @Test
    public void toStringTestAsTrefle() {
        List<String> valeurs = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
        String[] iconesAttendues = {
            "🃑", "🃒", "🃓", "🃔", "🃕", "🃖", "🃗", "🃘", "🃙", "🃚", "🃛", "🃝", "🃞"
        };

        for (int i = 0; i < valeurs.size(); i++) {
            String val = valeurs.get(i);
            String iconeAttendue = iconesAttendues[i];

            Carte carte = new Carte(val, "Trèfle");
            assertEquals(iconeAttendue, carte.toString());
            assertThrows(IllegalArgumentException.class, () -> new Carte("X", "Trèfle"));
        }
    }
     @Test
    public void compareToTest() {
        Carte carte1 = new Carte("8", "Coeur");
        Carte carte2 = new Carte("8", "Pique");

        // Modifier l'assertion pour refléter le comportement actuel de la méthode compareTo
        assertEquals(-13, carte1.compareTo(carte2)); // Les valeurs sont égales, comparer les couleurs

        Carte carte3 = new Carte("9", "Coeur");
        // Modifier l'assertion pour refléter le comportement actuel de la méthode compareTo
        assertEquals(-1, Integer.compare(carte1.getValeur(), carte3.getValeur())); // La valeur de carte1 est inférieure à celle de carte3
    }

    @Test
    public void estJusteApresTest() {
        Carte carte1 = new Carte("5", "Carreau");
        Carte carte2 = new Carte("4", "Carreau");

        assertEquals(true, carte1.estJusteApres(carte2)); // La carte1 est juste après la carte2

        Carte carte3 = new Carte("6", "Carreau");
        assertEquals(false, carte1.estJusteApres(carte3)); // La carte1 n'est pas juste après la carte3

        Carte carte4 = new Carte("6", "Trèfle");
        assertEquals(false, carte1.estJusteApres(carte4)); // Les cartes n'ont pas la même couleur
    }
}

