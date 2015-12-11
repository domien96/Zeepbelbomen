import zeepbelboom.*;
import zeepbelboom.iterators.ZeepbelNodesIterator;
import zeepbelboom.iterators.ZeepbelboomIterator;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by user on 13/11/2015.
 */
public class Common {

    /**************************
     *  DRAWING METHODEN
     *  Kunnen gebruikt worden om de structuur van een boom weer te geven. (en eventueel na te tekenen op papier)
     *  Handig bij het debuggen.
     *************************/

    /**
     * Geeft de nodes per zeepbel weer (en de parents van die nodes).
     * @param b : de zeepbelboom
     * @param treeNode : WortelNode van b
     * @param drawingName : Naam van de tekening, om makkelijk onderscheid te maken op de console.
     */
    protected static void draw(Zeepbelboom<Integer> b,Node<Integer> treeNode,String drawingName) {
        System.out.println("\nDRAW:"+ drawingName);

        Iterator<Zeepbel<Integer>> it1 = b.zeepbelIterator();
        while(it1.hasNext()) {
            Zeepbel<Integer> zpbl = it1.next();
            System.out.println("Zeepbel : "+zpbl.geefKleur());
            ZeepbelNodesIterator<Integer> it2 = new ZeepbelNodesIterator<>(zpbl.getWortelNode());
            while (it2.hasNext()) {
                Node<Integer> n = it2.next();
                if(n.getParent() == null) {
                    System.out.println("key " + n.getKey() + " | parent : null | ROOT");
                    assertTrue("Meer dan 1 root gevonden, waarde: "+n.getKey()+".\n Er kan maar 1 root in de volledige boom zijn", n == treeNode);
                } else {
                    System.out.println("key " + n.getKey() + " | parent " + n.getParent().getKey());
                }
            }
        }
    }

    /**
     * Geeft van de kleinste node (van waarde) naar de grootste node het volgende weer:
     * Zijn waarde, zijn linkerwaarde, zijn rechterwaarde, zijn parentwaarde.
     * @param root : root van deze boom (of vanwaar je wilt beginnen tekenen).
     * @param drawingName : Naam van de tekening, om makkelijk onderscheid te maken op de console.
     */
    protected static void draw2(Node<Integer> root, String drawingName) {
        System.out.println("\nDRAW 2:"+ drawingName);

        ZeepbelboomIterator<Integer> it = new ZeepbelboomIterator<>(root);
        while(it.hasNext()) {
            Node<Integer> n = it.next();
            System.out.println("key "+n.getKey()+" | P : "+(n.getParent()!=null? n.getParent().getKey() : "ROOT")+" | L : "+(n.getLeft()!= null? n.getLeft().getKey() : "GEEN") +" | R : "+(n.getRight()!=null? n.getRight().getKey() : "GEEN"));
        }
    }

    /**
     * Breedte eerst printing: Print niveau per niveau, startend vanaf wortel.
     * @param root
     * @param drawingName
     */
    protected static void draw3(Node<Integer> root, String drawingName) {
        System.out.println("\nDRAW 3:"+ drawingName);
        new BTreePrinter().printTree(root);

    }

    /**************************
     * SAMENHANGENDHEID TESTEN
     *************************/




    /*****************
     * HULPMETHODES
     *****************/

    /**
     * Kijkt of elke zeepbel samenhangt.
     * @param b
     */
    protected static void checkSamenHangendheid(AbstracteZeepbelboom<Integer> b) {
        // check samenhangendheid
        ////voorbereidingen
        ZeepbelboomIterator<Integer> it2 = new ZeepbelboomIterator<>(b.getWortelNode());
        Map<Zeepbel,Collection<Node<Integer>>> zeepbellenMap = new HashMap<>();
        Node<Integer> curr;
        while(it2.hasNext()) {
            curr= it2.next();
            Zeepbel zpbl = curr.getZeepbel();
            if(!zeepbellenMap.containsKey(zpbl)) {
                zeepbellenMap.put(zpbl, new ArrayList<>());
            }
            zeepbellenMap.get(zpbl).add(curr);
        }

        //// echte controle
        for(Zeepbel z : zeepbellenMap.keySet()) {
            checkHangtSamen(zeepbellenMap.get(z), z);
        }
    }

