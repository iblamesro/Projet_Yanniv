package fr.pantheonsorbonne.miage.game;

public class Carte implements Comparable<Carte> {
    private String valeur;
    private String couleur;
    private String icone; // Nouveau champ icone

    public Carte(String valeur, String couleur) {
        if (valeur == null || valeur.isEmpty()) {
            throw new IllegalArgumentException("La valeur de la carte ne peut pas être nulle ou vide.");
        }
        this.valeur = valeur;
        this.couleur = couleur;
        this.icone = assignIcone(); // Attribuer une icône lors de la création de la carte

    }

    @Override
    public String toString() {
        if ("Joker".equals(couleur)) {
            return "" + icone; // Afficher uniquement l'icône pour les Jokers
        } else {
            return "" + icone; // Afficher uniquement l'icône pour les cartes normales
        }
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
            // Retourner une valeur pour les figures (valet, dame, roi)
            switch (valeur) {
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                default:
                    return 0; // Par exemple,si figure inconnu valent 0
            }
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

    public String assignIcone() {
        if ("Joker".equals(couleur)) {
            return "🃏"; // Utilisez l'icône que vous préférez pour représenter le Joker
        }
        switch (couleur) {
            case "Coeur":
                return switch (valeur) {
                    case "A" -> "🂱";
                    case "2" -> "🂲";
                    case "3" -> "🂳";
                    case "4" -> "🂴";
                    case "5" -> "🂵";
                    case "6" -> "🂶";
                    case "7" -> "🂷";
                    case "8" -> "🂸";
                    case "9" -> "🂹";
                    case "10" -> "🂺";
                    case "J" -> "🂻";
                    case "Q" -> "🂽";
                    case "K" -> "🂾";
                    case "11" -> "🂿"; // Ajoutez la correspondance pour la valeur "11"
                    default -> throw new IllegalArgumentException("Valeur de carte non reconnue : " + valeur);
                };
            case "Pique":
                return switch (valeur) {
                    case "A" -> "🂡";
                    case "2" -> "🂢";
                    case "3" -> "🂣";
                    case "4" -> "🂤";
                    case "5" -> "🂥";
                    case "6" -> "🂦";
                    case "7" -> "🂧";
                    case "8" -> "🂨";
                    case "9" -> "🂩";
                    case "10" -> "🂪";
                    case "J" -> "🂫";
                    case "Q" -> "🂭";
                    case "K" -> "🂮";
                    case "11" -> "🂿"; // Ajoutez la correspondance pour la valeur "11"
                    default -> throw new IllegalArgumentException("Valeur de carte non reconnue : " + valeur);
                };
            case "Carreau":
                return switch (valeur) {
                    case "A" -> "🃁";
                    case "2" -> "🃂";
                    case "3" -> "🃃";
                    case "4" -> "🃄";
                    case "5" -> "🃅";
                    case "6" -> "🃆";
                    case "7" -> "🃇";
                    case "8" -> "🃈";
                    case "9" -> "🃉";
                    case "10" -> "🃊";
                    case "J" -> "🃋";
                    case "Q" -> "🃍";
                    case "K" -> "🃎";
                    case "11" -> "🂿"; // Ajoutez la correspondance pour la valeur "11"
                    default -> throw new IllegalArgumentException("Valeur de carte non reconnue : " + valeur);
                };
            case "Trèfle":
                return switch (valeur) {
                    case "A" -> "🃑";
                    case "2" -> "🃒";
                    case "3" -> "🃓";
                    case "4" -> "🃔";
                    case "5" -> "🃕";
                    case "6" -> "🃖";
                    case "7" -> "🃗";
                    case "8" -> "🃘";
                    case "9" -> "🃙";
                    case "10" -> "🃚";
                    case "J" -> "🃛";
                    case "Q" -> "🃝";
                    case "K" -> "🃞";
                    case "11" -> "🂿"; // Ajoutez la correspondance pour la valeur "11"
                    default -> throw new IllegalArgumentException("Valeur de carte non reconnue : " + valeur);
                };
            default:
                throw new IllegalArgumentException("Couleur de carte non reconnue : " + couleur);
        }
    }
}
