package zeepbelboom.iterators;

import zeepbelboom.AbstracteZeepbel;
import zeepbelboom.Node;
import zeepbelboom.Zeepbel;

import java.util.Iterator;

/**
 * Itereert over alle zeepbellen in de boom.
 * Typre paramater "Key" : Class of the keys of the elements that are within the cells ( = zeepbellen).
 * Created by user on 31/10/2015.
 */
public class ZeepbelIterator<Key extends Comparable<Key>> implements Iterator<Zeepbel<Key>> {

    private final ZeepbelboomIterator<Key> it;
    /**
     * De waarde op index I betekent: De zeepbel met kleur I(kleur is voorgesteld als een getal) is bezocht.
     * Automatisch staat alles op False.
     */
    private final boolean[] visited;

    /**
     * De volgende zeepbel wordt op voorhand gezocht en in deze variabele opgeslagen.
     * @param root
     */
    Zeepbel<Key> next;

    public ZeepbelIterator(Node<Key> root) {
        this.it = new ZeepbelboomIterator<>(root);
        visited = new boolean[AbstracteZeepbel.getHuidigeZeepbelKleur()+1];
        findNext();
    }

    private void findNext() {
        Node<Key> curr;
        while(it.hasNext()) {
            curr = it.next();
            if(!visited[curr.getZeepbel().geefKleur()]) {
                visited[curr.getZeepbel().geefKleur()] = true;
                assert(curr == curr.getZeepbel().getWortelNode());
                next = curr.getZeepbel();
                return;
            }
        }
        next = null;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public Zeepbel<Key> next() {
        Zeepbel<Key> old = next;
        findNext();
        return old;
    }
}
