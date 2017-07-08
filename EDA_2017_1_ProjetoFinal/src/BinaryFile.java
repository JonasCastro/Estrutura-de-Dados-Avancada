import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class BinaryFile {

	private String filename;
	private char readOrWrite;
	public BufferedWriter inw;
	public BufferedReader inr;
	private static final int EOF = -1; // end of file

	private static int buffer; // one character buffer
	private static int n; // number of bits left in buffer

	public BinaryFile(String filename, char readOrWrite) {
		this.filename = filename;
		this.readOrWrite = readOrWrite;

		if (readOrWrite == 'w')
			try {
				this.inw = new BufferedWriter(new FileWriter(filename));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if (readOrWrite == 'r')
			try {
				this.inr = new BufferedReader(new FileReader(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public boolean endOfFile() {
		return buffer == EOF;

	}

	public boolean readBit() {

		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");
		n--;
		boolean bit = ((buffer >> n) & 1) == 1;
		if (n == 0)
			fillBuffer();
		return bit;

	}

	public void fillBuffer() {
		try {
			buffer = inr.read();

			n = 8;
		} catch (IOException e) {
			System.out.println("EOF");
			buffer = EOF;
			n = -1;
		}
	}

	public void writeBit(boolean bit) {
		System.out.println("writeBit : bit chegou  " + bit);
		// add bit to buffer
		System.out.println("writeBit como ta buffer: " + buffer);
		buffer <<= 1;
		System.out.println("writeBit como ta buffer agora: " + buffer);
		if (bit) {
			System.out.println(buffer | 1);
			buffer = buffer | 1;
		}
		System.out.println("writeBit como ta buffer APOS ADD: " + buffer);
		// if buffer is full (8 bits), write out as a single byte
		System.out.println("writeBit como ta : " + buffer);
		n++;
		System.out.println("n=" + n);
		if (n == 8)
			clearBuffer();
	}

	// write out any remaining bits in buffer to standard output, padding with
	// 0s
	// write out any remaining bits in buffer to standard output, padding with
	// 0s
	private void clearBuffer() {
		if (n == 0)
			return;
		if (n > 0)
			buffer <<= (8 - n);
		try {
			System.out.println("clearBuffer : " + buffer);
			inw.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		n = 0;
		buffer = 0;
	}

	public char readChar() {
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");

		// special case when aligned byte
		if (n == 8) {
			int x = buffer;
			fillBuffer();
			return (char) (x & 0xff);
		}

		// combine last n bits of current buffer with first 8-n bits of new
		// buffer
		int x = buffer;
		x <<= (8 - n);
		int oldN = n;
		fillBuffer();
		// if (endOfFile())
		// throw new NoSuchElementException("Reading from empty input stream");
		n = oldN;
		x |= (buffer >>> n);
		return (char) (x & 0xff);
		// the above code doesn't quite work for the last character if n = 8
		// because buffer will be -1, so there is a special case for aligned
		// byte

	}

	public void writeChar(char x) {
		if (x < 0 || x >= 256)
			throw new IllegalArgumentException("Illegal 8-bit char = " + x);
		System.out.println("writeChar: " + x);
		writeByte(x);
	}

	private void writeByte(int x) {

		// optimized if byte-aligned
		if (n == 0) {
			try {
				System.out.println("writeByte IF: " + x);
				inw.write(x);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// otherwise write one bit at a time
		for (int i = 0; i < 8; i++) {
			boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
			System.out.println("writeByte FOR: " + bit);
			writeBit(bit);
		}
	}

	public void close() {
		try {
			if (readOrWrite == 'w') {
				flush();
				inw.close();
			} else if (readOrWrite == 'r')
				inr.close();
		} catch (IOException ioe) {
			throw new IllegalStateException("Could not close BinaryStdIn", ioe);
		}
	}

	public void flush() {
		clearBuffer();
		try {
			inw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
