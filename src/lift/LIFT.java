package lift;

import java.util.List;

import data.ARFF;
import data.ChiSquare;
import data.WritingFile;
import libsvm.svm_model;
import svm.SVM;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

public class LIFT {
	private int numberOfLabel;
	private String linkTrain;
	private String linkTest;

	private int numberAx;

	public LIFT(int numberOfLabel, String linkTrain, String linkTest) {
		super();
		this.numberOfLabel = numberOfLabel;
		this.linkTrain = linkTrain;
		this.linkTest = linkTest;
	}

	// phan cum kmean
	private Instances kmean(Instances instances, int numberOfClusterKmean) throws Exception {
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(numberOfClusterKmean);
		for (int i = 0; i < numberOfLabel; i++) {
			instances.deleteAttributeAt(instances.numAttributes() - 1);
		}
		kmeans.buildClusterer(instances);
		return kmeans.getClusterCentroids();
	}

	private void createFileForSVM(Instances p, Instances n, Instances test) throws Exception {
		int numberOfClusterKmean = p.numInstances() > n.numInstances() ? n.numInstances() : p.numInstances();
		numberOfClusterKmean = numberOfClusterKmean * 82 / 100;
		Instances centroidP = kmean(p, numberOfClusterKmean);
		Instances centroidN = kmean(n, numberOfClusterKmean);
		this.numberAx = centroidN.numInstances() + centroidN.numInstances();
		EuclideanDistance distance = new EuclideanDistance(centroidP);
		StringBuffer tempTrain = new StringBuffer("");
		for (int i = 0; i < p.numInstances(); i++) {
			int k = 1;
			tempTrain.append("1");
			for (int j = 0; j < centroidP.numInstances(); j++) {
				tempTrain.append(" " + (k++) + "#" + distance.distance(p.instance(i), centroidP.instance(j)));
			}
			for (int j = 0; j < centroidN.numInstances(); j++) {
				tempTrain.append(" " + (k++) + "#" + distance.distance(p.instance(i), centroidN.instance(j)));
			}
			tempTrain.append("\n");
		}
		for (int i = 0; i < n.numInstances(); i++) {
			tempTrain.append("-1");
			int k = 1;
			for (int j = 0; j < centroidP.numInstances(); j++) {
				tempTrain.append(" " + (k++) + "#" + distance.distance(n.instance(i), centroidP.instance(j)));
			}
			for (int j = 0; j < centroidN.numInstances(); j++) {
				tempTrain.append(" " + (k++) + "#" + distance.distance(n.instance(i), centroidN.instance(j)));
			}
			tempTrain.append("\n");
		}
		WritingFile.writeInstanceForSvm(tempTrain, "input/lift/svmtrain.txt");
		StringBuffer tempTest = new StringBuffer("");
		for (int i = 0; i < test.numInstances(); i++) {
			int k = 1;
			tempTest.append("1");
			for (int j = 0; j < centroidP.numInstances(); j++) {
				tempTest.append(" " + (k++) + "#" + distance.distance(test.instance(i), centroidP.instance(j)));
			}
			for (int j = 0; j < centroidN.numInstances(); j++) {
				tempTest.append(" " + (k++) + "#" + distance.distance(test.instance(i), centroidN.instance(j)));
			}
			tempTest.append("\n");
		}
		WritingFile.writeInstanceForSvm(tempTest, "input/lift/svmtest.txt");
	}

	public ResultLift clusteringLift(boolean chi) throws Exception {
		// tach thanh tap am tap duong
		ResultLift resultLift = new ResultLift();
		PointLift point = new PointLift();
		Instances dataTest = ARFF.getAllInstances(linkTest);
		Instances dataTrain = ARFF.getAllInstances(linkTrain);
		// 1448
		int begin = 1942;

		for (int label = begin; label <= begin + 4; label++) {
			System.out.println("================= Test label : " + label + "=================");
			Instances dataTestTemp = new Instances(dataTest);
			Instances dataTrainTemp = new Instances(dataTrain);
			int tempLabel = label;
			if (chi) {
				List<Integer> temp = ChiSquare.tfidfChi(dataTrainTemp, label);
				int t = ChiSquare.deleteAtributeByChi(dataTrainTemp, temp);
				ChiSquare.deleteAtributeByChi(dataTestTemp, temp);
				label = label - t;
			}

			Instances dataPos = new Instances(dataTrainTemp, 0);
			Instances dataNeg = new Instances(dataTrainTemp, 0);
			for (int i = dataTrainTemp.numInstances() - 1; i >= 0; i--) {
				Instance inst = dataTrainTemp.instance(i);
				if ((int) inst.value(label) == 1) {
					dataPos.add(inst);
				} else {
					dataNeg.add(inst);
				}
			}
			// System.out.println("tap duong: \n" + dataPos+" tap am :\n"
			// +dataNeg);
			double[] labelOLd = dataTestTemp.attributeToDoubleArray(label);
			for (int i = 0; i < numberOfLabel; i++) {
				dataTestTemp.deleteAttributeAt(dataTestTemp.numAttributes() - 1);
			}
			// System.out.println("tap test :\n" + dataTest);
			// phan cum kmean + tinh anh xa ghi vao file
			createFileForSVM(dataPos, dataNeg, dataTestTemp);
			SVM t123 = new SVM();
			// phan cum nhi phan svm
			svm_model model2 = t123.svmTrain("input/lift/svmtrain.txt", dataTrain.numInstances(), numberAx);
			double[] labelSvm = t123.evaluate_all_instances("input/lift/svmtest.txt", model2, dataTest.numInstances());
			System.out.println("================= Operation complete =================");
			point = new PointLift(labelOLd, labelSvm);
			resultLift.getPointLift().add(point);
//			resultLift.print();
			label = tempLabel;

		}
		return resultLift;
	}

	public static void main(String[] args) throws Exception {
		// LIFT test = new LIFT(2, "input/data/example.arff",
		// "input/data/b.arff", 4);
		LIFT test = new LIFT(5, "kl_test/arff_train_2.arff", "kl_test/arff_test_2.arff");
		ResultLift result = test.clusteringLift(false);
		result.assessLift();
		System.out.println("F1-Score : " + result.getF1());
	}
}