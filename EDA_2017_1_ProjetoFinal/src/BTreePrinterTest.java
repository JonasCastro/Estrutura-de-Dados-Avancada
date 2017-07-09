
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PrinterTree {

	public static <T extends Comparable<?>> void printNode(HuffmanTree.Node root) {
		int maxLevel = PrinterTree.maxLevel(root);

		printNodeInternal(Collections.singletonList(root), 1, maxLevel);
	}

	private static <T extends Comparable<?>> void printNodeInternal(List<HuffmanTree.Node> list, int level,
			int maxLevel) {
		if (list.isEmpty() || PrinterTree.isAllElementsNull(list))
			return;

		int floor = maxLevel - level;
		int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
		int firstSpaces = (int) Math.pow(2, (floor)) - 1;
		int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

		PrinterTree.printWhitespaces(firstSpaces);

		List<HuffmanTree.Node> newNodes = new ArrayList<HuffmanTree.Node>();
		for (HuffmanTree.Node node : list) {
			if (node != null) {
				if (node.getCh() == '\0')
					System.out.print(' ');
				else
					System.out.print(node.getCh());
				newNodes.add(node.getLeft());
				newNodes.add(node.getRight());
			} else {
				newNodes.add(null);
				newNodes.add(null);
				System.out.print(" ");
			}

			PrinterTree.printWhitespaces(betweenSpaces);
		}
		System.out.println("");

		for (int i = 1; i <= endgeLines; i++) {
			for (int j = 0; j < list.size(); j++) {
				PrinterTree.printWhitespaces(firstSpaces - i);
				if (list.get(j) == null) {
					PrinterTree.printWhitespaces(endgeLines + endgeLines + i + 1);
					continue;
				}

				if (list.get(j).getLeft() != null)
					System.out.print("/");
				else
					PrinterTree.printWhitespaces(1);

				PrinterTree.printWhitespaces(i + i - 1);

				if (list.get(j).getRight() != null)
					System.out.print("\\");
				else
					PrinterTree.printWhitespaces(1);

				PrinterTree.printWhitespaces(endgeLines + endgeLines - i);
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

		return Math.max(PrinterTree.maxLevel(root.getLeft()), PrinterTree.maxLevel(root.getRight())) + 1;
	}

	private static <T> boolean isAllElementsNull(List<T> list) {
		for (Object object : list) {
			if (object != null)
				return false;
		}

		return true;
	}

}