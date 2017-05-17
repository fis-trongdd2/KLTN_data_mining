package extra;
import main.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Main {
	
	//tinh tung dong du lieu cho ra 1 map
	public static void writeToFileUtf8 (String link,List<String > listData) {
			try {
			File file = new File(link);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw =  new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));
			for(int i = 0; i < listData.size();i++){
				bw.write(listData.get(i));
				bw.newLine();
			}
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeToFileUtf8Buffer (String link,String  listData) {
		try {
		File file = new File(link);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw =  new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));
			bw.write(listData );
		bw.close();
		System.out.println("Done");
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	public static Map<String,Integer> compute (List<String> words,String dl) {
		int count;
		Map<String, Integer> result = new HashMap <>();
		List<String> dls = new ArrayList<String>(Arrays.asList(dl.split(" ")));
		for (String word : words) {
			count = 0;
			for (String d : dls) {
				if (word.equals(d)) {
					count++;
				}
			}
			result.put(word, count);
		}
		return result;
	}
	public static String parseLabel (String label) {
		String result = "";
		if(label.contains("1:")){
			result+="1;";
		}
		if(label.contains("2:")){
			result+="2;";
		}
		if(label.contains("3:")){
			result+="3;";
		}
		if(label.contains("4:")){
			result+="4;";
		}
		if(label.contains("5:")){
			result+="5;";
		}
		return result.substring(0,result.length()-1);
	}
	//	1:1 1
	//	1:0 1
	//	1:-1 2
	//	2:1 3
	//	2:0 3
	//	2:-1 4
	//	3:1 5
	//	3:0 5
	//	3:-1 6
	//	4:1 7
	//	4:0 7
	//	4:-1 8
	//	5:1 9
	//	5:0 9
	//	5:-1 10
	public static String parseLabel10 (String label) {
		String result = "";
		if(label.contains("1:1") || label.contains("1:0") ){
			result+="1;";
		}
		if(label.contains("1:-1")){
			result+="2;";
		}

		if(label.contains("2:1") || label.contains("2:0") ){
			result+="3;";
		}
		if(label.contains("2:-1")){
			result+="4;";
		}

		if(label.contains("3:1") || label.contains("3:0") ){
			result+="5;";
		}
		if(label.contains("3:-1")){
			result+="6;";
		}

		if(label.contains("4:1") || label.contains("4:0") ){
			result+="7;";
		}
		if(label.contains("4:-1")){
			result+="8;";
		}
		if(label.contains("5:1") || label.contains("5:0") ){
			result+="9;";
		}
		if(label.contains("5:-1")){
			result+="10;";
		}
		if (result.length()==0){
			System.out.println("s");
		}
		return result.substring(0,result.length()-1);
		
	}

	public static List<String> readFileToListString (String link) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> listData = new ArrayList<String>();
		FileInputStream fi = new FileInputStream(link);
		InputStreamReader isr = new InputStreamReader(fi, "UTF8");
		try(BufferedReader br = new BufferedReader(isr)) {
			for(String line; (line = br.readLine()) != null; ) {
				listData.add(line);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return listData;
	}
	public static void main(String[] args) {
		//tim listdata1 , listdata2
		List<String> listData1 = new ArrayList<>();
		List<String> listData2 = new ArrayList<>();
		List<String> listLast = new ArrayList<>();
		try {
			listData1 =	Main.readFileToListString("input/dl/data1unlabel.txt");
			listData2 = Main.readFileToListString("input/dl/data2old.txt");
			listLast = Main.readFileToListString("input/dl/label_fix.txt");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		List<String> newListData1 = new ArrayList<>();
		String temp;
		for (int i = 0; i < listLast.size(); i++) {
			temp = listData2.get(i);
			temp.trim();
			temp.replace("\t", "");
			temp =temp+ "\t" + parseLabel10(listLast.get(i));
 			newListData1.add(temp);
		}
			System.out.println(newListData1.size());

		Main.writeToFileUtf8("input/dl/data2_10.txt", newListData1);
//		//list last cua data2
//		for (String str : listData1) {
//			temp = str;
//			temp.trim();
//			while (temp.indexOf(" ") >= 0) {
//				temp = temp.substring(temp.lastIndexOf(" ")+1);
//				temp.trim();
//			}
//			if (temp.indexOf("\t") >=0) {
//				temp = temp.substring(temp.lastIndexOf("\t")+1);
//				temp.trim();
//			}
//			while (temp.indexOf(".") >=0) {
//				temp = temp.substring(temp.lastIndexOf(".")+1);
//				temp.trim();
//			}
//			while (temp.indexOf("!") >=0) {
//				temp = temp.substring(temp.lastIndexOf("!")+1);
//				temp.trim();
//			}
//			while (temp.indexOf(" ") >= 0) {
//				temp = temp.substring(temp.lastIndexOf(" ")+1);
//				temp.trim();
//			}
//
//			if (temp.indexOf(":") == -1)
//				temp = "";
//			temp.trim();
//			listLast.add(temp);
//			
//		}
		
	}
}