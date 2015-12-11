package zeepbelboom;

import com.sun.javafx.sg.prism.NodePath;

/**
 * Weet wat er gedaan moet worden voor het verwijderen van een node
 * die alleen zit in zijn zeepbel en verwijderd moet worden.
 * Bij conventie wordt het (enige) kind van de te verwijderen node LINKS gezet (ongeacht de waarde van dat kind).
 * Created by user on 21/11/2015.
 */
public class Deletor<Key extends Comparable<Key>> {

    private final AbstracteZeepbelboom<Key> b;

    public Deletor(AbstracteZeepbelboom<Key> b) {
        this.b = b;
    }
    /*
     * PRECONDITIE: De gegeven node is de enige node in zijn zeepbel en is een blad.
     *              Hij bevat ook een parent.
     */
    public void delete(Node<Key> node) {
        boolean rebalanceNeeded = true;
        while(rebalanceNeeded) {
            rebalanceNeeded = executeCorrectMethod(node);
        }
    }

    /**
     * Determineert en voert de geschikte methode uit voor de huidige toestand van de node
     * en zijn broerzeepbellen en parentzeepbel.
     * PRECONDITIE: De gegeven node is de enige node in zijn zeepbel.
     * @param node :
     * @return: true indien deze methode nog eens moet opgeroepen worden voor herbalancering.
     */
    private boolean executeCorrectMethod(Node<Key> node) {
        if(node.isRoot()) {
            return case1Basisgeval(node);
        }

        Zeepbel<Key> linkerBroerzeepbel = node.getZeepbel().geefLinkerBroerzeepbel(),
                rechterBroerzeepbel = node.getZeepbel().geefRechterBroerzeepbel();
        assert((linkerBroerzeepbel != null || rechterBroerzeepbel != null));


        if(linkerBroerzeepbel!=null) {
            if (node.getParent().zitAlleenInZeepbel()) {
                return case1MetLinks(linkerBroerzeepbel, node);
            } else if (linkerBroerzeepbel.containsMoreThan1Key()) {
                return case2MetLinks(linkerBroerzeepbel,node);
            } else {
                return case3MetLinks(node,linkerBroerzeepbel);
            }
        } else {
            if (node.getParent().zitAlleenInZeepbel()) {
                return case1MetRechts(rechterBroerzeepbel, node);
            } else if (rechterBroerzeepbel.containsMoreThan1Key()) {
                return case2MetRechts(node, rechterBroerzeepbel);
            } else {
                return case3MetRechts(rechterBroerzeepbel, node);
            }
            
        }
    }


    /*****************************************
     * CASE 1 :
     *      One Sibling, Parent alone in Cell
     * Return is steeds True, herbalancering is dus nodig. (tem het basisgeval)
     *****************************************/

    private boolean case1Basisgeval(Node<Key> node) {
        b.changeRoot(node.getLeft());
        b.forceDeleteNode(node);
        return false;
    }

    /**
     * Parent bevat maar 1 key en de te verwijderen node bevindt zich rechts.
     * @param linkerBroerzeepbel : bevat 1 key
     * @param node : is rechterkind
     */
    private boolean case1MetLinks(Zeepbel<Key> linkerBroerzeepbel, Node<Key> node) {
        Node<Key> linkerRoot = linkerBroerzeepbel.getWortelNode(),
                    parent = linkerRoot.getParent(),
                    grandparent = parent.getParent();
        assert(parent == node.getParent());
        parent.setRight(node.getLeft());
        linkerRoot.setZeepbel(parent.getZeepbel());
        node.setLeft(parent);
        if(grandparent!= null) {
            grandparent.setChild(node);
        } else {
            b.changeRoot(node);
        }
        return true;
    }

    /**
     * Parent bevat maar 1 key en de te verwijderen node bevindt zich links.
     * @param node : is linkerkind
     * @param rechterBroerzeepbel : bevat 1 key
     */
    private boolean case1MetRechts(Zeepbel<Key> rechterBroerzeepbel, Node<Key> node) {
        Node<Key> rechterRoot = rechterBroerzeepbel.getWortelNode(),
                parent = rechterRoot.getParent(),
                grandparent = parent.getParent();
        assert(parent == node.getParent());
        parent.setLeft(node.getLeft());
        rechterRoot.setZeepbel(parent.getZeepbel());
        node.setLeft(parent);
        if(grandparent!= null) {
            grandparent.setChild(node);
        } else {
            b.changeRoot(node);
        }
        return true;
    }

