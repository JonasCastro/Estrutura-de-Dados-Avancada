import java.io.IOException;

import javax.security.auth.PrivateCredentialPermission;


public class HuffmanTree {
	// alphabet size of extended ASCII
	private static final int R = 256;
	static Node rooot;

	// Huffman trie node
	static class Node implements Comparable<Node> {
		private final char ch;
		private final int freq;
		private final Node left, right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		// is the node a leaf node?
		private boolean isLeaf() {
			assert ((getLeft() == null) && (getRight() == null)) || ((getLeft() != null) && (getRight() != null));
			return (getLeft() == null) && (getRight() == null);
		}

		// compare, based on frequency
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}

		public Node getLeft() {
			return left;
		}

		public Node getRight() {
			return right;
		}

		public char getCh() {
			return ch;
		}

		public int getFreq() {
			return freq;
		}

	}

	// build the Huffman trie given frequencies
	private static Node buildTrie(int[] freq) {

		// initialze priority queue with singleton trees
		HeapBinario<Node> pq = new HeapBinario();
		for (char i = 0; i < R; i++) {
			// System.out.println("TESTE "+ (char) i);
			if (freq[i] > 0) {
				// System.out.println("MANDOU "+ (char) i);
				pq.insert(new Node(i, freq[i], null, null));

			}
		}

		// special case in case there is only one character with a nonzero
		// frequency
		if (pq.size() == 1) {
			if (freq['\0'] == 0)
				pq.insert(new Node('\0', 0, null, null));
			else
				pq.insert(new Node('\1', 0, null, null));
		}
		// pq.delMin();
		// merge two smallest trees
		while (pq.size() > 1) {
			// for(Node x : pq){
			// System.out.print("("+ x.freq +","+x.ch +")");
			//
			// }

			System.out.println("ETRACMIN 1");
			// for (Node x : pq) {
			// System.out.print("(" + x.freq + "," + x.ch + ")");
			//
			// }
			for (int i = 0; i < pq.size(); i++) {
				System.out.print("(" + pq.getHeap().get(i).freq + "," + pq.getHeap().get(i).ch + ")");
				//
			}
			System.out.println();
			Node left = pq.extractMin();
			left = precedencia(left, pq);
			System.out.println("ETRACMIN 2");
			for (int i = 0; i < pq.size(); i++) {
				System.out.print("(" + pq.getHeap().get(i).freq + "," + pq.getHeap().get(i).ch + ")");
				//
			}
//			 for (Node x : pq) {
//			 System.out.print("(" + x.freq + "," + x.ch + ")");
//			
//			 }
			
			System.out.println();
			Node right = pq.extractMin();
			right = precedencia(right, pq);
			System.out.println(left.ch + "------------------" + right.ch);
			Node parent = new Node('-', left.freq + right.freq, left, right);

			// System.out.println("INSERT");

			BTreePrinter.printNode(parent);

			// for(Node x : pq){
			// System.out.print("("+ x.ch +","+x.freq +")");
			//
			// }
			// for (int i = 0; i< pq.size(); i++){
			// System.out.print("("+ pq.getHeap().get(i).freq
			// +","+pq.getHeap().get(i).ch +")");
			//
			// }
			System.out.println();
			pq.insert(parent);
			// for (int i = 0; i< pq.size(); i++){
			// System.out.print("("+ pq.getHeap().get(i).freq
			// +","+pq.getHeap().get(i).ch +")");
			//
			// }
			System.out.println();
			System.out.println("novo processo");
		}
		return pq.extractMin();
	}

	public static Node precedencia(Node left, HeapBinario<Node> pq) {
		// if(pq.Min().ch == '-')
		if (pq.size() == 0)
			return left;
		System.out.print("(" + pq.min().freq + "," + pq.min().ch + "),");
		System.out.print("(" + left.freq + "," + left.ch + ")");
		System.out.println();
		if (!(pq.min().freq == left.freq)) {
			return left;

		}

		if (!pq.min().isLeaf() && left.isLeaf()) {
			Node aux = left;
			Node prec = precedencia(pq.extractMin(), pq);
			pq.insert(aux);
			return prec;

			// } else if (!pq.Min().isLeaf() && !left.isLeaf()) {

		} else if (pq.min().isLeaf() && left.isLeaf()) {
			if (pq.min().ch < left.ch) {
				Node aux = left;
				Node prec = precedencia(pq.extractMin(), pq);
				pq.insert(aux);
				return prec;

			}

		}
		Node aux = pq.extractMin();
		Node prec = precedencia(left, pq);
		pq.insert(aux);
		return prec;
		// }
		// return left;

	}

	private static void compress() throws IOException {
		// read the input
		TextFile textFileRead = new TextFile("infile", 'r');

		String s = textFileRead.readString();
		System.out.println("->" + s.substring(0, s.length() - 1));
		char[] input = s.substring(0, s.length() - 1).toCharArray();

		int[] freq = calculateFrequency(input);

		for (int i = 0; i < input.length; i++) {
			// if (freq[i] != 0)
			System.out.println(input[i] + "  " + freq[input[i]]);

		}
		// build Huffman trie
		Node root = buildTrie(freq);
		rooot =root;
		// build code table
		String[] st = new String[R];
		buildCode(st, root, "");
		for (int i = 0; i < input.length; i++) {
			// if (freq[i] != 0)
			System.out.println(input[i] + "  " + st[input[i]]);

		}

		//
		// // print trie for decoder
		// writeTrie(root);
		//
		// // print number of bytes in original uncompressed message
		// BinaryStdOut.write(input.length);
		//
		// // use Huffman code to encode input
		 for (int i = 0; i < input.length; i++) {
		 String code = st[input[i]];
		 System.out.println(input[i] + " :" + code);
		for (int j = 0; j < code.length(); j++) {
			if (code.charAt(j) == '0') {
				BinaryStdOut.write(false);
			} else if (code.charAt(j) == '1') {
				BinaryStdOut.write(true);
			} else
				throw new IllegalStateException("Illegal state");
		}
	}BTreePrinter.printNode(root);

	// printString(root,"");
	// // close output stream
	// BinaryStdOut.close();
	}

	// make a lookup table from symbols and their encodings
	private static void buildCode(String[] st, Node x, String s) {
		if (!x.isLeaf()) {
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		} else {
			st[x.ch] = s;
		}
	}

	public static int[] calculateFrequency(char[] input) {
		// tabulate frequency counts
		int[] freq = new int[R];
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;
		return freq;
	}

	public static void printString(Node r, String recoil) {
		if (r != null) {
			printString(r.right, recoil + "         ");
			System.out.println(recoil + r.ch + ",");
			// printString(r.center, recoil + " ");
			printString(r.left, recoil + "         ");
		} else
			System.out.println(recoil + "-");
	}
	public static void expand() {

		// read in Huffman trie from input stream
		Node root = rooot;

		// number of bytes to write
		int length = BinaryStdIn3.readInt();
		System.out.println(length);
		// decode using the Huffman trie
		for (int i = 0; i < length; i++) {
			
			Node x = root;
			while (!BinaryStdIn2.isEmpty() && !x.isLeaf()) {
//				System.out.println("ff");
				
				boolean bit = BinaryStdIn2.readBoolean();
				System.out.println("->"+bit);
				if (bit)
					x = x.getRight();
				else
					x = x.getLeft();
			}
//			System.out.println("->"+x.getCh());
			BinaryStdOut2.write(x.getCh(), 8);
		}
		BinaryStdOut2.close();
	}

	public static void main(String[] args) throws IOException {
		System.out.println(args[0]);
		System.out.println(args[1]);
		System.out.println(args[2]);
		System.out.println(args[3]);

		if (args[0].equals("-c")) {
			compress();
			expand();

		} else if (args[0].equals("-d")) {

		}
		// 
		else
			throw new IllegalArgumentException("Illegal command line argument");

	}

}
