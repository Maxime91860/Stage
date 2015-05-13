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
public class Etat {
    int num_etat;
    boolean estFinal;
    
    public Etat (int num_etat, boolean estFinal){
        this.num_etat = num_etat;
        this.estFinal = estFinal;
    }
    
    public Etat (int num_etat){
        this(num_etat,false);
    }
    
    public boolean estFinal(){
        return estFinal;
    }
    
    public String toString(){
        return "q"+num_etat;
    }
}
