package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LTObject {
	List <Candidate> _listCandidate = new ArrayList<>();
	Set<Integer> l1 = new HashSet<>(); 
	Set<Integer> l2 = new HashSet<>();
	List<Integer> lamda = new ArrayList<>();
	public List<Integer> getLamda() {
		return lamda;
	}

	public void setLamda(List<Integer> lamda) {
		this.lamda = lamda;
	}

	public LTObject(List<Candidate> _listCandidate, Set<Integer> l1, Set<Integer> l2, List<Integer> list) {
		Set<Integer> nList1 = l1.stream().map(Integer::new).collect(Collectors.toSet());
		for (Candidate c : _listCandidate) {
			try {
				this._listCandidate.add(c.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		this.l1 = l1.stream().map(Integer::new).collect(Collectors.toSet());
		this.l2 = l2.stream().map(Integer::new).collect(Collectors.toSet());
		this.lamda = list.stream().map(Integer::new).collect(Collectors.toList());;
		
	}
	

	public LTObject() {
		// TODO Auto-generated constructor stub
	}

	public void setLTObject(List<Candidate> _listCandidate,Set<Integer> l1,Set<Integer> l2) {
		this._listCandidate = _listCandidate;
		this.l1 = l1;
		this.l2 = l2;
	}
	
	public List<Candidate> get_listCandidate() {
		return _listCandidate;
	}

	public void set_listCandidate(List<Candidate> _listCandidate) {
		this._listCandidate = _listCandidate;
	}

	public Set<Integer> getL1() {
		return l1;
	}

	public void setL1(Set<Integer> l1) {
		this.l1 = l1;
	}

	public Set<Integer> getL2() {
		return l2;
	}

	public void setL2(Set<Integer> l2) {
		this.l2 = l2;
	}
	
}
