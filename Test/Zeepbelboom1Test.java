import zeepbelboom.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Testen voor balanceringsvariant 1.
 * Created by user on 31/10/2015.
 */
public class Zeepbelboom1Test extends Common {
    public static void main(String[] args) {
        toevoegen1_balance1();
        toevoegen2_balance1();
        toevoegen3_balance1();
        verwijderen1_balance1();
        verwijderen_case1();
        contains1();
        small_rotate_test1();

    }

    private static void toevoegen3_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(3);
        int[] a = {1,2,3,4,5,6,7,8,9,10};
        for (int i : a){
            b.add(i);
        }

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
        // check sorting
        assertTrue(b.getWortelSleutel() == 4);
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        assertTrue(s.equals("12345678910"));
    }

    private static void verwijderen_case1() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(5);
        for (int i=1; i<=32;i++) {
            //System.out.println("adding "+i);
            b.add(i);
        }
        //draw3(b.getWortelNode(),"lol");
    }

    /**
     * Verwijderen 16
     */
    private static void verwijderen1_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(5);
        int[] a = {50,30,60,25,40,55,70,15,26,65,80,10,16,75,90,9,11,85,100,95,110};
        for (int i : a){
            b.add(i);
        }
        //
        b.remove(16);
        // check iteration
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        assertTrue(s.equals("91011152526304050556065707580859095100110"));
        //
        checkSamenHangendheid(b);
        //
        Node<Integer> node15 = b.getWortelNode().getLeft().getLeft().getLeft();
        assertTrue(node15.getKey() == 15);
        assertTrue(node15.getRight() == null);
        assertTrue(node15.getLeft().getKey() == 10);
        assertTrue(node15.getParent().getKey() == 25);
        assertTrue(node15.getParent().getRight().getKey() == 26);

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void small_rotate_test1() {
        System.out.println("TEST:" + new Object() {}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(3);
        b.add(60);
        b.add(70);
        b.add(65);
        b.add(63);
        // check samenhangendheid
        checkSamenHangendheid(b);
    }

    private static void contains1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(4);
        Integer[]a = {30,20,50,45,56,53,58,57,59,15,12,18,25,22,28,27,29,21,24,23};
        List<Integer> alist = Arrays.asList(a);
        b.addAll(alist);
        boolean contains;
        for (int i=0 ; i<60;i++) {
            contains=alist.contains(i);
            assertTrue(b.contains(i) == contains);
        }
    }

    private static void toevoegen2_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(4);
        int[]a = {30,20,50,45,56,53,58,57,59,15,12,18,25,22,28,27,29,21,24,23};
        for (Integer i : a) {
            b.add(i);
        }

        // check sorting
        assertTrue(b.getWortelSleutel() == 30);
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        assertTrue(s.equals("1215182021222324252728293045505356575859"));

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void toevoegen1_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(4);
        int[]a = {59,50,30,20,100,120,130,140,150};
        for (Integer i : a) {
            b.add(i);
        }
        //draw3(b.getWortelNode(),"draw3");
        // check sorting
        assertTrue(b.getWortelSleutel() == 59);
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        assertTrue(s.equals("20305059100120130140150"));

        // check samenhangendheid
        checkSamenHangendheid(b);

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }
}