package Automate;

import java.util.ArrayList;

public class Automate {

    ArrayList<Etat> ensemble_etats;
    ArrayList<Transition> delta;
    private int nb_finaux;

    public Automate(int nb_etat, ArrayList<Transition> transitions, int[] etats_finaux) {
        delta = transitions;
        ensemble_etats = new ArrayList<>();
        for (int i = 0; i < nb_etat; i++) {
            ensemble_etats.add(new Etat(i));
        }
        nb_finaux = 0;
        for (int i = 0; i < etats_finaux.length; i++) {
            if (etats_finaux[i] < nb_etat) {
                ensemble_etats.get(etats_finaux[i]).estFinal = true;
                nb_finaux++;
            }
        }
    }

    public boolean accepte(String mot) {
        Etat etat_courant = ensemble_etats.get(0);
        int caract_courant = 0;
        int nouvel_etat;
        while (caract_courant != mot.length()) {
            if (!existeTransition(etat_courant, mot.charAt(caract_courant) + "")) {
                return false;
            }
            nouvel_etat = nouvelEtat(etat_courant, mot.charAt(caract_courant) + "");
            etat_courant = ensemble_etats.get(nouvel_etat);
            caract_courant++;
        }
        return etat_courant.estFinal();
    }

    public boolean estDeterministe() {
        for (int i = 0; i < delta.size(); i++) {
            for (int j = 0; j < delta.size(); j++) {
                if (!delta.get(i).equals(delta.get(j))) {
                    if ((delta.get(i).etat_courant == delta.get(j).etat_courant) && (delta.get(i).caractere_lu.equals(delta.get(j).caractere_lu))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean estComplet() {
        String[] alphabet = alphabet();
        for (int i = 0; i < ensemble_etats.size(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (!existeTransition(ensemble_etats.get(i), alphabet[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public String[] alphabet() {
        ArrayList<String> alphabet = new ArrayList<>();
        for (int i = 0; i < delta.size(); i++) {
            String c = "" + delta.get(i).caractere_lu;
            if (!alphabet.contains(c)) {
                alphabet.add(c);
            }
        }

        return alphabet.toArray(new String[alphabet.size()]);
    }

    public String toString() {
        String alphabet = "{";
        String[] alph_tab = alphabet();
        for (int i = 0; i < alph_tab.length; i++) {
            if (i != alph_tab.length - 1) {
                alphabet += alph_tab[i] + ",";
            } else {
                alphabet += alph_tab[i] + "}";
            }
        }

        int finaux_ecrits = 0;
        String finaux = "{";
        String etats = "{";
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (i != ensemble_etats.size() - 1) {
                etats += ensemble_etats.get(i) + ",";
            } else {
                etats += ensemble_etats.get(i) + "}";
            }
            if (ensemble_etats.get(i).estFinal) {
                if (finaux_ecrits != nb_finaux-1) {
                    finaux += ensemble_etats.get(i) + ",";
                    finaux_ecrits++;
                } else {
                    finaux += ensemble_etats.get(i);
                }
            }
        }
        finaux += "}";

        String delta_str = "";
        for (int i = 0; i < delta.size(); i++) {
            delta_str += delta.get(i) + "\n";
        }

        // M = { alphabet, Q , F , q0, delta }
        String s = "{ " + alphabet + " , " + etats + " , " + finaux + " , {" + ensemble_etats.get(0) + "} , delta }\nAvec delta:\n" + delta_str;
        return s;
    }

    public boolean existeTransition(Etat etat, String caract) {
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant == etat.num_etat && delta.get(i).caractere_lu.equals(caract)) {
                return true;
            }
        }
        return false;
    }

    public int nouvelEtat(Etat etat, String caract) {
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant == etat.num_etat && delta.get(i).caractere_lu.equals(caract)) {
                return delta.get(i).etat_suivant;
            }
        }
        return -1;
    }

    public void completer() {
        if (!estComplet()) {
            //Ajout du puit
            Etat puit = new Etat(ensemble_etats.size(), false);
            ensemble_etats.add(puit);
            String[] alphabet = alphabet();
            for (int i = 0; i < ensemble_etats.size(); i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    if (!existeTransition(ensemble_etats.get(i), alphabet[j])) {
                        delta.add(new Transition(ensemble_etats.get(i).num_etat, alphabet[j], puit.num_etat));
                    }
                }
            }

        }
    }

}
