package lda;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.headvances.util.IOUtil;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import data.Bin;
import data.tfidf;

public class VectorTopicModel {
	public static final String modelRes = "file:model.25.obj";

	public static List<Map<Integer, Double>> createVectorLda(List<String> docs, int begin)
			throws IOException, ClassNotFoundException {
		ObjectInputStream s = new ObjectInputStream(IOUtil.loadRes(modelRes));
		ParallelTopicModel model = (ParallelTopicModel) s.readObject();
		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequence2FeatureSequence());
		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		InstanceList testing = new InstanceList(instances.getPipe());
		TopicInferencer inferencer = model.getInferencer();

		// tao map vector bo xung lda
		List<Map<Integer, Double>> result = new ArrayList<>();
		for (int i = 0; i < docs.size(); i++) {
			Map<Integer, Double> temp = new TreeMap<>();
			testing.addThruPipe(new Instance(docs.get(i), null, "test instance", null));
			double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 100, 1, 5);
			for (int j = 0; j < testProbabilities.length; j++) {
				temp.put(begin + j, testProbabilities[j]);
			}
			result.add(temp);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		List<String> docs = Bin.getDictionary("input/filetest.txt");

		List<Map<Integer, Double>> resultLDA = new ArrayList<>();
		resultLDA = VectorTopicModel.createVectorLda(docs, 1917);

		List<String> wordlist = Bin.getDictionary("kl_test/500/wordlist.txt");
		List<List<String>> alltext = Bin.readFileToListString("kl_test/500/file_text_2.txt");
		List<Map<Integer, Double>> result = new ArrayList<>();
		
		// tao vector tf idf
		result = tfidf.createVectorTfidf(alltext, wordlist);

		System.out.println("Vector TF-IDF : " + printMap(result.get(6)));
		System.out.println("Vector LDA : " + printMap(resultLDA.get(0)));
		result.get(6).putAll(resultLDA.get(0));
		System.out.println("TF-IDF + LDA : " + printMap(result.get(6)));

	}

	public static String printMap(Map<Integer, Double> map) {
		StringBuilder end = new StringBuilder();
		int i = 0;
		end.append("{");
		for (Entry<Integer, Double> entry : map.entrySet()) {
			end.append(entry.getKey() + " " + entry.getValue() + ",");
			i++;
			if(i >=4) {
				end.append("\n");
				i=0;
			}
			
		}
		end.deleteCharAt(end.length() - 1);
		end.append("}\n");

		return end.toString();
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * ObjectInputStream s = new ObjectInputStream(IOUtil.loadRes(modelRes));
	 * ParallelTopicModel model = (ParallelTopicModel) s.readObject();
	 * 
	 * // Begin by importing documents from text to feature sequences
	 * ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
	 * 
	 * // Pipes: lowercase, tokenize, remove stopwords, map to features
	 * pipeList.add(new CharSequenceLowercase()); pipeList.add(new
	 * CharSequence2TokenSequence(Pattern
	 * .compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}"))); pipeList.add(new
	 * TokenSequence2FeatureSequence());
	 * 
	 * InstanceList instances = new InstanceList(new SerialPipes(pipeList));
	 * 
	 * String text =
	 * "biệt_thự khánh lâm xinh_đẹp phòng_ốc sạch_sẽ thuận_tiện chủ_nhà nhiệt_tình"
	 * ; InstanceList testing = new InstanceList(instances.getPipe());
	 * testing.addThruPipe(new Instance(text, null, "test instance", null));
	 * 
	 * TopicInferencer inferencer = model.getInferencer(); double[]
	 * testProbabilities = inferencer.getSampledDistribution( testing.get(0),
	 * 100, 1, 5); // for (int i = 0; i < testProbabilities.length; i++) //// if
	 * (testProbabilities[i] > 0.001) // System.out.println(i + "\t" +
	 * testProbabilities[i]); StringBuffer result = new StringBuffer("{"); for
	 * (int i = 0; i < testProbabilities.length; i++) { if (i %2==0 && i !=0)
	 * result.append(""); if (testProbabilities[i] > 0.001) { result.append(i+1
	 * + " " + testProbabilities[i]+","); } else { result.append(i+1 + " " + 0+
	 * ", "); } } result.deleteCharAt(result.length()-1);
	 * result.deleteCharAt(result.length()-1); result.append("}");
	 * System.out.println(result); }
	 */

}
