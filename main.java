import java.util.*;
import java.io.*;

class Main {

	public static ArrayList<String> dictionary = new ArrayList<String>();
	public static Tree tree;

	// read training data from a file
	public static ArrayList<String> readInTrainingData() {
		File f = new File("C:\\Users\\thoma\\Desktop\\AUTOCOMPLETE\\Training\\training.txt");
        Scanner scan = null;

        ArrayList<String> training = new ArrayList<String>();
  
        try {
            scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String[] line = scan.nextLine().split(" ");
                // format and add text to training data arraylist
                for (int i = 0; i < line.length; i++) {
                	training.add(line[i].replaceAll("\\W", "").toLowerCase());
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }


        // // DEBUG
        // for (int i = 0; i < 500; i++) {
        // 	System.out.print(training.get(i) + " ");
        // }

        return training;
	}

	public static void populateDictionary() {
		File f = new File("C:\\Users\\thoma\\Desktop\\AUTOCOMPLETE\\Training\\dictionary.txt");
        Scanner scan = null;
  
        try {
            scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String[] line = scan.nextLine().split(" ");

                for (int i = 0; i < line.length; i++) {
                	dictionary.add(line[i]);
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		populateDictionary();
		ArrayList<String> train = readInTrainingData();

		tree = new Tree(train);

		// logTree(t);

		System.out.println("\n\nResult: " + autocomplete("yo"));

	}


	public static String autocomplete(String fragment) {
		System.out.println("Attempting to complete \"" + fragment + "\"");

		Node node = tree.search(fragment);

		if (node != null) {
			if (node.isWord) {
				return node.string;
			} else {

				while (node.children.size() > 0) {

					logNode(node);

					Node maxProb = node.children.get(0);
					boolean allSameProb = true;

					for (int i = 0; i < node.children.size(); i++) {
						Node child = node.children.get(i);
						if (child.probability > maxProb.probability) {
							maxProb = child;
						}
						if (i < node.children.size() - 1 && child.probability != node.children.get(i + 1).probability) {
							allSameProb = false;
						}
					}

					// if word and current probability greater than all of children
					if (node.isWord && node.probability > maxProb.probability) {
						return node.string;
					}

					if (allSameProb) {
						// get closest word
						Node best = node.children.get(0);
						for (int i = 0; i < node.children.size(); i++) {
							if (node.children.get(i).isWord) {
								best = node.children.get(i);
							}
						}

						node = best;
					} else {
						node = maxProb;
					}
				}

				return node.string;
			}
		} else {
			System.out.println("Tree search failed");
			return "";
		}
	}

	// display node info
	public static void logNode(Node n) {
		System.out.println("\n\nNode String: " + n.string);
		System.out.println("Is word? " + n.isWord);
		System.out.println("Probability: " + n.probability);
		if (n.parent != null) {
			System.out.println("Parent string: " + n.parent.string);
		}

		System.out.println("Child strings: ");
		for (int i = 0; i < n.children.size(); i++) {
			System.out.println(n.children.get(i).string + " (prob: " + n.children.get(i).probability + ")");
		}
	}

	public static void logTree(Tree t) {
		for (int i = 0; i < t.layers.size(); i++) {
			System.out.println("Layer " + i + ": ");

			for (int j = 0; j < t.layers.get(i).size(); j++) {
				logNode(t.layers.get(i).get(j));
			}
		}
	}
}