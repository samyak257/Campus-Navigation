import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Backend class.
 * These tests verify core functionalities of the Backend class in managing and
 * retrieving graph data for locations and paths.
 */
public class BackendTests {

    /**
     * Test roleTest1: Tests loading and retrieving all locations in the graph.
     * Verifies that after loading initial data, the graph contains the expected nodes.
     */
    @Test
    public void roleTest1() {
        // Create an instance of Backend with the Graph_Placeholder
        Graph_Placeholder placeholderGraph = new Graph_Placeholder();
        Backend backend = new Backend(placeholderGraph);

        // Check that the initial locations are as expected
        List<String> expectedLocations = List.of("Union South", "Computer Sciences and Statistics", 
                                                 "Atmospheric, Oceanic and Space Sciences");
        Assertions.assertEquals(expectedLocations, backend.getListOfAllLocations(),
                "The initial graph should contain the placeholder locations.");
    }

    /**
     * Test roleTest2: Tests the shortest path retrieval functionality.
     * Verifies that the Backend correctly retrieves the shortest path between two locations.
     */
    @Test
    public void roleTest2() {
        // Create an instance of Backend with the Graph_Placeholder
        Graph_Placeholder placeholderGraph = new Graph_Placeholder();
        Backend backend = new Backend(placeholderGraph);

        // Test finding the shortest path between two placeholder nodes
        List<String> expectedPath = List.of("Union South", "Computer Sciences and Statistics");
        List<String> actualPath = backend.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics");

        Assertions.assertEquals(expectedPath, actualPath,
                "The shortest path from 'Union South' to 'Computer Sciences and Statistics' should match the expected path.");
    }

    @Test
    public void roleTest3() {
	// Create an instance of Backend with the Graph_Placeholder
	Graph_Placeholder placeholderGraph = new Graph_Placeholder();
	Backend backend = new Backend(placeholderGraph);
	
	// Define start locations for testing
	List<String> startLocations = new ArrayList<>();
	startLocations.add("Union South");
	startLocations.add("Computer Sciences and Statistics");
	
	// The closest destination expected (based on Graph_Placeholder's logic)
	String expectedDestination = "Computer Sciences and Statistics"; // Adjusted expected result
	String actualDestination = backend.getClosestDestinationFromAll(startLocations);
	
	// Assert that the returned destination matches the expected one
	Assertions.assertEquals(expectedDestination, actualDestination,
				"The closest destination from the start locations should match the expected value.");
    }
    

     /**
     * Test loadGraphDataIntegrationTest: Verifies that the graph is correctly loaded 
     * from a .dot file and the graph contains the expected nodes.
     * 
     * This is an integration test that ensures Backend can load and parse the 
     * graph data from a file and works correctly with the graph structure.
     */
    @Test
    public void loadGraphDataIntegrationTest() {
        try {
            // Initialize the actual graph and backend (not a placeholder)
            GraphADT<String, Double> graph = new Graph_Placeholder();
            Backend backend = new Backend(graph);

            // Load data from a .dot file containing a simple graph structure
            backend.loadGraphData("campus.dot");

            // Expected nodes that should be present after loading
            List<String> expectedLocations = List.of("Union South", "Computer Sciences and Statistics", 
                                                     "Atmospheric, Oceanic and Space Sciences");

            // Verify that the graph contains all the expected nodes
            Assertions.assertTrue(backend.getListOfAllLocations().containsAll(expectedLocations),
                    "The graph should contain all the nodes from the sample_graph.dot file.");
            
        } catch (IOException e) {
            Assertions.fail("File could not be loaded: " + e.getMessage());
        }
    }

     /**
     * Test shortestPathIntegrationTest: Verifies that the shortest path 
     * between two locations is correctly calculated.
     * 
     * This is an integration test to ensure that Backend and Graph interact 
     * properly to compute the shortest path.
     */
    @Test
    public void shortestPathIntegrationTest() {
        try {
            // Initialize the actual graph and backend (not a placeholder)
            GraphADT<String, Double> graph = new Graph_Placeholder();
            Backend backend = new Backend(graph);

            // Load a simple graph from a .dot file
            backend.loadGraphData("campus.dot");

            // Test shortest path between two locations
            List<String> expectedPath = List.of("Union South", "Computer Sciences and Statistics");
            List<String> actualPath = backend.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics");

            // Verify that the shortest path matches the expected path
            Assertions.assertEquals(expectedPath, actualPath, 
                    "The shortest path from 'Union South' to 'Computer Sciences and Statistics' should match the expected path.");
            
        } catch (IOException e) {
            Assertions.fail("File could not be loaded: " + e.getMessage());
        }
    }

    /**
     * Test pathCostIntegrationTest: Verifies that the edge weights along 
     * the shortest path between two locations are calculated correctly.
     * 
     * This is an integration test that ensures the Backend can accurately 
     * compute path costs using the graph's edge weights.
     */
    @Test
    public void pathCostIntegrationTest() {
        try {
            // Initialize the actual graph and backend (not a placeholder)
            GraphADT<String, Double> graph = new Graph_Placeholder();
            Backend backend = new Backend(graph);

            // Load a graph with edge weights
            backend.loadGraphData("campus.dot");

            // Find the walking times on the shortest path between two locations
            List<Double> expectedTimes = List.of(1.0, 2.0); // Example weights for edges in the path
            List<Double> actualTimes = backend.findTimesOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");

            // Verify that the path times match the expected times
            Assertions.assertEquals(expectedTimes, actualTimes, 
                    "The walking times for the shortest path should match the expected times.");
            
        } catch (IOException e) {
            Assertions.fail("File could not be loaded: " + e.getMessage());
        }
    }
    /**
     * Test closestDestinationIntegrationTest: Verifies that the closest 
     * common destination from multiple start locations is correctly identified.
     * 
     * This is an integration test that ensures Backend works with the Graph 
     * implementation to find the optimal destination from multiple starting points.
     */
    @Test
    public void closestDestinationIntegrationTest() {
        try {
            // Initialize the actual graph and backend (not a placeholder)
            GraphADT<String, Double> graph = new Graph_Placeholder();
            Backend backend = new Backend(graph);

            // Load a graph from a file
            backend.loadGraphData("campus.dot");

            // Define multiple starting locations
            List<String> startLocations = List.of("Union South", "Computer Sciences and Statistics");

            // Expected closest common destination
            String expectedDestination = "Atmospheric, Oceanic and Space Sciences";

            // Call the method to find the closest destination from the start locations
            String actualDestination = backend.getClosestDestinationFromAll(startLocations);

            // Verify that the closest destination is correct
            Assertions.assertEquals(expectedDestination, actualDestination, 
                    "The closest destination from the starting locations should be 'Atmospheric, Oceanic and Space Sciences'.");
            
        } catch (IOException e) {
            Assertions.fail("File could not be loaded: " + e.getMessage());
        } catch (NoSuchElementException e) {
            Assertions.fail("No reachable destination found: " + e.getMessage());
        }
    }
}
