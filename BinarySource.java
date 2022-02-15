package BinarySourceAndHuffmanPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BinarySource {

	private double a;
	private double b;
	private double probOfZero;
	private double probOfOne;
	private double entropyOfSource;
	private File originalFile;
	public static final int FILESIZE = 1024 * 1025;
	protected double probMatrix[][];
	protected int row, column;

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getProbOfZero() {
		return probOfZero;
	}

	public double getProbOfOne() {
		return probOfOne;
	}

	public File getOriginalFile() {
		return originalFile;
	}

	public double getEntropyOfSource() {
		return entropyOfSource;
	}

	public double[][] getProbMatrix() {
		return probMatrix;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	private void generateProbs() {
		probOfOne = (1 - b) / (2 - a - b);
		probOfZero = 1 - probOfOne;
	}

	public BinarySource(double a, double b) {
		super();
		this.a = a;
		this.b = b;
		generateProbs();
		generateEntropy();
	}

	public void initializeMatrix(int p) {
		row = p;
		column = (int) Math.pow(2, p);

		probMatrix = new double[row][column];
		probMatrix[0][0] = probOfZero;
		probMatrix[0][1] = probOfOne;

		double ZaZ = b;
		double ZaO = 1 - a;
		double OaO = a;
		double OaZ = 1 - b;

		for (int i = 0; i < row - 1; ++i)
			for (int j = 0; j < (int) Math.pow(2, i + 1); ++j) {
				
				if ((j * 2) % 4 == 0) {
					
					probMatrix[i + 1][j * 2] = probMatrix[i][j] * ZaZ;
					probMatrix[i + 1][j * 2 + 1] = probMatrix[i][j] * OaZ;
				}
				
				if ((j * 2) % 4 == 2) {
					
					probMatrix[i + 1][j * 2] = probMatrix[i][j] * ZaO;
					probMatrix[i + 1][j * 2 + 1] = probMatrix[i][j] * OaO;
				}
			}
	}

	public void generateOriginalFile(String s) {
		originalFile = new File(s + ".txt");
		try {
			if (originalFile.createNewFile()) {
				System.out.println("File created : " + originalFile.getName());
				writeFile(originalFile);
				System.out.println();
			} else
				System.out.println("File has already been defined.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(File myFile) {
		try {
			FileWriter fw = new FileWriter(myFile.getName());
			Random r = new Random();
			boolean lastCharZero = false;
			if (r.nextDouble() <= probOfZero) {
				lastCharZero = true;
				fw.write('0');
			} else
				fw.write('1');
			for (int i = 0; i < FILESIZE; ++i) {
				if (lastCharZero) {
					if (r.nextDouble() <= b)
						fw.write('0');
					else {
						fw.write('1');
						lastCharZero = false;
					}
				} else {
					if (r.nextDouble() <= a)
						fw.write('1');
					else {
						fw.write('0');
						lastCharZero = true;
					}
				}
			}
			fw.close();
			System.out.println("File : " + myFile.getName() + " has been written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateEntropy() {
		entropyOfSource = probOfOne
				* (a * (Math.log(1 / a) / Math.log(2)) + (1 - a) * (Math.log(1 / (1 - a)) / Math.log(2)));
		entropyOfSource += probOfZero
				* (b * (Math.log(1 / b) / Math.log(2)) + (1 - b) * (Math.log(1 / (1 - b)) / Math.log(2)));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Binary source:\n").append("a= ").append(String.format("%,.4f", a)).append(" , b= ")
				.append(String.format("%,.4f", b)).append("\n");
		sb.append("P(0)= ").append(String.format("%,.4f", probOfZero));
		sb.append(" , P(1)= ").append(String.format("%,.4f", probOfOne)).append("\n");
		sb.append("H(S)= ").append(String.format("%,.4f", entropyOfSource)).append("\n");
		sb.append("The compression before extension: 1, the efficiency : "
				+ String.format("%,.2f", entropyOfSource * 100)).append("%").append("\n");
		return sb.toString();
	}

}
