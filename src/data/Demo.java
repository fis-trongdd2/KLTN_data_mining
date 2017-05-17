package data;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Demo {
	public static void main(String []args) throws FileNotFoundException, UnsupportedEncodingException {
		List<List<String>> result = Bin.readFileToListString("kl_test/file_text.txt");
		int cout = 0;
		for (int i = 0; i < result.size();i++) {
			cout+= result.get(i).size();
		}
		System.out.println(cout);
	}
}
