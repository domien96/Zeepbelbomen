package zeepbelboom;

import zeepbelboom.iterators.ZeepbelNodesIterator;

import java.util.Arrays;

/**
 * Created by user on 31/10/2015.
 */
public class Zeepbelboom2<Key extends Comparable<Key> > extends AbstracteZeepbelboom<Key> {

    /**
     * Constructor.
     * @param cell_size : maximum size of a cell/zeepbel. Needs to be bigger or equal to 2, otherwise value will be set to 2.
     */
    public Zeepbelboom2(int cell_size) {
        super(cell_size);
    }

    /**
     * Balanceert de zeepbel waarin (@param node) is toegevoegd.
     * PRECONDITIE : De node bevindt zich in een bladzeepbel.
     * @complexiteit: O(3k*log n) = O(k*log n)
     */
    @Override
    protected void balanceer(Node<Key> node) {
        Zeepbel<Key> ouderZeepbel = node.getZeepbel().getOuderZeepbel();
        // kijkt of we later nog eens zullen moeten balanceren op de ouderzeepbel erboven.
        boolean balanceerLaterNogEens = ouderZeepbel==null? false : ! ouderZeepbel.heeftPlaats();
        // De node die later gebalanceerd zal worden, indien nodig.
        Node<Key> balanceerLater;
        //

        if(node.getZeepbel().zitOverVol()) {
            Node<Key> wortelHuidigeZeepbel = node.getZeepbel().getWortelNode();
            Node<Key> origin = wortelHuidigeZeepbel.getParent();

            node.getZeepbel().balanceer();  // O(k)
            Node<Key> newRoot= node.getZeepbel().getWortelNode();
            if (origin == null) {
                // this is the root

                changeRoot(newRoot);
                splitsEnDuwNaarBoven(newRoot);  // O(k)

            } else {
                origin.setChild(newRoot);
                splitsEnDuwNaarBoven(newRoot); // O(k)

                if(balanceerLaterNogEens) {
                    balanceer(newRoot);
                }
            }
        } else {
            // do nothing
        }
    }

}
