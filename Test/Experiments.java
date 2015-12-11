import zeepbelboom.Zeepbelboom1;
import zeepbelboom.Zeepbelboom2;
import zeepbelboom.Zeepbelboom3;

import java.util.Random;

/**
 * Created by user on 29/11/2015.
 */
public class Experiments {
    private static int maxInputValue = 100000;
    
    public static void main(String[] args) {
        int[] random10000= makeRandomValues(10000);
        int[] random100000= makeRandomValues(100000);
        int[] random1000000= makeRandomValues(1000000);
        int[] random10000000= makeRandomValues(10000000);
        System.out.println("VARIANT 1");
        eenTweeDrieEnz_bal1(10000, 3);
        eenTweeDrieEnz_bal1(100000, 3);
        eenTweeDrieEnz_bal1(1000000, 3);
        eenTweeDrieEnz_bal1(10000000, 3);
        array_bal1(random10000,3);
        array_bal1(random100000,3);
        array_bal1(random1000000,3);
        array_bal1(random10000000,3);

        System.out.println("nVARIANT 2");
        eenTweeDrieEnz_bal2(10000, 3);
        eenTweeDrieEnz_bal2(100000, 3);
        eenTweeDrieEnz_bal2(1000000, 3);
        eenTweeDrieEnz_bal2(10000000, 6);
        array_bal2(random10000, 3);
        array_bal2(random100000, 3);
        array_bal2(random1000000, 3);
        array_bal2(random10000000, 3);

        System.out.println("\nVARIANT 1");
        eenTweeDrieEnz_bal3(10000, 3);
        eenTweeDrieEnz_bal3(100000, 3);
        eenTweeDrieEnz_bal3(1000000, 3);
        eenTweeDrieEnz_bal3(10000000, 3);
        array_bal3(random10000, 3);
        array_bal3(random100000, 3);
        array_bal3(random1000000, 3);
        array_bal3(random10000000, 3);
        
    }

    private static void array_bal1(int[] arr, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName() 
                + "; k="+k+" n="+arr.length+"->");
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(k);

        long start = System.currentTimeMillis();
        for (int i:arr) {
            b.add(i);
        }
        long end = System.currentTimeMillis();

        System.out.print(end - start);
    }

    private static void array_bal2(int[] arr, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName()
                + "; k="+k+" n="+arr.length+"->");
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(k);

        long start = System.currentTimeMillis();
        for (int i:arr) {
            b.add(i);
        }
        long end = System.currentTimeMillis();

        System.out.print(end - start);
    }

    private static void array_bal3(int[] arr, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName()
                + "; k="+k+" n="+arr.length+"->");
        Zeepbelboom3<Integer> b = new Zeepbelboom3<>(k);

        long start = System.currentTimeMillis();
        for (int i:arr) {
            b.add(i);
        }
        long end = System.currentTimeMillis();

        System.out.print(end - start);
    }

    private static int[] makeRandomValues(int amount) {
        int[] res = new int[amount];
        for(int i=0;i<amount;i++) {
            res[i] = new Random().nextInt(amount);
        }
        return res;
    }


    private static void eenTweeDrieEnz_bal1(int inputgrootte, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName() + "; k="+k+" n="+inputgrootte+"->");
        Zeepbelboom1<Integer> b = new Zeepbelboom1<>(k);

        long start = System.currentTimeMillis();
        for (int i=1; i<=inputgrootte;i++) {
            b.add(i);
        }
        long end = System.currentTimeMillis();

        System.out.print(end - start);
        //System.out.println(b.getWortelNode().getKey()+"root");
    }

    private static void eenTweeDrieEnz_bal2(int inputgrootte, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName() + "; k="+k+" n="+inputgrootte+"->");
        Zeepbelboom2<Integer> b = new Zeepbelboom2<>(k);

        long start = System.currentTimeMillis();
        for (int i=0; i<=inputgrootte;i++) {
            b.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.print(end - start);
    }

    private static void eenTweeDrieEnz_bal3(int inputgrootte, int k) {
        System.out.println("\nTEST:" + new Object(){}.getClass().getEnclosingMethod().getName() + "; k="+k+" n="+inputgrootte+"->");
        Zeepbelboom3<Integer> b = new Zeepbelboom3<>(k);

        long start = System.currentTimeMillis();
        for (int i=0; i<=inputgrootte;i++) {
            b.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.print(end - start);
    }
}
