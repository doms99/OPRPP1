package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.Processor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    /**
     * Vraća kolekciju napunjenu s <code>item</code> elemenata počinjući od 0
     * @param items broj elemenata koje treba dodati
     * @return vraća novostvorenu koleciju
     */
    private LinkedListIndexedCollection getCollection(int items) {
        LinkedListIndexedCollection result = new LinkedListIndexedCollection();
        for(int i = 0; i < items; i++) {
            result.add(i);
        }
        return result;
    }

    /*
    @Test
    public void testOtherCollectionConstructor() {
        LinkedListIndexedCollection toBeSentCollection = getCollection(3);
        LinkedListIndexedCollection resultArray = new LinkedListIndexedCollection(toBeSentCollection);
        assertArrayEquals(new Object[] {0, 1, 2}, resultArray.toArray());
    }

    @Test
    public void testEmptyOtherCollectionConstructor() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection(new LinkedListIndexedCollection());
        assertEquals(0, collection.size());
    }

    @Test
    public void testNullOtherCollectionConstructor() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    @Test
    public void testIsEmptyFunctionOnEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testIsEmptyFunctionOnNonEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        assertFalse(collection.isEmpty());
    }

    @Test
    public void testSizeFunctionEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertEquals(0, collection.size());
    }

    @Test
    public void testSizeFunctionNonEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add(2);
        assertEquals(2, collection.size());
    }

    @Test
    public void testAddFunctions() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        assertEquals(1, collection.size());
    }

    @Test
    public void testAddNullFunction() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertThrows(NullPointerException.class, () -> collection.add(null));
    }

    @Test
    public void testGetFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertEquals(1, collection.get(1));
    }

    @Test
    public void testGetFunctionIndexToBig() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(4));
    }

    @Test
    public void testGetFunctionIndexToSmall() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
    }

    @Test
    public void testGetFunctionOnEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(0));
    }

    @Test
    public void testInsertFunctionBeginning() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.insert(5, 0);
        assertArrayEquals(new Object[] {5, 0, 1, 2}, collection.toArray());
    }

    @Test
    public void testInsertFunctionBeginningFirstValue() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.insert(5, 0);
        assertEquals(5, collection.getFirst());
    }

    @Test
    public void testInsertFunctionMiddle() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.insert(5, 1);
        assertArrayEquals(new Object[] {0, 5, 1, 2}, collection.toArray());
    }

    @Test
    public void testInsertFunctionEnd() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.insert(5, 3);
        assertArrayEquals(new Object[] {0, 1, 2, 5}, collection.toArray());
    }

    @Test
    public void testInsertFunctionEndLastValue() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.insert(5, 3);
        assertEquals(5, collection.getLast());
    }

    @Test
    public void testInsertFunctionToBigIndex() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(5, 5));
    }

    @Test
    public void testInsertFunctionToSmallIndex() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(5, -1));
    }

    @Test
    public void testInsertNullFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(NullPointerException.class, () -> collection.insert(null, 1));
    }

    @Test
    public void testInsertIntoEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.insert(1, 0);
        assertArrayEquals(new Object[] {1}, collection.toArray());
    }

    @Test
    public void testFirstValueInsertIntoEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.insert(1, 0);
        assertEquals(1, collection.getFirst());
    }

    @Test
    public void testLastValueInsertIntoEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.insert(1, 0);
        assertEquals(1, collection.getLast());
    }

    @Test
    public void testIndexOfExistingFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertEquals(1, collection.indexOf(1));
    }

    @Test
    public void testIndexOfNonExistingFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertEquals(-1, collection.indexOf(5));
    }

    @Test
    public void testIndexOfNullFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertEquals(-1, collection.indexOf(null));
    }

    @Test
    public void testContainsExistingFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertTrue(collection.contains(1));
    }

    @Test
    public void testContainsNonExistingFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertFalse(collection.contains(5));
    }

    @Test
    public void testContainsNullFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertFalse(collection.contains(null));
    }

    @Test
    public void testRemoveUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(1);
        assertArrayEquals(new Object[] {0, 2}, collection.toArray());
    }

    @Test
    public void testRemoveFirstUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(0);
        assertArrayEquals(new Object[] {1, 2}, collection.toArray());
    }

    @Test
    public void testFirstValueRemoveFirstUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(0);
        assertEquals(1, collection.getFirst());
    }

    @Test
    public void testRemoveLastUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(2);
        assertArrayEquals(new Object[] {0, 1}, collection.toArray());
    }

    @Test
    public void testLastValueRemoveLastUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(2);
        assertEquals(1, collection.getLast());
    }

    @Test
    public void testRemoveOnlyObjectUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(1);
        collection.remove(0);
        assertArrayEquals(new Object[] {}, collection.toArray());
    }

    @Test
    public void testLastValueRemoveOnlyObjectUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(1);
        collection.remove(0);
        assertEquals(null, collection.getLast());
    }

    @Test
    public void testFirstValueRemoveOnlyObjectUsingIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(1);
        collection.remove(0);
        assertEquals(null, collection.getLast());
    }

    @Test
    public void testRemoveUsingToBigIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(5));
    }

    @Test
    public void testRemoveUsingToSmallIndexFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
    }

    @Test
    public void testRemoveUsingIndexEmptyCollectionFunction() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(0));
    }

    @Test
    public void testArrayRemoveUsingObjectFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove((Integer) 1);
        assertArrayEquals(new Object[] {0, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingObjectFunction() { // Testing the middle because of the optimization when searching from both sides
        LinkedListIndexedCollection collection = getCollection(3);
        assertTrue(collection.remove((Integer) 1));
    }

    @Test
    public void testArrayRemoveUsingNonExistingObjectFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove((Integer) 5);
        assertArrayEquals(new Object[] {0, 1, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingNonExistingObjectFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertFalse(collection.remove((Integer) 5));
    }

    @Test
    public void testArrayRemoveUsingNullObjectFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        collection.remove(null);
        assertArrayEquals(new Object[] {0, 1, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingNullObjectFunction() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertFalse(collection.remove(null));
    }

    @Test
    public void testToArrayFunctionEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertEquals(0, collection.toArray().length);
    }

    @Test
    public void testToArrayFunctionNonEmptyCollection() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add(2);
        assertArrayEquals(new Object[] {1, 2}, collection.toArray());
    }

    @Test
    public void testForEach() {
        LinkedListIndexedCollection collection = getCollection(3);
        LinkedListIndexedCollection emptyCollection = new LinkedListIndexedCollection();

        class TestProcessor implements Processor {
            public void process(Object value) {
                emptyCollection.add(value);
            }
        }

        collection.forEach(new TestProcessor());
        assertArrayEquals(collection.toArray(), emptyCollection.toArray());
    }

    @Test
    public void testForEachNullProcessor() {
        LinkedListIndexedCollection collection = getCollection(3);
        assertThrows(NullPointerException.class, () -> collection.forEach(null));
    }

    @Test
    public void testSizeAfterClear() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(0);
        collection.add(1);
        collection.add(2);
        collection.clear();
        assertEquals(0, collection.size());
    }
    */
}
