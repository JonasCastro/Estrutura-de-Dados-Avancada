import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileDemo {

	public static void testa(File file, List<String> d) {

		for (File ff : file.listFiles()) {

			if (ff.isDirectory())
				testa(ff, d);

			else if (ff.isFile()) {
				System.out.println(ff.getName());
				d.add(ff.getPath());
			}
		}

	}

	public static void main(String[] args) {
		File f = null;
		String path;
		boolean bool = false;

		try {
			List<String> d = new ArrayList<>();
			// create new file
			f = new File("teste");
			testa(new File("teste"), d);
			System.out.println(d);
			// true if the file path is directory, else false
			// bool = f.isDirectory();
			// String[] sa = f.list();
			// for (String s : sa) {
			// f = new File(f.getPath()+"/"+s);
			// System.out.println(f.isDirectory());
			// System.out.println(f.getPath());
			// }
			//
			// // get the path
			// path = f.getPath();
			//
			// // prints
			// System.out.println(path + " is directory? " + bool);
			//
			// // create new file
			// f = new File("c:/test.txt");
			//
			// // true if the file path is directory, else false
			// bool = f.isDirectory();
			//
			// // get the path
			// path = f.getPath();
			//
			// // prints
			// System.out.print(path + " is directory? " + bool);

		} catch (Exception e) {

			// if any error occurs
			e.printStackTrace();
		}
	}
}