    /*****************************************
     * CASE 2 :
     *      A direct sibling contains 2 or more keys
     * Return is steeds False, herbalancering is dus niet meer nodig.
     *
     *****************************************/

    /**
     * Linkerzeepbel heeft minstens 2 keys.
     * @param linkerBroerzeepbel :
     * @param node :
     */
    private boolean case2MetLinks(Zeepbel<Key> linkerBroerzeepbel, Node<Key> node) {
        Node<Key> lca = node.lowestCommonAncestor(linkerBroerzeepbel.getOuderZeepbel().getWortelNode(),
                linkerBroerzeepbel.getWortelNode());
        Node<Key> maxLinks = linkerBroerzeepbel.vindMax(),
                    nodeChild = node.getLeft();
        Node<Key> nodeP = node.getParent();

        // deze variabele houdt bij of de verwijderde node kind is van de lca (case1) of meerdere generaties verder ligt (case 2)
        int lcaCase;
        if(lca==node.getParent()) {
            //case: node is 1 niveau lager als lca
            lcaCase = 1;
        } else {
            //case: node is meerdere niveau's lager als lca
            lcaCase = 2;
        }
        b.switchNodes(lca,maxLinks);
        lca.getParent().setChild(lca.getLeft());
        lca.setLeft(lca.getRight());
        lca.setRight(nodeChild);
        switch(lcaCase) {
            case 1:
                maxLinks.setRight(lca);
                break;
            case 2:
                nodeP.setLeft(lca);
                break;
        }

        // lca kan node zijn kleur overnemen.
        lca.setZeepbel(node.getZeepbel());
        lca.getZeepbel().setWortelNode(lca);
        return false;
    }

    /**
     * Rechterzeepbel heeft minstens 2 keys.
     * @param node :
     * @param rechterBroerzeepbel :
     */
    private boolean case2MetRechts(Node<Key> node, Zeepbel<Key> rechterBroerzeepbel) {
        Node<Key> lca = node.lowestCommonAncestor(rechterBroerzeepbel.getOuderZeepbel().getWortelNode(),
                rechterBroerzeepbel.getWortelNode());
        Node<Key> minrechts = rechterBroerzeepbel.vindMin(),
                nodeChild = node.getLeft();
        Node<Key> nodeP = node.getParent();

        int lcaCase;
        if(lca==node.getParent()) {
            //case node is 1 niveau lager als lca
            lcaCase = 1;
        } else {
            //case node is meerdere niveau's lager als lca
            lcaCase = 2;
        }
        b.switchNodes(lca, minrechts);
        lca.getParent().setChild(lca.getRight());
        lca.setRight(lca.getLeft());
        lca.setLeft(nodeChild);
        switch(lcaCase) {
            case 1:
                minrechts.setLeft(lca);
                break;
            case 2:
                nodeP.setRight(lca);
                break;
        }

        // lca kan node zijn kleur overnemen.
        lca.setZeepbel(node.getZeepbel());
        lca.getZeepbel().setWortelNode(lca);
        return false;
    }

    /*****************************************
     * CASE 3 :
     *      Mergen met sibling met 1 key.
     *      - The (deleted) node contains more than 1 sibling, m.a.w.
     *        de parent-zeepbel bevat minstens 2 keys.
     *      - An immediate sibling with exactly 1 keys.
     *       (the other immediate sibling if it exists, will remain untouched)
     * Return is steeds False, herbalancering is dus niet meer nodig.
     *
     *****************************************/

