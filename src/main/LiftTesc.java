package main;
import method.support.LTSupport;
import method.support.MethodSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LiftTesc {
	public static List<Cluster> listCluster = new ArrayList<>();
	public static String pnameD0 = new String("D");
	public static String pnameD1 = new String("D");
	public static String pnameD2 = new String("D");
	public static String pL1 = new String("L");
	public static String pL2 = new String("L");
	public static Map<String, List<Candidate>> nameD = new HashMap<>();
	public static Map<String, Set<Integer>> nameL = new HashMap<>();
	public static List<Integer> lamda = new ArrayList<>();
	public  static void LiftTescAlgorithm (List<Candidate> d,Set<Integer> l1, Set<Integer> l2) throws InterruptedException {
		//tinh lamda
		List<Candidate> dl = new ArrayList<>();
		List<Candidate> du = new ArrayList<>();
		for (Candidate c : d) {
			if (c.getListSeqLabel().size() == 0) {
				du.add(c);
			}
			else {
				dl.add(c);
			}
		}
		int t = LTSupport.findLamda(dl, l2);
		lamda.add(t);
		for (Candidate c :  dl) {
			c.divideToSet(lamda);
		}
		List<Cluster> clusteringD = MethodSupport.setListClusters(d, MethodSupport.ADDLABEL);
		clusteringD = MethodSupport.clustering(clusteringD);
		pnameD0 += "0";
		pnameD1 += "1";
		pnameD2 += "2";
		nameD.put(pnameD0, new ArrayList<Candidate>());
		nameD.put(pnameD1, new ArrayList<Candidate>());
		nameD.put(pnameD2, new ArrayList<Candidate>());
		for (Cluster c : clusteringD) {
			if (c.getLabelOfCluster() == 1) {
				nameD.get(pnameD0).addAll(c.getListCandidate());
			} 
			else {
				if (c.getLabelOfCluster() == 2) {
					nameD.get(pnameD1).addAll(c.getListCandidate());
				}
				else
					nameD.get(pnameD2).addAll(c.getListCandidate());
			}
		}
		pL1 += "1";
		pL2 += "2";
		nameL.put(pL1, new HashSet<>());
		nameL.put(pL2, new HashSet<>());
		for (Integer i : l1) {
			nameL.get(pL1).add(i);
		}
		for (Integer i : l2) {
			nameL.get(pL2).add(i);
		}
		
		if (nameD.get(pnameD0).size() != 0) {
			List<Cluster> clusterD0 = MethodSupport.setListClusters(nameD.get(pnameD0), MethodSupport.ADDLABEL);
			clusterD0 = MethodSupport.clustering(clusterD0);
			for (Cluster c : clusterD0) {
				listCluster.add(c);			
			}
			nameD.remove(pnameD0);
		}
		if (nameD.get(pnameD1).size() != 0) {
			if (LTSupport.checkLabel(nameD.get(pnameD1))) {
			List<Cluster> clusterD1 = MethodSupport.setListClusters(nameD.get(pnameD1), MethodSupport.ADDLABEL);
			clusterD1 = MethodSupport.clustering(clusterD1);
			for (Cluster c : clusterD1) {
				listCluster.add(c);			
			}
			nameD.get(pnameD1);
			}
			else {
				nameL.get(pL1).addAll(lamda);
				nameL.get(pL2).removeAll(lamda);
				LiftTescAlgorithm(nameD.get(pnameD1),nameL.get(pL1),nameL.get(pL2));
			}
		}
		if (nameD.get(pnameD2).size() != 0) {
			if (LTSupport.checkLabel(nameD.get(pnameD2))) {
				List<Cluster> clusterD2 = MethodSupport.setListClusters(nameD.get(pnameD2), MethodSupport.ADDLABEL);
				clusterD2 = MethodSupport.clustering(clusterD2);
				for (Cluster c : clusterD2) {
					listCluster.add(c);			
				}
				nameD.remove(pnameD2);
			}		
			else {
				nameL.get(pL2).removeAll(lamda);
				LiftTescAlgorithm(nameD.get(pnameD2),nameL.get(pL1),nameL.get(pL2));
			}
		}
		if (nameD.size() == 0) {
			return;
		}

	}
	public static void main (String[] args) throws InterruptedException {
		List<Candidate> example = MethodSupport.setCandidate ("input/ltexamplearff.arff","input/ltnhanexample.xml");
		Set<Integer> l1 = new HashSet<>();
		Set<Integer> l2 = new HashSet<>();
		l2.add(1);
		l2.add(2);
		l2.add(3);
		LiftTescAlgorithm (example,l1,l2);
		for (Cluster c : LiftTesc.listCluster) {
			c.printCluster();
		}
	}
}
