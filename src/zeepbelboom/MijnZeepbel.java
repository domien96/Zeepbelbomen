package zeepbelboom;

import zeepbelboom.iterators.ZeepbelNodesIterator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by user on 25/10/2015.
 */
public class MijnZeepbel<Key extends Comparable<Key>> extends AbstracteZeepbel<Key> {
    int maxSize;
    int color; // kleur voorgesteld als unieke int.
    Node<Key> root;

    public MijnZeepbel(int size, Node<Key> root) {
        assert(root != null);
        this.maxSize = size>=2? size : 2; // default waarde : 2
        this.color = getVolgendeZeepbelIndex();
        this.root = root;
    }

    @Override
    public Key getWortelSleutel() {
        assert(root != null);
        return root.getKey();
    }

    @Override
    public Node<Key> getWortelNode() {
        return root;
    }

    public void setWortelNode(Node<Key> root) {
        root.setZeepbel(this);
        this.root = root;
    }

    @Override
    public Zeepbel<Key> getOuderZeepbel() {
        Zeepbel<Key> zeepbel;
        if (root.getParent() == null) {
            zeepbel = null;
        } else {
            zeepbel = root.getParent().getZeepbel();
        }
        return zeepbel;
    }

    @Override
    public boolean zitOverVol() {
        return size() > maxSize;
    }

    @Override
    public boolean heeftPlaats() {
        return size() < maxSize ;
    }

    @Override
    public void splits() {
        assert(root.geefAantalKinderen() == 2);
        ZeepbelNodesIterator<Key> it = new ZeepbelNodesIterator<>(root);
        Node<Key> curr = it.next();
        Zeepbel<Key> nieuweZeepbel = new MijnZeepbel<>(maxSize,root.getLeft());
        while(curr != root) {
            curr.setZeepbel(nieuweZeepbel);
            curr = it.next();
        }
    }

    @Override
    public int geefKleur() {
        return this.color;
    }

    public int size() {
        int count = 0;
        Iterator<Key> it = iterator();
        while(it.hasNext()) {
            count++;
            it.next();
        }
        return count;
    }

    @Override
    public Zeepbel<Key> geefLinkerBroerzeepbel() {
        if (getWortelNode().getParent() == null) {
            return null; // dan is dit de wortelzeepbel
        }
        Node<Key> zpblroot = getWortelNode();
        Zeepbel<Key> ouderzpbl = getOuderZeepbel();
        if (zpblroot.isLinkerkind()) {
            Node<Key> node = zpblroot.getParent();
            while (node.isLinkerkind()) {
                if (node.getParent() != null && node.getParent().getZeepbel() == ouderzpbl) {
                    node = node.getParent();
                } else {
                    return null;// geen linkerzeepbel, dit is de meest linkse
                }
            }
            Node<Key> nodeP = node.getParent();
            return  node.isRoot() || nodeP.getZeepbel() != ouderzpbl?
                     null :
                    nodeP.getLeft().inZelfdeZeepbelAls(nodeP)?
                            vindMaxInZeepbelVanuit(nodeP.getLeft()).getRight().getZeepbel() :
                                nodeP.getLeft().getZeepbel();
        } else {
            // zpblroot is een rechterkind van zijn parent.
            assert(zpblroot.isRechterkind());
            Node<Key> sibling = zpblroot.getSibling();
            assert(sibling != null); // zeepbel structuur, deze zeepbel is geen root
            if(sibling.getZeepbel() != getOuderZeepbel()) {
                assert(sibling.getZeepbel().getWortelNode() == sibling);
                return sibling.getZeepbel();
            } else {
                return vindMaxInZeepbelVanuit(sibling).getRight().getZeepbel();
            }
        }
    }

