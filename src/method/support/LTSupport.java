package method.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Candidate;
import main.Point;

public class LTSupport {
	
	//phuong thuc tim nhan xuat hien nhieu lan nhat trong 1 tap the hien
	public static int findLamda (List<Candidate> candidates, Set<Integer> l) {
		Map<Integer,Integer> map = new HashMap<Integer, Integer> ();
		for (Candidate c : candidates) {
			for (Integer i : c.getListSeqLabel()) {
				if (l.contains(i)) {
					if (map.containsKey(i)) {
						map.put(i,map.get(i)+1);
					}
					else {
						map.put(i, 1);
					}
				}
				else {
					continue;
				}
			}
		}
		int lamda = map.entrySet().iterator().next().getKey();
		int max = map.get(lamda);
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				lamda = entry.getKey();
			}
		}
		return lamda;
	}
	
	// ham nay can xem lai
	public static List<Candidate> joinList (List<Candidate> l1, List<Candidate> l2) {
		List<Candidate> l = new ArrayList<>();
		for (Candidate c : l1) {
			try {
				l.add(c.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		for (Candidate c : l2) {
			try {
				l.add(c.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return l;
	}
	
	//ham kiem tra dong nhat nhan 
	public static boolean checkLabel (List<Candidate> listC) {
		int length = listC.size();
		for (int i = 0; i < length; i++) {
			for (int j = i+1; j < length; j++) {
				if (!listC.get(i).getListSeqLabel().equals(listC.get(j).getListSeqLabel())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main (String []args) {
		List <Integer> a =new ArrayList<>();
		a.add(1);		
		a.add(2);
		a.add(1);

		List <Integer> b =new ArrayList<>();
		b.add(1);
		b.add(2);
		b.add(1);
		System.out.println(a.equals(b));
	}
}
