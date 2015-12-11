import zeepbelboom.Node;
import zeepbelboom.Zeepbelboom1;

import static org.junit.Assert.*;

/**
 * Created by user on 21/11/2015.
 */
public class ZeepbelTests extends Common {
    public static void main(String[] args) {
        geefBroerzeepbelTest1();
        geefBroerzeepbelTest2();
    }

    private static void geefBroerzeepbelTest2() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1(3);
        int a[] = new int[]{20,30,70,100,50,40,60,55};
        for (int i: a) {
            b.add(i);
        }
        Node<Integer> root = b.getWortelNode();
        // linker
        assertTrue(findKey(b,21) == null);
        assertTrue(findKey(b, 30).getZeepbel().geefLinkerBroerzeepbel() == null);
        assertTrue(findKey(b, 20).getZeepbel().geefLinkerBroerzeepbel() == null);
        assertTrue(findKey(b,40).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 20);
        assertTrue(findKey(b,60).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 40);
        assertTrue(findKey(b,100).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 60);
        // rechter
        assertTrue(findKey(b, 30).getZeepbel().geefRechterBroerzeepbel() == null);
        assertTrue(findKey(b,100).getZeepbel().geefRechterBroerzeepbel() == null);
        assertTrue(findKey(b,60).getZeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 100);
        assertTrue(findKey(b,40).getZeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 60);
        assertTrue(findKey(b,20).getZeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 40);
    }

    private static void geefBroerzeepbelTest1() {
        Zeepbelboom1<Integer> b = new Zeepbelboom1(5);
        for(int i=0; i<=32;i++) {
            b.add(i);
        }
        //draw(b,b.getWortelNode(),"zeepbellen");
        Node<Integer> root = b.getWortelNode();
        // Ik mag hier casten, want om te testen ben ik natuurlijk op de hoogte van mijn eigen implementatie.
        assertTrue(findKey(b,21).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 17);
        assertTrue(findKey(b, 21).getZeepbel().geefRechterBroerzeepbel() == null);
        assertTrue(findKey(b,9).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 5);
        assertTrue(findKey(b,9).getZeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 13);
        assertTrue(findKey(b,9).getZeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 17);
        assertTrue(findKey(b,9).getZeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel().getWortelNode().getKey() == 21);
        assertTrue(findKey(b,9).getZeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel().geefRechterBroerzeepbel() == null);
        assertTrue(findKey(b,2).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 0);
        assertTrue(findKey(b,2).getZeepbel().geefRechterBroerzeepbel() == null);
        assertTrue(findKey(b,20).getZeepbel().geefLinkerBroerzeepbel() == null);
        assertTrue(findKey(b,14).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 12);
        assertTrue(findKey(b,13).getZeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 9);
        assertTrue(findKey(b,13).getZeepbel().geefLinkerBroerzeepbel().geefLinkerBroerzeepbel().getWortelNode().getKey() == 5);
    }



}