    @Override
    public Zeepbel<Key> geefRechterBroerzeepbel() {
        if (getWortelNode().getParent() == null) {
            return null; // dan is dit de wortelzeepbel
        }
        Node<Key> zpblroot = getWortelNode();
        Zeepbel<Key> ouderzpbl = getOuderZeepbel();
        if (zpblroot.isRechterkind()) {
            Node<Key> node = zpblroot.getParent();
            while (node.isRechterkind()) {
                if (node.getParent() != null && node.getParent().getZeepbel() == ouderzpbl) {
                    node = node.getParent();
                } else {
                    return null;// geen linkerzeepbel, dit is de meest rechtse
                }
            }
            Node<Key> nodeP = node.getParent();
            return node.isRoot() || nodeP.getZeepbel() != ouderzpbl?
                    null :
                    nodeP.getRight().inZelfdeZeepbelAls(nodeP)?
                            vindMinInZeepbelVanuit(nodeP.getRight()).getLeft().getZeepbel() :
                            nodeP.getRight().getZeepbel();
        } else {
            // zpblroot is een linkerkind van zijn parent.
            assert(zpblroot.isLinkerkind());
            Node<Key> sibling = zpblroot.getSibling();
            assert(sibling != null); // zeepbel structuur, deze zeepbel is geen root
            if(sibling.getZeepbel() != getOuderZeepbel()) {
                assert(sibling.getZeepbel().getWortelNode() == sibling);
                return sibling.getZeepbel();
            } else {
                return vindMinInZeepbelVanuit(sibling).getLeft().getZeepbel();
            }
        }
    }

    @Override
    public boolean containsMoreThan1Key() {
        assert(root.getParent()==null || root.getParent().getZeepbel() != this);

        return (root.hasLeft() && root.getLeft().getZeepbel() == this) ||
                (root.hasRight() && root.getRight().getZeepbel() == this);
    }

    @Override
    public Node<Key> vindMax() {
        return vindMaxInZeepbelVanuit(root);
    }

    /**
     * Zoekt binnen zeepbel en vanaf gegeven node (die erboven worden niet bekeken, ook al zitten ze in dezelfde zeepbel).
     * @param start : bevindt zich in deze zeepbel
     * @return :
     */
    private Node<Key> vindMaxInZeepbelVanuit(Node<Key> start) {
        assert(start.getZeepbel() == this);
        Node<Key> max = start;
        Zeepbel<Key> zpbl = start.getZeepbel();
        while (max.hasRight() && max.getRight().getZeepbel() == zpbl) {
            max = max.getRight();
        }
        return max;
    }

    @Override
    public Node<Key> vindMin() {
        return vindMinInZeepbelVanuit(root);
    }

    /**
     * Zoekt binnen zeepbel en vanaf gegeven node (die erboven worden niet bekeken, ook al zitten ze in dezelfde zeepbel).
     * @param start :
     * @return :
     */
    private Node<Key> vindMinInZeepbelVanuit(Node<Key> start) {
        Node<Key> min = start;
        Zeepbel<Key> zpbl = start.getZeepbel();
        while (min.hasLeft() && min.getLeft().getZeepbel() == zpbl) {
            min = min.getLeft();
        }
        return min;
    }


    @Override
    public Iterator<Key> iterator() {
        /**
         * Zelfde als zeepbelboom.iterators.ZeepbelNodesIterator, alleen moeten nu gewoon de sleutels teruggegeven worden.
         */
        Iterator<Key> it = new Iterator<Key>(){

            ZeepbelNodesIterator<Key> it = new ZeepbelNodesIterator<Key>(root);

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Key next() {
                return it.next().getKey();
            }
        };
        return it;
    }

    @Override
    /**
     * Compares 2 cells.
     * "Groter dan zijn" voor zeepbellen is equivalent met het resultaat van de
     *  (zeepbelwortel van a).compareTo(de zeepbelwortel van b).
     */
    public int compareTo(Zeepbel<Key> o) {
        return this.getWortelNode().compareTo(o.getWortelNode());
    }

    /*****************************
     * BALANCEER ZEEPBEL METHODEN
     *****************************/

