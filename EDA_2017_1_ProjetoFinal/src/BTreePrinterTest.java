
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTreePrinterTest {



 
}

//class Node<T extends Comparable<?>> {
//    Node<T> left, right;
//    T data;
//
//    public Node(T data) {
//        this.data = data;
//    }
//}

class BTreePrinter {

    public static <T extends Comparable<?>> void printNode(HuffmanTree.Node root) {
        int maxLevel = BTreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<?>> void printNodeInternal(List<HuffmanTree.Node> list, int level, int maxLevel) {
        if (list.isEmpty() || BTreePrinter.isAllElementsNull(list))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BTreePrinter.printWhitespaces(firstSpaces);

        List<HuffmanTree.Node> newNodes = new ArrayList<HuffmanTree.Node>();
        for (HuffmanTree.Node node : list) {
            if (node != null) {
                System.out.print(node.getCh());
                newNodes.add(node.getLeft());
                newNodes.add(node.getRight());
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BTreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < list.size(); j++) {
                BTreePrinter.printWhitespaces(firstSpaces - i);
                if (list.get(j) == null) {
                    BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (list.get(j).getLeft() != null)
                    System.out.print("/");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(i + i - 1);

                if (list.get(j).getRight() != null)
                    System.out.print("\\");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<?>> int maxLevel(HuffmanTree.Node root) {
        if (root == null)
            return 0;

        return Math.max(BTreePrinter.maxLevel(root.getLeft()), BTreePrinter.maxLevel(root.getRight())) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }

}