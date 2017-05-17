package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import method.support.LTSupport;
import method.support.MethodSupport;


public class LiftTescRun {
	public static List<Cluster> listCluster = new ArrayList<>();
	public static void main (String []args) throws CloneNotSupportedException, InterruptedException {
		
//		List<Candidate> example = MethodSupport.setCandidate ("input/ltexamplearff.arff","input/ltnhanexample.xml");
		List<Candidate> example = MethodSupport.setCandidate ("input/5/5.valid.arff","input/nhan.xml");
        List<Candidate> listCandidateTrains = new ArrayList<Candidate>();
        for (int  i = 0; i < 700; i++) {
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
		List<Candidate> d0 = new ArrayList<>();
		List<Candidate> d1 = new ArrayList<>();
		List<Candidate> d2 = new ArrayList<>();
		int i;
			
		for (i = 0; i < listObject.size(); i++) {
			if (listObject.get(i) == null) continue;
			System.out.println( "size :" + listObject.size());
	
			int t = -1;
			t = LTSupport.findLamda(listObject.get(i).get_listCandidate(), listObject.get(i).getL2());
			listObject.get(i).getLamda().add(t);
			for (Candidate c :   listObject.get(i).get_listCandidate()) {
				c.divideToSet(listObject.get(i).getLamda());
			}
			List<Cluster> clusteringD = MethodSupport.setListClusters(listObject.get(i).get_listCandidate(), MethodSupport.ADDLABEL);
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
			System.out.println("d0 size : " + d0.size() + " d1 size : " + d1.size() + " d2 size : " + d2.size());
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
        for (i = 700; i < 850; i++) {
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
        System.out.println(MethodSupport.assessClusteringCombination(listCandidateTests,listCandidateTestsToLabel)); 
        System.out.println(MethodSupport.assessClusteringC1(listCandidateTests,listCandidateTestsToLabel));
        //        
//        
//        List<Candidate> listCandidatesUnlabel = new ArrayList<Candidate>();
//        for (i = 600; i < 700; i++) {
//            try {
//            	listCandidatesUnlabel.add(example.get(i).clone());
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//        }
//        listCandidatesUnlabel = MethodSupport.setLabelForUnlabel(listCandidatesUnlabel, listCluster, -1);
//        WritingFile.writeCandidatesToFile(listCandidatesUnlabel,"unlabeled");
	}
}