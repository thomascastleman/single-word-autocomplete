import java.util.*;
import java.io.*;

class Node {

	String string;
	boolean isWord;
	int probability = 1;

	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();

	public Node(String string_) {
		string = string_;

		if (Main.dictionary.contains(string)) {
			isWord = true;
		}
	}
}