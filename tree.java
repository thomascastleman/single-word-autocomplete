import java.util.*;
import java.io.*;

class Tree {

	ArrayList<ArrayList<Node>> layers = new ArrayList<ArrayList<Node>>();

	public Tree(ArrayList<String> training_text) {

		// get length of longest word in training data
		int maxLen = training_text.get(0).length();
		for (int i = 0; i < training_text.size(); i++) {
			if (training_text.get(i).length() > maxLen) {
				maxLen = training_text.get(i).length();
			}
		}

		// initialize layers as empty arraylists
		for (int i = 0; i <= maxLen; i++) {
			layers.add(new ArrayList<Node>());
		}

		for (int i = 0; i < training_text.size(); i++) {
			String word = training_text.get(i);			// current word
			Node[] allNodes = new Node[word.length()];	// array of all nodes from substrings in word

			// create all nodes from this word
			for (int end_of_sub = 1; end_of_sub <= word.length(); end_of_sub++) {
				allNodes[end_of_sub - 1] = new Node(word.substring(0, end_of_sub));
			}

			boolean[] existsInTree = new boolean[allNodes.length];

			for (int n = 0; n < allNodes.length; n++) {
				existsInTree[n] = false;
				Node node = allNodes[n];
				ArrayList<Node> corresponding_layer = layers.get(node.string.length());

				// establish which nodes exist already in tree
				for (int w = 0; w < corresponding_layer.size(); w++) {
					if (corresponding_layer.get(w).string.equals(node.string)) {
						existsInTree[n] = true;
						allNodes[n] = corresponding_layer.get(w);
						break;
					}
				}
			}

			// update parents and children
			for (int n = 0; n < allNodes.length; n++) {
				if (existsInTree[n]) {
					allNodes[n].probability++;

					// if next node does not exist in tree
					if (n != allNodes.length - 1 && (!existsInTree[n + 1])) {
						allNodes[n].children.add(allNodes[n + 1]);
					}

				} else {
					if (n != 0) {
						allNodes[n].parent = allNodes[n - 1];
					}
					if (n != allNodes.length - 1) {
						allNodes[n].children.add(allNodes[n + 1]);
					}

					// add to tree
					layers.get(allNodes[n].string.length()).add(allNodes[n]);
				}
			}


		}
	}

	// search within the tree for a node with a given string value
	public Node search(String str) {
		ArrayList<Node> layer = layers.get(str.length());

		for (int i = 0; i < layer.size(); i++) {
			if (layer.get(i).string.equals(str)) {
				return layer.get(i);
			}
		}

		return null;
	}
}