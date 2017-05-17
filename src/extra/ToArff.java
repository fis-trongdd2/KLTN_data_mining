package extra;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToArff {
	public static Map<Integer,Integer> countword (List<String> word, List<String> dl) {
		Map<Integer,Integer> result = new HashMap<>();
		int count = 0;
		for (int i = 0 ; i < word.size(); i++) {
			count = 0;
			for (String r : dl) {
				if (word.get(i).equals(r)) {
					count++;
				}
			}
			result.put(i,count);
		} 
		return result;
	}
	public static void main (String []args) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> word =Main.readFileToListString("input/dl/word-list.txt");
		List<String> dl =Main.readFileToListString("input/dl/data2_10.txt");
		List<Map<Integer,Integer>> result = new ArrayList<>();
		Map<Integer,Integer> map ;
		List<String> dltemp2;
		for (String row : dl) {
			map = new HashMap<>();
			row = row.substring(0,row.lastIndexOf("\t")+1);
			dltemp2 = new ArrayList<String>(Arrays.asList(row.split(" "))); //part row to list
			map = countword(word, dltemp2);
			result.add(map);
		}
		List<String> listlaynhan5 = Main.readFileToListString("input/dl/data2_10.txt");
		List<String> listnhan5 =  new ArrayList<>();
		for (String str : listlaynhan5) {
			listnhan5.add(str.substring(str.lastIndexOf("\t")+1));
		}
		
		StringBuilder end = new StringBuilder();
		end.append("@relation gap\n");
		for (int i = 0; i < word.size(); i++) {
			end.append("@attribute word_" + i+" numeric\n");
		}
		for (int i = 1; i < 11; i++) {
			end.append("@attribute label_" + i+ " {0,1}\n");
		}
		System.out.println(listlaynhan5.size());
		System.out.println(result.size());
		for (int i = 0; i < result.size(); i++) {
			end.append("{");
			for (Map.Entry<Integer, Integer> entry : result.get(i).entrySet()){
				end.append(entry.getKey() + " " + entry.getValue() + ",");
			}
			List<String> temp = Arrays.asList(listnhan5.get(i).split(";"));
			for (int j = 1; j < 11; j++) {
				if (temp.contains(String.valueOf(j))) {
					end.append (word.size()-1+j+ " 1,");
				}
				else {
					end.append (word.size()-1+j+ " 0,");
				}
			}
			end.deleteCharAt(end.length()-1);
			end.append("}\n");
		}
		Main.writeToFileUtf8Buffer("input/dl/arff.arff", end.toString());

		
		
	}
}
