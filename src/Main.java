import java.io.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/5/4
 * @description test main
 */
public class Main {
    public static final String STR = "aaaaaaaaaaaaaaaabbbbbbbbccccdde";

    public static void testReadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/metamorphoses.txt");
//            byte[] arr = new byte[5];
            int len;
            int count = 0;
            while ((len = fileInputStream.read()) != -1) {
//                System.out.println(new String(arr, 0, len));
                System.out.println((char) len);
                System.out.println("---");
                count += 1;
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        InputStream in;
        PrefixCode huff = new Huffman();

//        System.out.println(getIndex(97));

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

        Integer[] testArr = new Integer[]{4, 5, 6, 6, 6, 7, 7, 1, 2, 3, 4};
        Arrays.sort(testArr);
        System.out.println(Arrays.toString(testArr));
    }

    protected static int getIndex(Object x) {
        // fix a random odd integer
        Random r = new Random();
        int z = (r.nextInt() << 1) + 1;
//        System.out.println(z);
        // get the first logCapacity bits of z * x.hashCode()
        int logCapacity = 4;
        return ((x.hashCode()) % 16);
    }
}
