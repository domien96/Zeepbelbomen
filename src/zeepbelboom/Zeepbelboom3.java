package zeepbelboom;

/**
 * Created by user on 1/11/2015.
 */
public class Zeepbelboom3<Key extends Comparable<Key>> extends AbstracteZeepbelboom<Key>{

    /**
     * Constructor
     * @param cell_size : max zeepbel size
     */
    public Zeepbelboom3(int cell_size) {
        super(cell_size);
    }

    @Override
    protected void balanceer(Node<Key> node) {
        Zeepbel<Key> zpbl = node.getZeepbel();
        if (zpbl.zitOverVol()) {
            RECURSIEF_balanceer(zpbl);
        }
    }

    /**
     *
     * @param zpbl: de zeepbel die geherbalanceerd moet worden.
     */
    protected void RECURSIEF_balanceer(Zeepbel<Key> zpbl) {
        Node<Key> origin = zpbl.getWortelNode().getParent();
        zpbl.balanceer();
        if(origin == null) {
            changeRoot(zpbl.getWortelNode());
        } else {
            origin.setChild(zpbl.getWortelNode());
        }

        if(zpbl.getOuderZeepbel()!=null) {
            int parSize = zpbl.getOuderZeepbel().size();
            int amount = pushInternalKeys(zpbl);
            if(amount+parSize > max_zeepbel_size) {
                RECURSIEF_balanceer(zpbl.getOuderZeepbel());
            }
        } else {
            pushInternalKeys(zpbl);
        }

    }

    /**
     * Duwt alle interne nodes van deze zeepbel naar boven.
     * @return : aantal geduwde sleutels.
     */
    private int pushInternalKeys(Zeepbel<Key> zpbl) {
        Node<Key> curr = zpbl.getWortelNode(),
            left = curr.getLeft(),
            right = curr.getRight();
        assert(isInterneNode(curr));
        int amount = 1;
        splitsEnDuwNaarBoven(curr);
        if(left!=null && isInterneNode(left)) {
            amount += RECURSIEF_pushInternalKeys(left);
        }
        if(right!=null && isInterneNode(right)) {
            amount += RECURSIEF_pushInternalKeys(right);
        }
        return amount;
    }

    /**
     * 
     * @param current : is interne node
     * @return
     */
    private int RECURSIEF_pushInternalKeys(Node<Key> current) {
        int amount = 1;
        splitsEnDuwNaarBoven(current);
        Node<Key> left = current.getLeft(),
                right = current.getRight();
        if(left!=null && isInterneNode(left)) {
            amount += RECURSIEF_pushInternalKeys(left);
        }
        if(right!=null && isInterneNode(right)) {
            amount += RECURSIEF_pushInternalKeys(right);
        }
        return amount;
    }

    /**
     * interne node = kinderen in zelfde zeepbel
     * @param node
     */
    private boolean isInterneNode(Node<Key> node) {
        Zeepbel<Key> zpbl = node.getZeepbel();
        return
                (node.getLeft()!=null && node.getLeft().getZeepbel() == zpbl) &&
                (node.getRight()!=null && node.getRight().getZeepbel() == zpbl);
    }
}
