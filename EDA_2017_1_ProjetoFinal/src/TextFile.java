import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class TextFile {
	private String filename;
	private char readOrWrite;
	public BufferedWriter inw;
	public BufferedReader inr;
	private static final int EOF = -1; // end of file

	private static int buffer; // one character buffer
	private static int n; // number of bits left in buffer

	public TextFile(String filename, char readOrWrite) throws IOException {

		this.filename = filename;
		this.readOrWrite = readOrWrite;

		if (readOrWrite == 'w')
			this.inw = new BufferedWriter(new FileWriter(filename));
		else if (readOrWrite == 'r')
			this.inr = new BufferedReader(new FileReader(filename));
	}

	public String readString() {
		String string = new String();
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");

		while (!endOfFile()) {
			char c = readChar();
			string = string + c;
			System.out.println("c= " + c);
			System.out.println(string);

		}

		return string;
	}

	public boolean endOfFile() {
		return buffer == EOF;
	}

	private void fillBuffer() {
		try {
			buffer = inr.read();
			n = 8;
		} catch (IOException e) {
			System.out.println("EOF");
			buffer = EOF;
			n = -1;
		}
	}

	public char readChar() {
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");

		// special case when aligned byte
		// if (n == 8) {
		fillBuffer();
		int x = buffer;

		return (char)  (x & 0xff);
		// }

		// // combine last n bits of current buffer with first 8-n bits of new
		// // buffer
		// int x = buffer;
		// x <<= (8 - n);
		// int oldN = n;
		// fillBuffer();
		// if (endOfFile())
		// throw new NoSuchElementException("Reading from empty input stream");
		// n = oldN;
		// x |= (buffer >>> n);
		// return (char) (x & 0xff);
		// // the above code doesn't quite work for the last character if n = 8
		// // because buffer will be -1, so there is a special case for aligned
		// // byte
	}

	public void writeChar(char c) {
		try {
			System.out.println(c);
			inw.append(c);

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

	}

	public void rewind() {

	}

	public void close() throws IOException {

		try {
			if (readOrWrite == 'w')
				inw.close();
			else if (readOrWrite == 'r')
				inr.close();
		} catch (IOException ioe) {
			throw new IllegalStateException("Could not close BinaryStdIn", ioe);
		}

	}
	public static void main(String[] args) {
		System.out.println("!".compareTo("C") > 0);
	}

	public void writeChar(int c) {
		try {
			System.out.println("------------------------------"+""+c);
			inw.write(c);
//			inw.newLine();
			inw.flush();
			// Fechando conexão e escrita do arquivo.

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}
		
	}
	
	
	
	
	

}
