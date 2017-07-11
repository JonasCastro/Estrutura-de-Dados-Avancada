import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		// outfile = "saida";
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

		BinaryFile bf = new BinaryFile(outfile, 'w');
		// • Imprimir numero de bytes diretorio e diretorio
		bf.writeInt(infile.length());
		for (int j = 0; j < infile.length(); j++) {
			char c = infile.charAt(j);
			bf.writeChar(c);
		}
		bf.flush();
		// • Imprimir numero de bytes
		bf.writeInt(input.length);
		bf.flush();
		// • Imprimir a árvore de Huffman para o arquivo de saı́da

		writeTree(root, bf);

		bf.flush();

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
		bf.inw.newLine();
		bf.close();
		// BinaryFile bff = new BinaryFile(outfile, 'w');
		// • Imprimir numero de bytes diretorio e diretorio
		// bff.writeInt(10);
		// bff.flush();
		// bff.close();
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

	public static void expand(String infile, String d) throws IOException {

		BinaryFile bfr = new BinaryFile(infile, 'r');
		bfr.fillBuffer();

		/** Corre de 1 ate N sendo n o numero de arquivos comprimidos */
		int qtdFiles = bfr.readInt();
		for (int ir = 1; ir <= qtdFiles; ir++) {

			/** Le numero de bytes da descricao so diretorio */
			int lengthDirectory = bfr.readInt();

			/** Ler diretorio */
			String directory = "";
			for (int j = 0; j < lengthDirectory; j++)
				directory += bfr.readChar();

			/** •Le numero de bytes contidos no arquivo comprimido */
			int length = bfr.readInt();

			/** • Ler a árvore de Huffman do arquivo de entrada */
			Node root = readTrie(bfr);
			rooot = root;

			/** Caso nao exista,o diretorio "directory" é criado */
			mkdirs(directory);

			/** • Decodificar a entrada usando a árvore de Huffman */
			TextFile txw = new TextFile(directory, 'w');
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

			txw.close();
			bfr.inr.readLine();
			bfr.fillBuffer();
			
			if(d.equals("-v")){
			System.out.println("\n\nNOME DO ARQUIVO DESCOMPRIMIDO: "+nameArq(directory));

			System.out.println("\nÁRVORE DE HUFFMAN:\n");
			PrinterTree.printNode(rooot);
			}
		}
		bfr.close();
		
	}

	private static void mkdirs(String diretorio) {
		String mkDirectory = "";
		String[] files = diretorio.split("/");
		if (files.length > 1) {
			for (int i = 0; i < files.length - 1; i++) {
				mkDirectory = mkDirectory + files[i] + "/";
			}
		}
		File file = new File(mkDirectory);
		file.mkdirs();
	}

	public static List<String> listFiles(File file) {
		List<String> list = new ArrayList<>();

		if (file.isFile())
			list.add(file.getName());

		else if (file.isDirectory())
			listFiles(file, list);

		return list;

	}

	private static void listFiles(File file, List<String> list) {

		for (File ff : file.listFiles()) {

			if (ff.isDirectory())
				listFiles(ff, list);

			else if (ff.isFile())
				list.add(ff.getPath());

		}

	}

	public static void main(String[] args) throws IOException {
		args[2] = "-d";
		args[1] = "testMultCompress";
		args[0] = "teste";

		switch (args[2]) {
		case "-c":
			File file = new File(args[0]);
			List<String> listFiles = listFiles(file);

			(new File(args[1])).delete();
			BinaryFile bf = new BinaryFile(args[1], 'w');
			bf.writeInt(listFiles.size());
			bf.flush();
			bf.close();

			for (String ar : listFiles) {

				compress(ar, args[1]);

				if (args.length == 4 && args[3].equals("-v")) {
					
					System.out.println(Debugging.getInstance());
					System.out.println("\nÁRVORE DE HUFFMAN:");
					 PrinterTree.printNode(rooot);
				}

			}

			break;

		case "-d":

			if (args.length == 4 && args[3].equals("-v"))
				expand(args[1], args[3]);
			expand(args[1], "");
			

			break;

		default:
			throw new IllegalArgumentException("Illegal command line argument");
		}

	}

	private static String nameArq(String ar) {
		String[] directory = ar.split("/");
		if (directory.length > 1)
			return directory[directory.length - 1];
		else
			return ar;
	}

}
