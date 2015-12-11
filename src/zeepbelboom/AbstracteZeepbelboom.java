package zeepbelboom;

import zeepbelboom.iterators.ZeepbelIterator;
import zeepbelboom.iterators.ZeepbelboomIterator;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Created by user on 27/10/2015.
 */
public abstract class AbstracteZeepbelboom<Key extends Comparable<Key>> extends AbstractCollection<Key> implements Zeepbelboom<Key> {

    protected AbstracteZeepbelboom(int cell_size) {
        if (cell_size < 2) {
            cell_size = 2;
        }
        this.max_zeepbel_size = cell_size;
        this.rotator = new Rotator<>(this);
        this.deletor = new Deletor<>(this);
    }

    /**
     * The root of the underlying binary search tree.
     */
    protected Node<Key> root;

    /**
     * The maximum size of keys in a "zeepbel".
     */
    protected int max_zeepbel_size;

    /**
     * This is the amount of keys/nodes in this tree.
     * Used for determining the size in O(1).
     */
    protected int size;

    /**
     * Dit object kan gebruikt worden om te roteren.
     */
    protected Rotator<Key> rotator;

    /**
     * Weet het algoritme voor het verwijderen
     * van een top die alleen in een zeepbel zit.
     */
    protected Deletor<Key> deletor;

    /**
     * De balanceermethode
     * @param node
     */
    protected abstract void balanceer(Node<Key> node);

    public int getMax_zeepbel_size() {
        return max_zeepbel_size;
    }

    /**
     * PRECONDITIONS : The given node contains 2 'real' children. (Ze moeten niet pers� in dezelfde zeepbel zitten.)
     * POSTCONDITIONS : Er gebeuren 2 dingen:
     *                  1) Indien de 2 kinderen van de gegeven node in dezelfde zeepbel zitten, zullen alle nodes links van deze node
     *                     EN binnenin diezelfde zeepbel een nieuwe kleur krijgen.
     *                     O(k-1) = O(1) , want k is constant.
     *                  2) Deze node zal in alle instanties geduwd worden naar de zeepbel erboven (en krijgt dus dezelfde kleur als die zeepbel).
     *                     Indien deze node de wortel van de volledige boom is, wordt ze een nieuwe zeepbel.
     *                     O(1)
     * @param wortelHuidigeZeepbel : given node.
     * @complexiteit: O(k), k is de zeepbel-constante.
     */
    protected void splitsEnDuwNaarBoven(Node<Key> wortelHuidigeZeepbel) {
        assert(wortelHuidigeZeepbel.geefAantalKinderen() == 2);
        // 1)
        if (wortelHuidigeZeepbel.getLeft().getZeepbel() == wortelHuidigeZeepbel.getRight().getZeepbel()) {
            wortelHuidigeZeepbel.getZeepbel().splits(); // kost O(k)
        }
        // 2)
        duwNaarBoven(wortelHuidigeZeepbel);
        // zeepbel rechts heeft nu ook een nieuwe wortel, dit wordt niet gedaan door de splits() functie, dus doen we het zelf.
        Node<Key> rChild = wortelHuidigeZeepbel.getRight();
        rChild.getZeepbel().setWortelNode(rChild);
    }

    /**
     * Duwt de gegeven node naar de zeepbel erboven of wordt een nieuwe zeepbel.
     * @param wortelHuidigeZeepbel
     */
    protected void duwNaarBoven(Node<Key> wortelHuidigeZeepbel) {
        Zeepbel<Key> ouderZeepbel = wortelHuidigeZeepbel.getZeepbel().getOuderZeepbel();
        if (ouderZeepbel== null) {
            ouderZeepbel = new MijnZeepbel<>(max_zeepbel_size,wortelHuidigeZeepbel);
        }
        wortelHuidigeZeepbel.setZeepbel(ouderZeepbel);

    }

    /**
     * Verandert de node in de root van zijn zeepbel en deze volledige boom.
     * @param newRoot
     */
    protected void changeRoot(Node<Key> newRoot) {
        root = newRoot;
        if (newRoot != null) {
            newRoot.getZeepbel().setWortelNode(newRoot);
            newRoot.removeParent();
        }
    }