    /**
     * Balanceert de nodes van deze zeepbel zo goed mogelijk.
     * @complexiteit:
     */
    @Override
    public void balanceer() {
        Node<Key>[] arr = maakGesorteerdeArray(); // O(k)
        // kinderen bijhouden
        Node<Key>[] childs = externalChildsToArray(arr); // O(k)
        // nieuwe zeepbelroot correct plaatsen
        this.root = maakGebalanceerdeBoom(arr); // O(k)
        //oorspronkelijke externe kinderen plaatsen
        ZeepbelNodesIterator<Key> it = new ZeepbelNodesIterator<>(root);
        int childIDX=0;
        Node<Key> curr;
        while(it.hasNext()){ //O(k)
            curr= it.next();
            if (curr.getLeft()==null ) {
                curr.setLeft(childs[childIDX]);
                childIDX++;
            }
            if (curr.getRight()==null){
                curr.setRight(childs[childIDX]);
                childIDX++;
            }
        }
    }

    /**
     * Slaat de externe kinderen op in een array en geeft deze terug.
     * @complexiteit: O(k)
     */
    private Node<Key>[] externalChildsToArray(Node<Key>[] arr) {
        Node<Key>[] childs = new Node[(maxSize+1)+1];
        int childIDX=0;// automatisch ook gesorteerd aangezien "var" gesorteerd is.
        for(int idx=0;idx<arr.length;idx++){
            if(arr[idx]!=null){
                // alle EXTERNE kinderen worden alleen toegevoegd, want later worden de interne toch geherbalanceerd.
                if(arr[idx].getLeft()==null || arr[idx].getLeft().getZeepbel()!=this) {
                    childs[childIDX] = arr[idx].getLeft();
                    childIDX++;
                }
                if(arr[idx].getRight()==null || arr[idx].getRight().getZeepbel()!=this) {
                    childs[childIDX] =arr[idx].getRight();
                    childIDX++;
                }
            }
        }
        return childs;
    }

    /**
     * Returnt een array van alle nodes binnen deze zeepbel die gesorteerd zijn
     * volgens hun sleutelwaarde en dat van klein naar groot.
     * @return : de array
     * @complexiteit: O(k), k is de zeepbel-constante.
     */
    private Node<Key>[] maakGesorteerdeArray() {
        Node<Key>[] array = new Node[maxSize+1];
        ZeepbelNodesIterator<Key> it = new ZeepbelNodesIterator<>(this.getWortelNode());
        int idx = 0;
        while(it.hasNext()) {
            array[idx] = it.next();
            idx++;
        }
        return array;
    }

    /**
     * Deze methode maakt met een array van nodes een zoek goed mogelijk gebalanceerde binaire zoekboom.
     * Ale verwijzingen naar externe kinderen worden op null gezet.
     * PRECONDITIE: De Array is gesorteerd volgens de sleutels van de nodes en dat van klein naar groot.
     *
     * @return : de root van deze niewe boom.
     * @complexiteit: Recursie van de vorm T(k) = 2*T(k/2) + 0. k is de zeepbel-constante.
     *                M.b.v. de mastermethode bekomen we voor deze functie O(k).
     */
    private Node<Key> maakGebalanceerdeBoom(Node<Key>[] nodes) {
        int midIdx = nodes.length / 2;
        Node<Key> root = nodes[midIdx];
        root.setLeft(null);
        root.setRight(null);
        if(nodes.length == 1) {
            return root;
        } else if (nodes.length == 2) {
            root.setChild(nodes[0]);
            nodes[0].setLeft(null);
            nodes[0].setRight(null);
        } else if(nodes.length == 3) {
            root.setLeft(nodes[0]);
            nodes[0].setLeft(null);
            nodes[0].setRight(null);

            root.setRight(nodes[2]);
            nodes[2].setLeft(null);
            nodes[2].setRight(null);
        } else {
            Node<Key>[] linkerdeel = Arrays.copyOfRange(nodes, 0, midIdx);
            root.setLeft(maakGebalanceerdeBoom(linkerdeel));
            Node<Key>[] rechterdeel = Arrays.copyOfRange(nodes,midIdx+1,nodes.length);
            root.setRight(maakGebalanceerdeBoom(rechterdeel));
        }
        return root;
    }
}
