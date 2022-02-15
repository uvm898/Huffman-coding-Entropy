package BinarySourceAndHuffmanPackage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

class HuffmanNode {
	double frequency;
	int sequence;
	HuffmanNode left;
	HuffmanNode right;
}

class CodeNode {
	double frequency;
	String code = "";
}

class MyComparator implements Comparator<HuffmanNode> {
	public int compare(HuffmanNode x, HuffmanNode y) {

		return (x.frequency - y.frequency) > 0 ? 1 : -1;
	}
}

public class HuffmanCoding {
	private BinarySource bs;
	private int expansionLevel;
	private HuffmanNode root = null;
	private PriorityQueue<HuffmanNode> pq;
	private CodeNode arrayCN[];

	public HuffmanCoding(BinarySource bs, int expansionLevel) {
		super();
		this.bs = bs;
		this.expansionLevel = expansionLevel;
		this.initializeQueue();
		this.makeHuffmanTree();
		this.makeCodes();
	}

	private void makeCodes() {
		arrayCN = new CodeNode[(int) Math.pow(2, expansionLevel)];
		readTheTree(root, "");
	}

	private void readTheTree(HuffmanNode r, String s) {
		if (r.left == null && r.right == null && r.sequence >= 0) {
			CodeNode newCN = new CodeNode();
			newCN.code = s;
			newCN.frequency = r.frequency;
			arrayCN[r.sequence] = newCN;
			return;
		}
		readTheTree(r.left, s + "0");
		readTheTree(r.right, s + "1");
	}

	private void makeHuffmanTree() {
		while (pq.size() > 1) {
			HuffmanNode min1 = pq.peek();
			pq.poll();
			HuffmanNode min2 = pq.peek();
			pq.poll();
			HuffmanNode newNode = new HuffmanNode();
			newNode.sequence = -1;
			newNode.frequency = min1.frequency + min2.frequency;
			newNode.left = min1;
			newNode.right = min2;
			root = newNode;
			pq.add(newNode);
		}
	}

	private void initializeQueue() {
		pq = new PriorityQueue<HuffmanNode>((int) Math.pow(2, expansionLevel), new MyComparator());
		for (int i = 0; i < (int) Math.pow(2, expansionLevel); ++i) {
			HuffmanNode hn = new HuffmanNode();
			hn.frequency = bs.probMatrix[expansionLevel - 1][i];
			hn.left = null;
			hn.right = null;
			hn.sequence = i;
			pq.add(hn);
		}
	}

	@Override
	public String toString() {
		return " root frequency: " + String.format("%,.4f", root.frequency);
	}

	public void listTheData() {
		System.out.println("\n--------The data after " + expansionLevel + ". expansion--------");
		System.out.println(String.format("H(S): %,.4f", expansionLevel * bs.getEntropyOfSource()));
		System.out.println(String.format("Lsr: %,.4f", computeLSR()));
		System.out.println(
				String.format("efficiency: %,.4f", expansionLevel * bs.getEntropyOfSource() / computeLSR() * 100)
						+ "%");
		System.out.println(String.format("compression coef: %,.2f", expansionLevel * 1.0 / computeLSR()));
	}

	private double computeLSR() {
		double ret = 0;
		for (int i = 0; i < arrayCN.length; ++i)
			ret += arrayCN[i].frequency * arrayCN[i].code.length();
		return ret;
	}

	public void compressTheOriginalFile() {
		File compressedFile = new File(expansionLevel + bs.getOriginalFile().getName());
		try {
			if (compressedFile.createNewFile()) {
				compressfile(bs.getOriginalFile(), compressedFile);
				System.out.println("Compressed file has been created");
			} else {
				System.out.println("File has already been created");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void compressfile(File originalFile, File compressedFile) {
		try {
			FileWriter myWriter = new FileWriter(compressedFile);
			FileReader myReader = new FileReader(originalFile);
			char[] cbuf = new char[expansionLevel];
			int num, index;
			while ((num = myReader.read(cbuf, 0, expansionLevel)) != -1 && num == expansionLevel) {
				String s = new String(cbuf);
				index = Integer.parseInt(s, 2);
				myWriter.write(arrayCN[index].code);
			}
			myWriter.close();
			myReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
