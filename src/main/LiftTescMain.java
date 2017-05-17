package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import data.WritingFile;
import method.support.LTSupport;
import method.support.MethodSupport;


public class LiftTescMain {
	public static List<Cluster> listCluster = new ArrayList<>();
	public static void main (String []args) throws CloneNotSupportedException, InterruptedException {
		
//		List<Candidate> example = MethodSupport.setCandidate ("input/ltexamplearff.arff","input/ltnhanexample.xml");
		List<Candidate> example = MethodSupport.setCandidate ("input/5/5.valid.arff","input/nhan.xml");
        List<Candidate> listCandidateTrains = new ArrayList<Candidate>();
        for (int  i = 0; i < 1000; i++) {
            listCandidateTrains.add(example.get(i).clone());
        }
		Set<Integer> l1 = new HashSet<>();
		Set<Integer> l2 = new HashSet<>();
		l2.add(1);
		l2.add(2);
		l2.add(3);
		l2.add(4);
		l2.add(5);
		System.out.println(listCandidateTrains.size());
		List <LTObject> listObject= new ArrayList<>();
		listObject.add(new LTObject(listCandidateTrains,l1,l2,new ArrayList<>()));
		List<Candidate> dl = new ArrayList<>();
		List<Candidate> du = new ArrayList<>();
		List<Candidate> d0 = new ArrayList<>();
		List<Candidate> d1 = new ArrayList<>();
		List<Candidate> d2 = new ArrayList<>();
		int i;
			
		for (i = 0; i < listObject.size(); i++) {
			if (listObject.get(i) == null) continue;
			System.out.println( "size :" + listObject.size());
			dl = new ArrayList<>();
			du = new ArrayList<>();
			for (Candidate c : listObject.get(i).get_listCandidate()) {
				if (c.getListSeqLabel().size() == 0) {
					du.add(c.clone());
				}
				else {
					dl.add(c.clone());
				}
			}
			int t = -1;
			try {
				t = LTSupport.findLamda(listObject.get(i).get_listCandidate(), listObject.get(i).getL2());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			listObject.get(i).getLamda().add(t);
			for (Candidate c :  dl) {
				c.divideToSet(listObject.get(i).getLamda());
			}
			listObject.get(i).get_listCandidate().clear();
			for (Candidate c : du) {
				listObject.get(i).get_listCandidate().add(c);
			}
			for (Candidate c : dl) {
				listObject.get(i).get_listCandidate().add(c);
			}
			List<Cluster> clusteringD = MethodSupport.setListClusters(listObject.get(i).get_listCandidate(), MethodSupport.ADDLABEL);
			clusteringD = MethodSupport.clustering(clusteringD);
			d0 =  new ArrayList<>();
			d1 =  new ArrayList<>();
			d2 =  new ArrayList<>();
			for (Cluster c : clusteringD) {
				if (c.getLabelOfCluster() == 1) {
					for (Candidate cd : c.getListCandidate()) {
						d0.add(cd.clone());
					}
				} 
				else {
					if (c.getLabelOfCluster() == 2) {
						for (Candidate cd : c.getListCandidate()) {
							d1.add(cd.clone());
						}
					}
					else {
						for (Candidate cd : c.getListCandidate()) {
							d2.add(cd.clone());
						}
					}
				}
			}
			if (d0.size() != 0) {
				List<Cluster> clusterD0 = MethodSupport.setListClusters(d0, MethodSupport.LABEL);
				clusterD0 = MethodSupport.clustering(clusterD0);
				for (Cluster c : clusterD0) {
					listCluster.add(c);			
				}
				System.out.println("ko add");
			}
			LTObject objectToAdd = new LTObject();
			Set<Integer> nList2 = listObject.get(i).getL2().stream().map(Integer::new).collect(Collectors.toSet());
			nList2.remove(t);
			Set<Integer> nList1 = listObject.get(i).getL1().stream().map(Integer::new).collect(Collectors.toSet());
			Set<Integer> nListClone = listObject.get(i).getL1().stream().map(Integer::new).collect(Collectors.toSet());
			if (d1.size() != 0) {
				if (LTSupport.checkLabel(d1)) {
					List<Cluster> clusterD1 = MethodSupport.setListClusters(d1, MethodSupport.LABEL);
					clusterD1 = MethodSupport.clustering(clusterD1);
					for (Cluster c : clusterD1) {
						listCluster.add(c);	
					}
				}
				else {
					nList1.add(t);
					objectToAdd = new LTObject(d1, nList1,nList2,listObject.get(i).getLamda());
					if (Objects.nonNull(objectToAdd))
						listObject.add(objectToAdd);
					System.out.println(" add 1");
				}
			}
			if (d2.size() != 0) {
				if (LTSupport.checkLabel(d2)) {
					List<Cluster> clusterD2 = MethodSupport.setListClusters(d2, MethodSupport.LABEL);
					clusterD2 = MethodSupport.clustering(clusterD2);
					for (Cluster c : clusterD2) {
						listCluster.add(c);			
					}
				}		
				else {
					objectToAdd = new LTObject(d2 , nListClone,nList2,new ArrayList<>());
					if (Objects.nonNull(objectToAdd))
						listObject.add(objectToAdd);
					System.out.println(" add 2");

				}
			}
			listObject.remove(i);
			i--;
			System.out.println("done");
		}
        List<Candidate> listCandidateTests = new ArrayList<Candidate>();
        for (i = 300; i < 400; i++) {
            try {
                listCandidateTests.add(example.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        List<Candidate> listCandidateTestsToLabel = new ArrayList<Candidate>();
        for (Candidate c :listCandidateTests) {
            try {
                listCandidateTestsToLabel.add(c.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        listCandidateTestsToLabel = MethodSupport.setLabelForUnlabel(listCandidateTestsToLabel,listCluster,-1);
        System.out.println(MethodSupport.assessClusteringCombination(listCandidateTestsToLabel,listCandidateTests));
        
        
        List<Candidate> listCandidatesUnlabel = new ArrayList<Candidate>();
        for (i = 1000; i < 1200; i++) {
            try {
            	listCandidatesUnlabel.add(example.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        listCandidatesUnlabel = MethodSupport.setLabelForUnlabel(listCandidatesUnlabel, listCluster, -1);
        WritingFile.writeCandidatesToFile(listCandidatesUnlabel,"unlabeled");
	}
}