package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.Cluster;
import main.Point;
import method.support.ThreadDistance;

/**
 * Created by trong_000 on 7/19/2016.
 * Class nay co muc dich luu tat cac cac khoang cach giua cac cum vao 1 file distance.txt
 *
 * Trong do co phuong thuc static updateDistance() rat quan trong : cap nhat lai danh sach khoang cach nay,
 * sau khi gop cum
 */
public class Distance {
    private Map<Point,Double> distances_;
//    //chi cho phep lay map khoang cach nay tu file distance ra
    public Map<Point, Double> getMapDistances (List<Cluster> a) throws InterruptedException {
        Map<Point, Double> nDistances_ = new HashMap<Point, Double>();
        ThreadDistance R1 = new ThreadDistance( "Thread-1",0,a.size()/4,a);
		Thread tr1 = new Thread(R1);
		tr1.start();
		ThreadDistance R2 = new ThreadDistance( "Thread-2",a.size()/4,a.size()/2,a );
		Thread tr2 = new Thread(R2);
		tr2.start();
		ThreadDistance R3 = new ThreadDistance( "Thread-3",a.size()/2,a.size()/4*3,a);
		Thread tr3 = new Thread(R3);
		tr3.start();
		ThreadDistance R4 = new ThreadDistance( "Thread-4",a.size()/4*3,a.size(),a);
		Thread tr4 = new Thread(R4);
		tr4.start();
		tr1.join();
		tr2.join();
		tr3.join();
		tr4.join();
        
		List <Map<Point,Double>> listMap = new ArrayList<Map<Point, Double>>();
       	listMap.add(R1.getMap());
       	listMap.add(R2.getMap());
       	listMap.add(R3.getMap());
       	listMap.add(R4.getMap());
       	
        for (Map<Point,Double> temp : listMap) {
        	for (Map.Entry<Point,Double> entry : temp.entrySet()) {
        		nDistances_.put(entry.getKey(), entry.getValue());
	        }
        }
        distances_ = new LinkedHashMap<Point, Double>();
        distances_ = sortByComparator(nDistances_);
        return distances_;
    }
//    //them cac gia tri vao map nay. dong thoi ghi ra file input/distance
//    public void setMapDistancesSorted (List<Cluster> listClusters){
//    	 try {
//			MethodSupport.runThreadDistance(listClusters);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map updateDistance (List<Cluster> clusters, Map map, int x, int y) {
        List<Map.Entry<Point,Double>> list =
                new LinkedList<Map.Entry<Point,Double>>(map.entrySet());
        for (Iterator<Map.Entry<Point,Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Point,Double> entry = it.next();
            if (entry.getKey().getB() == y || entry.getKey().getA() == y){
                //System.out.println("da xoa " + entry.getKey().getA() +" "+entry.getKey().getB());
                map.remove(entry.getKey());
            }
            else {
                //System.out.println("size sau khi xoa  "+map.size()+"x="+x+"y=" +y);
                if (entry.getKey().getB() > y ){
                    if (entry.getKey().getA() > y ) {
                        //System.out.println("them x,y : "+entry.getKey().getA()+" "+entry.getKey().getB());
                        map.put(new Point(entry.getKey().getA()-1, entry.getKey().getB() - 1), entry.getValue());
                        map.remove(entry.getKey());
                    }
                    else {
                        //System.out.println("them y : "+entry.getKey().getA()+" "+entry.getKey().getB());
                        map.put(new Point(entry.getKey().getA(), entry.getKey().getB() - 1), entry.getValue());
                        map.remove(entry.getKey());
                    }
                }
            }
        }

        list = new LinkedList<Map.Entry<Point,Double>>(map.entrySet());
        //System.out.println("map size" + map.size());
        for (Iterator<Map.Entry<Point,Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Point,Double> entry = it.next();
            if (entry.getKey().getA() == x || entry.getKey().getB() == x){
                map.put(entry.getKey(),clusters.get(entry.getKey().getA()).computeDistance(clusters.get(entry.getKey().getB())));
            }
        }
        return sortByComparator(map);
    }
    private static Map<Point,Double> sortByComparator(Map<Point,Double> unsortMap) {

        // Convert Map to List
        List<Map.Entry<Point,Double>> list =
                new LinkedList<Map.Entry<Point,Double>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Point,Double>>() {
            public int compare(Map.Entry<Point,Double> o1,
                               Map.Entry<Point,Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<Point,Double> sortedMap = new LinkedHashMap<Point,Double>();
        for (Iterator<Map.Entry<Point,Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Point,Double> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    public static void main (String []args) {
       Map<Point,Double> map = new HashMap<Point,Double>();
        map.put(new Point(1,2), 10.0);
        map.put(new Point(1,2), 30.0);
        map.put(new Point(1,2), 50.0);
        map.put(new Point(1,2), 40.0);
        map.put(new Point(1,2), 100.0);
        map.put(new Point(1,2), 60.0);
        map.put(new Point(1,2), 110.0);
        map.put(new Point(1,2), 50.0);
        map.put(new Point(1,2), 90.0);
        map.put(new Point(1,2), 70.0);
        map.put(new Point(1,2), 80.0);
        map = sortByComparator(map);
        for (Map.Entry<Point,Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }

    }
}
