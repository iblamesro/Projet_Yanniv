package fr.pantheonsorbonne.miage.game;

import java.util.Collections;
import java.util.List;

public class CombinaisonsDeCartes {

    // Méthodes pour vérifier les différentes combinaisons

    public static boolean estSuite(List<Carte> cartes) {
        // Triez les cartes par valeur
        Collections.sort(cartes);

        // Vérifiez si les valeurs des cartes forment une suite
        for (int i = 0; i < cartes.size() - 1; i++) {
            if (cartes.get(i).getValeur() + 1 != cartes.get(i + 1).getValeur()) {
                return false;
            }
        }
        return true;
    }

    public static boolean estDouble(List<Carte> cartes) {
        // Trie les cartes par valeur
        Collections.sort(cartes);

        // Vérifiez si au moins deux cartes ont la même valeur
        for (int i = 0; i < cartes.size() - 1; i++) {
            if (cartes.get(i).getValeur() == cartes.get(i + 1).getValeur()) {
                return true;
            }
        }
        return false;
    }

    public static boolean estBrelan(List<Carte> cartes) {
        Collections.sort(cartes);

        // Vérifie si au moins trois cartes ont la même valeur
        for (int i = 0; i < cartes.size() - 2; i++) {
            if (cartes.get(i).getValeur() == cartes.get(i + 1).getValeur()
                    && cartes.get(i).getValeur() == cartes.get(i + 2).getValeur()) {
                return true;
            }
        }
        return false;
    }

    public static boolean estCarre(List<Carte> cartes) {
        Collections.sort(cartes);

        // Vérifie si au moins quatre cartes ont la même valeur
        for (int i = 0; i < cartes.size() - 3; i++) {
            if (cartes.get(i).getValeur() == cartes.get(i + 1).getValeur()
                    && cartes.get(i).getValeur() == cartes.get(i + 2).getValeur()
                    && cartes.get(i).getValeur() == cartes.get(i + 3).getValeur()) {
                return true;
            }
        }
        return false;
    }


    public static int nombreOccurences(List<Carte> cartes, int valeur) {
        // Compte le nombre d'occurrences de la valeur dans la liste de cartes
        int count = 0;
        for (Carte carte : cartes) {
            if (carte.getValeur() == valeur) {
                count++;
            }
        }
        return count;
    }

}
