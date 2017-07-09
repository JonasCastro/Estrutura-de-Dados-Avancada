import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class BinaryFile {

	private char readOrWrite;
	public BufferedWriter inw;
	public BufferedReader inr;
	private static final int EOF = -1; // end of file

	private static int buffer; // one character buffer
	private static int n; // number of bits left in buffer

	public BinaryFile(String filename, char readOrWrite) {
		this.readOrWrite = readOrWrite;

		if (readOrWrite == 'w')
			try {
				this.inw = new BufferedWriter(new FileWriter(filename,true));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if (readOrWrite == 'r')
			try {

				this.inr = new BufferedReader(new FileReader(filename));
//				fillBuffer();
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
			buffer = EOF;
			n = -1;
		}
	}

	public void writeBit(boolean bit) {
		buffer <<= 1;
		if (bit) {
			buffer = buffer | 1;
		}
		n++;
		if (n == 8)
			clearBuffer();
	}

	private void clearBuffer() {
		if (n == 0)
			return;
		if (n > 0) {
			buffer <<= (8 - n);
		}
		try {
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
		if (endOfFile())
			throw new NoSuchElementException("Reading from empty input stream");
		n = oldN;
		x |= (buffer >>> n);
		return (char) (x & 0xff);

	}

	public void writeChar(char x) {
		if (x < 0 || x >= 256)
			throw new IllegalArgumentException("Illegal 8-bit char = " + x);
		writeByte(x);
	}

	private void writeByte(int x) {
		// optimized if byte-aligned
		if (n == 0) {
			try {
				inw.write(x);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// otherwise write one bit at a time
		for (int i = 0; i < 8; i++) {
			boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
			writeBit(bit);
		}
	}

	public void close() {
		try {
			if (readOrWrite == 'w') {
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

	public int readInt() {
		fillBuffer();
		int x = 0;
		for (int i = 0; i < 4; i++) {
			char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}

	public void writeInt(int x) {
		writeByte((x >>> 24) & 0xff);
		writeByte((x >>> 16) & 0xff);
		writeByte((x >>> 8) & 0xff);
		writeByte((x >>> 0) & 0xff);
	}
}
