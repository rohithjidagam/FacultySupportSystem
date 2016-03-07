package com.uh.cs.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Bayes {

	static HashMap<String, List<Integer>> ATT = new HashMap<>();
	static HashMap<String, List<Integer>> QZ = new HashMap<>();
	static HashMap<String, List<Integer>> ASS = new HashMap<>();
	static HashMap<String, List<Integer>> CP = new HashMap<>();
	static HashMap<String, List<Integer>> EX = new HashMap<>();
	static int firstCount;
	static int secondCount;
	static int thirdCount;
	static int failCount;
	static int totalCount;

	static List<String> results = new ArrayList<String>();

	public static void main(String[] args) {
		intialization();
		process();
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			File file = new File(
					"C:/Users/rohith/workspace/JavaExamples/Result.csv");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			reader = new BufferedReader(new FileReader(
					"C:/Users/rohith/workspace/JavaExamples/TestData.csv"));
			writer = new BufferedWriter(fw);
			String csvLine;
			while ((csvLine = reader.readLine()) != null) {
				String[] row = csvLine.split(",");
				String att = row[0];
				String qz = row[1];
				String ass = row[2];
				String cp = row[3];
				String ex = row[4];
				String st = row[5];
				double f, s, t, fail;
				f = (double) (((double) ATT.get(att).get(0) / firstCount)
						* ((double) QZ.get(qz).get(0) / firstCount)
						* ((double) ASS.get(ass).get(0) / firstCount)
						* ((double) CP.get(cp).get(0) / firstCount)
						* ((double) EX.get(ex).get(0) / firstCount) * ((double) firstCount / totalCount));
				s = (double) (((double) ATT.get(att).get(1) / secondCount)
						* ((double) QZ.get(qz).get(1) / secondCount)
						* ((double) ASS.get(ass).get(1) / secondCount)
						* ((double) CP.get(cp).get(1) / secondCount)
						* ((double) EX.get(ex).get(1) / secondCount) * ((double) secondCount / totalCount));
				t = (double) (((double) ATT.get(att).get(2) / thirdCount)
						* ((double) QZ.get(qz).get(2) / thirdCount)
						* ((double) ASS.get(ass).get(2) / thirdCount)
						* ((double) CP.get(cp).get(2) / thirdCount)
						* ((double) EX.get(ex).get(2) / thirdCount) * ((double) thirdCount / totalCount));
				fail = (double) (((double) ATT.get(att).get(3) / failCount)
						* ((double) QZ.get(qz).get(3) / failCount)
						* ((double) ASS.get(ass).get(3) / failCount)
						* ((double) CP.get(cp).get(3) / failCount)
						* ((double) EX.get(ex).get(3) / failCount) * ((double) failCount / totalCount));
				int i = (f > s) ? ((f > t) ? 0 : 2) : ((s > t) ? 1 : 2);
				writer.write(att + "," + qz + "," + ass + "," + cp + "," + ex
						+ "," + st + ",");
				if (i == 0) {
					if (f > fail)
						writer.write("First");
					else
						writer.write("Fail");
				} else if (i == 1) {
					if (s > fail)
						writer.write("Second");
					else
						writer.write("Fail");
				} else {
					if (t > fail)
						writer.write("Third");
					else
						writer.write("Fail");
				}
				writer.write("\n");
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException("Error while closing Reader: " + e);
			}
		}

	}

	public static void intialization() {
		String[] labels = { "Good", "Average", "Poor" };
		// ATT
		for (int i = 0; i < 3; i++) {
			ATT.put(labels[i], new ArrayList(Arrays.asList(0, 0, 0, 0)));
		}
		// QZ
		for (int i = 0; i < 3; i++) {
			QZ.put(labels[i], new ArrayList(Arrays.asList(0, 0, 0, 0)));
		}
		// ASS
		for (int i = 0; i < 3; i++) {
			ASS.put(labels[i], new ArrayList(Arrays.asList(0, 0, 0, 0)));
		}
		// CP
		for (int i = 0; i < 3; i++) {
			CP.put(labels[i], new ArrayList(Arrays.asList(0, 0, 0, 0)));
		}
		// EX
		for (int i = 0; i < 3; i++) {
			EX.put(labels[i], new ArrayList(Arrays.asList(0, 0, 0, 0)));
		}
	}

	public static void process() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(
					"C:/Users/rohith/workspace/JavaExamples/TrainData.csv"));
			String csvLine;
			HashMap<String, Integer> result = new HashMap<>();
			String[] labels = { "First", "Second", "Third", "Fail" };
			for (int i = 0; i <= 3; i++) {
				result.put(labels[i], i);
			}
			while ((csvLine = reader.readLine()) != null) {
				String[] row = csvLine.split(",");
				countIncrement(row[5]);
				int index = (int) result.get(row[5]);
				// ATT
				int j = ATT.get(row[0]).get(index) + 1;
				ATT.get(row[0]).set(index, j);
				// QZ
				j = QZ.get(row[1]).get(index) + 1;
				QZ.get(row[1]).set(index, j);
				// ASS
				j = ASS.get(row[2]).get(index) + 1;
				ASS.get(row[2]).set(index, j);
				// CP
				j = CP.get(row[3]).get(index) + 1;
				CP.get(row[3]).set(index, j);
				// EX
				j = EX.get(row[4]).get(index) + 1;
				EX.get(row[4]).set(index, j);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException("Error while closing Reader: " + e);
			}
		}
	}

	public static void countIncrement(String s) {
		totalCount++;
		if (s.equals("First"))
			firstCount++;
		else if (s.equals("Second"))
			secondCount++;
		else if (s.equals("Third"))
			thirdCount++;
		else
			failCount++;
	}
}
