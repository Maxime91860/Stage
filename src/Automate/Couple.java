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
public class Couple {
    Etat etat;
    int cle;
    
    public Couple(Etat e, int c){
        etat = e;
        cle = c;
    }
    
    public String toString(){
        return "("+etat+","+cle+")";
    }
    
}
