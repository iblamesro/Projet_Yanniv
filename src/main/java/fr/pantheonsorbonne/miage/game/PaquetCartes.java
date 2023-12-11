package fr.pantheonsorbonne.miage.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PaquetCartes {

    protected List<Carte> cartes;
    protected List<Carte> defausse;
    private static int sensJeu;

    public PaquetCartes() {
        cartes = new ArrayList<>();
        String[] valeurs = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
        String[] couleurs = { "Coeur", "Carreau", "Trèfle", "Pique", };
        defausse = new ArrayList<>();

        for (String valeur : valeurs) {
            for (String couleur : couleurs) {
                cartes.add(new Carte(valeur, couleur));
            }
        }
        // Ajoute les Jokers au paquet
        cartes.add(new Carte("Joker", "Joker"));
        cartes.add(new Carte("Joker", "Joker"));
    }

    public Carte piocherCarte() {
        if (!cartes.isEmpty()) {
            return cartes.remove(0);
        } else {
            // Permet de gérer le cas où le paquet est vide, par exemple en mélangeant la défausse
            melangerDefausse();
            // Reprend la première carte de la défausse ou genere une nouvelle carte
            if (!cartes.isEmpty()) {
                return cartes.remove(0);
            } else {
                // Gére le cas où le paquet reste vide après mélange
                System.out.println("Le paquet est toujours vide après mélange.");
                return genererCarteAleatoire(); // Génère une nouvelle carte
            }
        }
    }

    private void melangerDefausse() {
        // Logique pour mélanger la défausse et la remettre dans le paquet
        // On a mélanger la défausse et on la copier dans le paquet
        Collections.shuffle(defausse);
        cartes.addAll(defausse);
        defausse.clear(); // Efface la défausse car les cartes ont été ajoutées au paquet
    }

    public Carte genererCarteAleatoire() {
        Random random = new Random();
        int valeur = random.nextInt(13) + 1; // Valeur entre 1 et 13 inclus
        String couleur = "Coeur";
        return new Carte(Integer.toString(valeur), couleur);
    }

    public void ajouterALaDefausse(Carte carteAJouer) {
        defausse.add(carteAJouer);
    }

    public void melanger() {
        Collections.shuffle(cartes);
        System.out.println("Le paquet a été mélangé.");
    }

    public boolean estVide() {
        return cartes.isEmpty() && defausse.isEmpty();
    }

    public Carte retirerCarte() {
        if (!cartes.isEmpty()) {
            return cartes.remove(0);
        } else if (!defausse.isEmpty()) {
            // Si le paquet est vide, mais la défausse a des cartes, remettre la défausse dans le paquet
            melangerDefausse();
            // Retire et renvoie la première carte du paquet
            return cartes.remove(0);
        } else {
            // Si à la fois le paquet et la défausse sont vides, retourne null
            return null;
        }
    }

    public static int getSensJeu() {
        return sensJeu;
    }

    public static void setSensJeu(int sens) {
        sensJeu = sens;
    }

    public int getNombreCartes() {
        // Retourne le nombre de cartes dans le paquet
        return cartes.size();
    }

    public void ajouterCartes(List<Carte> nouvellesCartes) {
        cartes.addAll(nouvellesCartes);
    }

    public List<Carte> creerPioche() {
        List<Carte> cartesRestantes = new ArrayList<>(cartes);
        return cartesRestantes;
    }

    public void ajouterDefausse(Carte carte) {
        if (defausse == null) {
            defausse = new ArrayList<>();
        }
        defausse.add(carte);
    }

    public Carte piocherDefausse() {
        if (!defausse.isEmpty()) {
            Carte cartePiochee = defausse.remove(defausse.size() - 1);
            return cartePiochee;
        } else {
            // Gere le cas où la défausse est vide
            System.out.println("La défausse est vide. Pioche dans la pioche à la place.");
            return piocherCarte();
        }
    }

    public boolean piocherCarteApresJeter() {
        if (!defausse.isEmpty()) {
            // Logique pour piocher depuis la défausse
            return true;
        } else {
            // Logique pour piocher depuis la pioche
            return false;
        }
    }

    public List<Carte> getDefausse() {
        return defausse;
    }

    public int getTailleDefausse() {
        return defausse.size();
    }

    public void ajouterALaPioche(Carte carte) {
    }

    public List<Carte> getPioche() {
        return null;
    }

    
}
