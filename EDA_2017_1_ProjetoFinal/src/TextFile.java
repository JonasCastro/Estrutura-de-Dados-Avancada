import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class TextFile {
	private char readOrWrite;
	public BufferedWriter inw;
	public BufferedReader inr;
	private static final int EOF = -1; // end of file
	private static int n; // number of bits left in buffer

	private static int buffer; // one character buffer

	public TextFile(String filename, char readOrWrite) throws IOException {

		this.readOrWrite = readOrWrite;
		if (readOrWrite == 'w')
			this.inw = new BufferedWriter(new FileWriter(filename));
		else if (readOrWrite == 'r'){
		
			this.inr = new BufferedReader(new FileReader(filename));
//			fillBuffer();
		}
	}

	public String readLineAll() {
		String string = new String();
//		fillBuffer();
		buffer = 0;
		
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");

		while (!endOfFile()) {
			char c = readChar();
			if (c >= 0 && c < 255) 
				string = string + c;
				

		}

		return string;
	}

	public boolean endOfFile() {
		return buffer == EOF;
	}

	public void fillBuffer() {
		try {
			buffer = inr.read();
			n = 8;
		} catch (IOException e) {
			buffer = EOF;
			n = -1;
		}
	}

	public char readChar() {
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");

		// special case when aligned byte
		fillBuffer();
		int x = buffer;
		return (char) (x & 0xff);
	}
	
	// Sem Necessidade
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


	public void writeChar(char c) {
		try {
			inw.write(c);
			inw.flush();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

	}


}
