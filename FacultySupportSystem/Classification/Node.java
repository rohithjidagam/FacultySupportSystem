package com.uh.decisiontrees;

import java.util.Vector;

public class Node {

	public double entropy;

	public Vector data;

	public int decompositionAttribute;

	public int decompositionValue;

	public Node[] children;

	public Node parent;

	public Node() {

		data = new Vector();

	}

}