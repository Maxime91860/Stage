package Automate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

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

    public Automate(Automate a) {
        ensemble_etats = new ArrayList<Etat>();
        delta = new ArrayList<Transition>();
        for (int i = 0; i < a.ensemble_etats.size(); i++) {
            ensemble_etats.add(new Etat(a.ensemble_etats.get(i).nom_etat, a.ensemble_etats.get(i).estFinal));
        }
        for (int i = 0; i < a.delta.size(); i++) {
            Etat etat_courant = null;
            Etat etat_suivant = null;
            for (int j = 0; j < ensemble_etats.size(); j++) {
                if (a.delta.get(i).etat_courant.equals(ensemble_etats.get(j))) {
                    etat_courant = ensemble_etats.get(j);
                }
                if (a.delta.get(i).etat_suivant.equals(ensemble_etats.get(j))) {
                    etat_suivant = ensemble_etats.get(j);
                }
            }
            delta.add(new Transition(etat_courant, a.delta.get(i).caractere_lu, etat_suivant));
        }
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
     *
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
     *
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
     *
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
     * Renvoie true s'il existe une transition de l'état etat par le caractère
     * caract.
     *
     * @param etat
     * @param caract
     * @return
     */
    public boolean existeTransition(Etat etat, String caract) {
        if (!ensemble_etats.contains(etat)) {
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
     *
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
     * * Renvoie les états obtenus en empruntant la transition d(etat,caract),
     * (Automate non déterministe).
     *
     * @param etat
     * @param caract
     * @return
     */
    public HashSet<Etat> nouveauxEtat(Etat etat, String caract) {
        HashSet<Etat> nouveauxEtat = new HashSet<>();
        for (int i = 0; i < delta.size(); i++) {
            if (delta.get(i).etat_courant.equals(etat) && delta.get(i).caractere_lu.equals(caract)) {
                nouveauxEtat.add(delta.get(i).etat_suivant);
            }
        }
        return nouveauxEtat;
    }

    /**
     * Renvoie les états obtenus d'un ensemble d'états par un caractère.
     *
     * @param etats
     * @param caract
     * @return
     */
    public HashSet<Etat> nouveauxEtat(HashSet<Etat> etats, String caract) {
        HashSet<Etat> nouveauxEtat = new HashSet<>();
        Iterator<Etat> it = etats.iterator();
        while (it.hasNext()) {
            /*//On calcule les états de l'élément courant de etats.
             HashSet<Etat> nouveaux_etats = nouveauxEtat(it.next(), caract);
             //On ajoute les états trouvés à l'ensemble.
             Iterator<Etat> it2 = nouveaux_etats.iterator();
             while(it2.hasNext()) {
             nouveauxEtat.add(it2.next());
             }
             */
            nouveauxEtat.addAll(nouveauxEtat(it.next(), caract));
        }
        return nouveauxEtat;
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
     * Remplace dans le caractère à la position idx de la chaine, par le
     * caractère monCharRempl.
     *
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
     *
     * @return
     */
    public Automate determiniser() {
        if (ensemble_etats.isEmpty()) {
            return this;
        }
        String[] alphabet = alphabet();
        Automate automate_determinise = new Automate();
        //EpsilonCloture();

        HashSet< HashSet<Etat>> A_TRAITER = new HashSet<>();
        HashSet<Etat> etat_traite = new HashSet<>();
        etat_traite.add(etat_initial());
        A_TRAITER.add(etat_traite);
        automate_determinise.ajouteEtat(etat_initial());

        while (!A_TRAITER.isEmpty()) {
            Iterator<HashSet<Etat>> it = A_TRAITER.iterator();
            etat_traite = it.next();
            A_TRAITER.remove(etat_traite);
            for (int i = 0; i < alphabet.length; i++) {
                HashSet<Etat> nouvel_ens_etats = nouveauxEtat(etat_traite, alphabet[i]);
                //System.out.println("Pour " + afficheEnsembleEtat(etat_traite) + " et la lettre " + alphabet[i] + ", on obtient" + afficheEnsembleEtat(nouvel_ens_etats));
                //System.out.println(!nouvel_ens_etats.isEmpty());
                if (!nouvel_ens_etats.isEmpty()) {
                    Etat nouvel_etat = regroupeEtat(nouvel_ens_etats);
                    if (automate_determinise.ajouteEtat(nouvel_etat)) {
                        A_TRAITER.add(nouvel_ens_etats);
                    }
                    automate_determinise.ajouteTransition(new Transition(regroupeEtat(etat_traite), alphabet[i], nouvel_etat));
                }
            }
        }

        return automate_determinise;
    }

    public Automate EpsilonCloture() {
        Automate a = new Automate(this);
        boolean fusion = false;

        do {
            fusion = false;
            ListIterator<Transition> it = a.delta.listIterator();
            while (it.hasNext()) {
                Transition t = it.next();
                System.out.println("Transition courante: " + t);
                if (t.caractere_lu.equals("E")) {
                    it.remove();
                    a.fusionEtat(t.etat_courant, t.etat_suivant);
                    System.out.println("Automate après fusion :" + a);
                    System.out.println("Suppression de " + t);
                    a.effaceTransitionDouble();
                    fusion = true;
                    break;
                }
            }
        } while (fusion);
        return a;
    }

    private void fusionEtat(Etat e1, Etat e2) {
        System.out.println("Fusion de " + e1 + " et " + e2);
        Etat fusion;
        if (e2.equals(ensemble_etats.get(0))) {
            fusion = e2;
            ensemble_etats.remove(e1);
        } else {
            fusion = e1;
            ensemble_etats.remove(e2);
        }
        fusion.nom_etat = e1.toString() + e2.toString();
        fusion.estFinal = e1.estFinal() || e2.estFinal();
        ListIterator<Transition> it = delta.listIterator();
        while (it.hasNext()) {
            Transition t = it.next();
            if (t.etat_courant.equals(e1) || t.etat_courant.equals(e2)) {
                Transition tmp = new Transition(fusion, t.caractere_lu, t.etat_suivant);
                if (!delta.contains(tmp)) {
                    it.set(tmp);
                }
            }
        }
    }

    private void effaceTransitionDouble() {
        ListIterator<Transition> it = delta.listIterator();
        int i = 0;
        int j = 0;
        while (it.hasNext()) {
            Transition t = it.next();
            ListIterator<Transition> it2 = delta.listIterator();
            while (it2.hasNext()) {
                Transition t2 = it2.next();
                if (t2.equals(t) && i != j) {
                    it2.remove();
                }
                j++;
            }
            i++;
            j = 0;
        }
    }

    public void afficheDelta() {
        String[] alphabet = alphabet();
        //Cacul des transitions
        Etat[][] table = new Etat[ensemble_etats.size()][alphabet.length];
        for (int i = 0; i < ensemble_etats.size(); i++) {
            for (int j = 0; j < alphabet.length; j++) {
                table[i][j] = nouvelEtat(ensemble_etats.get(i), alphabet[j]);
            }
        }

        afficheTable(table);
    }

    public void afficheTable(Etat[][] T) {
        String[] alphabet = alphabet();
        for (int j = 0; j < alphabet.length; j++) {
            System.out.print("\t" + alphabet[j]);
        }
        System.out.println("\n___________________");
        for (int i = 0; i < ensemble_etats.size(); i++) {
            System.out.print("Etat " + ensemble_etats.get(i) + " | ");
            for (int j = 0; j < alphabet.length; j++) {
                System.out.print(T[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println("___________________");
    }

    /**
     * Renvoie la version minimal de l'automate.
     */
    public Automate minimise() {
        if (ensemble_etats.isEmpty()) {
            return this;
        }
        ArrayList<ArrayList<Etat>> ensemble = new ArrayList<ArrayList<Etat>>();
        ensemble.add(list_etats_finaux());
        ensemble.add(list_etats_non_finaux());
        String[] alphabet = alphabet();

        boolean eclatement = true;
        do {
            eclatement = false;

            //Iterator<HashSet<Etat>> it = ensemble.iterator();
            //Parcours de l'ensemble de l'ensemble des états
            //while (it.hasNext()) {
            for (int a = 0; a < ensemble.size(); a++) {
                ArrayList<Etat> list_courante = ensemble.get(a);

                //Iterator<Etat> it2 = ens_courant.iterator();
                //Parcours de l'ensemble d'états courant
                //while (it2.hasNext()) {
                for (int b = 0; b < list_courante.size(); b++) {
                    ArrayList<Etat> list_tmp = new ArrayList<Etat>();
                    Etat etat_tmp = list_courante.get(b);
                    list_courante.remove(list_courante.get(b));
                    b--;
                    list_tmp.add(etat_tmp);

                    //Vérification si chaque état peut resté dans l'ensemble.
                    //Iterator<Etat> it3 = ens_courant.iterator();
                    //while (it3.hasNext()) {
                    for (int c = 0; c < list_courante.size(); c++) {
                        Etat etat_tmp2 = list_courante.get(c);
                        boolean ok = true;
                        for (int i = 0; i < alphabet.length; i++) {
                            if (ListeEtat(ensemble, nouvelEtat(etat_tmp, alphabet[i])) != ListeEtat(ensemble, nouvelEtat(etat_tmp2, alphabet[i]))) {
                                ok = false;
                            }
                        }
                        if (ok) {
                            list_tmp.add(etat_tmp2);
                            list_courante.remove(etat_tmp2);
                            c--;
                        } else {
                            eclatement = true;
                        }
                    }
                    ensemble.add(list_tmp);
                }
                ensemble.remove(ensemble.get(a));
            }

        } while (eclatement);

        //System.out.println(ensemble);
        //Construction de l'automate minimal
        Automate automate_minimal = new Automate();

        //Ajout de l'état initial
        ArrayList<Etat> liste_etat_initial = ListeEtat(ensemble, etat_initial());
        Etat etat_initial = regroupeEtat(liste_etat_initial);
        automate_minimal.ajouteEtat(etat_initial);

        //Ajout du reste des états et de leur transition
        for (int i = 0; i < ensemble.size(); i++) {
            Etat etat_courant = regroupeEtat(ensemble.get(i));
            automate_minimal.ajouteEtat(etat_courant);
            for (int j = 0; j < alphabet.length; j++) {
                ArrayList<Etat> liste_tmp = ListeEtat(ensemble, nouvelEtat(ensemble.get(i).get(0), alphabet[j]));
                if (liste_tmp != null) {
                    automate_minimal.ajouteTransition(etat_courant, alphabet[j], regroupeEtat(liste_tmp));
                }
            }
        }

        return automate_minimal;
    }

    private HashSet<Etat> EnsembleEtat(HashSet< HashSet<Etat>> ens_ensemble, Etat e) {
        Iterator<HashSet<Etat>> it = ens_ensemble.iterator();
        while (it.hasNext()) {
            HashSet<Etat> ens_courant = it.next();
            Iterator<Etat> it2 = ens_courant.iterator();
            while (it2.hasNext()) {
                Etat etat_courant = it2.next();
                if (etat_courant.equals(e)) {
                    return ens_courant;
                }
            }
        }
        return null;
    }

    private ArrayList<Etat> ListeEtat(ArrayList< ArrayList<Etat>> list_list, Etat e) {
        for (int i = 0; i < list_list.size(); i++) {
            for (int j = 0; j < list_list.get(i).size(); j++) {
                Etat etat_courant = list_list.get(i).get(j);
                if (etat_courant.equals(e)) {
                    return list_list.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Renvoie les états finaux de l'automate.
     *
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

    public Etat etat_initial() {
        if (!ensemble_etats.isEmpty()) {
            return ensemble_etats.get(0);
        }
        return null;
    }

    public HashSet<Etat> ens_etats_finaux() {
        Etat[] finaux = etats_finaux();
        HashSet<Etat> ens_finaux = new HashSet<Etat>();
        for (int j = 0; j < finaux.length; j++) {
            ens_finaux.add(finaux[j]);
        }
        return ens_finaux;
    }

    public ArrayList<Etat> list_etats_finaux() {
        ArrayList<Etat> finaux = new ArrayList<Etat>();
        finaux.addAll(ens_etats_non_finaux());
        return finaux;
    }

    /**
     * Renvoie les états finaux de l'automate.
     *
     * @return
     */
    public HashSet<Etat> ens_etats_non_finaux() {
        HashSet<Etat> non_finaux = new HashSet<Etat>();
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (!ensemble_etats.get(i).estFinal()) {
                non_finaux.add(ensemble_etats.get(i));
            }
        }
        return non_finaux;
    }

    public ArrayList<Etat> list_etats_non_finaux() {
        ArrayList<Etat> non_finaux = new ArrayList<Etat>();
        non_finaux.addAll(ens_etats_finaux());
        return non_finaux;
    }

    /**
     * Ajoute un état à l'automate.
     *
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
     * Ajoute une transition t à l'automate, si l'état courant/suivant de t
     * n'appartient pas à l'automate, il est ajouté.
     *
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

    public boolean ajouteTransition(Etat e1, String a, Etat e2) {
        return ajouteTransition(new Transition(e1, a, e2));
    }

    /**
     * Regroupe un ensemble d'états en un même état.
     *
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

    private Etat regroupeEtat(ArrayList<Etat> list_etats) {
        HashSet<Etat> ens_etats = new HashSet<Etat>();
        ens_etats.addAll(list_etats);
        return regroupeEtat(ens_etats);
    }

    public void renommeEtats() {
        for (int i = 0; i < ensemble_etats.size(); i++) {
            String nouveau_nom = "q" + i;
            for (int j = 0; j < delta.size(); j++) {
                if (ensemble_etats.get(i).equals(delta.get(j).etat_courant)) {
                    delta.get(j).etat_courant.nom_etat = nouveau_nom;
                }
                if (ensemble_etats.get(i).equals(delta.get(j).etat_suivant)) {
                    delta.get(j).etat_suivant.nom_etat = nouveau_nom;
                }
            }
            ensemble_etats.get(i).nom_etat = nouveau_nom;
        }
    }

    public String toString() {
        String alphabet = "{";
        String[] alph_tab = alphabet();
        for (int i = 0; i < alph_tab.length; i++) {
            if (i != alph_tab.length - 1) {
                alphabet += alph_tab[i] + ",";
            } else {
                alphabet += alph_tab[i];
            }
        }
        alphabet += "}";

        int finaux_ecrits = 0;
        int nb_finaux = etats_finaux().length;
        String finaux = "{";
        String etats = "{";
        for (int i = 0; i < ensemble_etats.size(); i++) {
            if (i != ensemble_etats.size() - 1) {
                etats += ensemble_etats.get(i) + ",";
            } else {
                etats += ensemble_etats.get(i);
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
        etats += "}";
        finaux += "}";

        String delta_str = "";
        for (int i = 0; i < delta.size(); i++) {
            delta_str += delta.get(i) + "\n";
        }

        // M = { alphabet, Q , F , q0, delta }
        String s = "{ " + alphabet + " , " + etats + " soit " + ensemble_etats.size() + " etat(s), " + finaux + " , {" + etat_initial() + "} , delta }\nAvec delta:\n" + delta_str;
        return s;
    }

    private String afficheEnsembleEtat(HashSet<Etat> ensemble) {
        Iterator<Etat> it = ensemble.iterator();
        String s = "{";
        while (it.hasNext()) {
            s += it.next() + ",";
        }
        char[] tab = s.toCharArray();
        tab[s.length() - 1] = '}';
        return String.valueOf(tab);
    }
}
