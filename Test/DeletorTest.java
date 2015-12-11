import zeepbelboom.MijnZeepbel;
import zeepbelboom.Node;
import zeepbelboom.Zeepbelboom1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;
/**
 * Created by user on 24/11/2015.
 */
public class DeletorTest extends Common{
    public static void main(String[] args) {
        Case2Metlinks11();
        Case2MetlinksXX();
        Case3MetLinks11();
        Case3MetLinks1X();
        Case3MetLinksX1();
        Case3MetLinksXX();

        RecursiveDeleteTest1();
        RecursiveDeleteTest2();
        RecursiveDeleteTest3();
        RecursiveDeleteTest4();
    }

    private static void RecursiveDeleteTest1() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(2);
        for(int i=0; i<30;i++) {
            b.add(i);
        }
        b.remove(29);b.remove(28);
        Node<Integer> node25 = findKey(b,25);
        assertNotNull(node25);
        assertTrue(node25.getKey() == 25);

        Node<Integer> node27 = node25.getRight();
        assertNotNull(node27);
        assertTrue(node27.getKey() == 27);

        Node<Integer> node26 = node27.getLeft();
        assertNotNull(node26);
        assertTrue(node26.getKey() == 26);

        assertFalse(node25.inZelfdeZeepbelAls(node27));
        assertTrue(node27.inZelfdeZeepbelAls(node26));
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());

    }

    private static void RecursiveDeleteTest2() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(2);
        for(int i=0; i<31;i++) {
            b.add(i);
        }
        b.remove(30);
        Node<Integer> node29 = findKey(b, 29);
        assertTrue(node29.inZelfdeZeepbelAls(node29.getLeft()));
        assertFalse(node29.inZelfdeZeepbelAls(node29.getParent()));
        assertTrue(node29.getLeft().getKey()== 28);
        assertTrue(node29.getParent().getKey()== 27);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());

    }

    private static void RecursiveDeleteTest3() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(2);
        for(int i=0; i<31;i++) {
            b.add(i);
        }
        b.remove(29);
        Node<Integer> node30 = findKey(b, 30);
        assertTrue(node30.inZelfdeZeepbelAls(node30.getLeft()));
        assertFalse(node30.inZelfdeZeepbelAls(node30.getParent()));
        assertTrue(node30.getLeft().getKey()== 28);
        assertTrue(node30.getParent().getKey()== 27);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());

    }

    private static void RecursiveDeleteTest4() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(2);
        for(int i=0; i<31;i++) {
            b.add(i);
        }
        b.remove(28);
        Node<Integer> node29 = findKey(b, 29);
        assertNull(node29.getLeft());
        assertTrue(node29.inZelfdeZeepbelAls(node29.getRight()));
        assertFalse(node29.inZelfdeZeepbelAls(node29.getParent()));
        assertTrue(node29.getRight().getKey()== 30);
        assertTrue(node29.getParent().getKey()== 27);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());

    }

    private static void Case3MetLinksXX() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(6);
        int[] a = {20,10,9,11,50,60,70,55,56,57,52,51,53,54,40,39,45,44,43,47,46,48};
        for(int i : a) {
            b.add(i);
        }
        b.add(54); b.add(54); b.add(54);
        b.remove(43);b.remove(44);b.remove(46);b.remove(47);b.remove(48);
        b.remove(51);
        Node<Integer> node40 = findKey(b,40);

        assertNotNull(node40);
        assertTrue(node40.getKey() == 40);
        assertTrue(node40.getRight().getKey() == 52);
        Node<Integer> node50 = node40.getRight().getLeft();
        assertTrue(node50.getKey() == 50);
        assertTrue(node40.getLeft().isBlad());
        assertNull(node50.getRight());
        assertTrue(node50.getLeft().getKey() == 45);
        assertTrue(node50.getLeft().isBlad());
        assertNotNull(node50.getLeft().getParent());

        assertTrue(node40.inZelfdeZeepbelAls(node40.getParent()));
        assertTrue(node40.inZelfdeZeepbelAls(node40.getRight()));
        assertTrue(node50.inZelfdeZeepbelAls(node50.getLeft()));
        assertFalse(node40.inZelfdeZeepbelAls(node50));
        assertFalse(node40.inZelfdeZeepbelAls(node50));
        assertFalse(node50.inZelfdeZeepbelAls(node50.getRight()));
        assertFalse(node50.inZelfdeZeepbelAls(node50.getParent()));

        assertTrue(node40.getParent().getKey() == 55);
        assertTrue(node40.getParent().getRight().getKey() == 60);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void Case3MetLinksX1() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(5);
        for(int i =31;i>=6;i--) {
            b.add(i);
        }
        b.add(2); b.add(1); b.add(3); b.add(4); b.add(5);b.add(0);
        b.remove(7);
        Node<Integer> node5 = findKey(b,5);

        assertNotNull(node5);
        assertTrue(node5.getKey() == 5);
        assertTrue(node5.getRight().getKey() == 6);
        Node<Integer> node2 = node5.getLeft();
        assertTrue(node2.getKey() == 2);
        assertTrue(node5.getLeft().geefAantalKinderen() == 2);
        assertTrue(node2.getRight().getKey() == 3);
        assertTrue(node2.getRight().getRight().getKey() == 4);

        assertTrue(node5.inZelfdeZeepbelAls(node2));
        assertTrue(node5.inZelfdeZeepbelAls(node5.getParent()));
        assertTrue(node2.getRight().inZelfdeZeepbelAls(node2.getRight().getRight()));
        assertFalse(node2.inZelfdeZeepbelAls(node2.getRight()));
        assertFalse(node5.inZelfdeZeepbelAls(node5.getRight()));

        assertTrue(node5.getRight().isBlad());
        assertTrue(node5.getParent().getKey() == 8);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void Case3MetLinks1X() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(5);
        for(int i =0;i<28;i++) {
            b.add(i);
        }
        b.add(29); b.add(28); b.add(30); b.add(31); b.add(32);b.add(33);
        b.remove(28);
        Node<Integer> node29 = findKey(b,29);

        assertNotNull(node29);
        assertTrue(node29.getKey() == 29);
        assertTrue(node29.getRight().getKey() == 30);
        assertTrue(node29.getLeft().getKey() == 27);
        assertTrue(node29.getLeft().geefAantalKinderen() == 1);
        assertTrue(node29.getLeft().getLeft().getKey() == 26);
        assertTrue(node29.getLeft().getLeft().isBlad());
        assertTrue(node29.getParent().getKey() == 25);
        assertFalse(node29.inZelfdeZeepbelAls(node29.getLeft()));
        assertFalse(node29.inZelfdeZeepbelAls(node29.getRight()));
        assertTrue(node29.inZelfdeZeepbelAls(node29.getParent()));
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void Case3MetLinks11() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(5);
        for(int i =0;i<=31;i++) {
            b.add(i);
        }
        b.remove(31);b.remove(30);b.remove(29); // no special case removals
        b.remove(28);
        Node<Integer> node27 = findKey(b,27);

        assertNotNull(node27);
        assertTrue(node27.getKey() == 27);
        assertTrue(node27.getRight() == null);
        assertTrue(node27.getLeft().getKey() == 26);
        assertTrue(node27.getParent().getKey() == 25);
        assertTrue(node27.inZelfdeZeepbelAls(node27.getLeft()));
        assertFalse(node27.inZelfdeZeepbelAls(node27.getParent()));
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void Case2MetlinksXX() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(4);
        int a[]= {20,30,19,25,50,49,27,24,23,22,21,51,52,53,54,29,26,28};
        for(int i: a) {
            b.add(i);
        }
        b.remove(49);
        Node<Integer> root = b.getWortelNode();
        assertTrue(root.getKey() == 20);
        assertTrue(root.getRight().getKey() == 29);
        Node<Integer> dertig = root.getRight().getRight().getLeft();
        assertTrue(dertig.getKey() == 30);
        assertTrue(dertig.geefAantalKinderen() == 0);
        Node<Integer> zevenentwintig = root.getRight().getLeft().getRight();
        assertTrue(zevenentwintig.getKey() == 27);
        assertTrue(zevenentwintig.hasLeft() && zevenentwintig.getLeft().isBlad());
        assertTrue(zevenentwintig.getLeft().getKey() == 26);
        assertTrue(zevenentwintig.hasRight() && zevenentwintig.getRight().isBlad());
        assertTrue(zevenentwintig.getRight().getKey() == 28);
        //
        checkSamenHangendheid(b);
        checkZeepbelBalancering(root);
    }

    /**
     * LCA positie , case 11
     */
    private static void Case2Metlinks11() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(2);
        int a[]= {19,20,30,50,22,24};
        for(int i: a) {
            b.add(i);
        }
        b.remove(50);
        Node<Integer> root = b.getWortelNode();
        assertTrue(root.getKey() == 20);
        assertTrue(root.getRight().getKey() == 24);
        checkSamenHangendheid(b);
        assertTrue(checkZeepbelBalancering(root) == 2);
    }
}
