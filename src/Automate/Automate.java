package Automate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Automate {

    ArrayList<Etat> ensemble_etats;
    ArrayList<Transition> delta;

    /**
     * Construit un Automate, les états et les transitions seront ajoutés plus
     * tard.
     */
    public Automate() {
        ensemble_etats = new ArrayList<>();
        delta = new ArrayList<>();
    }

    /**
     * Construit un Automate à partir d'une table de transitions.
     *
     * @param delta
     */
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

    /**
     * Renvoie true si le mot est accepté par l'automate.
     *
     * @param mot
     * @return
     */
    public boolean accepte(String mot) {

        Etat etat_courant = ensemble_etats.get(0);
        String[] alphabet = alphabet();
        int caract_courant = 0;

        while (caract_courant != mot.length()) {
            //Pour les mots à trous.
            if (mot.charAt(caract_courant) == '#') {
                //Construction des nouveaux mots, en remplacement le # par chaque lettre de l'alphabet.
                String[] mots_substitues = new String[alphabet.length];
                for (int i = 0; i < alphabet.length; i++) {
                    String tmp = mot;
                    tmp = substitution(tmp, caract_courant, alphabet[i]);
                    mots_substitues[i] = tmp;
                }
                //Test des nouveaux mots.
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

    /**
     * Renvoie true si l'automate est déterministe.
     * @return
     */
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

    /**
     * Renvoie true si l'automate est complet.
     * @return 
     */
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

    /**
     * Renvoie l'alphabet utilisé par l'automate.
     * @return 
     */
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

    /**
     * Renvoie true s'il existe une transition de l'état etat par le caractère caract.
     * @param etat
     * @param caract
     * @return 
     */
    public boolean existeTransition(Etat etat, String caract) {
        if(!ensemble_etats.contains(etat)){
            return false;
        }
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie l'état obtenu en empruntant la transition d(etat,caract).
     * @param etat
     * @param caract
     * @return 
     */
    public Etat nouvelEtat(Etat etat, String caract) {
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                return delta.get(i).etat_suivant;
            }
        }
        return null;
    }

    /**
     * * Renvoie les états obtenus en empruntant la transition d(etat,caract), (Automate non déterministe).
     * @param etat
     * @param caract
     * @return 
     */
    public Etat[] nouveauxEtat(Etat etat, String caract) {
        ArrayList<Etat> nouveauxEtat = new ArrayList<>();
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                nouveauxEtat.add(delta.get(i).etat_suivant);
            }
        }
        return nouveauxEtat.toArray(new Etat[nouveauxEtat.size()]);
    }

    /**
     * Renvoie les états obtenus d'un ensemble d'états par un caractère.
     * @param etats
     * @param caract
     * @return 
     */
    public Etat[] nouveauxEtat(HashSet<Etat> etats, String caract) {
        HashSet<Etat> nouveauxEtat = new HashSet<>();
        Iterator<Etat> it = etats.iterator();
        while (it.hasNext()) {
            Etat[] nouveaux_etats = nouveauxEtat(it.next(), caract);
            for (int i = 0; i < nouveaux_etats.length; i++) {
                nouveauxEtat.add(nouveaux_etats[i]);
            }
        }
        return nouveauxEtat.toArray(new Etat[nouveauxEtat.size()]);
    }

    /**
     * Complete l'automate.
     */
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
    /**
     * Remplace dans le caractère à la position idx de la chaine, par le caractère monCharRempl.
     * @param chaine
     * @param idx
     * @param monCharRempl
     * @return 
     */
    private String substitution(String chaine, int idx, String monCharRempl) {
        char[] tab = chaine.toCharArray();
        char c = monCharRempl.charAt(0);
        tab[idx] = c;
        return String.valueOf(tab);
    }

    /**
     * Renvoie la version déterminisée de l'automate.
     * @return 
     */
    public Automate determiniser() {
        String[] alphabet = alphabet();
        Automate automate_determinise = new Automate();

        HashSet< HashSet<Etat> > A_TRAITER = new HashSet<>();
        HashSet<Etat> etat_traite = new HashSet<>();
        etat_traite.add(ensemble_etats.get(0));
        A_TRAITER.add(etat_traite);
        automate_determinise.ajouteEtat(ensemble_etats.get(0));

        while (!A_TRAITER.isEmpty()) {
            Iterator<HashSet<Etat>> it = A_TRAITER.iterator();
            etat_traite = it.next();
            A_TRAITER.remove(etat_traite);
            for (int i = 0; i < alphabet.length; i++) {
                Etat[] nouveauxEtat = nouveauxEtat(etat_traite, alphabet[i]);
                HashSet<Etat> nouvel_ens_etats = new HashSet<Etat>();
                String s = "";
                boolean estFinal = false;
                for (int j = 0; j < nouveauxEtat.length; j++) {
                    nouvel_ens_etats.add(nouveauxEtat[j]);
                    s += nouveauxEtat[j];
                    estFinal |= nouveauxEtat[j].estFinal();
                }
                if (!s.isEmpty()) {
                    Etat nouvel_etat = new Etat(s,estFinal);
                    if (automate_determinise.ajouteEtat(nouvel_etat)) {
                        A_TRAITER.add(nouvel_ens_etats);
                    }
                    automate_determinise.ajouteTransition(new Transition(regroupeEtat(etat_traite), alphabet[i], nouvel_etat));
                }
            }
        }

        return automate_determinise;
    }

    /**
     * Renvoie les états finaux de l'automate.
     * @return 
     */
    public Etat[] etats_finaux() {
        ArrayList<Etat> finaux = new ArrayList<>();
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (ensemble_etats.get(i).estFinal()) {
                finaux.add(ensemble_etats.get(i));
            }
        }
        return finaux.toArray(new Etat[finaux.size()]);
    }

    /**
     * Ajoute un état à l'automate.
     * @param e
     * @return 
     */
    public boolean ajouteEtat(Etat e) {
        if (!ensemble_etats.contains(e)) {
            return ensemble_etats.add(e);
        }
        return false;
    }

    /**
     * Ajoute une transition t à l'automate, si l'état courant/suivant de t n'appartient pas à l'automate, il est ajouté.
     * @param t
     * @return 
     */
    public boolean ajouteTransition(Transition t) {
        if (!delta.contains(t)) {
            if (!ensemble_etats.contains(t.etat_courant)) {
                ajouteEtat(t.etat_courant);
            }
            if (!ensemble_etats.contains(t.etat_suivant)) {
                ajouteEtat(t.etat_suivant);
            }
            return delta.add(t);
        }

        return false;
    }

    /**
     * Regroupe un ensemble d'états en un même état.
     * @param etats
     * @return 
     */
    private Etat regroupeEtat(HashSet<Etat> etats) {
        boolean estFinal = false;
        String s = "";
        Iterator<Etat> it = etats.iterator();
        while (it.hasNext()) {
            Etat tmp = it.next();
            estFinal |= tmp.estFinal;
            s += tmp.nom_etat;
        }
        return new Etat(s, estFinal);
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
                    finaux += ensemble_etats.get(i)+"}";
                }
            }
        }

        String delta_str = "";
        for (int i = 0; i < delta.size(); i++) {
            delta_str += delta.get(i) + "\n";
        }

        // M = { alphabet, Q , F , q0, delta }
        String s = "{ " + alphabet + " , " + etats + " , " + finaux + " , {" + ensemble_etats.get(0) + "} , delta }\nAvec delta:\n" + delta_str;
        return s;
    }
}
