package BinarySourceAndHuffmanPackage;

public class Test {
	public static final string ORIGINAL_FILE_NAME = "original";
	public static int maximumExpansion = 5;

	public static void main(String[] args) {
		BinarySource binarySource = new BinarySource(0.01, 0.01);

		System.out.println(binarySource.toString());

		binarySource.generateOriginalFile(ORIGINAL_FILE_NAME);
		binarySource.initializeMatrix(maximumExpansion);

		System.out.println("\n +++++++++++++++++Initialization completed+++++++++++++++++\n");

		for (int i = 2; i <= maximumExpansion; ++i) {
			HuffmanCoding coding = new HuffmanCoding(binarySource, i);
			coding.listTheData();
			coding.compressTheOriginalFile();
		}
	}

}
