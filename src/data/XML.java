package data;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by trong_000 on 7/1/2016.
 * doc file xml de xd cac nhan.
 * cac nhan thuong la cac thuoc tinh cuoi cung, nen o day chi tim so nhan,sau do
 * doc nguoc file arff va lay dung so nhan nay, ta co tap nhan.
 */
public class XML {
    private int nLabel_;
    private ArrayList<ArrayList<Integer>> listCombinations_;

    public XML(String linkXML) {

        //tu link file xml, lay duoc so nhan.
        int count = 0;
        try {
            File inputFile = new File(linkXML);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("label");
            count = nList.getLength();

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.nLabel_ = count;

        //tu so nhan ghi vao file nhan.txt cac to hop nhom
        File file = new File("input/nhan.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            for(int i = 0; i <= Math.pow(2, count)- 1; i++){
                int tmp = Integer.parseInt(Integer.toBinaryString(i));
                for(int j = 0; j < count; j++ ){
                    bw.write(tmp%10+" ");
                    tmp = tmp /10;
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //doc file nhan.txt, lay ra list to hop nhom : dstohop
        listCombinations_ = new ArrayList<ArrayList<Integer>>();
        Path filePath = Paths.get("input/nhan.txt");
        Scanner scanner = null;
        ArrayList<Integer> con = new ArrayList<Integer>();
        int a = this.getNumberOfLabel();
        try {
            scanner = new Scanner(filePath);
            int j = 0;
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    int q = scanner.nextInt();
                    con.add(q);
                    j++;
                    if (j >= a) {
                        listCombinations_.add(con);
                        con = new ArrayList<Integer>();
                        j = 0;
                    }
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        }

    }

    public int getNumberOfLabel() {
        return nLabel_;
    }

    public ArrayList<ArrayList<Integer>> getListCombination() {
        return listCombinations_;
    }

}
