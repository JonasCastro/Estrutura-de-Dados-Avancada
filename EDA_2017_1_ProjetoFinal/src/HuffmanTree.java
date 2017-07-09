import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

		// compare, based on f||requency
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
	private static Node buildTree(int[] freq) {

		// initialze priority queue with singleton trees
		HeapBinario<Node> heap = new HeapBinario<>();
		for (char i = 0; i < R; i++) {
			if (freq[i] > 0) {
				heap.insert(new Node(i, freq[i], null, null));

			}
		}

		// special case in case there is only one character with a nonzero
		// TODO ???
		if (heap.size() == 1) {
			if (freq['\0'] == 0)
				heap.insert(new Node('\0', 0, null, null));
			else
				heap.insert(new Node('\1', 0, null, null));
		}
		while (heap.size() > 1) {
			Node left = precedencia(heap.extractMin(), heap);
			Node right = precedencia(heap.extractMin(), heap);
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			heap.insert(parent);
		}
		return heap.extractMin();
	}

	public static Node precedencia(Node left, HeapBinario<Node> heap) {
		if (heap.size() == 0 || !(heap.min().freq == left.freq))
			return left;

		if (!heap.min().isLeaf() && left.isLeaf()) {
			Node aux = left;
			Node prec = precedencia(heap.extractMin(), heap);
			heap.insert(aux);
			return prec;

		} else if (!heap.min().isLeaf() && !left.isLeaf()) {
			// TODO FAZER

		} else if (heap.min().isLeaf() && left.isLeaf())
			if (heap.min().ch < left.ch) {
				Node aux = left;
				Node prec = precedencia(heap.extractMin(), heap);
				heap.insert(aux);
				return prec;
			}

		Node aux = heap.extractMin();
		Node prec = precedencia(left, heap);
		heap.insert(aux);
		return prec;

	}

	private static void compress(String infile, String outfile) throws IOException {
		// infile = "tinyTale";
		outfile = "saida";
		// • Ler um arquivo de entrada
		TextFile textFileRead = new TextFile(infile, 'r');
		String s = textFileRead.readLineAll();
		textFileRead.close();
		char[] input = s.toCharArray();

		// • calcular a frequência de todos os caracteres
		int[] freq = calculateFrequency(input);

		// • Construir uma árvore de Huffman
		Node root = buildTree(freq);
		rooot = root;

		// • Construir uma tabela de sı́mbolos que contém os códigos
		String[] codigos = new String[R];
		buildCode(codigos, root, "");

		// • Imprimir a árvore de Huffman para o arquivo de saı́da
		BinaryFile bf = new BinaryFile(outfile, 'w');
		writeTree(root, bf);
		bf.flush();

		// • Imprimir numero de bytes
		bf.writeInt(input.length);

		// • Usar a tabela de sı́mbolos para codificar o arquivo
		for (int i = 0; i < input.length; i++) {
			String code = codigos[input[i]];
			// • Imprimir a versão codificada do arquivo de entrada para o
			// arquivo de saı́da
			for (int j = 0; j < code.length(); j++) {
				if (code.charAt(j) == '0')
					bf.writeBit(false);
				else if (code.charAt(j) == '1')
					bf.writeBit(true);
				else
					throw new IllegalStateException("Illegal state");
			}
		}
		bf.flush();
		bf.close();
		buildDebugging(infile, outfile, input, codigos, freq);
	}

	private static void buildDebugging(String infile, String outfile, char[] input, String[] codigos, int[] freq) {
		Debugging debugging = Debugging.getInstance();
		debugging.setNomeArquivo(outfile);
		debugging.setNomeArquivoIn(infile);
		debugging.setInput(input);
		debugging.setCodigos(codigos);
		debugging.setFrequencia(freq);

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

	// write bitstring-encoded trie to standard output
	private static void writeTree(Node x, BinaryFile bf) {
		if (x.isLeaf()) {
			bf.writeBit(true);
			bf.writeChar(x.getCh());
			return;
		}
		bf.writeBit(false);
		writeTree(x.getLeft(), bf);
		writeTree(x.getRight(), bf);
	}

	public static int[] calculateFrequency(char[] input) {
		// tabulate frequency counts
		int[] freq = new int[R];
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;
		return freq;
	}

	private static Node readTrie(BinaryFile bfr) throws IOException {

		boolean isLeaf = bfr.readBit();
		if (isLeaf) {
			char c = bfr.readChar();
			return new Node(c, -1, null, null);
		} else {
			return new Node('\0', -1, readTrie(bfr), readTrie(bfr));
		}

	}

	public static void expand(String infile, String outfile) throws IOException {
		// infile = "saida";
		// outfile = "saida2";
		// • Ler a árvore de Huffman do arquivo de entrada
		BinaryFile bfr = new BinaryFile(infile, 'r');
		Node root = readTrie(bfr);
		rooot = root;

		// • Ler numero de bytes
		int length = bfr.readInt();

		// • Decodificar a entrada usando a árvore de Huffman
		TextFile txw = new TextFile(outfile, 'w');
		for (int i = 0; i < length; i++) {
			Node x = root;
			while (!x.isLeaf()) {
				boolean bit = bfr.readBit();
				if (bit)
					x = x.getRight();
				else
					x = x.getLeft();
			}
			txw.writeChar(x.getCh());
		}
		bfr.close();
		txw.close();
	}

	public static void main(String[] args) throws IOException {

		switch (args[2]) {
		case "-c":

			compress(args[0], args[1]);

			if (args.length == 4 && args[3].equals("-v")) {

				System.out.println("\n\nÁrvore de Huffman:");
				 BTreePrinter.printNode(rooot);
				System.out.println(Debugging.getInstance());
			}

			break;

		case "-d":

			expand(args[0], args[1]);

			if (args.length == 4 && args[3].equals("-v")) {
				System.out.println("\n\nNome do arquivo descomprimido:");
				System.out.println(args[1]);

				System.out.println("\n\nÁrvore de Huffman:");
				BTreePrinter.printNode(rooot);
			}

			break;

		default:
			throw new IllegalArgumentException("Illegal command line argument");
		}

	}

}
