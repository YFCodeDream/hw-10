/**
 * <p>An interface representing an unordered set of objects of type
 * <code>E</code>. Here, a <em>set</em> is a collection of
 * <em>distinct</em> elements. The state of a <code>SimpleUSet</code>
 * is represented by a (mathematical) set of elements <code>{x_1, x_2,
 * ..., x_n}</code>, where <code>n</code> denotes the size (i.e.,
 * number of elements) in the set. Sets do not contain duplicate
 * elements. Thus for each <code>i != j</code>, we have that
 * <code>x_i</code> and <code>x_j</code> are semantically
 * distinct. That is, <code>x_i.equals(x_j)</code> evaluates to
 * <code>false</code>. Operations are provided to determine the size
 * of the <code>SimleUSet</code>, test if it empty (contains no
 * elements), as well as adding, removing, and finding elements.</p>
 *
 * @author Will Rosenbaum
 */

public interface SimpleUSet<E> {
    /**
     * <p>Return the size (i.e., number of elements contained in) the
     * <code>SimpleUSet</code>. If the set has state <code>{x_1, x_2,
     * ..., x_n}</code>, then the value <code>n</code> is
     * returned.</p>p
     *
     * @return the size of the set
     */
    int size();

    /**
     * <p>Determine if the <code>SimpleUSet</code> is empty (i.e.,
     * contains no elements)</p>
     *
     * @return <code>true</code> if and only if the set is empty
     */
    boolean isEmpty();

    /**
     * <p>Add a new element to the set. This method first checks if
     * the set already contains an element <code>y</code> that is
     * equal to the element <code>x</code> being added (i.e.,
     * <code>y</code> satisfies <code>x.equals(y)</code>). If no such
     * element is found then <code>x</code> is added to the set, and
     * the value <code>true</code> is returned. If such an element
     * <code>y</code> is found, then the set is not modified and the
     * method returns <code>false</code></p>
     *
     * <p>Formally, if the <code>SimpleUSet</code> is in state <code>S
     * = {x_1, x_2, ..., x_m}</code>, then after a call to
     * <code>add(x)</code>, the state state is not modified if there
     * exists some <code>x_i</code> in <code>S</code> satisfying
     * <code>x.equals(x_i)</code>. Otherwise, the state is updated to
     * <code>S = {x_1, x_2, ..., x_n, x}</code>.</p>
     *
     * @param x the element to be added to the set
     * @return <code>true</code> if and only if the element was successfully added
     */
    boolean add(E x);

    /**
     * <p>Remove an element equal to <code>x</code> from the
     * set. Specifically, if the set contains an element
     * <code>y</code> satisfying <code>x.equals(y)</code>, then
     * <code>y</code> is removed from the set and returned. Otherwise,
     * the method returns the value <code>null</code>.</p>
     *
     * <p>More formally, if the <code>SimpleUSet</code> has state
     * <code>S = {x_1, x_2, ..., x_n}</code> and there is no element
     * <code>x_i</code> in <code>S</code> satisfying
     * <code>x.equals(x_i)</code>, then the value <code>null</code> is
     * returned, and <code>S</code> remains unchanged. Otherwise, if
     * <code>x.equals(x_i)</code>, then the state is updated to
     * <code>{x_1, x_2,...,x_{i-1}, x_{i+1},...,x_{n}}</code>, and
     * <code>x_i</code> is returned.</p>
     *
     * @param x the element to be removed from the set (if present)
     * @return an element <code>y</code> in <code>S</code> satisfying
     * <code>x.equals(y)</code>, if present, and <code>null</code>
     * otherwise
     */
    E remove(E x);


    /**
     * <p>Find an element equal to <code>x</code> in the set. If such
     * an element <code>y</code> satisfying <code>x.equals(y)</code>
     * is found, return it. Otherwise return <code>null</code>. This
     * method does not change the state of the set.</p>
     *
     * @param x the element to be found in the set
     * @return an element <code>y</code> in the set satisfying
     * <code>x.equals(y)</code>, if present, and <code>null</code>
     * otherwise
     */
    E find(E x);
}
