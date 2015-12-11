import zeepbelboom.Zeepbelboom3;

/**
 * Created by user on 29/11/2015.
 */
public class Zeepbelboom3Test extends Common{
    public static void main(String[] args) {
        toevoegen1_balance3();
    }

    private static void toevoegen1_balance3() {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName());
        Zeepbelboom3<Integer> b = new Zeepbelboom3<>(4);
        int[]a = {30,20,50,45,56,53,58,57,59,15,12,18,25,22,28,27,29,21,24,23};
        for (Integer i : a) {
            b.add(i);
            draw3(b.getWortelNode(),i.toString());
        }
        draw(b,b.getWortelNode(),"lol");
        // Visual check OK
    }
}
