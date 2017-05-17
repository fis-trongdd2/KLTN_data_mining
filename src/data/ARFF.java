package data;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by trong_000 on 7/1/2016.
 * doc file arff .
 * input: link file arff.
 * output : list ten cac thuoc tinh, danh sach gia tri cua tung thuoc tinh.
 */
public class ARFF {
    private ArrayList<ArrayList<Double>> listValues_ ;
    private ArrayList<String> listAtributes_;
    private int nCadidateFirst_;

    public static Instances getAllInstances (String link) {
    	 BufferedReader reader = null;
         try {
             reader = new BufferedReader(new FileReader(link));
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
         ArffLoader.ArffReader arff = null;
         try {
             arff = new ArffLoader.ArffReader(reader);
             reader.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    	return arff.getData();
    }
    public ARFF(String link) {
        listValues_ = new ArrayList<ArrayList<Double>>();
        listAtributes_ = new ArrayList<String>();
        //lay duong dan
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(link));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArffLoader.ArffReader arff = null;
        try {
            arff = new ArffLoader.ArffReader(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //instance data : bien data lay gia tri.
        Instances data = arff.getData();
        //dat gia tri cho Map dsgiatri
        ArrayList<Double> con = new ArrayList<Double>();
        for (int i = 0; i < data.numAttributes(); i++) {
            double[] values = data.attributeToDoubleArray(i);
             con = new ArrayList<Double>();
            for (int j = 0; j < values.length; j++) {
                con.add(values[j]);
            }
            listValues_.add(con);
        }
        nCadidateFirst_ = con.size();
        //dat gia tri cho List thuoc tinh
        for (int i = 0; i < data.numAttributes(); i++) {
            listAtributes_.add(data.attribute(i).name());
        }
    }

    public ArrayList<ArrayList<Double>> getListValue() {
        return listValues_;
    }


    public ArrayList<String> getListAtribute() {
        return listAtributes_;
    }


    public int getNumberOfCadidateFirst() {
        return nCadidateFirst_;
    }

}