/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automate;

import java.util.ArrayList;

public class Test {

    public static void main(String[] arg) {
        ArrayList<Transition> delta = new ArrayList<Transition>();
        delta.add(new Transition(0, "a", 1));
        delta.add(new Transition(0, "b", 0));
        delta.add(new Transition(1, "a", 2));
        delta.add(new Transition(1, "b", 2));
        int[] finaux = {2};
        Automate M = new Automate(3, delta, finaux);
        System.out.println(M);
        System.out.println("M.estDeterministe() = " + M.estDeterministe() + "\n");

        /*
         char caract = 'a';
      
         for(int i=0; i<3; i++){
         caract += i;
         for(int j=0; j<M.ensemble_etats.length; j++){
         System.out.println("d("+M.ensemble_etats[j]+","+caract+") existe?  -> "+M.existeTransition(M.ensemble_etats[j],caract+""));
         }
         }/*//*/
         System.out.println("M.estComplet() = " + M.estComplet() + "\n");
         M.completer();
         System.out.println(M);
         System.out.println("M.estComplet() = " + M.estComplet() + "\n");
         /*
         System.out.println("M.accepte(\"bbbab\") = "+M.accepte("bbbab"));
         System.out.println("M.accepte(\"cbbbab\") = "+M.accepte("cbbbab"));
         System.out.println("M.accepte(\"aaa\") = "+M.accepte("aaa"));
         //*/
        ArrayList<String> test = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        test.add("a");
        test.add("b");
        test.add("c");
        test.add("#");

        tmp.addAll(test);

        for (int i = 0; i < 3; i++) {
            ArrayList<String> tmp2 = new ArrayList<>();
            for (int j = 0; j < tmp.size(); j++) {
                tmp2.add(tmp.get(j) + 'a');
                tmp2.add(tmp.get(j) + 'b');
                tmp2.add(tmp.get(j) + 'c');
                tmp2.add(tmp.get(j) + '#');
            }
            tmp = tmp2;
            test.addAll(tmp);
        }

        System.out.println(afficheL(test));

        /*
        for (int j = 0; j < test.size(); j++) {
            System.out.println("M.accepte(\"" + test.get(j) + "\") = " + M.accepte(test.get(j)));
        }
        */
        
        /*
        System.out.println("M.accepte(\"a#\") = " + M.accepte("a#"));
        System.out.println("M.accepte(\"b#\") = " + M.accepte("b#"));
        System.out.println("M.accepte(\"#a\") = " + M.accepte("#a"));
        System.out.println("M.accepte(\"#b\") = " + M.accepte("#b"));
                //*/
        
        ArrayList<Transition> delta2 = new ArrayList<Transition>();
        delta2.add(new Transition(0, "a", 0));
        delta2.add(new Transition(0, "b", 0));
        delta2.add(new Transition(0, "c", 1));
        delta2.add(new Transition(1, "a", 1));
        delta2.add(new Transition(1, "b", 1));
        delta2.add(new Transition(1, "c", 1));
        int[] finaux2 = {1};
        Automate M2 = new Automate(2, delta2, finaux2);
        System.out.println(M2);
        
                
        for (int j = 0; j < test.size(); j++) {
            System.out.println("M2.accepte(\"" + test.get(j) + "\") = " + M2.accepte(test.get(j)));
        }

        
    }

    public static String afficheL(ArrayList<String> list) {
        String s2 = "{";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                s2 += list.get(i) + ",";
            } else {
                s2 += list.get(i) + "}";
            }
        }
        return s2;
    }
}
