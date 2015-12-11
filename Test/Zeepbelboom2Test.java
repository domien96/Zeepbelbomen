import zeepbelboom.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Testen voor balanceringsvariant 2.
 * Created by user on 31/10/2015.
 */
public class Zeepbelboom2Test extends Common {

    public static void main(String[] args) {
        toevoegen1_balance1();
        toevoegen2_balance1();
        toevoegen3_balance1();
        contains1();
        small_rotate_test1();
    }

    private static void toevoegen3_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(3);
        for (int i=0; i<=10000;i++) {
            b.add(i);
        }

        // check sorting
        //assertTrue(b.getWortelSleutel() == 262143);
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        String check = "" ;
        for (int i=0;i<=10000;i++) {
            check += i;
        }
        assertTrue(s.equals(check));

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());

        // check samenhangendheid
        checkSamenHangendheid(b);
    }

    private static void small_rotate_test1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(3);
        b.add(60);
        b.add(70);
        b.add(65);
        b.add(63);
        // check samenhangendheid
        checkSamenHangendheid(b);

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void contains1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(4);
        Integer[]a = {30,20,50,45,56,53,58,57,59,15,12,18,25,22,28,27,29,21,24,23};
        List<Integer> alist = Arrays.asList(a);
        b.addAll(alist);
        boolean contains;
        for (int i=0 ; i<60;i++) {
            contains=alist.contains(i);
            assertTrue(b.contains(i) == contains);
        }

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void toevoegen2_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(4);
        int[]a = {30,20,50,45,56,53,58,57,59,15,12,18,25,22,28,27,29,21,24,23};
        for (Integer i : a) {
            try {
                b.add(i);
            } catch (Exception e) {
                System.out.println("Fout bij toevoegen : "+i);
                e.printStackTrace();
                System.exit(-1);
            }
        }

        // check sorting
        assertTrue(b.getWortelSleutel() == 25);
        Iterator<Integer> it = b.iterator();
        String s = "";
        while (it.hasNext()) {
            s +=(it.next());
        }
        assertTrue(s.equals("1215182021222324252728293045505356575859"));

        // check samenhangendheid
        checkSamenHangendheid(b);

        // balancering checken
        checkZeepbelBalancering(b.getWortelNode());
    }

    private static void toevoegen1_balance1() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(4);
        int[]a = {59,50,30,20,100,120,130,140,150};
        for (Integer i : a) {
            b.add(i);
        }

        // check sorting
        assertTrue(b.getWortelSleutel() == 50);
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