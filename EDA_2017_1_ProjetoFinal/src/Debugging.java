import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		return "" + (float) (tamanho * 8) / (tamanhot * 8);
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
		// ([\w]+)\.txt
		System.out.println("inicio");
		File fileFolder = new File("teste");
		// Double x = 1.0078902 + 0.88750166 +1.0130618 +1.0175654 +
		// 0.89982176+0.982323 + 0.97647303 + 1.0683519 + 0.9133574+ 0.99717313;
		// System.out.println(x);
		// System.out.println((Double)x/10);
		fileFolder.mkdirs();
		System.out.println("fim");
		// new File("teste/seila").mkdir();
		String name = "o/Co-r.txt";
		Pattern padrao = Pattern.compile("(([\\w-_>]+)\\.txt)");
		Matcher matcher = padrao.matcher(name);
		if (matcher.find()) {
			System.out.println(matcher.group());
			
			System.out.println("".equals(name.replace(matcher.group(), "")));
		}
		// Pattern palavraPT = Pattern.compile(palavraRE);
		// Matcher matcher;
		// String linha;
		// String str;
		// matcher = palavraPT.matcher(linha);
		// Object dicionario;
		// if (matcher.find() && !dicionario.contains(matcher.group(1))) {
		// str =
		// StringUtils.stripAccents(matcher.group(1).toLowerCase().trim());
		// dicionario.put(str.replaceAll("([\w]+)\.txt", "?"), "");
		// }
		// System.out.println(dicionario.keys());
		// return dicionario;

	}
}
