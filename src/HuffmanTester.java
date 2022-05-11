import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class HuffmanTester {
    public static final String STR = "aaaaaaaaaaaaaaaabbbbbbbbccccdde";
    public static final char[] STR_CHARS = {'a', 'b', 'c', 'd', 'e'};
    public static final int[] STR_LENGTHS = {1, 2, 3, 4, 4};
    public static final int STR_ORIGINAL_SIZE = 31;
    public static final int STR_COMPRESSED_SIZE = 56 / 8;

    public static final String METAMORPHOSES_PATH = "src/metamorphoses.txt";
    public static final int METAMORPHOSES_ORIGINAL_SIZE = 140963;
    public static final int METAMORPHOSES_COMPRESSED_SIZE = 80011;


    public static final String SHAKESPEARE_PATH = "src/shakespeare.txt";
    public static final int SHAKESPEARE_ORIGINAL_SIZE = 5756698;
    public static final int SHAKESPEARE_COMPRESSED_SIZE = 3485420;
    public static final int SHAKESPEARE_MAX_RUNNING_TIME = 20;

    public static void main(String[] args) {
        InputStream in;
        PrefixCode huff = new Huffman();

        System.out.println("Testing short encoding task...");
        System.out.println("  generating code from string...");
        try {
            in = new ByteArrayInputStream(STR.getBytes("US-ASCII"));
            huff.generateCode(in);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding: US-ASCII\n" +
                    "no test performed!");
            System.exit(1);
        }

        System.out.println("   printing code...");
        if (!printCodewords(huff)) {
            System.out.println("  ...printing failed\n" +
                    "     null string returned (should return \"\")");
            System.exit(0);
        }

        System.out.println("  testing encoding size...");
        if (testSize(huff, STR_ORIGINAL_SIZE, STR_COMPRESSED_SIZE)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("  testing codeword length...");
        if (testShortEncoding(huff)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("  testing char encode/decode...");
        if (testCharEncoding(huff)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("  testing string encode/decode...");
        if (testStringEncoding(huff, STR)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("Testing Metamorphoses encoding task...");
        System.out.println("  generating code from " + METAMORPHOSES_PATH);
        try {
            in = new FileInputStream(METAMORPHOSES_PATH);
            huff = new Huffman();
            huff.generateCode(in);
        } catch (FileNotFoundException e) {
            System.err.println("File " + METAMORPHOSES_PATH + " not found\n" +
                    "no test performed!");
            System.exit(1);
        }

        System.out.println("  testing encoding size...");
        if (testSize(huff, METAMORPHOSES_ORIGINAL_SIZE, METAMORPHOSES_COMPRESSED_SIZE)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("Testing Shakespeare encoding task...");
        System.out.println("  generating code from " + SHAKESPEARE_PATH);
        RunTimer rt = new RunTimer();
        try {
            in = new FileInputStream(SHAKESPEARE_PATH);
            huff = new Huffman();
            rt.start();
            huff.generateCode(in);
            rt.stop();
        } catch (FileNotFoundException e) {
            System.err.println("File " + METAMORPHOSES_PATH + " not found\n" +
                    "no test performed!");
            System.exit(1);
        }

        System.out.println("  testing encoding size...");
        if (testSize(huff, SHAKESPEARE_ORIGINAL_SIZE, SHAKESPEARE_COMPRESSED_SIZE)) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

        System.out.println("  testing encoding running time...");
        if (rt.getElapsedSecs() <= SHAKESPEARE_MAX_RUNNING_TIME) {
            System.out.println("  ...test passed");
        } else {
            System.out.println("  ...test failed");
            System.exit(0);
        }

    }

    static boolean testShortEncoding(PrefixCode pc) {
        char ch;
        int length;
        String codeword;

        for (int i = 0; i < STR_CHARS.length; ++i) {
            ch = STR_CHARS[i];
            length = STR_LENGTHS[i];
            codeword = pc.getCodeword(ch);

            if (codeword.length() != length) {
                System.out.println("  char '" + ch + "' improperly encoded\n" +
                        "  expected codeword length: " + length + "\n" +
                        "  returned codeword length: " + codeword.length());

                return false;
            }
        }

        return true;
    }

    static boolean testCharEncoding(PrefixCode pc) {
        char ch;
        String codeword;
        int decoded;

        for (int i = 0; i < 256; ++i) {
            ch = (char) i;

            codeword = pc.getCodeword(ch);

            if (codeword == "") continue;

            decoded = pc.getChar(codeword);

            if (ch != decoded) {
                System.out.println("  failed to properly decode character!\n" +
                        "    character: " + (int) ch + "\n" +
                        "    codeword: " + codeword + "\n" +
                        "    decoded value: " + decoded);
                return false;
            }
        }

        return true;
    }

    static boolean testSize(PrefixCode pc, int expectedOriginalSize, int expectedCompressedSize) {
        int originalSize = pc.originalSize();
        int compressedSize = pc.compressedSize();

        if (expectedOriginalSize != originalSize) {
            System.out.println("  incorrect original size\n" +
                    "  expected: " + expectedOriginalSize + "\n" +
                    "  returned: " + originalSize);

            return false;
        }

        if (expectedCompressedSize != compressedSize) {
            System.out.println("  incorrect compressed size\n" +
                    "  expected: " + expectedCompressedSize + "\n" +
                    "  returned: " + compressedSize);

            return false;
        }

        return true;

    }

    static boolean testStringEncoding(PrefixCode pc, String str) {
        String encoded = pc.encode(str);
        String decoded = pc.decode(encoded);

        if (!decoded.equals(str)) {
            System.out.println("  string \"" + str + "\" failed to decode\n" +
                    "  encoded string: " + encoded + "\n" +
                    "  decoded string: " + decoded);

            return false;
        }

        return true;
    }

    static boolean printCodewords(PrefixCode pc) {
        for (int i = 0; i < 256; ++i) {

            String cw = pc.getCodeword((char) i);

            if (cw == null) return false;

            if (cw != "") {
                System.out.printf("   %3d: %s\n", i, cw);
            }

        }

        return true;
    }

}
    
