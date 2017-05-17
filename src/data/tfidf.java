package data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import extra.Main;
import lda.VectorTopicModel;

public class tfidf {
	/**
	 * tuan suat xuat hien cua tu trong van ban = so lan xuat hien/ so tu
	 */
	public double tf(List<String> doc, String term) {
		double result = 0;
		for (String word : doc) {
			if (term.equalsIgnoreCase(word))
				result++;
		}
		return result / doc.size();
	}

	/**
	 * log(so van ban/ so van ban co tu term)
	 */
	public double idf(List<List<String>> docs, String term) {
		double n = 0;
		for (List<String> doc : docs) {
			for (String word : doc) {
				if (term.equalsIgnoreCase(word)) {
					n++;
					break;
				}
			}
		}
		return Math.log(docs.size() / n);
	}

	/**
	 * tf.idf = tf * idf
	 */
	public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
		return tf(doc, term) * idf(docs, term);

	}

	public static List<Map<Integer, Double>> createVectorTfidf(List<List<String>> docs, List<String> wordlist) {
		tfidf calculator = new tfidf();
		double tfidf = 0;
		List<Map<Integer, Double>> result = new ArrayList<>();
		for (List<String> doc : docs) {
			Map<Integer, Double> temp = new TreeMap<>();
			for (int i = 0; i < wordlist.size(); i++) {
				tfidf = calculator.tfIdf(doc, docs, wordlist.get(i));
				if (tfidf > 0) {
					temp.put(i, tfidf);
				}
			}
			result.add(temp);
		}
		return result;
	}

	public static List<List<Integer>> readLabel(String link) {
		List<List<Integer>> listCombinations_ = new ArrayList<>();
		Path filePath = Paths.get(link);
		Scanner scanner = null;
		ArrayList<Integer> con = new ArrayList<Integer>();
		try {
			scanner = new Scanner(filePath);
			int j = 0;
			while (scanner.hasNext()) {
				if (scanner.hasNextInt()) {
					int q = scanner.nextInt();
					con.add(q);
					j++;
					if (j >= 5) {
						listCombinations_.add(con);
						con = new ArrayList<Integer>();
						j = 0;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listCombinations_;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		List<String> wordlist = Bin.getDictionary("kl_test/500/wordlist.txt");
		List<List<String>> alltext = Bin.readFileToListString("kl_test/500/file_text.txt");
		List<List<Integer>> listLabel = readLabel("kl_test/500/label.txt");
		Integer max = 1423;
		List<Map<Integer, Double>> result = new ArrayList<>();

		// tao vector tf idf
		result = createVectorTfidf(alltext, wordlist);

		// them dac trung lda
		List<String> docs = Bin.getDictionary("kl_test/500/file_text.txt");
		List<Map<Integer, Double>> resultLDA = new ArrayList<>();
		resultLDA = VectorTopicModel.createVectorLda(docs, max);
		for (int i = 0; i < result.size(); i++) {
			result.get(i).putAll(resultLDA.get(i));
		}
		max += 25;

		// them nhan
		List<Integer> temp = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			temp = listLabel.get(i);
			for (int j = 0; j < temp.size(); j++) {
				result.get(i).put(j + max, (double) temp.get(j));
			}
		}
		System.out.println(result.toString());

		StringBuilder end = new StringBuilder();
		end.append("@relation gap\n");
		for (int i = 0; i < max; i++) {
			end.append("@attribute word_" + i + " numeric\n");
		}
		for (int i = 1; i < 6; i++) {
			end.append("@attribute label_" + i + " {0,1}\n");
		}
		// end.append("\n@data\n");
		// for (int i = 0; i < result.size(); i++) {
		// end.append("{");
		// for (Entry<Integer, Double> entry : result.get(i).entrySet()) {
		// if (entry.getKey() >= max) {
		// end.append(entry.getKey() + " " + entry.getValue().intValue() + ",");
		// } else {
		// end.append(entry.getKey() + " " + entry.getValue() + ",");
		// }
		//
		// }
		// end.deleteCharAt(end.length() - 1);
		// end.append("}\n");
		// }
		// Main.writeToFileUtf8Buffer("kl_test/arff.arff", end.toString());
		end.append("\n@data\n");
		for (int i = 0; i < 450; i++) {
			end.append("{");
			for (Entry<Integer, Double> entry : result.get(i).entrySet()) {
				if (entry.getKey() >= max) {
					end.append(entry.getKey() + " " + entry.getValue().intValue() + ",");
				} else {
					end.append(entry.getKey() + " " + entry.getValue() + ",");
				}

			}
			end.deleteCharAt(end.length() - 1);
			end.append("}\n");
		}
		Main.writeToFileUtf8Buffer("kl_test/500/arff_train_500_2.arff", end.toString());

	}

	// public static void main(String[] args) throws FileNotFoundException,
	// UnsupportedEncodingException {
	// List<String> wordlist = Bin.getDictionary("output/wordlist.txt");
	// List<List<String>> alltext =
	// Bin.readFileToListString("input/file_text.txt");
	//
	// List<Object> lst = alltext.stream().flatMap(x ->
	// x.stream()).collect(Collectors.toList());
	// List<String> doc1 = alltext.get(6);
	// tfidf calculator = new tfidf();
	// double tfidf = 0;
	// StringBuffer result = new StringBuffer("");
	// result.append("Document : ");
	// for (String str : doc1) {
	// result.append(str + " ");
	// }
	// result.deleteCharAt(result.length() - 1);
	//
	// result.append("\nVector TF-IDF : \n{");
	// String str = ",\n";
	// for (int i = 0; i < wordlist.size(); i++) {
	// if (i == wordlist.size() - 1) {
	// str = "";
	// }
	// tfidf = calculator.tfIdf(doc1, alltext, wordlist.get(i));
	// if (tfidf > 0) {
	// result.append(i + " " + tfidf + str);
	// }
	// }
	// result.deleteCharAt(result.length() - 1);
	// result.deleteCharAt(result.length() - 1);
	// result.append("}");
	// System.out.println(result.toString());
	// }

}
