import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/5/11
 * @description Huffman code class
 */
public class Huffman implements PrefixCode {
    private Node[] nodes;

    // private LinkedList<Character> rawChars;
    private LinkedSimpleList<Character> rawChars;

    private HashSimpleMap<Character, String> huffCodesMap;
    private int originalSize;

    /**
     * 生成Huffman编码
     * @param in the input stream containing the characters of the
     */
    @Override
    public void generateCode(InputStream in) {
        // 调用getFrequencyCounts方法，获取字符种类及其频率
        HashSimpleMap<Character, Integer> frequencyCounts = getFrequencyCounts(in);

        // 获取所有的字符种类，即frequencyCounts的键值
        LinkedSimpleList<Character> keys = frequencyCounts.keys();
        // 获取所有的Entry
        LinkedSimpleList<HashSimpleMap.Entry<Character, Integer>> frequencyCountsEntries
                = frequencyCounts.getTotalEntries();

        // 将出现频率进行升序排序，作为建立Huffman树的准备
        Integer[] freqSorted = new Integer[keys.size()];
        getSortedFreq(frequencyCounts, keys, freqSorted);

        // 依据升序排序的出现频率，对应排序所有的字符种类
        Character[] keySorted = new Character[keys.size()];
        getSortedKeys(frequencyCountsEntries, freqSorted, keySorted);

        CompleteBinaryHeapPriorityDeque<Node> nodePriorityDeque = new CompleteBinaryHeapPriorityDeque<>();
        nodes = new Node[freqSorted.length];

        // 依据出现频率初始化优先队列
        // 优先级就是出现频率
        // 因为出现频率低的在创建Huffman树的时候应该先弹出，所以优先级更高
        for (int i = 0; i < freqSorted.length; i++) {
            // 初始化节点列表，记录weight和character
            nodes[i] = new Node(freqSorted[i], keySorted[i]);
            nodePriorityDeque.insert(freqSorted[i], nodes[i]);
        }

        // 循环创建Huffman树，直到优先队列里只剩下一个节点，即根节点
        while (nodePriorityDeque.size() > 1) {
            // 从节点的优先队列里选出两个出现频率最低的节点
            Node leftNode = nodePriorityDeque.removeMin();
            Node rightNode = nodePriorityDeque.removeMin();
            // 计算得到双亲节点的weight，并连接左子节点和右子节点
            Node parent = new Node(leftNode.weight + rightNode.weight, leftNode, rightNode);
            // 将双亲节点加入优先队列
            nodePriorityDeque.insert(parent.weight, parent);
        }

        // 完成循环，从优先队列中取出最后的节点，即根节点
        // 至此，Huffman树构建完毕
        Node root = nodePriorityDeque.removeMin();

        // 递归遍历Huffman树，生成每个节点的Huffman编码
        encodeByRecursion(root, "");

        // 记录所有字符与其Huffman编码的Hashmap
        huffCodesMap = new HashSimpleMap<>();
        for (Node node : nodes) {
            huffCodesMap.put(node.character, node.code);
        }
    }

    /**
     * 根据指定字符，返回其对应的Huffman编码
     * @param ch the character whose codeword is sought
     * @return Huffman code of character ch
     */
    @Override
    public String getCodeword(char ch) {
        // 两种方法
        // 1. 遍历节点列表，找到ch字符对应的节点，将该节点的code字段返回
        // assert freqSorted.length == keySorted.length;
        // for (int i = 0; i < keySorted.length; i++) {
        //     if (keySorted[i].equals(ch)) {
        //         return nodes[i].code;
        //     }
        // }
        // return "";

        // 2. 直接从生成的huffCodesMap中取ch的Huffman编码
        String codeWord = this.huffCodesMap.get(ch);
        return codeWord != null ? codeWord : "";
    }

    /**
     * 依据指定的Huffman编码，返回对应字符
     * @param codeword a (binary) string of a codeword
     * @return codeword对应的字符
     */
    @Override
    public int getChar(String codeword) {
        // 遍历节点列表，匹配与codeword相等的节点
        for (Node node : nodes) {
            if (node.code.equals(codeword)) {
                return node.character;
            }
        }
        return 0;
    }

    /**
     * 依据指定的字符串，生成编码后的字符串
     * @param str the string to be encoded
     * @return 编码后的字符串
     */
    @Override
    public String encode(String str) {
        // 依据输入的字符串，生成输入流
        ByteArrayInputStream strIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        // 调用generateCode方法生成Huffman树
        generateCode(strIn);

        // 以下是生成Huffman编码的过程
        StringBuilder encoderBuilder = new StringBuilder();

        int len;
        strIn = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

        // 读取每一个字符
        while ((len = strIn.read()) != -1) {
            Character currentChar = (char) len;
            // 从huffCodesMap找到字符并记录
            encoderBuilder.append(huffCodesMap.get(currentChar));
        }

        // 返回编码后的字符串
        return encoderBuilder.toString();
    }

