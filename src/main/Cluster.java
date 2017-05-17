package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trong_000 on 6/29/2016.
 */
public class Cluster {
    private int labelOfCluster_;
    private List<Candidate> listCandidates_ = new ArrayList<Candidate>();
    private boolean identified_ = false;
    private List<Integer> listSeqLabel = new ArrayList<>();
    

	public List<Integer> getListSeqLabel() {
		return listSeqLabel;
	}

	public void setListSeqLabel(List<Integer> listSeqLabel) {
		this.listSeqLabel = listSeqLabel;
	}

	public boolean getIdentified() {
        return identified_;
    }

    public void setIdentified(boolean identified) {
        this.identified_ = identified;
    }

    public int getListCandidateSize() {
        return listCandidates_.size();
    }


    public int getLabelOfCluster() {
        return labelOfCluster_;
    }

    public void setLabelOfCluster(int labelOfCluster) {
        this.labelOfCluster_ = labelOfCluster;
    }

    public List<Candidate> getListCandidate() {
        return listCandidates_;
    }

    public void joinCluster (Cluster a)  {
        for (Candidate c : a.getListCandidate()) {
            listCandidates_.add(c);
        }
        a = null;
    }
    public void addCandidate (Candidate a) {
        this.listCandidates_.add(a);
    }

    public ArrayList<Double> getCentroid () {
        ArrayList<Double> a = new ArrayList<Double>(this.getListCandidate().get(0).getValue());
        int numberOfAtribute = this.getListCandidate().get(0).getNumberOfAtribute();
        for (int i = 1; i < this.getListCandidateSize(); i++) {
            for (int j = 0; j < numberOfAtribute; j++) {
                a.set(  j,  a.get(j) + this.getListCandidate().get(i).getValue().get(j));
            }
        }
        for (int i = 0; i < numberOfAtribute; i++) {
            a.set(i,a.get(i)/this.getListCandidateSize());
        }
        return a;
    }
    public double computeDistance (Cluster a) {
        double distance = 0;
        for (int i = 0; i < this.getCentroid().size(); i++) {
            distance += (this.getCentroid().get(i) - a.getCentroid().get(i))*(this.getCentroid().get(i) - a.getCentroid().get(i));
        }
        return Math.sqrt(distance);
    }
    public void printCluster () {
        System.out.print("List candidate : ");
        for (Candidate c : this.getListCandidate()) {
            System.out.print(c.getValue());
        }
        System.out.println(" Label cluster : " + this.getLabelOfCluster());
    }
    public String printClusterToString () {
        String a = "list candidate : ";
        for (Candidate c : this.getListCandidate()) {
            a+= c.getValue();
        }
        a = a + " ;Label of cluster : " + this.getLabelOfCluster();
        return a;
    }
    

	public Cluster clone()throws CloneNotSupportedException{
        return (Cluster)super.clone();
    }
    
    public static void main(String []args) {

    }
}
