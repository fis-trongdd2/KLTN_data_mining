package lift;

public class PointLift {
	private int TP;
	private int FP;
	private int FN;

	public PointLift(int tP, int fP, int fN) {
		super();
		TP = tP;
		FP = fP;
		FN = fN;
	}

	public PointLift() {
		super();
	}

	public int getTP() {
		return TP;
	}

	public void setTP(int tP) {
		TP = tP;
	}

	public int getFP() {
		return FP;
	}

	public void setFP(int fP) {
		FP = fP;
	}

	public int getFN() {
		return FN;
	}

	public void setFN(int fN) {
		this.FN = fN;
	}

	public PointLift(double[] labelOld, double[] labelSvm) {
		int countTP = 0;
		int countFP = 0;
		int countFN = 0;
		for (int i = 0; i < labelOld.length; i++) {
			System.out.println(labelOld[i] + " " + labelSvm[i]);
			if (labelSvm[i] == 1) {
				if ((labelOld[i] == labelSvm[i])) {
					countTP++;
				} else {
					countFP++;
				}
			} else {
				if (labelOld[i] == 1) {
					countFN++;
				}
				;
			}

		}
		this.FN = countFN;
		this.FP = countFP;
		this.TP = countTP;
	}
}
