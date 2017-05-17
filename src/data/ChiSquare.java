package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import weka.core.Instance;
import weka.core.Instances;

public class ChiSquare {
	// chon gia tri toi han 6.64, do chinh xac 90%// 0.01
	public static final double pvalue = 0.015;

	public static double computeChiSquare(int N11, int N10, int N01, int N00) {
		if ((((N11 == N01) && (N11 == 0)) || ((N11 == N10) && (N11 == 0)) || ((N10 == N00) && (N10 == 0))
				|| ((N01 == N00) && (N01 == 0))))
			return 0;
		double a = (N11 + N10 + N01 + N00) * Math.pow(N11 * N00 - N10 * N01, 2);
		// System.out.println("a= "+ a);
		// System.out.println(N11 +" "+ N10 +" "+ N01 +" "+ N00);
		double b = (a / (N11 + N01) / (N11 + N10) / (N10 + N00) / (N01 + N00));
		return b;
	}

	public static List<Integer> tfidfChi(Instances instances, int label) {
		List<Double> listChi = new ArrayList<>();
		ChiSquare a = new ChiSquare();
		Instances dataPos = new Instances(instances, 0);
		Instances dataNeg = new Instances(instances, 0);
		for (int i = 0; i < instances.numInstances(); i++) {
			Instance inst = instances.instance(i);
			if ((int) inst.value(label) == 1) {
				dataPos.add(inst);
			} else {
				dataNeg.add(inst);
			}
		}
		int N11 = 0;// thuoc lop c co tu t
		int N10 = 0;// kong thuoc lop c co tu t
		int N01 = 0;// thuoc lop c khong chua tu t
		int N00 = 0;// khong thuoc lop c khong chua tu t
		// System.out.println(dataPos);
		// System.out.println(dataNeg);
		for (int i = 0; i < instances.numAttributes() - 5; i++) {
			for (int j = 0; j < dataPos.numInstances(); j++) {
				Instance instPos = dataPos.instance(j);
				if (instPos.value(i) != 0) {
					N11++;
				} else {
					N01++;
				}
			}

			for (int j = 0; j < dataNeg.numInstances(); j++) {
				Instance instNeg = dataNeg.instance(j);
				if (instNeg.value(i) != 0) {
					N10++;
				} else {
					N00++;
				}
			}
			// System.out.println(N11 +" "+ N10 +" "+ N01 +" "+ N00);
			listChi.add(computeChiSquare(N11, N10, N01, N00));
			N11 = 0;
			N10 = 0;
			N01 = 0;
			N00 = 0;
		}
		System.out.println("Label : 1");
		System.out.println("List chi-square value :" +printListchi(listChi));
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < listChi.size(); i++) {
			if (listChi.get(i) > 0 && listChi.get(i) <= pvalue) {
				result.add(i);
			}
		}
		return result;
	}

	public static int deleteAtributeByChi(Instances instances, List<Integer> chi) {
		for (int i = 0; i < chi.size(); i++) {
			instances.deleteAttributeAt(chi.get(i));
			for (int j = i + 1; j < chi.size(); j++) {
				chi.set(j, chi.get(j) - 1);
			}
		}
		return chi.size();
	}

	public static void main(String[] args) {
		Instances dataTrain = ARFF.getAllInstances("kl_test/arff_train_2.arff");
//		 Instances dataTrain = ARFF.getAllInstances("kl_test/arff_tes.arff");
//		 int t =deleteAtributeByChi(dataTrain,tfidfChi(dataTrain,1942));
		
		List<Integer> result = tfidfChi(dataTrain, 1942);
		
		System.out.println(result.size()+ " removal features : " +result);
		// System.out.println(computeChiSquare(49, 27652, 141, 774106));
	}
	public static String printListchi(List<Double> list) {
		StringBuilder end = new StringBuilder();
		int i = 0;
		end.append("{");
		for (Double temp : list) {
			if(temp ==0 ) continue;
			end.append( String.format( "%.6f",temp) + ",");
			i++;
			if(i >= 120) {
				end.append("\n");
				i=0;
			}
			
		}
		end.deleteCharAt(end.length() - 1);
		end.append("}\n");

		return end.toString();
	}
}