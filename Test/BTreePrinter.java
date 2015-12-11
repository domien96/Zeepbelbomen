import zeepbelboom.Node;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Print een binaire zoekboom diagram.
 * Created by user on 29/11/2015.
 */
public class BTreePrinter<T extends Comparable<T>> {

    public void printTree(Node<Integer> root)  {
        if (root.getRight() != null) {
            printTree(root.getRight(), true, "");
        }
        printNodeValue(root);
        if (root.getLeft() != null) {
            printTree(root.getLeft(), false, "");
        }
    }
    private void printNodeValue(Node<Integer> node) {
        if (node.getKey() == null) {
            System.out.print("<null>");
        } else {
            System.out.print(node.getKey().toString());
        }
        System.out.print('\n');
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(Node<Integer> node,boolean isRight, String indent){
        if (node.getRight() != null) {
            printTree(node.getRight(), true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue(node);
        if (node.getLeft() != null) {
            printTree(node.getLeft(), false, indent + (isRight ? " |      " : "        "));
        }
    }

}
