package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    /**
     * Vraća kolekciju napunjenu s <code>item</code> elemenata počinjući od 0
     * @param items broj elemenata koje treba dodati
     * @return vraća novostvorenu koleciju
     */
    private ArrayIndexedCollection getCollection(int items) {
            ArrayIndexedCollection result = new ArrayIndexedCollection();
            for(int i = 0; i < items; i++) {
                result.add(i);
            }
            return result;
    }

    @Test
    public void testArraySizeDefaultConstructor() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertEquals(16, collection.arraySize());
    }

    @Test
    public void testArraySizeValidInitialCapacityConstructor() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(5);
        assertEquals(5, collection.arraySize());
    }

    @Test
    public void testArraySizeInvalidInitialCapacityConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
    }

    @Test
    public void testOtherCollectionConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection);
        assertArrayEquals(new Object[] {0, 1, 2}, resultArray.toArray());
    }

    @Test
    public void testEmptyOtherCollectionConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(new ArrayIndexedCollection()));
    }

    @Test
    public void testNullOtherCollectionConstructor() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
    }

    @Test
    public void testArraySizeOtherCollectionAndSmallerInitialCapacityConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection, 2);
        assertEquals(3, resultArray.arraySize());
    }

    @Test
    public void testArrayElementsOtherCollectionAndSmallerInitialCapacityConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection, 2);
        assertArrayEquals(new Object[] {0, 1, 2}, resultArray.toArray());
    }

    @Test
    public void testArraySizeOtherCollectionAndLargerInitialCapacityConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection, 5);
        assertEquals(5, resultArray.arraySize());
    }

    @Test
    public void testArrayElementsOtherCollectionAndLargerInitialCapacityConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection, 2);
        assertArrayEquals(new Object[] {0, 1, 2}, resultArray.toArray());
    }

    @Test
    public void testArraySizeOtherCollectionAndInvalidInitialCapacityConstructor() {
        ArrayIndexedCollection toBeSentCollection = getCollection(3);
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(toBeSentCollection, 0);
        assertEquals(3, resultArray.arraySize());
    }

    @Test
    public void testNullOtherCollectionAndInitialCapacityConstructor() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 5));
    }

    @Test
    public void testArraySizeEmptyOtherCollectionAndInitialCapacityConstructor() {
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(new ArrayIndexedCollection(), 5);
        assertEquals(5, resultArray.arraySize());
    }

    @Test
    public void testArrayElementsEmptyOtherCollectionAndLargerInitialCapacityConstructor() {
        ArrayIndexedCollection resultArray = new ArrayIndexedCollection(new ArrayIndexedCollection(), 5);
        assertArrayEquals(new Object[] {}, resultArray.toArray());
    }

    @Test
    public void testIsEmptyFunctionOnEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertEquals(true, collection.isEmpty());
    }

    @Test
    public void testIsEmptyFunctionOnNonEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        assertEquals(false, collection.isEmpty());
    }

    @Test
    public void testSizeFunctionEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertEquals(0, collection.size());
    }

    @Test
    public void testSizeFunctionNonEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add(2);
        assertEquals(2, collection.size());
    }

    @Test
    public void testAddFunctions() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add(2);
        assertEquals(2, collection.size());
    }

    @Test
    public void testAddFunctionOnFullArray() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(2);
        collection.add(1);
        collection.add(2);
        collection.add(3);
        assertEquals(4, collection.arraySize());
    }

    @Test
    public void testAddNullFunction() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(2);
        assertThrows(NullPointerException.class, () -> collection.add(null));
    }

    @Test
    public void testGetFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(1, collection.get(1));
    }

    @Test
    public void testGetFunctionIndexToBig() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(4));
    }

    @Test
    public void testGetFunctionIndexToSmall() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
    }

    @Test
    public void testGetFunctionOnEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(0));
    }

    @Test
    public void testInsertFunctionBeginning() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.insert(5, 0);
        assertArrayEquals(new Object[] {5, 0, 1, 2}, collection.toArray());
    }

    @Test
    public void testInsertFunctionMiddle() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.insert(5, 1);
        assertArrayEquals(new Object[] {0, 5, 1, 2}, collection.toArray());
    }

    @Test
    public void testInsertFunctionEnd() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.insert(5, 3);
        assertArrayEquals(new Object[] {0, 1, 2, 5}, collection.toArray());
    }

    @Test
    public void testInsertFunctionToBigIndex() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(5, 5));
    }

    @Test
    public void testInsertFunctionToSmallIndex() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(5, -1));
    }

    @Test
    public void testInsertNullFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(NullPointerException.class, () -> collection.insert(null, 1));
    }

    @Test
    public void testIndexOfExistingFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(1, collection.indexOf(1));
    }

    @Test
    public void testIndexOfNonExistingFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(-1, collection.indexOf(5));
    }

    @Test
    public void testIndexOfNullFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(-1, collection.indexOf(null));
    }

    @Test
    public void testContainsExistingFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(true, collection.contains(1));
    }

    @Test
    public void testContainsNonExistingFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(false, collection.contains(5));
    }

    @Test
    public void testContainsNullFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(false, collection.contains(null));
    }

    @Test
    public void testRemoveUsingIndexFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.remove(1);
        assertArrayEquals(new Object[] {0, 2}, collection.toArray());
    }

    @Test
    public void testRemoveUsingToBigIndexFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(5));
    }

    @Test
    public void testRemoveUsingToSmallIndexFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
    }

    @Test
    public void testRemoveUsingToIndexEmptyCollectionFunction() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(0));
    }

    @Test
    public void testArrayRemoveUsingObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.remove((Integer) 1);
        assertArrayEquals(new Object[] {0, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(true, collection.remove((Integer) 1));
    }

    @Test
    public void testArrayRemoveUsingNonExistingObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.remove((Integer) 5);
        assertArrayEquals(new Object[] {0, 1, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingNonExistingObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(false, collection.remove((Integer) 5));
    }

    @Test
    public void testArrayRemoveUsingNullObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        collection.remove(null);
        assertArrayEquals(new Object[] {0, 1, 2}, collection.toArray());
    }

    @Test
    public void testBooleanRemoveUsingNullObjectFunction() {
        ArrayIndexedCollection collection = getCollection(3);
        assertEquals(false, collection.remove(null));
    }

    @Test
    public void testToArrayFunctionEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertEquals(0, collection.toArray().length);
    }

    @Test
    public void testToArrayFunctionNonEmptyCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add(2);
        assertArrayEquals(new Object[] {1, 2}, collection.toArray());
    }

    @Test
    public void testForEach() {
        ArrayIndexedCollection collection = getCollection(3);
        Integer rand = (int) Math.random();

        class TestProcessor extends Processor {
            public void process(Object value) {
                int index = collection.indexOf(value);
                collection.remove(index);
                collection.insert(rand, index);
            }
        }

        collection.forEach(new TestProcessor());
        assertArrayEquals(new Object[] {rand, rand, rand}, collection.toArray());
    }

    @Test
    public void testForEachNullProcessor() {
        ArrayIndexedCollection collection = getCollection(3);
        assertThrows(NullPointerException.class, () -> collection.forEach(null));
    }

    @Test
    public void testSizeAfterClear() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(5);
        collection.add(0);
        collection.add(1);
        collection.add(2);
        collection.clear();
        assertEquals(0, collection.size());
    }

    @Test
    public void testArraySizeAfterClear() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(5);
        collection.add(0);
        collection.add(1);
        collection.add(2);
        collection.clear();
        assertEquals(5, collection.arraySize());
    }
}