    /**
     * case3 met linkerzeepbel.
     * @param node : heeft een directe linkerzeepbel met 1 key. Parent moet meerdere hebben, zoniet gebruik case 1.
     * @param linkerBroerzeepbel :
     */
    private boolean case3MetLinks(Node<Key> node, Zeepbel<Key> linkerBroerzeepbel) {
        Node<Key> lca = node.lowestCommonAncestor(linkerBroerzeepbel.getOuderZeepbel().getWortelNode(),
            linkerBroerzeepbel.getWortelNode());
        Node<Key> lNode = linkerBroerzeepbel.vindMax(),
                nodeChild = node.getLeft();
        assert(linkerBroerzeepbel.size() == 1 && lNode == linkerBroerzeepbel.getWortelNode());
        Node<Key> nodeP = node.getParent();


        if (lNode.getParent() == lca && lca == nodeP) {
            //11
            assert (lNode == node.getSibling());
        } else if(lNode.getParent() == lca && lca != nodeP) {
            //1?
            if(lca.getParent()!= null) {
                lca.getParent().setChild(lca.getRight());
            } else {
                b.changeRoot(lca.getRight());
            }
            nodeP.setLeft(lca);
        } else if (lNode.getParent() != lca && lca != nodeP) {
            //?1 + ??
            b.switchNodes(lca, lNode.getParent());
            lca.getParent().setChild(lca.getLeft());
            lca.setLeft(lca.getRight());
            nodeP = node.getParent(); // new parent
            nodeP.setChild(lca);
        }
        lca.setRight(nodeChild);
        lca.setZeepbel(lNode.getZeepbel());
        lca.getZeepbel().setWortelNode(lca);
        return false;
    }

    /**
     * case3 met rechterzeepbel
     *  @param rechterBroerzeepbel :
     * @param node : heeft een directe rechterzeepbel met 1 key. Parent moet meerdere hebben, zoniet gebruik case 1.
     */
    private boolean case3MetRechts(Zeepbel<Key> rechterBroerzeepbel, Node<Key> node) {
        Node<Key> lca = node.lowestCommonAncestor(rechterBroerzeepbel.getOuderZeepbel().getWortelNode(),
                rechterBroerzeepbel.getWortelNode());
        Node<Key> rNode = rechterBroerzeepbel.vindMax(),
                nodeChild = node.getLeft();
        assert(rechterBroerzeepbel.size() == 1 && rNode == rechterBroerzeepbel.getWortelNode());
        Node<Key> nodeP = node.getParent();


        if (rNode.getParent() == lca && lca == nodeP) {
            //11
            assert (rNode == node.getSibling());
        } else if(rNode.getParent() == lca && lca != nodeP) {
            //?1
            if(lca.getParent()!= null) {
                lca.getParent().setChild(lca.getLeft());
            } else {
                b.changeRoot(lca.getLeft());
            }
            nodeP.setRight(lca);
        } else if (rNode.getParent() != lca && lca != nodeP) {
            //1? + ??
            b.switchNodes(lca, rNode.getParent());
            lca.getParent().setChild(lca.getRight());
            lca.setRight(lca.getLeft());
            nodeP = node.getParent(); // new parent
            nodeP.setChild(lca);
        }
        lca.setLeft(nodeChild);
        lca.setZeepbel(rNode.getZeepbel());
        lca.getZeepbel().setWortelNode(lca);
        return false;

    }

    /****************
     * Hulpmethodes *
     ****************/
    /**
     * Kijkt hoever de LCA (lowest common ancestor) van elkaar zich bevindt
     * PRECONDITIE: left <= right, left is de wortel van de linkerzeepbel van de zeepbel van right.
     *              Right moet ook de wortel van zijn zeepbel zijn.
     * @param left : left node
     * @param right : right node
     * @return : Een getal van 2 cijfers. Het linkse cijfer vertelt hoever de LCA zich bevindt van left.
     *              Het rechtse cijfer vertelt hoever de LCA zich bevindt van right
     *              Er zijn vier gevallen: 11 12 21 22
     */
    private int lcaPosition(Node<Key> left, Node<Key> right) {
        assert (left.getKey().compareTo(right.getKey()) <= 0);
        assert(left.getZeepbel().getWortelNode() == left);
        assert(right.getZeepbel().getWortelNode() == right);
        assert(left.getZeepbel().geefRechterBroerzeepbel() == right.getZeepbel());
        // => lP en rP != null
        Node<Key> lP = left.getParent() , rP = right.getParent();
        assert(lP != null && rP != null);
        Node<Key> lGP = lP.getParent() , rGP = rP.getParent();
        if(lP == rP) {
            return 11;
        } else if (rGP != null && rGP == lP) {
            return 12;
        } else if (lGP != null && lGP == rP) {
            return 21;
        } else {
            assert (lGP != null && rGP != null && lGP == rGP);
            return 22;
        }
    }
}
