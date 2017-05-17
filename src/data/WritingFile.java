package data;

import main.Candidate;
import main.Cluster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by trong_000 on 7/24/2016.
 */
public class WritingFile {

	// ghi cluster ra file trong input/clustered
	public static void writeClustersToFile(List<Cluster> clusters) {
		String a = "1";
		File f = new File("input/clustered/clustered_" + a + ".txt");

		int i = 0;
		while (f.exists() && !f.isDirectory()) {
			i++;
			a = "" + i;
			f = new File("input/clustered/clustered_" + a + ".txt");
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			for (Cluster c : clusters) {
				bw.write(c.printClusterToString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ghi candidate ra file trong input/candidate
	public static void writeCandidatesToFile(List<Candidate> candidates, String name) {
		String a = "1";
		File f = new File("input/candidate/" + name + "_" + a + ".txt");
		int i = 0;
		while (f.exists() && !f.isDirectory()) {
			i++;
			a = "" + i;
			f = new File("input/candidate/" + name + "_" + a + ".txt");
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			for (Candidate c : candidates) {
				bw.write(c.valueToString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// write dau vao cho svm nhi phan
	public static void writeInstanceForSvm(StringBuffer str, String link) {
		try {
			File file = new File(link);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
			bw.write(str.toString());
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String a = "aaaa";
		String b = a;
		System.out.print(b);
	}
}
