package com.uh.decisiontrees;

public class RunDecisionTree {
	
	public static void main(String[] args) throws Exception {
		DecisionTree decisionTree = new DecisionTree();
	  decisionTree.readData("C:\\Users\\rohith\\Downloads\\ML\\Final Project\\Data.txt");
		decisionTree.createDecisionTree();
	}
	
}
