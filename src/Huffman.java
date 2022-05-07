import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/5/4
 * @description Huffman code class
 */
@SuppressWarnings("CommentedOutCode")
public class Huffman implements PrefixCode {
    private Node root;
    private Node[] nodes;

    private InputStream fileIn;

    private Integer[] freqSorted;
    private Character[] keySorted;

    private String rawStr;
    private LinkedList<Character> rawChars;

    private HashSimpleMap<Character, String> huffCodesMap;
    private int originalSize;

    private static final int MAX_FILE_SIZE = 6000000;

    @Override
    public void generateCode(InputStream in) {
        HashSimpleMap<Character, Integer> frequencyCounts = getFrequencyCounts(in);

        LinkedSimpleList<Character> keys = frequencyCounts.keys();
        LinkedSimpleList<HashSimpleMap.Entry<Character, Integer>> frequencyCountsEntries
                = frequencyCounts.getTotalEntries();

//        int count = 0;
//        for (HashSimpleMap.Entry<Character, Integer> entry : frequencyCountsEntries) {
//            count += 1;
//        }
//        System.out.println(count);
//        System.out.println(originalSize);

        freqSorted = new Integer[keys.size()];
        getSortedFreq(frequencyCounts, keys, freqSorted);

        keySorted = new Character[keys.size()];
        getSortedKeys(frequencyCountsEntries, freqSorted, keySorted);

        CompleteBinaryHeapPriorityDeque<Node> nodePriorityDeque = new CompleteBinaryHeapPriorityDeque<>();
        nodes = new Node[freqSorted.length];

//        System.out.println(Arrays.toString(freqSorted));
//        System.out.println(freqSorted.length);
//        System.out.println(keySorted.length);
//        System.out.println(Arrays.asList(keySorted).contains('¢'));
//        for (Character key : keySorted) {
//            if (key == '¢')
//            System.out.println(key);
//        }

//        System.out.println(frequencyCounts);

        for (int i = 0; i < freqSorted.length; i++) {
            nodes[i] = new Node(freqSorted[i], keySorted[i]);
            nodePriorityDeque.insert(freqSorted[i], nodes[i]);
        }

        while (nodePriorityDeque.size() > 1) {
            Node leftNode = nodePriorityDeque.removeMin();
            Node rightNode = nodePriorityDeque.removeMin();
            Node parent = new Node(leftNode.weight + rightNode.weight, leftNode, rightNode);
            nodePriorityDeque.insert(parent.weight, parent);
        }

        root = nodePriorityDeque.removeMin();
        encodeByRecursion(root, "");

        huffCodesMap = new HashSimpleMap<>();
        for (Node node : nodes) {
            huffCodesMap.put(node.character, node.code);
        }
    }

    @Override
    public String getCodeword(char ch) {
        assert freqSorted.length == keySorted.length;
        for (int i = 0; i < keySorted.length; i++) {
            if (keySorted[i].equals(ch)) {
                return nodes[i].code;
            }
        }
        return "";
    }

    @Override
    public int getChar(String codeword) {
        for (Node node : nodes) {
            if (node.code.equals(codeword)) {
                return node.character;
            }
        }
        return 0;
    }

    @Override
    public String encode(String str) {
        ByteArrayInputStream strIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        generateCode(strIn);
        StringBuilder encoderBuilder = new StringBuilder();

        int len;
        strIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

        while ((len = strIn.read()) != -1) {
            Character currentChar = (char) len;
            encoderBuilder.append(huffCodesMap.get(currentChar));
        }
        //        for (int i = 0; i < str.length(); i++) {
//            if (huffCodesMap.get(str.charAt(i)) == null) {
//                Character test = str.charAt(i);
//                System.out.println(test);
//            }
//            encoderBuilder.append(huffCodesMap.get(str.charAt(i)));
//        }
        return encoderBuilder.toString();
    }

    @Override
    public String decode(String str) {
        StringBuilder decoderBuilder = new StringBuilder();
//        String encodeStr = encode(rawStr);
        LinkedSimpleList<HashSimpleMap.Entry<Character, String>> huffCodesEntries = huffCodesMap.getTotalEntries();
//        System.out.println(huffCodesMap);
        while (str.length() > 0) {
            for (HashSimpleMap.Entry<Character, String> huffCodesEntry : huffCodesEntries) {
                String huffCode = huffCodesEntry.value;
                if (str.startsWith(huffCode)) {
                    decoderBuilder.append(huffCodesEntry.key);
                    str = str.substring(huffCode.length());
                    break;
                }
            }
        }
        return decoderBuilder.toString();
    }

