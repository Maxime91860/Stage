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
public class Test2 {

    public static void main(String[] arg) {
        //Contient au moins une occurence de "ab"
        Automate M2 = new Automate();
        Etat q3 = new Etat("q3");
        Etat q4 = new Etat("q4");
        Etat q5 = new Etat("q5", true);
        M2.ajouteTransition(new Transition(q3, "a", q3));
        M2.ajouteTransition(new Transition(q3, "b", q3));
        M2.ajouteTransition(new Transition(q3, "a", q4));
        M2.ajouteTransition(new Transition(q4, "b", q5));
        M2.ajouteTransition(new Transition(q5, "a", q5));
        M2.ajouteTransition(new Transition(q5, "b", q5));

        //System.out.println(M2);
        /*
         Automate M3 = new Automate();
         Etat q6 = new Etat("q6");
         Etat q7 = new Etat("q7");
         Etat q8 = new Etat("q8");
         Etat q9 = new Etat("q9", true);
         Etat q10 = new Etat("q10", true);
        
        
        
         M3.ajouteTransition(new Transition(q6, "a", q9));
         M3.ajouteTransition(new Transition(q6, "E", q7));
         M3.ajouteTransition(new Transition(q6, "E", q8));
         M3.ajouteTransition(new Transition(q7, "b", q9));
         M3.ajouteTransition(new Transition(q8, "c", q10));
        
         System.out.println(M3);
         System.out.println("---------Epsilon Cloture---------");
         M3=M3.EpsilonCloture();
         System.out.println(M3);
         // Transition t = new Transition(q6, "a", q9);
         //System.out.println("t.equals(M3.delta.get(0)) = "+t.equals(M3.delta.get(0)));
         */
        
        //"a" en avant dernière position
        Automate M4 = new Automate();
        Etat q11 = new Etat("q0");
        Etat q12 = new Etat("q1");
        Etat q13 = new Etat("q2", true);
        //Etat q = new Etat("q");
        //M4.ajouteTransition(new Transition(q, "E", q11));
        M4.ajouteTransition(new Transition(q11, "a", q11));
        M4.ajouteTransition(new Transition(q11, "a", q12));
        M4.ajouteTransition(new Transition(q12, "a", q13));
        M4.ajouteTransition(new Transition(q12, "b", q13));

        /*System.out.println(M4);
         M4 = M4.determiniser();
         System.out.println(M4);*/
        
        
        Automate M5 = new Automate();
        Etat q14 = new Etat("q14");
        Etat q15 = new Etat("q15");
        Etat q16 = new Etat("q16", true);
        Etat q17 = new Etat("puit");
        M5.ajouteTransition(new Transition(q14, "a", q15));
        M5.ajouteTransition(new Transition(q14, "b", q14));
        M5.ajouteTransition(new Transition(q15, "a", q16));
        M5.ajouteTransition(new Transition(q15, "b", q17));
        M5.ajouteTransition(new Transition(q16, "a", q15));
        M5.ajouteTransition(new Transition(q16, "a", q14));
        M5.ajouteTransition(new Transition(q16, "b", q14));
        M5.ajouteTransition(new Transition(q17, "a", q17));
        M5.ajouteTransition(new Transition(q17, "b", q17));

        //System.out.println(M5);
        //M5= M5.determiniser();
        //System.out.println(M5);
        
        
        Automate M6 = new Automate();
        Etat[] q_M6 = {new Etat("q1"), new Etat("q2"), new Etat("q3"), new Etat("q4", true)};
        M6.ajouteTransition(q_M6[0], "a", q_M6[1]);
        M6.ajouteTransition(q_M6[0], "b", q_M6[0]);
        M6.ajouteTransition(q_M6[1], "a", q_M6[2]);
        M6.ajouteTransition(q_M6[1], "a", q_M6[0]);
        M6.ajouteTransition(q_M6[1], "b", q_M6[1]);
        M6.ajouteTransition(q_M6[2], "a", q_M6[3]);
        M6.ajouteTransition(q_M6[2], "b", q_M6[3]);
        M6.ajouteTransition(q_M6[3], "a", q_M6[3]);
        M6.ajouteTransition(q_M6[3], "b", q_M6[3]);

        /*System.out.println(M6);
         M6 = M6.determiniser();
         System.out.println(M6);//*/
        
        
        Automate M7 = new Automate();
        Etat[] q_M7 = {new Etat("q0"), new Etat("q1"), new Etat("q2"), new Etat("q3", true), new Etat("puit")};
        M7.ajouteTransition(q_M7[0], "a", q_M7[0]);
        M7.ajouteTransition(q_M7[0], "b", q_M7[0]);
        M7.ajouteTransition(q_M7[0], "a", q_M7[1]);
        M7.ajouteTransition(q_M7[1], "a", q_M7[2]);
        M7.ajouteTransition(q_M7[1], "b", q_M7[2]);
        M7.ajouteTransition(q_M7[2], "a", q_M7[3]);
        M7.ajouteTransition(q_M7[2], "b", q_M7[3]);
        M7.ajouteTransition(q_M7[3], "a", q_M7[4]);
        M7.ajouteTransition(q_M7[3], "b", q_M7[4]);

        /*
         System.out.println(M7);
         M7 = M7.determiniser();
         System.out.println(M7);
         M7 = M7.minimise(); 
         System.out.println(M7);//*/
        
        
        //Automate Manéa
        Automate M8 = new Automate();
        Etat[] q_M8 = {new Etat("q0"), new Etat("q1"), new Etat("q2"), new Etat("q3"), new Etat("q4"), new Etat("q5", true)};
        M8.ajouteTransition(q_M8[0], "a", q_M8[1]);
        M8.ajouteTransition(q_M8[0], "b", q_M8[0]);
        M8.ajouteTransition(q_M8[1], "a", q_M8[2]);
        M8.ajouteTransition(q_M8[1], "b", q_M8[2]);
        M8.ajouteTransition(q_M8[2], "a", q_M8[3]);
        M8.ajouteTransition(q_M8[2], "b", q_M8[3]);
        M8.ajouteTransition(q_M8[3], "a", q_M8[4]);
        M8.ajouteTransition(q_M8[3], "b", q_M8[4]);
        M8.ajouteTransition(q_M8[4], "a", q_M8[5]);
        M8.ajouteTransition(q_M8[5], "a", q_M8[0]);
        M8.ajouteTransition(q_M8[5], "a", q_M8[1]);
        M8.ajouteTransition(q_M8[5], "b", q_M8[0]);

        //*
        System.out.println(M8);
        M8 = M8.determiniser();
        System.out.println(M8);
        M8 = M8.minimise();
        System.out.println(M8);
        M8 = M8.minimise();
        System.out.println(M8);//*/

        //Automate minimisé dans l'exo
        Automate M9 = new Automate();
        Etat[] q_M9 = {new Etat("q0"), new Etat("q1"), new Etat("q2",true)};
        M9.ajouteTransition(q_M9[0], "a", q_M9[0]);
        M9.ajouteTransition(q_M9[0], "a", q_M9[1]);
        M9.ajouteTransition(q_M9[0], "b", q_M9[0]);
        M9.ajouteTransition(q_M9[1], "b", q_M9[2]);
        M9.ajouteTransition(q_M9[2], "a", q_M9[2]);
        M9.ajouteTransition(q_M9[2], "b", q_M9[2]);
        
        /*
        System.out.println(M9);
        M9 = M9.determiniser();
        System.out.println(M9);
        M9 = M9.minimise();
        System.out.println(M9);//*/

        
    }

}
