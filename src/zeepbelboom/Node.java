package zeepbelboom;

/**
 * Created by user on 27/10/2015.
 */
public class Node<Key extends Comparable<Key>> implements Comparable<Node<Key>> {
    private Zeepbel<Key> zeepbel;

    private Key key;
    private Node<Key> left; //kind

    private Node<Key> right; //kind
    private Node<Key> parent; //ouder

    public Node<Key> getParent() {
        return parent;
    }
    public Node(Key value, Zeepbel<Key> zeepbel) {
        this.key = value;
        this.zeepbel = zeepbel;
    }

    public Zeepbel<Key> getZeepbel() {
        return zeepbel;
    }

    public boolean isRoot(){ return parent == null; }

    public Key getKey() {
        return key;
    }

    public Node<Key> getLeft() {
        return left;
    }

    public Node<Key> getRight() {
        return right;
    }

    public void setZeepbel(Zeepbel<Key> zeepbel) {
        this.zeepbel = zeepbel;
    }

    /**
     * Bidirectional association: oorspronkelijk linkerkind verliest deze node als parent.
     * @param left : if null, Lchild becomes empty.
     */
    public void setLeft(Node<Key> left) {
        if (this.left != null) {
            this.left.parent = null;
        }
        this.left = left;
        if (left!= null) {
            if (left.parent == null) {
                left.parent = this;
            } else if (left.parent != this) {
                left.parent.removeChild(left);
                left.parent = this;
            } else if (left.parent == this) {
                assert(this.right == left);
                this.right = null;
            }
        }
    }

    /**
     * Bidirectional association: oorspronkelijk rechterkind verliest deze node als parent.
     * @param right : if null, Rchild becomes empty.
     */
    public void setRight(Node<Key> right) {
        if (this.right != null) {
            this.right.parent = null;
        }
        this.right = right;
        if(right != null) {
            if (right.parent == null) {
                right.parent = this;
            } else if (right.parent != this) {
                right.parent.removeChild(right);
                right.parent = this;
            } else if (right.parent == this) {
                assert(this.left == right);
                this.left = null;
            }
        }
    }

    /**
     * If child is not a child of this node, nothing happens.
     */
    public void removeChild(Node<Key> child) {
        if(child == this.getLeft()) {
            this.left = null;
            child.parent = null;
        } else if (child == this.getRight()) {
            this.right = null;
            child.parent = null;
        } else {
            // do nothing
        }
    }

    /**
     * Removes this nodes parent.
     * If parent == null (so this node is probably the root), nothing happens.
     */
    public void removeParent() {
        if(parent != null) {
            parent.removeChild(this);
        }
        this.parent = null;
    }

    /**
     * Bepaalt automatisch of de gegeven node linker- of rechterkind wordt van deze node.
     * Asl child==null gebeurt er niets.
     */
    public void setChild(Node<Key> child) {
        if(child==null) {
            return;
        }
        if (child.getKey().compareTo(getKey()) <= 0) {
            setLeft(child);
        } else {
            setRight(child);
        }
    }
    /**
     * Null kinderen tellen niet mee.
     * @return : 0 <= aantal <= 2
     */
    public int geefAantalKinderen() {
        return (this.left== null? 0:1) + (this.right==null? 0 : 1);
    }

    public boolean hasLeft() { return left != null; }

    public boolean hasRight() { return right != null; }

    /**
     * Kijkt of deze node een linkerkind is van zijn parent.
     * Indien dit de root is wordt een false gereturnd.
     * @return :
     */
    public boolean isLinkerkind() {
        return this.getParent() != null &&
            this == this.getParent().getLeft();
    }

    /**
     * Kijkt of deze node een rechterkind is van zijn parent.
     * Indien dit de root is wordt een false gereturnd.
     * @return :
     */
    public boolean isRechterkind() {
        return this.getParent() != null &&
                this == this.getParent().getRight();
    }

    /**
     * Geeft het andere kidn van zijn parent terug.
     * Null indien onbestaand of indien dit de root is (die natuurlijk geen broer heeft).
     */
    public Node<Key> getSibling() {
        if (getParent() == null) {
            return null;
        } else {
            return this.isLinkerkind()? getParent().getRight() : getParent().getLeft();
        }
    }

    /**
     * Vind de eerste gemeenschapplijke ouder. Met eerste wordt degene bedoeld met
     * de grootste diepte (of de laagste hoogte).
     * @param start : de "zogezegde" root van de boom. Dit stelt de grootst mogelijke gemeenschappelijke ouder voor.
     * @param other: node where lca must be found (with this node)
     * @return : the lca
     * @throws NullPointerException : if one of the ndoes is null or is not present.
     */
    public Node<Key> lowestCommonAncestor(Node<Key> start,Node<Key> other) {
        if (start == null) {
            return null;
        }

        // If the root is one of a or b, then it is the LCA
        if (start == this || start == other) {
            return start;
        }

        Node<Key> left = lowestCommonAncestor(start.left, other);
        Node<Key> right = lowestCommonAncestor(start.right, other);

        // If both nodes lie in left or right then their LCA is in left or right,
        // Otherwise root is their LCA
        if (left != null && right != null) {
            return start;
        }

        return (left != null) ? left : right;
    }

    /**
     * Is deze node een blad?
     * @return : true if blad, false anders.
     */
    public boolean isBlad() { return geefAantalKinderen() == 0;}

    /**
     * PRECONDITIE: zeepbel hangt samen.
     * @return
     */
    public boolean zitAlleenInZeepbel() {
        if (parent!=null && parent.getZeepbel() == getZeepbel()) {
            return false;
        }
        if (hasLeft() && getLeft().getZeepbel() == getZeepbel() ||
                hasRight() && getRight().getZeepbel() == getZeepbel()) {
            return false;
        }
        return true;
    }

    /**
     * Kijkt of 2 ndoes in dezelfde zeepbel zitten.
     * @param other: node
     * @return : true indien in zelfde zeepbel.
     */
    public boolean inZelfdeZeepbelAls(Node<Key> other) {
        return other != null && zeepbel == other.zeepbel;
    }

    @Override
    public int compareTo(Node<Key> o) {
        return getKey().compareTo(o.getKey());
    }
}
