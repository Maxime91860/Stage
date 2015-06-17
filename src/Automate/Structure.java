/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automate;

import java.util.HashSet;
import java.util.Iterator;

public class Structure {
    HashSet<Couple> ensemble_couple;
    
    public Structure(){
        ensemble_couple = new HashSet<Couple>();
    }
    
    public boolean ajouteCouple(Couple c){
        return ensemble_couple.add(c);
    }
    
    public HashSet<Etat> getEtats(int cle){
        HashSet<Etat> ens_etat = new HashSet<Etat>();
        Iterator<Couple> it = ensemble_couple.iterator();
        while(it.hasNext()){
            Couple tmp = it.next();
            if(tmp.cle == cle){
                ens_etat.add(tmp.etat);
            }
        }
        return ens_etat;
    }
    
    public int getCle(Etat e){
        Iterator<Couple> it = ensemble_couple.iterator();
        while(it.hasNext()){
            Couple tmp = it.next();
            if(tmp.etat.equals(e)){
                return tmp.cle;
            }
        }
        return -1;
    }
    
    public Iterator<Couple> iterator(){
        return ensemble_couple.iterator();
    }
    
    public String toString(){
        return ensemble_couple.toString();
    }
}
