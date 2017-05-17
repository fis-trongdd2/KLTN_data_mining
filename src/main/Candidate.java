package main;

import data.XML;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trong_000 on 6/29/2016.
 */
public class Candidate implements Cloneable {
    private List<Double> value_;
    private List <Integer> listValueLabels_;
    private int label_;
    private int labelAdd;
    private List <Integer> listSeqLabel_;

    public void setValue(ArrayList<Double> value) {
        this.value_ = value;
    }

    public int getLabel() {
        return label_;
    }

    public void setLabel(XML a) {
        this.label_ = 0;
        for (int i = 0; i < a.getListCombination().size(); i++) {
            if (this.listValueLabels_.equals( a.getListCombination().get(i))) {
                this.label_ = i;
                break;
            }
        }
    }

    public void setLabel (int l) {
        this.label_= l;
    }
    public int getNumberOfAtribute () {
        return value_.size();
    }


    public List<Double> getValue() {
        return value_;
    }

    
    public void setListValueLabel(List<Integer> listLabels) {
        this.listValueLabels_ = listLabels;
    }

    public List<Integer> getListValueLabel() {
    	return this.listValueLabels_;
    }
    public double computeDistance (Cluster a) {
        double distance = 0;
        for (int i = 0; i < this.value_.size(); i++) {
            distance += (this.value_.get(i) - a.getCentroid().get(i))*(this.value_.get(i) - a.getCentroid().get(i));
        }
        return Math.sqrt(distance);
    }

    public void printCandidate () {
        System.out.print("List data : "+ this.getValue());
        System.out.println(" Label candidate : " + this.getLabel());
    }

    public String valueToString () {
        return  "List data : "+ this.getValue() + " Label candidate : " + this.getLabel();
    }
    
    
    
    public List<Integer> getListSeqLabel() {
		return listSeqLabel_;
	}

	public void setListSeqLabel(List<Integer> listValueLabel) {
		List<Integer> temp = new ArrayList<>();
		for (int i = 0; i < listValueLabel.size(); i++) {
			if (listValueLabel.get(i) == 1) {
				temp.add(i+1);
			}
		}
		this.listSeqLabel_ = temp;
		temp = null;
	}
	

	public void setListSeqLabel_(List<Integer> listSeqLabel_) {
		this.listSeqLabel_ = listSeqLabel_;
	}

	public void divideToSet (List<Integer> lamda) {
		List <Integer> temp = new ArrayList<>(this.getListSeqLabel()) ;
		if (temp.containsAll(lamda)) {
			int t;
			if (temp.size() == lamda.size()) 
				t = 1;
			else t = 2;
			this.labelAdd = t;
		}
		else {
			this.labelAdd = 3;
		}
	}
	
	public int getLabelAdd() {
		return labelAdd;
	}

	public void setLabelAdd(int labelAdd) {
		this.labelAdd = labelAdd;
	}

	public Candidate clone()throws CloneNotSupportedException{
        return (Candidate)super.clone();
    }
	public static void main (String []args) {
		List<Integer> a = new ArrayList<>();
		List<Integer> b = new ArrayList<>();
		a.add(1);
		a.add(2);
		a.add(3);
		a.add(4);
		a.add(5);
		b.add(1);
		b.add(2);
		b.add(3);
		System.out.println(a.containsAll(b));
  	}
}

