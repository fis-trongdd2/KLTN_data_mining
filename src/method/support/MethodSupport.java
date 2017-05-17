package method.support;

import static data.Distance.updateDistance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import data.ARFF;
import data.Distance;
import data.XML;
import main.Candidate;
import main.Cluster;
import main.Point;

/**
 * Created by trong_000 on 7/23/2016.
 *
 * class nay dinh nghia cac phuong thuc static ho tro thuc hien thuat thoan trong ham main
 */
public class MethodSupport {
	public static final String LABEL = "LABEL";
	public static final String ADDLABEL = "ADDLABEL";
	
    // doc file .arff va .xml dua ra danh sach the hien va nhan tuong ung
    public static ArrayList<Candidate> setCandidate (String linkArff, String linkXML) {
        //ArrayList<Cluster> listCluster = new ArrayList<Cluster>();
        ArrayList<Candidate> listCandidateFirst = new ArrayList<Candidate>();
        ARFF filearff = new ARFF(linkArff);
        XML filexml = new XML(linkXML);
        int numberOfLabel = filexml.getNumberOfLabel();
        int i,j;


        // vong for nay dua cac the hien vao 1 list. cac the hien nay da dc chuyen sang tu dang rut gon.
        for ( i = 0; i < filearff.getNumberOfCadidateFirst(); i++) {
            Candidate _candidate = new Candidate();
            ArrayList<Double> _ds = new ArrayList<Double>();
            for ( j = 0; j < filearff.getListAtribute().size() - numberOfLabel; j++ ) {
                _ds.add(filearff.getListValue().get(j).get(i));
            }
            _candidate.setValue(_ds);
            listCandidateFirst.add(_candidate);
        }

        //vong for nay lay cac nhan dua vao ds nhan cua tung the hien.
        for ( i = 0; i < filearff.getNumberOfCadidateFirst(); i++) {
            ArrayList<Integer> _ds2 = new ArrayList<Integer>();
            for ( j = filearff.getListAtribute().size() - numberOfLabel; j < filearff.getListAtribute().size(); j++ ) {
                _ds2.add(filearff.getListValue().get(j).get(i).intValue());
            }
            listCandidateFirst.get(i).setListValueLabel(_ds2);
            listCandidateFirst.get(i).setListSeqLabel(_ds2); 
        }
 
        //vong for nay dua cac to hop trong file tohopnhom vao 1 list. tu list nay se xac dinh nhom moi cua cac cum.
        //xac dinh cac nhan moi cua cac cum. tuong ung voi danh sach tohopnhom lay ra o tren.
        for (Candidate c : listCandidateFirst) {
            c.setLabel(filexml);
        }

        return listCandidateFirst;
    }


    //phuong thuc static gan moi the hien vao 1 cum : phuong thuc nay dung de khoi tao.
    public static List<Cluster> setListClusters (List <Candidate> candidates, String type) {

        List<Cluster> listClusters = new ArrayList<Cluster>();
        for (Candidate c : candidates) {
            Cluster cluster = new Cluster();
            cluster.addCandidate(c);
            if (type == LABEL) {
            	cluster.setLabelOfCluster(c.getLabel());
            } else {
            	cluster.setLabelOfCluster(c.getLabelAdd());;
            }
            cluster.setListSeqLabel(c.getListSeqLabel());
            listClusters.add(cluster);
        }
        return listClusters;
    }

    //phuong thuc kiem tra dk dung cua vong lap khi phan cum. dk dung unlabelCluster < 2
    public static boolean checkFinish(List<Cluster> listCheck) {
        int count = 0;
        for (Cluster c : listCheck) {
            if (count >= 2) return false;
            if (c.getIdentified() == false) {
                count++;
            }
        }
        return true;
    }


