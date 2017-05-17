package method.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Cluster;
import main.Point;

public class ThreadDistance implements Runnable {
	private Thread t;
	private String threadName;
	List<Cluster> listClusters;
	private int begin;
	private int end;
	private Map<Point,Double> map ;
	public ThreadDistance( String name,int begin,int end,List<Cluster> listClusters) {
		this.threadName = name;
		this.begin = begin;
		this.end = end;
		this.listClusters = listClusters;
	}
	public void run() {
		int j;
		int i;
	    map  = new HashMap<Point, Double>();
		double d = 0;
		Point p;
		for (i = begin; i < end; i++) {
			for (j = i + 1; j < listClusters.size(); j++) {
				d = listClusters.get(i).computeDistance(listClusters.get(j));
				p = new Point(i,j);
				 
				this.map.put(p,d);
			}
		}
		
	}
	public void start () {
		if (t == null) {
			t = new Thread (this, threadName);
			t.start ();
		}
	}
	public Map<Point, Double> getMap() {
		return this.map;
	}
	
}
