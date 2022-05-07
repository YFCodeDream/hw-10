import java.util.Objects;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/4/28
 * @description HashMap
 */
public class HashSimpleMap<K extends Comparable<K>, V> implements SimpleMap<K, V> {
    private final HashUSet<Entry<K, V>> entryHashUSet;

    public HashSimpleMap() {
        entryHashUSet = new HashUSet<>();
    }

    @Override
    public int size() {
        return entryHashUSet.size();
    }

    @Override
    public boolean isEmpty() {
        return entryHashUSet.isEmpty();
    }

    @Override
    public V get(K key) {
        Entry<K, V> kvEntry = entryHashUSet.find(new Entry<>(key, null));
        return kvEntry != null ? kvEntry.value : null;
    }

    @Override
    public V put(K key, V value) {
        Entry<K, V> currentEntry = entryHashUSet.find(new Entry<>(key, null));
        if (currentEntry == null) {
            entryHashUSet.add(new Entry<>(key, value));
            return null;
        }
        entryHashUSet.remove(currentEntry);
        entryHashUSet.add(new Entry<>(key, value));
        return currentEntry.value;
    }

    @Override
    public V remove(K key) {
        Entry<K, V> removedEntry = entryHashUSet.find(new Entry<>(key, null));
        if (removedEntry == null) {
            return null;
        }
        entryHashUSet.remove(removedEntry);
        return removedEntry.value;
    }

    @Override
    public boolean contains(K key) {
        Entry<K, V> currentEntry = entryHashUSet.find(new Entry<>(key, null));
        return currentEntry != null;
    }

    public LinkedSimpleList<Entry<K, V>> getTotalEntries() {
        return entryHashUSet.getTotalElements();
    }

    public LinkedSimpleList<K> keys() {
        LinkedSimpleList<Entry<K, V>> totalEntries = getTotalEntries();
        LinkedSimpleList<K> totalKeys = new LinkedSimpleList<>();
        int count = 0;
        for (Entry<K, V> entry : totalEntries) {
            totalKeys.add(count, entry.key);
        }
        return totalKeys;
    }

    public void clear() {
        LinkedSimpleList<K> keys = keys();
        for (K key : keys) {
            remove(key);
        }
    }

    @Override
    public String toString() {
        return "HashSimpleMap{" +
                "entryHashUSet=" + entryHashUSet +
                '}';
    }

    @SuppressWarnings("unchecked")
    public static class Entry<K1, V1> {
        K1 key;
        V1 value;

        public Entry(K1 key, V1 value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<K1, V1> entry = (Entry<K1, V1>) o;
            return Objects.equals(key, entry.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
