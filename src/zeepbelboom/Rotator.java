package zeepbelboom;

/**
 * This class knows the algorithms for all different rotations.
 * Created by user on 15/11/2015.
 * @complexiteit: Alle methodes zijn O(1).
 */
public class Rotator<Key extends Comparable<Key>> {

    private final AbstracteZeepbelboom<Key> b;

    public Rotator(AbstracteZeepbelboom<Key> b) {
        this.b = b;
    }

    /**
     * Bepaalt welke rotatie er uitgevoerd zal worden.
     * PRECONDITIE: Node is de wortel van een zeepbel met minstens 3 nodes.
     *              Uiteraard moet de zeepbel samenhangend zijn.
     *              Indien de wortel (@param node) 2 kinderen heeft,
     *              bevinden deze 2 kinderen zich 2 verschillende zeepbellen waarvan
     *              precies 1 ook de opgegeven node bevat.
     * @param node : de wortel van zijn zeepbel.
     * @throws NullPointerException : Node bevat geen 2 kinderen.
     */
    public void rotate(Node<Key> node) {
        // zeepbel van de opegegeven node
        Zeepbel<Key> zeepbel = node.getZeepbel();

        Node<Key> child;
        // hieronder gaan we uit dat de preconditie geldt. Precies 1 kind zit in dezelfde zeepbel als node.
        child = (node.hasLeft() && zeepbel == node.getLeft().getZeepbel())? node.getLeft() : node.getRight();
        // snelle opvraging van positie van kind tov node. Links = true, Rechts = false.
        boolean posChildisLeft = (node.hasLeft() && zeepbel == node.getLeft().getZeepbel());

        /**
         * Als node, child en grandchild op 1 lijn liggen, kunnen we een enkelvoudige rotatie doen.
         * Zoniet doen we een dubbele. In geval van beide gevallen (zigzag en 1 lijn) gaat de prioriteit naar
         * de dubbele rotatie.
         * Hieronder gaan we dus eerst de prioriteit geven aan het checken op een zigzag.
         */
        if (posChildisLeft) {
            if(child.hasRight() && zeepbel == child.getRight().getZeepbel()) {
                // zigzag: links-rechts
                doubleRotateLeftRight(node);
            } else {
                // 1 lijn
                singleRotateWithClock(node);
            }
        } else {
            if(child.hasLeft() && zeepbel == child.getLeft().getZeepbel()) {
                // zigzag: rechts-links
                doubleRotateRightLeft(node);
            } else {
                // 1 lijn
                singleRotateAntiClock(node);
            }

        }
    }

    /**
     * Rotates anti-clockwise.
     *
     *  (@param node)    becomes     y
     *  /     \                     / \
     * A       y               (node)  C
     *       /  \              /    \
     *      B    C            A     B
     *
     * PRECONDITIE : y en node (in bovenstaand voorbeeld) zijn niet null.
     * @throws NullPointerException: y of node is null.
     * @param node
     */
    public void singleRotateAntiClock(Node<Key> node) {
        Node<Key> rChild = node.getRight();

        if (node.getParent() != null) {
            node.getParent().setChild(rChild);
        } else { // node is de wortel
            b.changeRoot(rChild);
        }
        //
        node.setRight(rChild.getLeft());
        //
        rChild.setLeft(node);
        rChild.getZeepbel().setWortelNode(rChild);
    }

    /**
     * Rotates clockwise. (Zeepbellen worden niet aangepast).
     *
     *      (@param node)    becomes      y
     *      /     \                     /  \
     *    y        C                   A   (node)
     *  /  \                               /    \
     * A    B                             B      C
     *
     * PRECONDITIE : y en node (in bovenstaand voorbeeld) zijn niet null.
     * @throws NullPointerException: y of node is null.
     * @param node
     */
    public void singleRotateWithClock(Node<Key> node) {
        Node<Key> lChild = node.getLeft();

        if (node.getParent() != null) {
            node.getParent().setChild(lChild);
        } else { // node is de wortel
            b.changeRoot(lChild);
        }
        //
        node.setLeft(lChild.getRight());
        //
        lChild.setRight(node);
        lChild.getZeepbel().setWortelNode(lChild);
    }

    /**
     * Double rotates (Left-Right geval in boek AD1).
     * Algemeen voorbeeld:
     *
     *      (@param node)    becomes      y
     *      /     \                     /   \
     *    x        D                   x     (node)
     *  /  \                          / \   /    \
     * A    y                        A  B  C      D
     *     / \
     *   B    C
     *
     * PRECONDITIE : x,y en node (in bovenstaand voorbeeld) zijn niet null.
     * @throws NullPointerException: x,y of node is null.
     */
    public void doubleRotateLeftRight(Node<Key> node) {
        Node<Key> x = node.getLeft();
        singleRotateAntiClock(x);
        singleRotateWithClock(node);
    }

    /**
     * Double rotates (Right-Left geval in boek AD1).
     * Algemeen voorbeeld:
     *
     *      (@param node)    becomes         y
     *      /       \                     /    \
     *     A         x               (node)     x
     *              / \              /    \    / \
     *             y   D            A      B  C   D
     *            / \
     *           B   C
     *
     * PRECONDITIE : x,y en node (in bovenstaand voorbeeld) zijn niet null.
     * @throws NullPointerException: x,y of node is null.
     */
    public void doubleRotateRightLeft(Node<Key> node) {
        Node<Key> x = node.getRight();
        singleRotateWithClock(x);
        singleRotateAntiClock(node);

    }
}
