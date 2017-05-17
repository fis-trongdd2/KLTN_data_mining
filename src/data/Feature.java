package data;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import extra.Main;

public class Feature {

	public static void main(String a[]) throws FileNotFoundException, UnsupportedEncodingException {
		// xay dung tap vector dac trung
		List<List<String>> result = Bin.readFileToListString("kl_test/file_text.txt");
		Map<String, Integer> listword = new HashMap<>();
		for (List<String> listStr : result) {
			for (String str : listStr) {
				if (listword.containsKey(str)) {
					listword.put(str, listword.get(str).intValue() + 1);
				} else {
					listword.put(str, 1);
				}
			}
		}
		List<String> words = new ArrayList<>(listword.keySet());
		List<String> stopword = Bin.getDictionary("input/stopword.txt");
		StringBuffer wordtofile = new StringBuffer("");
		int count = 1;
		for (int i = 0; i < words.size(); i++) {
			if (stopword.contains(words.get(i))) {
				words.remove(i);
				i--;
			} else {
				wordtofile.append(i + 1 + " " + words.get(i) + "; ");
				count++;
			}

			if (count % 8 == 0) {
				wordtofile.append("\n");
				count = 1;
			}
		}
		List<String> temp = new ArrayList<>();
		temp.add(wordtofile.toString());
//		Main.writeToFileUtf8("kl_test/features.txt", temp);
		Main.writeToFileUtf8("kl_test/500/wordlist.txt", words);
		System.out.println(temp);

	}

}