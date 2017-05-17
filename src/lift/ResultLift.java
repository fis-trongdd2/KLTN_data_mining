package lift;

import java.util.ArrayList;
import java.util.List;

public class ResultLift {
	private double precision;
	private double recall;
	private double f1;

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getF1() {
		return f1;
	}

	public void setF1(double f1) {
		this.f1 = f1;
	}

	private List<PointLift> pointLift = new ArrayList<>();

	public void assessLift() {
		int TP = 0;
		int FP = 0;
		int FN = 0;
		for (int i = 0; i < pointLift.size(); i++) {
			TP += pointLift.get(i).getTP();
			FN += pointLift.get(i).getFN();
			FP += pointLift.get(i).getFP();
		}
		this.precision = (double) TP / (TP + FP);
		this.recall = (double) TP / (TP + FN);
		this.f1 = 2 * this.precision * this.recall / (this.precision + this.recall);
	}

	public ResultLift(List<PointLift> pointLift) {
		super();
		this.pointLift = pointLift;
	}

	public ResultLift() {
		super();
	}

	public List<PointLift> getPointLift() {
		return pointLift;
	}

	public void setPointLift(List<PointLift> pointLift) {
		this.pointLift = pointLift;
	}

	public void print() {
		for (int i = 0; i < pointLift.size(); i++) {
			System.out.println("getTP = " + pointLift.get(i).getTP() + "getFN = " + pointLift.get(i).getFN()
					+ "getFP = " + pointLift.get(i).getFP());
		}
	}

}