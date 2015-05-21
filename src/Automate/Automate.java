package Automate;

import java.util.ArrayList;

public class Automate {

    ArrayList<Etat> ensemble_etats;
    ArrayList<Transition> delta;
    //private int nb_finaux;

    public Automate() {
        ensemble_etats = new ArrayList<>();
        delta = new ArrayList<>();
    }

    public Automate(int nb_etat, int[] etats_finaux) {
        delta = new ArrayList<>();
        ensemble_etats = new ArrayList<>();
        for (int i = 0; i < nb_etat; i++) {
            ensemble_etats.add(new Etat("q" + i));
        }
        //nb_finaux = 0;
        for (int i = 0; i < etats_finaux.length; i++) {
            if (etats_finaux[i] < nb_etat) {
                ensemble_etats.get(etats_finaux[i]).estFinal = true;
                //nb_finaux++;
            }
        }
    }

    public Automate(Transition[] delta) {
        //On suppose que la premiere transition est une transition de l'état initial.
        ensemble_etats = new ArrayList<>();
        this.delta = new ArrayList<>();
        for (int i = 0; i < delta.length; i++) {
            if (!ensemble_etats.contains(delta[i].etat_courant)) {
                ensemble_etats.add(delta[i].etat_courant);
            }
            this.delta.add(delta[i]);
        }
    }

    public boolean accepte(String mot) {
        Etat etat_courant = ensemble_etats.get(0);
        String[] alphabet = alphabet();
        int caract_courant = 0;
        //int nouvel_etat;
        while (caract_courant != mot.length()) {
            if (mot.charAt(caract_courant) == '#') {
                String[] mots_substitues = new String[alphabet.length];
                for (int i = 0; i < alphabet.length; i++) {
                    String tmp = mot;
                    tmp = substitution(tmp, caract_courant, alphabet[i]);
                    mots_substitues[i] = tmp;
                }
                boolean test = true;
                for (int i = 0; i < mots_substitues.length; i++) {
                    test = test && accepte(mots_substitues[i]);
                }
                return test;
            }
            if (!existeTransition(etat_courant, mot.charAt(caract_courant) + "")) {
                return false;
            }
            etat_courant = nouvelEtat(etat_courant, mot.charAt(caract_courant) + "");
            caract_courant++;
        }
        return etat_courant.estFinal();
    }

    public boolean estDeterministe() {
        for (int i = 0; i < delta.size(); i++) {
            for (int j = 0; j < delta.size(); j++) {
                if (!delta.get(i).equals(delta.get(j))) {
                    if ((delta.get(i).etat_courant.equals(delta.get(j).etat_courant)) && (delta.get(i).caractere_lu.equals(delta.get(j).caractere_lu))) {
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

    public boolean existeTransition(Etat etat, String caract) {
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                return true;
            }
        }
        return false;
    }

    public Etat nouvelEtat(Etat etat, String caract) {
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                return delta.get(i).etat_suivant;
            }
        }
        return null;
    }

    public Etat[] nouveauxEtat(Etat etat, String caract) {
        ArrayList<Etat> nouveauxEtat = new ArrayList<>();
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                nouveauxEtat.add(delta.get(i).etat_suivant);
            }
        }
        return nouveauxEtat.toArray(new Etat[nouveauxEtat.size()]);
    }

    public void completer() {
        if (!estComplet()) {
            //Ajout du puit
            Etat puit = new Etat("puit", false);
            ensemble_etats.add(puit);
            String[] alphabet = alphabet();
            for (int i = 0; i < ensemble_etats.size(); i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    if (!existeTransition(ensemble_etats.get(i), alphabet[j])) {
                        delta.add(new Transition(ensemble_etats.get(i), alphabet[j], puit));
                    }
                }
            }

        }
    }

    //A refaire
    private String substitution(String chaine, int idx, String monCharRempl) {
        char[] tab = chaine.toCharArray();
        char c = monCharRempl.charAt(0);
        tab[idx] = c;
        return String.valueOf(tab);
    }

    /*
     public Automate determiniser() {
     String[] alphabet = alphabet();

     ArrayList<Etat> nouveaux_etats = new ArrayList<>();

     ArrayList<Etat> A_TRAITER = new ArrayList<>();
     A_TRAITER.add(ensemble_etats.get(0));
     Etat etat_traite;

     while (!A_TRAITER.isEmpty()) {
     etat_traite = A_TRAITER.get(0);
     A_TRAITER.remove(A_TRAITER.get(0));
     for (int i = 0; i < alphabet.length; i++) {
     Etat tmp = new Etat(nouvelEtat(etat_traite, alphabet[i]));
     }
     }

     return new Automate(nouveaux_etats.size(), null, null);
     }
     //*/
    public Etat[] etats_finaux() {
        ArrayList<Etat> finaux = new ArrayList<>();
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (ensemble_etats.get(i).estFinal()) {
                finaux.add(ensemble_etats.get(i));
            }
        }
        return finaux.toArray(new Etat[finaux.size()]);
    }

    public boolean ajouteEtat(Etat e) {
        if (!ensemble_etats.contains(e)) {
            return ensemble_etats.add(e);
        }
        return false;
    }

    public boolean ajouteTransition(Transition t) {
        if (!delta.contains(t)) {
            if (ensemble_etats.contains(t.etat_courant)) {
                ajouteEtat(t.etat_courant);
            }
            if (ensemble_etats.contains(t.etat_suivant)) {
                ajouteEtat(t.etat_suivant);
            }
            return delta.add(t);
        }

        return false;
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
        int nb_finaux = etats_finaux().length;
        String finaux = "{";
        String etats = "{";
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (i != ensemble_etats.size() - 1) {
                etats += ensemble_etats.get(i) + ",";
            } else {
                etats += ensemble_etats.get(i) + "}";
            }
            if (ensemble_etats.get(i).estFinal) {
                if (finaux_ecrits != nb_finaux - 1) {
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
}