    /**
     * Kijkt of de gegeven nodes in dezelfde zeepbel zitten, t.t.z. dat ze samenhangen.
     * PRECONDITIE: Alle nodes horen bij dezelfde zeepbel. (Ze hoeven niet samenhangend te zijn).
     */
    private static void checkHangtSamen(Collection<Node<Integer>> nodes, Zeepbel<Integer> zeepbel) {
        Node<Integer> root = zeepbel.getWortelNode();
        assertTrue(root != null);
        Map<Node<Integer>, Boolean> geschrapt = new HashMap<>();
        for(Node n : nodes) {
            assertTrue(n.getZeepbel()==zeepbel);
            geschrapt.put(n,false);
        }

        // Een zeepbel iterator komt perfect overeen met samenhangende nodes in een zeepbel overlopen.
        ZeepbelNodesIterator<Integer> it = new ZeepbelNodesIterator<>(root);
        while (it.hasNext()) {
            geschrapt.put(it.next(),true);
        }

        // Controle
        for (Node<Integer> n : geschrapt.keySet()) {
            assertTrue("Key '"+n.getKey()+"' hangt niet samen met zijn zeepbelwortel", geschrapt.get(n) == true);
        }
    }


    /**************************
     * BALANCERING TESTEN
     *************************/

    /**
     * Kijkt of het zeepbelpad overal even lang is.
     * Zie opgave.
     * Dit is een recursieve functie, als root een blad is dan is de zeepbelpadlengte zowiezo 1.
     * PRECONDITIE: Een bladzeepbel heeft geen kind in een andere zeepbel.
     * @param root : de node waarvan moet gekeken worden of het zeepbelpad linsk even lang is als die van rechts.
     * @Throws AssertionError: Als de gegeven sleutel niet voldoet aan de balanceringsstructuur.
     */
    protected static int checkZeepbelBalancering(Node<Integer> root) {
        int padLength = RECURSIEF_berekenLengtepad(root);
        System.out.println("Lengte v/h zeepbelpad = " + padLength);
        return padLength;
    }

    /**
     * Berekent de lengte van het zeepbelpad.
     * @param node
     * @Throws AssertionError: Als de gegeven sleutel niet voldoet aan de balanceringsstructuur.
     */
    private static int RECURSIEF_berekenLengtepad(Node<Integer> node) {
        if(node == null) {
            // Dit is een bladzeepbel en deze bel telt dus als 1 stap.
            return 1;
        } else if (node.isBlad()) {
            // Dit is een bladzeepbel en deze bel telt dus als 1 stap.
            return 1;
        } else {
            Node<Integer> left = node.getLeft(), right = node.getRight();
            int lengteViaLinks = RECURSIEF_berekenLengtepad(left);
            if (left != null && node.getZeepbel() != left.getZeepbel()) {
                lengteViaLinks++;
            }
            int lengteViaRechts = RECURSIEF_berekenLengtepad(right);
            if (right != null && node.getZeepbel() != right.getZeepbel()) {
                lengteViaRechts++;
            }

            assertTrue("Balancering niet correct voor sleutel : "+node.getKey(),lengteViaLinks==lengteViaRechts);
            return lengteViaLinks; // of lengteViaRechts
        }
    }

    /***************
     *    ANDERE   *
     ***************/

    /**
     * Geeft de eerste node terug die correspondeert met de opgegeven key/waarde.
     * Null indien geen zo'n node gevonden. (Maw k bevindt zich niet in deze zeepbelboom).
     * @param k : waarde van de sleutel
     * @return : de eerste node die de key met waarde k bevat, null indien niet gevonden.
     * @throws NullPointerException : if specified element is null.
     */
    protected static Node<Integer> findKey(AbstracteZeepbelboom<Integer> b, Integer k) {
        if(b.isEmpty()) {
            return null;
        }
        Node<Integer> current = b.getWortelNode();
        while (current != null && current.geefAantalKinderen() != 0) {
            if (current.getKey().compareTo(k) < 0) {
                current = current.getRight();
            } else if (current.getKey().compareTo(k) > 0) {
                current = current.getLeft();
            } else {
                // they are equal
                return current;
            }
        }

        if (current != null && current.getKey().compareTo(k) == 0) {
            return current;
        } else {
            return null;
        }
    }
}
