package zeepbelboom.iterators;

import zeepbelboom.Node;
import zeepbelboom.Zeepbel;

import java.util.NoSuchElementException;

/**
 * Itereert over alle nodes binnen ��nezelfde zeepbel.
 * Created by user on 30/10/2015.
 */
public class ZeepbelNodesIterator<Key extends Comparable<Key>> {
    private Node<Key> next;
    private Zeepbel<Key> zeepbel;

    public ZeepbelNodesIterator(Node<Key> root){
        this.next = root;
        if(next == null) {
            return;
        }
        zeepbel = root.getZeepbel();
        while (next.getLeft() != null && next.getLeft().getZeepbel() == zeepbel) {
            next = next.getLeft();
        }
    }

    public boolean hasNext(){
        return next != null;
    }

    public Node<Key> next(){
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        Node<Key> r = next;
        // als je naar rechts kan, doe dat dan, ga dan zo ver als je kan naar links.
        // Als je niet naar rechts kan, ga omhoog tot je van links komt (dus tot je een linkerkind bent van een node).
        if(next.getRight() != null && next.getRight().getZeepbel() == zeepbel){
            next = next.getRight();
            while (next.getLeft() != null && next.getLeft().getZeepbel() == zeepbel)
                next = next.getLeft();
            return r;
        }else while(true){
            if(next.getParent() == null){
                next = null;
                return r;
            }
            if(next.isLinkerkind() && next.getParent().getZeepbel() == zeepbel){
                next = next.getParent();
                return r;
            }
            next = next.getParent();
        }
    }
}