    /**
     * 依据编码后的二进制字符串，解码出原本的字符串
     * @param str the binary string to be decoded
     * @return 原本的字符串
     */
    @Override
    public String decode(String str) {
        StringBuilder decoderBuilder = new StringBuilder();

        // 取出huffCodesMap的所有键值对
        LinkedSimpleList<HashSimpleMap.Entry<Character, String>> huffCodesEntries =
                huffCodesMap.getTotalEntries();

        // 逐渐解码二进制字符串，每解码一部分就删除一部分，知道二进制字符串全部被解码
        while (str.length() > 0) {
            // 遍历所有的字符种类及其编码
            for (HashSimpleMap.Entry<Character, String> huffCodesEntry : huffCodesEntries) {
                String huffCode = huffCodesEntry.value;
                // 找到二进制字符串以何种编码起始
                if (str.startsWith(huffCode)) {
                    // 加入对应字符
                    decoderBuilder.append(huffCodesEntry.key);
                    // 剔除已解码部分
                    str = str.substring(huffCode.length());
                    break;
                }
            }
        }

        return decoderBuilder.toString();
    }

    /**
     * 原始字符串长度
     * @return 原始字符串长度
     */
    @Override
    public int originalSize() {
        return originalSize;
    }

    /**
     * 压缩后的二进制编码长度
     * @return 压缩后的二进制编码长度
     */
    @Override
    public int compressedSize() {
        StringBuilder codeBuilder = new StringBuilder();

        for (Character character : rawChars) {
            codeBuilder.append(huffCodesMap.get(character));
        }

        // 因为一个字节是8bits，所以算字节数的时候除以8就可以
        return codeBuilder.toString().length() / 8;
    }

    /**
     * 递归创建Huffman编码
     * @param node 指定节点
     * @param code 指定节点的Huffman编码
     */
    private void encodeByRecursion(Node node, String code) {
        if (node == null) {
            return;
        }
        node.code = code;
        // 左子节点赋值为当前编码加0
        encodeByRecursion(node.lChild, node.code + "0");
        // 右子节点赋值为当前编码加1
        encodeByRecursion(node.rChild, node.code + "1");
    }

    /**
     * 根据输入流，统计所有字符的出现频率
     * @param in 输入流
     * @return 自定义的HashMap，键：出现的字符种类；值：该字符的出现频率
     */
    private HashSimpleMap<Character, Integer> getFrequencyCounts(InputStream in) {
        // 初始化字符频率统计HashMap
        HashSimpleMap<Character, Integer> frequencyCounts = new HashSimpleMap<>();
        // 存储每个字符
        int len;
        // 字符计数器
        int count = 0;

        // rawChars = new LinkedList<>();
        // 这里自己实现的单链表太慢了，应该改用双向链表，尾插比较快速
        // 或者直接头插，因为不需要保证方向，只需要统计文章所有字符
        rawChars = new LinkedSimpleList<>();

        try {
            // 读取文件
            while ((len = in.read()) != -1) {
                Character currentChar = (char) len;

                // rawChars.add(currentChar);

                // 记录语料单个字符
                rawChars.add(0, currentChar);

                // 记录字符出现频率的灵魂
                if (!frequencyCounts.contains(currentChar)) {
                    // 如果当前字符不在记录HashMap的键值里
                    // 则新建<当前字符, 1>的键值对
                    frequencyCounts.put(currentChar, 1);
                } else {
                    // 否则，取出当前字符对应的键值（即当前字符已经记录的出现频率）
                    Integer currentFreq = frequencyCounts.get(currentChar);
                    // 将其加一，重新赋值即可
                    frequencyCounts.put(currentChar, currentFreq + 1);
                }

                // 计数器加一
                count += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 这里就得到了原来的文本字符数
        originalSize = count;
        return frequencyCounts;
    }

    /**
     * 获取升序排序的频率数组
     * @param frequencyCounts 所有字符的出现频率Hashmap
     * @param keys 所有字符
     * @param keyFreq 所有字符的出现频率
     */
    private void getSortedFreq(HashSimpleMap<Character, Integer> frequencyCounts,
                                  LinkedSimpleList<Character> keys, Integer[] keyFreq) {
        int count = 0;
        for (Character key : keys) {
            keyFreq[count] = frequencyCounts.get(key);
            count += 1;
        }

        Arrays.sort(keyFreq);
    }

    /**
     * 获取依据升序排序的频率数组顺序的字符列表
     * @param frequencyCountsEntries 所有字符的出现频率Hashmap的Entry
     * @param freqSorted 升序排序的频率数组
     * @param keySorted 依据升序排序的频率数组顺序的字符列表
     */
    private void getSortedKeys(LinkedSimpleList<HashSimpleMap.Entry<Character, Integer>> frequencyCountsEntries,
                               Integer[] freqSorted, Character[] keySorted) {
        int count = 0;
        // 遍历freq
        for (Integer freq : freqSorted) {
            // 遍历每一个<key, freq>
            for (HashSimpleMap.Entry<Character, Integer> frequencyCountsEntry : frequencyCountsEntries) {
                // 找到了相同freq的key
                if (freq.equals(frequencyCountsEntry.value)) {
                    // 如果key不在已统计范围，才会添加
                    // 为了解决出现次数相同的情况
                    if (!Arrays.asList(keySorted).contains(frequencyCountsEntry.key)) {
                        keySorted[count] = frequencyCountsEntry.key;
                        count += 1;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 节点内部类
     */
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
