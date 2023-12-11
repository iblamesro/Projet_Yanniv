package fr.pantheonsorbonne.miage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Carte;

public class CarteTest {
    @Test
    public void toStringTest(){
        List<String> valeurs = new ArrayList<>();
        valeurs.addAll(Arrays.asList("A","2","3"));
        for (String val : valeurs){
            Carte abc = new Carte (val,"Pique");
            assertEquals("🂨  de Pique", abc.toString());
        }
        
        
    }
}

