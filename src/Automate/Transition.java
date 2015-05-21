/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automate;

/**
 *
 * @author Maxime
 */
public class Transition {
    Etat etat_courant;
    String caractere_lu;
    Etat etat_suivant;
    
    public Transition (Etat e1, String c, Etat e2){
        etat_courant = e1;
        caractere_lu = c;
        etat_suivant = e2;
    }
    
    
    public String toString(){
        //if(etat_suivant != null)
            return "d("+etat_courant+","+caractere_lu+") = "+etat_suivant;
        //return "d(q"+etat_courant.num_etat+","+caractere_lu+") = vide";
    }
    
    public boolean equals (Transition t){
        if(t==null)
            return false;
        if(t.etat_courant != etat_courant)
            return false;
        if(!t.caractere_lu.equals(caractere_lu))
            return false;
        if(t.etat_suivant != etat_suivant)
            return false;
        return true;
    }
}
