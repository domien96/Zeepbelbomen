package zeepbelboom;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by user on 25/10/2015.
 */
public interface Zeepbelboom<Key extends Comparable<Key>> extends Collection<Key> {

    /**
     *
     * @return
     */
    Iterator<Zeepbel<Key>> zeepbelIterator();

    /**
     * Geeft sleutel terug in wortel van de onderliggende bnaire zoekboom.
     * @return
     */
    Key getWortelSleutel();

    /**
     * Getter
     * @return : max #elementen in een zeepbel.
     */
    int getMax_zeepbel_size();
}
