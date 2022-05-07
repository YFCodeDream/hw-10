import java.util.Iterator;
import java.util.Objects;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/2/26
 * @description 链表
 */
@SuppressWarnings("GrazieInspection")
public class LinkedSimpleList<T> implements SimpleList<T>, Iterable<T> {
    private int size;

    private Node<T> head;

    public LinkedSimpleList() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * 获取第i个元素
     * @param i the index to be queried
     * @return 第i个元素的值
     */
    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        return Objects.requireNonNull(getNode(i)).value;
    }

    /**
     * 设置第i个元素的值
     * @param i the index of the element to be set
     * @param x the element to be inserted at index <code>i</code>
     */
    @Override
    public void set(int i, T x) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> node = getNode(i);
        Objects.requireNonNull(node).value = x;
    }

    @Override
    public void add(int i, T x) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> addNode = new Node<>(x);

        if (i == 0) {
            addNode.next = head;
            head = addNode;
        } else {
            Node<T> aheadNode = getNode(i - 1);
            Node<T> hinderNode = Objects.requireNonNull(aheadNode).next;
            aheadNode.next = addNode;
            addNode.next = hinderNode;
        }

        // 修改size和tail标记
        size += 1;

        // 更新序列号
        renewIndices();
    }

    @Override
    public T remove(int i) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        /*
         * 先讨论删除头部的情况，则：
         *      获取头部节点的后继节点
         *      将新头部置为原头部的后继节点
         *      将size减一
         *      更新序列号
         */
        if (i == 0) {
            T removedValue = head.value;
            head = head.next;

            size -= 1;
            renewIndices();

            return removedValue;
        }

        /*
         * 再讨论内部节点，则：
         *      先获取先前节点（此时先前节点必不为空，因为头部节点的情况已经在上面讨论过了）
         *      获取待删除节点
         *      将先前节点的后继节点置为待删除节点的后继节点
         *      将size减一
         *      更新序列号
         */
        Node<T> aheadNode = getNode(i - 1);

        assert aheadNode != null;
        Node<T> removedNode = aheadNode.next;

        assert removedNode != null;
        aheadNode.next = removedNode.next;

        size -= 1;
        renewIndices();

        return removedNode.value;
    }

    /**
     * 将下标为i的元素移动到链表头部
     * @param i 待移动元素下标
     */
    public void removeToFirst(int i) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        if (i == 0) {
            return;
        }

        Node<T> aheadNode = getNode(i - 1);

        assert aheadNode != null;
        Node<T> removedNode = aheadNode.next;

        assert removedNode != null;
        aheadNode.next = removedNode.next;
        removedNode.next = head;
        head = removedNode;

        renewIndices();
    }

    public void reverse() {
        Node<T> currentNode = head;
        Node<T> hinderNode = currentNode.next;
        Node<T> hinderNextNode = hinderNode != null ? hinderNode.next : null;

        while (hinderNode != null) {
            hinderNode.next = currentNode;
            if (currentNode == head) {
                currentNode.next = null;
            }
            currentNode = hinderNode;
            hinderNode = hinderNextNode;
            if (hinderNode != null) {
                hinderNextNode = hinderNode.next;
            }
        }

        head = currentNode;
        renewIndices();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedSimpleListItr();
    }

    /**
     * 节点私有内部类
     * @param <E> 节点值泛型
     */
    private static class Node<E> {
        /**
         * 标记先前节点
         */
        Node<E> next;

        /**
         * 节点值
         */
        E value;

        /**
         * 当前节点序列号，可无需此属性，该属性便于查看节点信息
         */
        int index;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "next=" + (next == null ? "null" : next.index) +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * 链表迭代器
     * 将LinkedSampleList实例化的对象变成可以迭代的对象
     */
    private class LinkedSimpleListItr implements Iterator<T> {
        private Node<T> currentNode;

        public LinkedSimpleListItr() {
            currentNode = head;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T currentValue = currentNode.value;
                currentNode = currentNode.next;
                return currentValue;
            }
            return currentNode.value;
        }
    }

    /**
     * 辅助方法，依据索引返回指定节点
     * 其实为返回index == i的节点
     * @param i 索引值
     * @return 指定索引值的节点
     */
    private Node<T> getNode(int i) {
        if (i < 0 || i >= size) return null;

        Node<T> currentNode = head;

        for (int j = 0; j < i; j++) {
            currentNode = currentNode.next;
        }

        return currentNode;
    }

    /**
     * 辅助方法，更新索引值
     */
    private void renewIndices() {
        int index = 0;
        for (Node<T> node = head; node != null; node = node.next) {
            node.index = index;
            index += 1;
        }
    }

    /**
     * 重写的toString方法，便于查看双向链表信息
     * @return 存有双向链表信息的字符串
     */
    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder();
        resStr.append("LinkedSimpleList: {\n");
        int index = 0;
        for (Node<T> node = head; node != null; node = node.next) {
            resStr.append("\t").append("node ").append(index).append(":\n\t").append(node).append("\n");
            index += 1;
        }
        resStr.append("}");
        return resStr.toString();
    }
}
