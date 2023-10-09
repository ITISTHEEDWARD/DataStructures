package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

	/* Your code goes here */

        sortedCharFreqList = new ArrayList<CharFreq>();

        CharFreq charToBeAdded = new CharFreq(StdIn.readChar(), 1);
        sortedCharFreqList.add(charToBeAdded);

        int totalNumber = 1;
        int tempIndex = -1;
        boolean isNew = true;
        double p = -1;

        while (StdIn.hasNextChar()) {

            Character newChar = StdIn.readChar();
            isNew = true;

            for (int i = 0; i < sortedCharFreqList.size(); i++) {

                if (newChar.equals(sortedCharFreqList.get(i).getCharacter())) {

                    isNew = false;
                    tempIndex = i;
                }
            }

            if(isNew != false) {

                totalNumber++;
                p = 1;
                charToBeAdded = new CharFreq(newChar, p);
                sortedCharFreqList.add(charToBeAdded);
            } else {

                double charCount = sortedCharFreqList.get(tempIndex).getProbOcc() + 1;
                totalNumber++;
                sortedCharFreqList.get(tempIndex).setProbOcc(charCount);
            }
        }

        for (int i = 0; i < sortedCharFreqList.size(); i++) {

            p = sortedCharFreqList.get(i).getProbOcc();
            p = p/totalNumber;
            sortedCharFreqList.get(i).setProbOcc(p);
        }

        Collections.sort(sortedCharFreqList);
    }

        

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	/* Your code goes here */

    Queue<TreeNode> sourceQueue = new Queue<>();
    Queue<TreeNode> targetQueue = new Queue<>();
    Queue<TreeNode> tempQueue = new Queue<>();

        for (int i = 0; i < sortedCharFreqList.size(); i++) {

            TreeNode leaf = new TreeNode(sortedCharFreqList.get(i), null, null);
            sourceQueue.enqueue(leaf);
        }

        while(!sourceQueue.isEmpty() || !targetQueue.isEmpty()) {

            if (tempQueue.size() == 2) {

                TreeNode one = tempQueue.dequeue();
                TreeNode two = tempQueue.dequeue();

                CharFreq data = new CharFreq(null, one.getData().getProbOcc() + two.getData().getProbOcc());

                if (one.getData().getProbOcc() <= two.getData().getProbOcc()) {

                    TreeNode mid = new TreeNode(data, one, two);
                    targetQueue.enqueue(mid);
                } else {

                    TreeNode mid = new TreeNode(data, two, one);
                    targetQueue.enqueue(mid);
                }
            } else if (targetQueue.isEmpty()) {
                tempQueue.enqueue(sourceQueue.dequeue());
            } else if(sourceQueue.isEmpty()) {
                tempQueue.enqueue(targetQueue.dequeue());
            } else if (sourceQueue.peek().getData().getProbOcc() <= targetQueue.peek().getData().getProbOcc()) {
                tempQueue.enqueue(sourceQueue.dequeue());
            } else {
                tempQueue.enqueue(targetQueue.dequeue());
            }
        }

        if (tempQueue.size() == 2) {

            TreeNode one = tempQueue.dequeue();
            TreeNode two = tempQueue.dequeue();

            CharFreq data = new CharFreq(null, one.getData().getProbOcc() + two.getData().getProbOcc());

            if (one.getData().getProbOcc() <= two.getData().getProbOcc()) {

                TreeNode mid = new TreeNode(data, one, two);
                huffmanRoot = mid;
                return;
            } else {

                TreeNode mid = new TreeNode(data, two, one);
                huffmanRoot = mid;
                return;
            }
        } else {

            huffmanRoot = tempQueue.dequeue();
            return;
        }
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

	/* Your code goes here */

        encodings = new String[128];

        treeSearch(huffmanRoot, "");
    }

    private void treeSearch(TreeNode huffyBoi, String s) {

        if (huffyBoi.getLeft() == null && huffyBoi.getRight() == null) {

            char charData = huffyBoi.getData().getCharacter();
            int index = (int) charData;
            encodings[index] = s;
            return;
        }

        treeSearch(huffyBoi.getLeft(), s + "0");
        treeSearch(huffyBoi.getRight(), s + "1");
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	/* Your code goes here */
        
        String bitString = "";

        while (StdIn.hasNextChar()) {

            int index = (int) StdIn.readChar();
            bitString = bitString + encodings[index];
        }

        writeBitString(encodedFile, bitString);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */

        String bitString = readBitString(encodedFile);

        TreeNode temp = huffmanRoot;

        for (int i = 0; i < bitString.length(); i++) {

            if (bitString.charAt(i) == '0') {

                temp = temp.getLeft();
            } else {

                temp = temp.getRight();
            }

            if (temp.getLeft() == null && temp.getRight() == null) {

                StdOut.print(temp.getData().getCharacter());
                temp = huffmanRoot;
            }
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
