package zeepbelboom;

/**
 * Created by user on 25/10/2015.
 */
public class Zeepbelboom1<Key extends Comparable<Key>> extends AbstracteZeepbelboom<Key> implements Zeepbelboom<Key> {

    /**
     * Constructor.
     * @param cell_size : maximum size of a cell/zeepbel. Needs to be bigger or equal to 2, otherwise value will be set to 2.
     */
    public Zeepbelboom1(int cell_size) {
        super(cell_size);
    }

    /**
     * Balanceert de zeepbel waarin (@param node) is toegevoegd.
     * @Complexiteit: O(k*log n), n de diepte in zeepbellen, k de zeepbel-constante.
     */
    protected void balanceer(Node<Key> node) {
        if(node.getZeepbel().zitOverVol()) {
            Zeepbel<Key> zeepbel = node.getZeepbel();
            Zeepbel<Key> ouderZeepbel = zeepbel.getOuderZeepbel();
            Node<Key> wortelHuidigeZeepbel = node.getZeepbel().getWortelNode();

            // kijkt of we later nog eens zullen moeten balanceren op de ouderzeepbel erboven.
            boolean balanceerLaterNogEens = ouderZeepbel==null? false : ! ouderZeepbel.heeftPlaats();
            // De node die later gebalanceerd zal worden, indien nodig.
            Node<Key> balanceerLater;
            //

            int childrenAmount = wortelHuidigeZeepbel.geefAantalKinderen();
            Node<Key> leftWortel = wortelHuidigeZeepbel.getLeft(),
                      rightWortel = wortelHuidigeZeepbel.getRight();
            if(childrenAmount==2 && leftWortel.getZeepbel()==rightWortel.getZeepbel()) {

                balanceerLater = wortelHuidigeZeepbel;
                splitsEnDuwNaarBoven(wortelHuidigeZeepbel); // O(k)

            } else if (childrenAmount==1 || childrenAmount==2 && leftWortel.getZeepbel()!=rightWortel.getZeepbel()) {
                rotator.rotate(wortelHuidigeZeepbel);
                balanceerLater = zeepbel.getWortelNode();
                splitsEnDuwNaarBoven(zeepbel.getWortelNode()); // O(k)
            } else {
                // amount == 0 , this can never be true.
                throw new AssertionError("BUG : Wortel van huidige zeepbel zou normaal steeds minstens 1 kind moeten hebben.");
            }

            if (balanceerLaterNogEens) {
                balanceer(balanceerLater);
            }
        } else {
            // do nothing
        }
    }

}
