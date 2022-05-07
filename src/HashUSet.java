import java.util.Random;

@SuppressWarnings("unchecked")
public class HashUSet<E> implements SimpleUSet<E> {
    public static final int DEFAULT_LOG_CAPACITY = 4;

    protected int logCapacity = DEFAULT_LOG_CAPACITY; // value d from lecture
    protected int capacity = 1 << logCapacity;        // n = 2^d
    protected int size = 0;
    protected Object[] table;                         // array of heads of linked lists

    // final = can't be changed after initial assignment!
    protected final int z;

    protected double loadFactor = 0.75;
    protected double uploadFactor = 0.25;

    public HashUSet() {
        // set capacity to 2^logCapacity
        int capacity = 1 << logCapacity;

        table = new Object[capacity];

        // add a sentinel node to each list in the table
        for (int i = 0; i < capacity; ++i) {
            table[i] = new Node<E>(null);
//            table[i] = null; wrong
        }

        // fix a random odd integer
        Random r = new Random();
        z = (r.nextInt() << 1) + 1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
	public boolean add(E x) {
        int currentTableIndex = getIndex(x);
        Node<E> currentTableNode = (Node<E>) table[currentTableIndex];

        if (currentTableNode.value == null) {
            currentTableNode.value = x;
            size += 1;
            return true;
        }

        Node<E> currentIndexLastNode = null;
        for (Node<E> node = currentTableNode; node != null; node = node.next) {
            if (x.equals(node.value)) {
                return false;
            }
            currentIndexLastNode = node;
        }

        currentIndexLastNode.next = new Node<>(x);
        size += 1;

        if (loadFactor * capacity < size) {
            increaseCapacity();
        }

        return true;
    }

    @Override
	public E remove(E x) {
        int currentTableIndex = getIndex(x);
        Node<E> currentTableNode = (Node<E>) table[currentTableIndex];

        if (currentTableNode.value == null) {
            return null;
        }

        if (x.equals(currentTableNode.value)) {
            E removedValue = currentTableNode.value;
            table[currentTableIndex] = currentTableNode.next != null ?
                    currentTableNode.next : new Node<E>(null);
            size -= 1;
//            if (uploadFactor * capacity > size) {
//                decreaseCapacity();
//            }
            return removedValue;
        }

        Node<E> aheadNode = currentTableNode;
        for (Node<E> node = currentTableNode; node != null; node = node.next) {
            if (x.equals(node.value)) {
                E removedValue = node.value;
                aheadNode.next = node.next;
                size -= 1;
//                if (uploadFactor * capacity > size) {
//                    decreaseCapacity();
//                }
                return removedValue;
            }
            aheadNode = node;
        }

        return null;
    }

    @Override
	public E find(E x) {
        int currentTableIndex = getIndex(x);
        Node<E> currentTableNode = (Node<E>) table[currentTableIndex];

        if (x.equals(currentTableNode.value)) {
            return currentTableNode.value;
        }

        for (Node<E> node = currentTableNode; node != null; node = node.next) {
            if (x.equals(node.value)) {
                return node.value;
            }
        }

        return null;
    }

    public LinkedSimpleList<E> getTotalElements() {
        LinkedSimpleList<E> totalElements = new LinkedSimpleList<>();
        int count = 0;
        for (Object o : table) {
            for (Node<E> node = (Node<E>) o; node != null; node = node.next) {
                if (node.value != null) {
                    totalElements.add(count, node.value);
                }
            }
        }
        return totalElements;
    }

    /**
     * hash算法
     * @param x 输入的数值
     * @return 0-capacity的数值
     */
    protected int getIndex(E x) {
        // get the first logCapacity bits of z * x.hashCode()
//        return ((z * x.hashCode()) >>> 32 - logCapacity);
        // raw hash function cause ArrayIndexOutOfBoundsException
//        System.out.println(capacity);
        return x.hashCode() % capacity;
    }

    protected void increaseCapacity() {
        logCapacity += 1;
        capacity = capacity << 1;

        copyTableValues();
    }

    protected void decreaseCapacity() {
        logCapacity -= 1;
        capacity = capacity >> 1;

        copyTableValues();
    }

    private void copyTableValues() {
        // store the old hash table
        Object[] oldTable = table;

        // make a new has table and initialize it
        table = new Object[capacity];

        for (int i = 0; i < table.length; ++i) {
            table[i] = new Node<E>(null);
        }

        // reset the size to 0 since it will get incremented when we
        // add elements to the new table
        size = 0;

        // iterate over lists in oldTable and add elements to new table
        for (Object o : oldTable) {
            Node<E> nd = ((Node<E>) o);
            while (nd != null) {
                if (nd.value != null) {
                    this.add(nd.value);
                }
                nd = nd.next;
            }
        }
    }

    protected class Node<T> {
        protected Node<T> next = null;
        protected T value;

        public Node(T value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder("HashUSet{\n");
        String[] resStrArr = new String[table.length];
        for (int i = 0; i < table.length; i++) {
            resStrArr[i] = "index: " + i + " ; values: ";
            for (Node<E> node = (Node<E>) table[i]; node != null; node = node.next) {
                resStrArr[i] += (node.value + " ");
            }
        }
        for (int i = 0; i < resStrArr.length; i++) {
            resStr.append("\t").append(resStrArr[i]).append("\n");
        }
        resStr.append("}");
        return resStr.toString();
    }
}
