import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Debugging {

	private String nomeArquivo;
	private String nomeArquivoIn;
	private long taxadeCompressao;
	private int[] frequencia;
	private String[] codigos;
	private char[] input;

	private static Debugging dep = new Debugging();

	private Debugging() {
	}

	public static Debugging getInstance() {
		return dep;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivoComprimido) {
		this.nomeArquivo = nomeArquivoComprimido;
	}

	public long getTaxadeCompressao() {
		return taxadeCompressao;
	}

	public void setTaxadeCompressao(long taxadeCompressao) {
		this.taxadeCompressao = taxadeCompressao;
	}


	public int[] getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(int[] frequencia) {
		this.frequencia = frequencia;
	}

	public String[] getCodigos() {
		return codigos;
	}

	public void setCodigos(String[] codigo) {
		this.codigos = codigo;
	}

	public char[] getInput() {
		return input;
	}

	public void setInput(char[] input) {
		this.input = input;
	}

	public String getNomeArquivoIn() {
		return nomeArquivoIn;
	}

	public void setNomeArquivoIn(String nomeArquivoIn) {
		this.nomeArquivoIn = nomeArquivoIn;
	}

	@Override
	public String toString() {
		return "\nNome do arquivo comprimido: " + nomeArquivo + "\n\nTaxa de compressão: " + taxaCompressao()
				+ "\n\nFrequencia:\n" + frequencia() + "\n\nCodigo:\n" + codigo();
	}

	public String taxaCompressao() {

		File in = new File(nomeArquivoIn);
		long tamanhot = in.length();
		File out = new File(nomeArquivo);
		long tamanho = out.length();

		return "" + (float)(tamanho * 8) / (tamanhot * 8);
	}

	private String frequencia() {
		Queue<Character> fila = new LinkedList<>();
		String string = "";
		for (int i = 0; i < input.length; i++) {

			if (!fila.contains(input[i])) {
				string += input[i] + " " + frequencia[input[i]] + "\n";
				fila.add(input[i]);
			}
		}
		return string;
	}

	private String codigo() {
		Queue<Character> fila = new LinkedList<>();
		String string = "";
		for (int i = 0; i < input.length; i++) {
			if (!fila.contains(input[i])) {
				string += input[i] + " " + codigos[input[i]] + "\n";
				fila.add(input[i]);
			}

		}

		return string;
	}
	public static void main(String[] args) {
		Double x = 1.0078902 + 0.88750166 +1.0130618 +1.0175654 + 0.89982176+0.982323 + 0.97647303 + 1.0683519 + 0.9133574+ 0.99717313;
		System.out.println(x);
		System.out.println((Double)x/10);
	}

}
