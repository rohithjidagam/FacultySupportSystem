package com.uh.decisiontrees;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.FastVector;
import weka.core.Instances;

public class CalculateAccuracy {
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}

		return inputReader;
	}

	public static Evaluation classify(Classifier model, Instances trainingSet,
	    Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);

		return evaluation;
	}

	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}

	public static Instances[][] crossValidation(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
		return split;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader inputData = readDataFile("C:\\Users\\rohith\\Downloads\\ML\\Final Project\\DataSet.txt");
		Instances data = new Instances(inputData);
		data.setClassIndex(data.numAttributes() - 1);
		Instances[][] split = crossValidation(data, 10);
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
		Classifier[] modeTrees = { new J48() };
		for (int j = 0; j < modeTrees.length; j++) {
			FastVector predictions = new FastVector();
			for (int i = 0; i < trainingSplits.length; i++) {
				Evaluation validation = classify(modeTrees[j], trainingSplits[i],
				    testingSplits[i]);
				predictions.appendElements(validation.predictions());
				System.out.println(modeTrees[j].toString());
			}
			double accuracy = calculateAccuracy(predictions);
			System.out.println("Total Calculated Accuracy for "
			    + modeTrees[j].getClass().getSimpleName() + ": " + accuracy);
		}

	}
}
