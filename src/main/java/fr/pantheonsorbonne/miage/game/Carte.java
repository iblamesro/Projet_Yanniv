package fr.pantheonsorbonne.miage.game;
public class Carte implements Comparable<Carte> {
    private String valeur;
    private String couleur;

    public Carte(String valeur, String couleur) {
        if (valeur == null || valeur.isEmpty()) {
            throw new IllegalArgumentException("La valeur de la carte ne peut pas être nulle ou vide.");
        }
        this.valeur = valeur;
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return valeur + " de " + couleur;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getValeur() {
        // Si la valeur est un nombre, on la parse et la retourne
        if (Character.isDigit(valeur.charAt(0))) {
            try {
                return Integer.parseInt(valeur);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Erreur de conversion de la valeur de la carte en entier.");
            }
        } else {
            // Retourner une valeur fixe pour les figures (valet, dame, roi)
            return 10; // par exemple, toutes les figures valent 10
        }
    }

    @Override
    public int compareTo(Carte autreCarte) {
        int valeurActuelle = this.getValeur();
        int valeurAutreCarte = autreCarte.getValeur();

        // Si les valeurs sont différentes, comparez simplement les valeurs
        if (valeurActuelle != valeurAutreCarte) {
            return Integer.compare(valeurActuelle, valeurAutreCarte);
        } else {
            // Si les valeurs sont égales, comparez les couleurs
            return this.getCouleur().compareTo(autreCarte.getCouleur());
        }
    }

    public boolean estJusteApres(Carte autreCarte) {
        int valeurActuelle = this.getValeur();
        int valeurAutreCarte = autreCarte.getValeur();

        // Règles générales : la valeur de la carte actuelle doit être égale à la valeur
        // de l'autre carte + 1
        if (valeurActuelle == valeurAutreCarte + 1) {
            // Vérifier si les cartes ont la même couleur
            return this.getCouleur().equals(autreCarte.getCouleur());
        }

        // Si aucune des conditions ci-dessus n'est satisfaite, la carte n'est pas juste
        // après
        return false;
    }
}