    @Override
    public int originalSize() {
        return originalSize;
    }

    @Override
    public int compressedSize() {
        int len;
        InputStream strIn;
        StringBuilder codeBuilder = new StringBuilder();

        if (this.fileIn == null) {
            strIn = new ByteArrayInputStream(rawStr.getBytes(StandardCharsets.UTF_8));
            try {
                while ((len = strIn.read()) != -1) {
                    Character currentChar = (char) len;
//                if (huffCodesMap.get(currentChar) == null) System.out.println(currentChar);
//                System.out.println(huffCodesMap.get(currentChar));
                    codeBuilder.append(huffCodesMap.get(currentChar));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (Character character : rawChars) {
                codeBuilder.append(huffCodesMap.get(character));
            }
        }

//        System.out.println(codeBuilder.toString().length() / 8);
//        return encode(rawStr).length() / 8;
        return codeBuilder.toString().length() / 8;
    }

    private void encodeByRecursion(Node node, String code) {
        if (node == null) {
            return;
        }
        node.code = code;
        encodeByRecursion(node.lChild, node.code + "0");
        encodeByRecursion(node.rChild, node.code + "1");
    }

    private HashSimpleMap<Character, Integer> getFrequencyCounts(InputStream in) {
        if (in.getClass().equals(FileInputStream.class)) {
            this.fileIn = new BufferedInputStream(in);
        } else {
            this.fileIn = null;
        }

        HashSimpleMap<Character, Integer> frequencyCounts = new HashSimpleMap<>();
        int len;
        int count = 0;
        rawChars = new LinkedList<>();
//        这里自己实现的单链表太慢了，应该改用双向链表，尾插比较快速
//        rawChars = new LinkedSimpleList<>();
        StringBuilder rawBuilder = new StringBuilder();
        try {
            while ((len = in.read()) != -1) {
                Character currentChar = (char) len;

                rawBuilder.append(currentChar);
                rawChars.add(currentChar);

                if (!frequencyCounts.contains(currentChar)) {
//                    if (currentChar == 'Â') System.out.println("Âtest");
                    frequencyCounts.put(currentChar, 1);
                } else {
                    Integer currentFreq = frequencyCounts.get(currentChar);
                    frequencyCounts.put(currentChar, currentFreq + 1);
//                    if (currentChar == 'Â') System.out.println(frequencyCounts.get('Â'));
                }
//                if (currentChar != '\n') {
//                    count += 1;
//                }
                count += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        rawStr = rawBuilder.toString();
//        System.out.println("rawStr.contains(\"Â\")" + rawStr.contains("Â"));
        originalSize = count;
        return frequencyCounts;
    }

    private void getSortedFreq(HashSimpleMap<Character, Integer> frequencyCounts,
                                  LinkedSimpleList<Character> keys, Integer[] keyFreq) {
        int count = 0;
        for (Character key : keys) {
            keyFreq[count] = frequencyCounts.get(key);
            count += 1;
        }

        Arrays.sort(keyFreq);
    }

    private void getSortedKeys(LinkedSimpleList<HashSimpleMap.Entry<Character, Integer>> frequencyCountsEntries,
                               Integer[] freqSorted, Character[] keySorted) {
        int count = 0;
        // 遍历freq
        for (Integer freq : freqSorted) {
            // 遍历每一个<key, freq>
            for (HashSimpleMap.Entry<Character, Integer> frequencyCountsEntry : frequencyCountsEntries) {
                // 找到了相同freq的key
                if (freq.equals(frequencyCountsEntry.value)) {
                    if (!Arrays.asList(keySorted).contains(frequencyCountsEntry.key)) {
                        keySorted[count] = frequencyCountsEntry.key;
                        count += 1;
                        break;
                    }
                }
            }
        }
    }

    public static class Node implements Comparable<Node> {
        int weight;

        Character character;

        String code;

        Node lChild;

        Node rChild;


        public Node(int weight, Character character) {
            this.weight = weight;
            this.character = character;
        }


        public Node(int weight, Node lChild, Node rChild) {
            this.weight = weight;
            this.lChild = lChild;
            this.rChild = rChild;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.weight, o.weight);
        }
    }
}