    @Override
    public Iterator<Zeepbel<Key>> zeepbelIterator() {
        return new ZeepbelIterator<>(root);
    }

    @Override
    public Key getWortelSleutel() {
        return root.getKey();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }


    /**
     *
     * @param o
     * @return
     * @throws ClassCastException : o cannot be casted to type parameter "Key". (Zie documentatie collection)
     */
    public boolean contains(Object o) {
        try {
            Key k = (Key) o; // SURPRESS WARNING , because possible exception will be caught.
            return findKey(k) != null;
        } catch (ClassCastException e) {
            //zie documentatie Collection.contains( ... )
            throw e;
        }
    }

    /**
     * Geeft de eerste node terug die correspondeert met de opgegeven key/waarde.
     * Null indien geen zo'n node gevonden. (Maw k bevindt zich niet in deze zeepbelboom).
     * @param k : waarde van de sleutel
     * @return : de eerste node die de key met waarde k bevat, null indien niet gevonden.
     * @throws NullPointerException : if specified element is null.
     */
    protected Node<Key> findKey(Key k) {
        if(isEmpty()) {
            return null;
        }
        Node<Key> current = this.root;
        while (current != null && current.geefAantalKinderen() != 0) {
            if (current.getKey().compareTo(k) < 0) {
                current = current.getRight();
            } else if (current.getKey().compareTo(k) > 0) {
                current = current.getLeft();
            } else {
                // they are equal
                return current;
            }
        }

        if (current != null && current.getKey().compareTo(k) == 0) {
            return current;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<Key> iterator() {
        return new Iterator<Key>() {

            ZeepbelboomIterator<Key> it = new ZeepbelboomIterator<>(root);
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Key next() {
                return it.next().getKey();
            }
        };
    }


    /***************************
     *
     *      ADD METHODS
     *
     **************************/
    public boolean add(Key k) {
        Key key = k;
        try {
            Node<Key> node = makeAndAppendNewNode(key, root);
            balanceer(node);
        } catch (IllegalStateException | IllegalArgumentException | NullPointerException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
        size += 1 ;
        return true;
    }

    /**
     * Gewone binaire zoekboom toevoeging. Geen balanceringsvoorwaarden.
     * Voeg nieuwe sleutel zonder controle toe aan de zeepbel van zijn parent.
     * Links : <=
     * Rechts : >
     * @param k
     * @return : new made node
     */
    protected Node<Key> makeAndAppendNewNode(Key k, Node<Key> startRoot) {
        if (root==null) {
            Zeepbel<Key> zeepbel = new MijnZeepbel<>(max_zeepbel_size,root);
            root = new Node<>(k,zeepbel);
            zeepbel.setWortelNode(root);
            return root;
        } else {
            Node<Key> newChild;
            while(true) {
                if (k.compareTo(startRoot.getKey()) <= 0) {
                    if (startRoot.getLeft() != null) {
                        startRoot = startRoot.getLeft();
                        continue;
                    } else {
                        newChild = new Node<>(k,startRoot.getZeepbel());
                        startRoot.setLeft(newChild);
                        break;
                    }
                } else { // strikt groter dan startvalue
                    if (startRoot.getRight() != null){
                        startRoot = startRoot.getRight();
                        continue;
                    } else {
                        newChild = new Node<>(k,startRoot.getZeepbel());
                        startRoot.setRight(newChild);
                        break;
                    }
                }
            }

            return newChild;
        }

    }

    /***************************
     *
     *      REMOVE METHODS
     *
     **************************/
    @Override
    @SuppressWarnings("cast")
    public boolean remove(Object o) {
        try {
            Key k = (Key) o; // SURPRESS WARNING , because possible exception will be caught.
            Node<Key> node = findKey(k);
            if(node==null)
                return false;
            removeNode(node);
        } catch (ClassCastException e) {
            //zie documentatie Collection.remove( ... )
            throw e;
        } catch (NullPointerException e) {
            //zie documentatie Collection.remove( ... )
            throw e;
        } catch (Exception e) {
            return false;
        }
        size -= 1 ;
        return true;
    }

    /**
     *
     * @param node : node die verwijderd moet worden.
     * @throws NullPointerException : if node is null
     */
    protected void removeNode(Node<Key> node) {
        if(size==1) {
            changeRoot(null);
        }
        // FROM HERE: size >=2 , at least 2 nodes.
        percolateDown(node);
        // FROM HERE: node is not the root since there were 2 nodes before percolation, daarboven is het een blad.
        if (!node.zitAlleenInZeepbel()) {
            // this cell contains at least 2 nodes.
            forceDeleteNode(node);
        } else {
            // this cell contains exactly 1 node. So is automatically the cell-root
            assert(node == node.getZeepbel().getWortelNode());
            //
            deletor.delete(node);
        }
    }

    /**
     * Deze functie zal percoleren naar beneden.
     * Men geeft een node die verwijderd moet worden, dus waar zogezegd het gat zich bevindt (bv. bij verwijdering).
     * Er zal daarna eerst gekeken worden of het rechterkind de nieuwe plaats kan innemen,
     * Indien die niet bestaat wordt het linkerkind de plaatsopvolger.
     * Indien het linkerkind ook niet bestaat (de opegegeven node is dus een blad), dan zal er niets gebeuren.
     *
     *      POSTCONDITIE:
     * De holeNode zal na afloop van deze functie dus steeds een blad zijn.
     * Merk op dat de holeNode niet aan de eisen van een binaire zoekboom voldoet, aangezien verondersteld wordt dat deze later verwijderd zal worden.
     * bv. De holenode kan als linkerkind (strikt) groter zijn dan zijn parent, of andersom.
     * @param holeNode : De node waarvan de percolatie zal starten. Er wordt verondersteld dat deze later verwijderd wordt.
     * @return : De node die het laatst naar boven werd gebracht. M.a.w. de parent-node van waar het gat zich nu bevindt.
     * @throws NullPointerException : if holeNode is null
     */
    private void percolateDown(Node<Key> holeNode) {
        while (holeNode.geefAantalKinderen() != 0) {
            if (holeNode.getRight() != null) {
                switchNodes(holeNode, holeNode.getRight());
            } else { // leftChild != null
                switchNodes(holeNode, holeNode.getLeft());
            }
        }
    }

    /**
     * De kinderen, zeepbellen en posities worden verwisseld ongeacht
     * de values van alle nodes.
     * @param node1
     * @param node2
     * @throws NullPointerException: if one of the paramaters is null.
     */
    public void switchNodes(Node<Key> node1 , Node<Key> node2) {
        if (node1 == node2) {
            return;
        } else if( node1.getParent()== node2) {
            switchParentAndChild(node2, node1);
        } else if (node2.getParent() == node1) {
            switchParentAndChild(node1,node2);
        } else {
            Node<Key> left1 = node1.getLeft(), right1 = node1.getRight(), parent1 = node1.getParent();
            // Houdt bij aan welke kant node1 ligt, niet gebruiken indien node1 de root is.
            // true == links, false == rechts
            boolean node1WasLeftChild = node1.isLinkerkind();
            boolean node1WasCellRoot = node1.getZeepbel().getWortelNode() == node1;
            Zeepbel<Key> zeepbel1 = node1.getZeepbel();
            // verander node1
            node1.setLeft(node2.getLeft());
            node2.setRight(node2.getRight());
            node1.setZeepbel(node2.getZeepbel());
            if(node2.getZeepbel().getWortelNode() == node2) {
                node2.getZeepbel().setWortelNode(node1);
            }

            if (node2.isLinkerkind()) {
                node2.getParent().setLeft(node1);
            } else if (node2.isRechterkind()) {
                node2.getParent().setRight(node1);
            } else {
                assert(node2.isRoot() && getWortelNode() == node2);
                changeRoot(node1);
            }

            //verander node 2
            node2.setLeft(left1);
            node2.setRight(right1);
            node2.setZeepbel(zeepbel1);
            if(node1WasCellRoot) {
                node2.getZeepbel().setWortelNode(node2);
            }
            if(node1.isRoot()){
                assert(getWortelNode() == node1);
                changeRoot(node2);
            } else if (node1WasLeftChild) {
                parent1.setLeft(node2);
            } else {
                parent1.setRight(node2);
            }
        }
    }

    /**
     * Switch nodes in this tree.
     * Childs and parents get switch.
     * Also cells gets switched.
     * PRECONDITION:
     * First node argument is parent of the second node argument. (Otherwise nothing happens)
     * POSTCONDITION : Tree may be out of balance because of values.
     * @param parent : parent of child. cannot be null
     * @param child : child of parent. cannot be null
     */
    private void switchParentAndChild(Node<Key> parent, Node<Key> child) {
        Node<Key> parLeft = parent.getLeft(), parRight = parent.getRight();
        Zeepbel<Key> parZeepbel = parent.getZeepbel();
        Node<Key> grandParent = parent.getParent();
        boolean parentWasCellRoot = parent.getZeepbel().getWortelNode() == parent;

        //positie veranderen
        if (child.isLinkerkind()) {
            if (grandParent == null) {
                //parent is the root
                changeRoot(child);
            } else if (grandParent.getLeft() == parent) {
                grandParent.setLeft(child);
            } else if (grandParent.getRight() == parent) {
                grandParent.setRight(child);
            } else {
                System.err.println("BUG : Ongewenste neveneffecten @AbstracteZeepbelboom.switchNodes-methode");
                new IllegalStateException().printStackTrace();
            }

            parent.setLeft(child.getLeft());
            parent.setRight(child.getRight());
            child.setLeft(parent);
            child.setRight(parRight);

        } else if (child.isRechterkind()) {
            if (grandParent == null) {
                //parent is the root
                changeRoot(child);
            } else if (parent.isLinkerkind()) {
                grandParent.setLeft(child);
            } else if (parent.isRechterkind()) {
                grandParent.setRight(child);
            } else {
                System.err.println("BUG : Ongewenste neveneffecten @AbstracteZeepbelboom.switchNodes-methode");
                new IllegalStateException().printStackTrace();
            }

            parent.setLeft(child.getLeft());
            parent.setRight(child.getRight());
            child.setRight(parent);
            child.setLeft(parLeft);

        } else {
            // precondition is niet voldaan. 'parent' is geen parentnode van 'child'.
            return;
        }

        //zeepbel veranderen
        parent.setZeepbel(child.getZeepbel());
        if(child.getZeepbel().getWortelNode() == child) {
            parent.getZeepbel().setWortelNode(parent);
        }

        child.setZeepbel(parZeepbel);
        if(parentWasCellRoot) {
            child.getZeepbel().setWortelNode(child);
        }
    }

    /**
     * Forces to delete this node from the tree.
     * This parent will "forget" this node as child.
     * USE ONLY WHEN THIS NODE IS A LEAF!
     * WARNING: Since there is no connnection from the tree to this node anymore, there won't be any
     * connection to this node's childs anymore. So all childs will be deleted also.
     * @param node
     */
    public void forceDeleteNode(Node<Key> node) {
        if (node.getParent()!= null) {
            node.getParent().removeChild(node);
        }
    }
    /***************************
     *
     * AID METHODS
     *
     **************************/
    public Node<Key> getWortelNode() {
        return root;
    }

    /**
     *  !!!
     * De methode hieronder zijn eenvoudig te implementeren mbv de basisbewerkingen hierboven.
     * Ik heb mijn tijd meer gebruikt voor het verslag, optimalisaties en testing.
     *  !!!
     */
//
//    @Override
//    public Key[] toArray() {
//        return null;//new Key[0];
//    }
//
//    @Override
//    public boolean addAll(Collection c) {
//        for(Object o : c) {
//            this.add((Key) o);
//        }
//        return true;
//    }
//
//    @Override
//    public void clear() {
//
//    }
//
//    @Override
//    public boolean retainAll(Collection c) {
//        return false;
//    }
//
//    @Override
//    public boolean removeAll(Collection c) {
//        boolean changed = false;
//        for(Object o : c) {
//            changed = this.remove((Key) o) && !changed ;
//        }
//        return changed;
//    }
//
//    @Override
//    public boolean containsAll(Collection c) {
//        return false;
//    }
//
//    public Object[] toArray(Object[] a) {
//        return null;// new T[0];
//    }



}
