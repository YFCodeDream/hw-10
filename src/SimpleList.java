/**
 * <p>A container that stores an ordered list of objects, referred to as
 * <em>elements</em>. A <code>SimpleList</code> represents a sequence
 * of elements <code>x0, x1, x2, ..., xn</code> where <code>n+1</code>
 * is the <em>size</em> of the list, and <code>xi</code> is the
 * element stored at <em>index</em> <code>i</code>. Items can be
 * accessed by index. Operations are provided to return or modify the
 * value of an element at a given index, and to insert or remove
 * elements to/from a specified index in the list. Insertion and
 * removal at index <code>i</code> have the effect of shifting the
 * indices of the remaining elements at indices <code>j &gt;=
 * i</code>.</p>
 *
 * @author Will Rosenbaum
 */


public interface SimpleList<E> {

    /**
     * <p>Get the number of elements currently stored in the list</p>
     *
     * @return the number of elements currently stored in the list
     */

    int size();


    /**
     * <p>Check if the list is empty (i.e., contains no elements)</p>
     *
     * @return true if and only if there are no elements in the list
     */
    
    boolean isEmpty();


    /**
     * <p>Get the i-th element stored in the list. Throws an
     * <code>IndexOutOfBoundsException</code> if there is no element
     * at index i in the list. That is, the exception is thrown if
     * <code>i &lt; 0</code> or <code>i &gt;= this.size()</code>. </p>
     *
     * @param i the index to be queried
     * @return the element stored at index i
     * @throws IndexOutOfBoundsException if there is no element at index i
     */
    E get(int i);

    /**
     * <p>Set the <code>i</code>th element to have value
     * <code>x</code>. This operation is only valid if the list
     * already contains an element at index <code>i</code>. Otherwise,
     * an <code>IndexOutOfBoundsException</code> is thrown. Note that
     * the <code>set</code> operation does not change the size of the
     * container. </p>
     *
     * @param i the index of the element to be set
     * @param x the element to be inserted at index <code>i</code>
     * @throws IndexOutOfBoundsException if there was no element stored at index <code>i</code> before the call to <code>set</code>
     */
    void set(int i, E x);

    
    /**
     * <p>Add a new element, <code>x</code>, into the list by
     * inserting it at index <code>i</code>. Each element
     * <code>y</code> with index <code>j &gt;= i</code> before the
     * call to <code>add</code> will have index <code>j + 1</code>
     * after the call. The operation fails and throws an
     * <code>IndexOutOfBoundsException</code> if <code>i</code> is not
     * a valid index into which to insert an element, i.e., if <code>i
     * &lt; 0</code> or <code>i &gt; this.size()</code>. If the method
     * call is successful, the size of the list increases by one.</p>
     *
     * @param i the index into which to insert the element <code>x</code>
     * @param x the element to be inserted at index <code>i</code>
     * @throws IndexOutOfBoundsException if <code>i</code> is not a valid index to insert a new element
     *
     */
    void add(int i, E x);

    /**
     * Remove the element currently at index <code>i</code> and return
     * its value. Each element <code>y</code> at index <code>j &gt;
     * i</code> will be stored at index <code>j - 1</code> after the
     * operation is executed. Throws an
     * <code>IndexOutOfBoundexException</code> if there is no element
     * at index <code>i</code>. If the operation is successful, the
     * size of the list is decremented by one.
     *
     * @param i the index of the element to be removed
     * @return the element removed from the list
     * @throws IndexOutOfBoundsException if there is no element at
     * index <code>i</code>
     */
    E remove(int i);
}
