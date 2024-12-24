// == CS400 Fall 2024 File Header Information ==
// Name: Samyak Jain	
// Email: sjain252@wisc.edu
// Group: P211.1804
// Lecturer: Prof. Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

	protected LinkedList<Pair>[] table = null;
	private int size = 0;

	protected class Pair {

		public KeyType key;
		public ValueType value;

		public Pair(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
		}

	}

	@SuppressWarnings("unchecked")
	public HashtableMap(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be greater than 0.");
		}
		table = (LinkedList<Pair>[]) new LinkedList[capacity];
	}

	public HashtableMap() {
		this(64); // Default to a capacity of 64 by invoking the parameterized constructor
	}

	/**
	 * Adds a new key,value pair/mapping to this collection.
	 * 
	 * @param key   the key of the key,value pair
	 * @param value the value that key maps to
	 * @throws IllegalArgumentException if key already maps to a value
	 * @throws NullPointerException     if key is null
	 */
	@Override
	public void put(KeyType key, ValueType value) throws IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}

		// Calculate the hash index
		int index = Math.abs(key.hashCode()) % table.length;

		// Initialize the LinkedList at the index if it is null
		if (table[index] == null) {
			table[index] = new LinkedList<>();
		}

		// Check for duplicate keys
		for (Pair pair : table[index]) {
			if (pair.key.equals(key)) {
				throw new IllegalArgumentException("Duplicate key. Key already exists.");
			}
		}

		// Add the new Pair to the LinkedList at the computed index
		table[index].add(new Pair(key, value));

		// Increment the size of the map
		size++;

		// Check if the load factor exceeds 80% and rehash if needed
		if ((double) size / table.length >= 0.8) {
			rehash();
		}

	}

	/*
	 * helper method
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		// Save the old table
		LinkedList<Pair>[] oldTable = table;

		// Double the capacity
		table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
		size = 0; // Reset size

		// Reinsert all key-value pairs
		for (LinkedList<Pair> bucket : oldTable) {
			if (bucket != null) {
				for (Pair pair : bucket) {
					this.put(pair.key, pair.value); // Rehash and reinsert
				}
			}
		}
	}

	/**
	 * Checks whether a key maps to a value in this collection.
	 * 
	 * @param key the key to check
	 * @return true if the key maps to a value, and false is the key doesn't map to
	 *         a value
	 */
	@Override
	public boolean containsKey(KeyType key) {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}

		// Calculate the hash index
		int index = Math.abs(key.hashCode()) % table.length;

		// If no bucket exists at this index, the key cannot be present
		if (table[index] == null) {
			return false;
		}

		// Iterate through the LinkedList at the computed index
		for (Pair pair : table[index]) {
			// Check if any Pair has the same key using .equals()
			if (pair.key.equals(key)) {
				return true; // Key found
			}
		}

		// Key not found
		return false;
	}

	/**
	 * Retrieves the specific value that a key maps to.
	 * 
	 * @param key the key to look up
	 * @return the value that key maps to
	 * @throws NoSuchElementException when key is not stored in this collection
	 */
	@Override
	public ValueType get(KeyType key) throws NoSuchElementException {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}

		// Calculate the hash index
		int index = Math.abs(key.hashCode()) % table.length;

		// If no bucket exists at this index, the key cannot be present
		if (table[index] == null) {
			throw new NoSuchElementException("Key not found in the map.");
		}

		// Iterate through the LinkedList at the computed index
		for (Pair pair : table[index]) {
			// Check if the key matches using .equals()
			if (pair.key.equals(key)) {
				return pair.value; // Return the associated value
			}
		}

		// If the key is not found in the bucket
		throw new NoSuchElementException("Key not found in the map.");
	}

	/**
	 * Remove the mapping for a key from this collection.
	 * 
	 * @param key the key whose mapping to remove
	 * @return the value that the removed key mapped to
	 * @throws NoSuchElementException when key is not stored in this collection
	 */
	@Override
	public ValueType remove(KeyType key) throws NoSuchElementException {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}

		// Calculate the hash index
		int index = Math.abs(key.hashCode()) % table.length;

		// If no bucket exists at this index, the key cannot be present
		if (table[index] == null) {
			throw new NoSuchElementException("Key not found in the map.");
		}

		// Iterate through the LinkedList at the computed index
		LinkedList<Pair> bucket = table[index];
		for (int i = 0; i < bucket.size(); i++) {
			Pair pair = bucket.get(i);
			// Check if the key matches using .equals()
			if (pair.key.equals(key)) {
				// Remove the Pair and decrement size
				bucket.remove(i);
				size--; // Update size
				return pair.value; // Return the associated value
			}
		}

		// If the key is not found in the bucket
		throw new NoSuchElementException("Key not found in the map.");
	}

	/**
	 * Removes all key,value pairs from this collection.
	 */
	@Override
	public void clear() {
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}

		// Reset the size to 0
		size = 0;

	}

	/**
	 * Retrieves the number of keys stored in this collection.
	 * 
	 * @return the number of keys stored in this collection
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * Retrieves this collection's capacity.
	 * 
	 * @return the size of te underlying array for this collection
	 */
	@Override
	public int getCapacity() {
	    return table.length;
	}
    
    @Test
    void testPutAddsKeyValuePair() {
	HashtableMap<String, Integer> map = new HashtableMap<>(10);
	map.put("key1", 100);
	Assertions.assertEquals(100, map.get("key1"), "Expected to retrieve value 100 for key 'key1'");
    }
    
    // Test 2: Checking if a key exists
    @Test
    void testContainsKey() {
	HashtableMap<String, Integer> map = new HashtableMap<>(10);
	map.put("key1", 100);
	Assertions.assertTrue(map.containsKey("key1"), "Expected map to contain key 'key1'");
	Assertions.assertFalse(map.containsKey("key2"), "Expected map not to contain key 'key2'");
    }
    
    // Test 3: Handling duplicate keys
    @Test
    void testPutDuplicateKeyThrowsException() {
	HashtableMap<String, Integer> map = new HashtableMap<>(10);
	map.put("key1", 100);
	Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> map.put("key1", 200));
	Assertions.assertEquals("Duplicate key. Key already exists.", exception.getMessage(),
				"Expected exception for duplicate key");
    }
    
    // Test 4: Removing a key-value pair
    @Test
    void testRemoveKeyValuePair() {
	HashtableMap<String, Integer> map = new HashtableMap<>(10);
	map.put("key1", 100);
	Object removedValue = map.remove("key1");
	Assertions.assertEquals(100, removedValue, "Expected to remove value 100 for key 'key1'");
	Assertions.assertFalse(map.containsKey("key1"), "Expected map not to contain key 'key1' after removal");
    }
    
    // Test 5: Clear method
    @Test
    void testClearRemovesAllEntries() {
	HashtableMap<String, Integer> map = new HashtableMap<>(10);
	map.put("key1", 100);
	map.put("key2", 200);
	map.clear();
	Assertions.assertEquals(0, map.getSize(), "Expected map size to be 0 after clear");
	Assertions.assertFalse(map.containsKey("key1"), "Expected map not to contain key 'key1' after clear");
	Assertions.assertFalse(map.containsKey("key2"), "Expected map not to contain key 'key2' after clear");
    }

	@Override
	public List<KeyType> getKeys() {
		 List<KeyType> keysList = new LinkedList<>(); // Create a new list to store keys

		    // Loop through each bucket in the table
		    for (LinkedList<Pair> bucket : table) {
		        if (bucket != null) { // If the bucket is not null
		            for (Pair pair : bucket) {
		                keysList.add(pair.key); // Add each key from the Pair to the keys list
		            }
		        }
		    }

		    return keysList; // Return the list of all keys
	}
}