    //phuong thuc PHAN CUM : thuat toan chinh de phan cum
    @SuppressWarnings("unchecked")
	public static List<Cluster> clustering (List<Cluster> listCluster) throws InterruptedException {

        ArrayList<Point> phanbiet = new ArrayList<Point>();
        Distance test = new Distance();

        //chi de tao dl khoang cach lan dau tien. tu lan sau doc file ra .
        Map<Point,Double> map = test.getMapDistances(listCluster);

        while (!checkFinish(listCluster)) {
            Iterator<Map.Entry<Point, Double>> entry = map.entrySet().iterator();
            Point saveIndex = new Point();
            Map.Entry<Point, Double> en;
            while (entry.hasNext()) {
                en = entry.next();
                saveIndex = en.getKey();
                if (!saveIndex.checkDistinct(phanbiet)) break;
            }
            //System.out.println("index " + saveIndex.getA()+" "+saveIndex.getB());
            if (listCluster.get(saveIndex.getA()).getLabelOfCluster() == listCluster.get(saveIndex.getB()).getLabelOfCluster()) {
                //System.out.println("bang nhau");
                listCluster.get(saveIndex.getA()).joinCluster(listCluster.get(saveIndex.getB()));
                listCluster.remove(saveIndex.getB());
                System.out.println(saveIndex.getA() + " join with " + saveIndex.getB()+";");
                map = updateDistance(listCluster, map, saveIndex.getA(), saveIndex.getB());
                phanbiet.clear();
            } else {
                //System.out.println("khac");
                if (listCluster.get(saveIndex.getA()).getLabelOfCluster() == 0) {
                    listCluster.get(saveIndex.getB()).joinCluster(listCluster.get(saveIndex.getA()));
                    System.out.println(saveIndex.getA() + " join with " + saveIndex.getB()+";");
                    listCluster.remove(saveIndex.getA());
                    map = updateDistance(listCluster, map, saveIndex.getB(), saveIndex.getA());
                    phanbiet.clear();
                } else {
                    if (listCluster.get(saveIndex.getB()).getLabelOfCluster() == 0) {
                        listCluster.get(saveIndex.getA()).joinCluster(listCluster.get(saveIndex.getB()));
                        System.out.println(saveIndex.getA() + " join with " + saveIndex.getB()+";");
                        listCluster.remove(saveIndex.getB());
                        map = updateDistance(listCluster, map, saveIndex.getA(), saveIndex.getB());
                        phanbiet.clear();
                    } else {
                        listCluster.get(saveIndex.getA()).setIdentified(true);
                        listCluster.get(saveIndex.getB()).setIdentified(true);
                        phanbiet.add(saveIndex);
                    }
                }

            }
        }
        return listCluster;
    }

    //phuong thuc GANNHAN :
    public static List<Candidate> setLabelForUnlabel (List<Candidate> newList, List<Cluster> listClustered, int number) {
        int i,j;
        double min ;
        int index;
        int max = (number==-1)?newList.size():number;
        newList = newList.subList(0,max);
        for ( i = 0; i < max; i++) {
            min = newList.get(i).computeDistance(listClustered.get(0));
            index = 0;
            for ( j = 1; j < listClustered.size(); j++) {
                if (newList.get(i).computeDistance(listClustered.get(j)) < min) {
                    min = newList.get(i).computeDistance(listClustered.get(j));
                    index = j;
                }
            }
            newList.get(i).setLabel(listClustered.get(index).getLabelOfCluster());
            newList.get(i).setListSeqLabel_(listClustered.get(index).getListSeqLabel());
        }
        return newList;
    }

    //tra ve % do chinh xac cua thuat toan phan cum doi voi list test
    public static String assessClusteringCombination (List<Candidate> listOld, List<Candidate> listNew) {
        double dem = 0;
        double length = listOld.size();
        for (int i = 0; i < length; i++) {
            if (listOld.get(i).getLabel() == listNew.get(i).getLabel()) {
                dem++;
            }
        }
        return new DecimalFormat("##.##").format(dem/length*100) + "%";
    }
    
    //tra ve % do chinh xac cua thuat toan theo so th dung
    public static String assessClusteringC1 (List<Candidate> listOld, List<Candidate> listNew) {
        double dem = 0;
        double total = 0;
        double length = listOld.size();
        for (int i = 0; i < length; i++) {
    		total += listOld.get(i).getListSeqLabel().size();
        	for (int j = 0; j < listOld.get(i).getListSeqLabel().size(); j++) {
        		for (int k = 0; k < listNew.get(i).getListSeqLabel().size(); k++) {
        			if (listOld.get(i).getListSeqLabel().get(j) == listNew.get(i).getListSeqLabel().get(k)) {
        				dem++;
        			}
        		}
        	}
        }
        return new DecimalFormat("##.##").format(dem/total*100) + "%";
    } 
//    public static void runThreadDistance (List<Cluster> a) throws IOException, InterruptedException {
//		ThreadDistance R1 = new ThreadDistance( "Thread-1",0,a.size()/4,a);
//		Thread tr1 = new Thread(R1);
//		tr1.start();
//		ThreadDistance R2 = new ThreadDistance( "Thread-2",a.size()/4,a.size()/2,a,"input/distance/distance2.txt");
//		Thread tr2 = new Thread(R2);
//		tr2.start();
//		ThreadDistance R3 = new ThreadDistance( "Thread-3",a.size()/2,a.size()/4*3,a,"input/distance/distance3.txt");
//		Thread tr3 = new Thread(R3);
//		tr3.start();
//		ThreadDistance R4 = new ThreadDistance( "Thread-4",a.size()/4*3,a.size(),a,"input/distance/distance4.txt");
//		Thread tr4 = new Thread(R4);
//		tr4.start();
//		tr1.join();
//		tr2.join();
//		tr3.join();
//		tr4.join();
//		
//    }
}
