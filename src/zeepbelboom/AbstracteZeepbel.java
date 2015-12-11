package zeepbelboom;

import javax.naming.LimitExceededException;

/**
 * Created by user on 27/10/2015.
 */
public abstract class AbstracteZeepbel<Key extends Comparable<Key>> implements Zeepbel<Key> {

    /**
     * Stelt de meest recente(nieuwe) kleur voor.
     * kleur is voorgesteld als een integer
     */
    private static int huidigeZeepbelKleur = 0;

    public static int getHuidigeZeepbelKleur() {
        return huidigeZeepbelKleur;
    }

    protected static int getVolgendeZeepbelIndex() {
        if (huidigeZeepbelKleur== Integer.MAX_VALUE) {
            huidigeZeepbelKleur = Integer.MIN_VALUE;
        } else if (huidigeZeepbelKleur == -1){
            throw new ZeepbellenLimitError("Maximum aantal zeepbellen bereikt.");
        }
        huidigeZeepbelKleur++;
        return huidigeZeepbelKleur;
    }
}
