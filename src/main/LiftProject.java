package main;

import data.WritingFile;
import method.support.MethodSupport;
import java.util.*;

/**
 * Created by trong_000 on 6/29/2016.
 */
public class LiftProject {

    public static void RUN (List<Candidate> listCandidatesLabel, List<Candidate> listCandidatesUnlabel, int nTrain) throws InterruptedException {
        int i;

        //tao list train: co x phan tu
        System.out.println("--------------Initializing_listCandidateTrains-----------------------");
        List<Candidate> listCandidateTrains = new ArrayList<Candidate>();
        for ( i = 0; i < nTrain; i++) {
            listCandidateTrains.add(listCandidatesLabel.get(i));
        }
        System.out.println(listCandidateTrains.size());

        //tao list test: co y phan tu
        System.out.println("--------------Initializing_listCandidateTests------------------------");
        List<Candidate> listCandidateTestsfull =  MethodSupport.setCandidate("input/example.arff","input/nhanexample.xml");;
        List<Candidate> listCandidateTests = new ArrayList<Candidate>();
        for (i = 0; i < 12; i++) {
            try {
                listCandidateTests.add(listCandidateTestsfull.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(listCandidateTests.size());

        System.out.println("--------------CLUSTERING---------------------------------------------");
        //them list train vao khoi tao.
        List<Cluster> listClusterTrains = MethodSupport.setListClusters(listCandidateTrains,MethodSupport.LABEL);
        //phan cum : dua vao list cu tra ve list moi
        listClusterTrains = MethodSupport.clustering(listClusterTrains);
        WritingFile.writeClustersToFile(listClusterTrains);


        //gan nhan cho tap test
         System.out.println("-------------------setLabelForTEST-----------------------------------");
        List<Candidate> listCandidateTestsToLabel = new ArrayList<Candidate>();
        for (Candidate c :listCandidateTests) {
            try {
                listCandidateTestsToLabel.add(c.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        listCandidateTestsToLabel = MethodSupport.setLabelForUnlabel(listCandidateTestsToLabel,listClusterTrains,-1);
        System.out.println("-------------------WRITE_TO_TEST-------------------------------------");
        WritingFile.writeCandidatesToFile(listCandidateTestsToLabel,"test");
        System.out.println("------------------------ASSESSING------------------------------------");
        System.out.println(MethodSupport.assessClusteringCombination(listCandidateTestsToLabel,listCandidateTests));


        //gan nhan cho tap ko nhan, mk dua vao input la so unlabel muon gan : 40 60 80
        System.out.println("-------------------setLabelForUnlabel--------------------------------");
        listCandidatesUnlabel = MethodSupport.setLabelForUnlabel(listCandidatesUnlabel, listClusterTrains, -1);
        System.out.println(listCandidatesUnlabel.size());
        System.out.println("-------------------WRITE_TO_Unlabel----------------------------------");
        WritingFile.writeCandidatesToFile(listCandidatesUnlabel,"unlabeled");


    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-------------------START---------------------------------------------");


        System.out.println("--------------Initializing_list_candidate_LABEL ---------------------");

//        ArrayList<Candidate> listCandidatesLabel = MethodSupport.setCluster("input/5/train_5.arff","input/nhan.xml");
        ArrayList<Candidate> listCandidatesLabel = MethodSupport.setCandidate ("input/example.arff","input/nhanexample.xml");

        System.out.println("--------------Initializing_list_candidate_UNLABEL--------------------");

//        ArrayList<Candidate> listCandidatesUnlabel = MethodSupport.setCluster("input/5/unlabeled5.arff","input/nhanexampletrong.xml");;
        ArrayList<Candidate> listCandidatesUnlabel = MethodSupport.setCandidate("input/exampleunlabel.arff","input/nhanexampletrong.xml");;


        System.out.println("-------------------RUNNING-------------------------------------------");

        RUN(listCandidatesLabel,listCandidatesUnlabel,12);


        System.out.println("---------------------END---------------------------------------------");

    }
}