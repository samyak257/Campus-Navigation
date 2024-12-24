import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The Backend class provides functionality to manage and interact with a graph of locations and paths.
 * It allows for loading graph data from a .dot file, retrieving location information, 
 * and finding shortest paths or optimal destinations based on multiple start locations.
 */

public class Backend implements BackendInterface {
    
    private GraphADT<String, Double> graph;
    
    /**
	 * Constructor initializes the graph with the provided GraphADT instance.
	 *
	 * @param graph object to store the backend's graph data
	 */
    public Backend(GraphADT<String, Double> graph) {
	this.graph = graph;
    }
    
    /**
     * Loads graph data from a .dot file. Clears the existing graph before loading
     * new data.
     *
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there is an issue reading from the file
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
	
	// Clear existing graph data
	for (String node : graph.getAllNodes()) {
	    graph.removeNode(node);
	}
       
	// Open the file and read each line
	File file = new File(filename);
	try (Scanner scanner = new Scanner(file)) {
	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine().trim();
		if (line.contains("->")) {
		    // Parse the line to extract nodes and edge weight
		    String[] parts = line.split("->|\\[|\\]");
		    String pred = parts[0].trim();
		    String succ = parts[1].trim();
		    double weight = Double.parseDouble(parts[2].replaceAll("[^0-9.]", ""));
		    
		    // Insert predecessor node only if it does not already exist in the graph
		    if (!graph.getAllNodes().contains(pred)) {
			graph.insertNode(pred);
		    }
		    
		    // Insert successor node only if it does not already exist in the graph
		    if (!graph.getAllNodes().contains(succ)) {
			graph.insertNode(succ);
		    }
		    
		    // Insert the edge between the predecessor and successor nodes
		    graph.insertEdge(pred, succ, weight);
		}
	    }
	} catch (FileNotFoundException e) {
	    throw new IOException("File not found: " + filename);
	}
    }
    
    /**
     * Retrieves a list of all locations (node data) in the graph.
     *
     * @return a list of all location names
     */
    @Override
    public List<String> getListOfAllLocations() {
	return graph.getAllNodes();
    }
    
    /**
     * Finds the shortest path of locations between startLocation and endLocation.
     *
     * @param startLocation the starting location
     * @param endLocation   the destination location
     * @return a list of nodes along the shortest path, or an empty list if no path
     *         exists
     */
    @Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
	try {
	    return graph.shortestPathData(startLocation, endLocation);
	} catch (NoSuchElementException e) {
	    return new ArrayList<>(); // Return empty list if no path exists
	}
    }
    
    /**
     * Finds the walking times (edge weights) between each node on the shortest
     * path.
     *
     * @param startLocation the starting location
     * @param endLocation   the destination location
     * @return a list of walking times between nodes along the shortest path, or an
     *         empty list if no path exists
     */
    @Override
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
	List<String> path = findLocationsOnShortestPath(startLocation, endLocation);
	List<Double> times = new ArrayList<>();
	// If no path exists, return an empty list
	if (path.isEmpty()) {
	    return times;
	}
	
	for (int i = 0; i < path.size() - 1; i++) {
	    String from = path.get(i);
	    String to = path.get(i + 1);
	    try {
		times.add(graph.getEdge(from, to)); // Get the edge weight between nodes
	    } catch (NoSuchElementException e) {
		return new ArrayList<>(); // Return empty if any edge is missing
	    }
	}
	return times;
    }
    
    /**
     * Finds the closest destination reachable from multiple start locations with
     * the shortest total time.
     *
     * @param startLocations the list of locations to minimize travel time from
     * @return the closest destination reachable from all start locations
     * @throws NoSuchElementException if no destination is reachable from all start
     *                                locations
     */
    @Override
    public String getClosestDestinationFromAll(List<String> startLocations) throws NoSuchElementException {
	double minTime = Double.MAX_VALUE; // Initialize the minimum total travel time to the maximum possible value
	String closestDestination = null; // Variable to store the closest destination node
	
	// Loop through all nodes in the graph to find the best destination
	for (String node : graph.getAllNodes()) {
	    double totalTime = 0; // Tracks the total travel time from all start locations to the current node
	    boolean allReachable = true; // Flag to check if all start locations can reach the current node
	    
	    // Loop through all start locations to calculate the total travel time to the
	    // current node
	    for (String start : startLocations) {
		try {
		    totalTime += graph.shortestPathCost(start, node); // Add the shortest path cost from the start to
		    // the current node
		} catch (NoSuchElementException e) {
		    // If any start location cannot reach the current node, mark it as unreachable
		    allReachable = false;
		    break; // Exit the loop early since this node is not a valid destination
		}
	    }
	    
	    // If the current node is reachable from all start locations and has a shorter
	    // total travel time
	    if (allReachable && totalTime < minTime) {
		minTime = totalTime; // Update the minimum total travel time
		closestDestination = node; // Update the closest destination to the current node
	    }
	}
	
	// If no common reachable destination was found, throw an exception
	if (closestDestination == null) {
	    throw new NoSuchElementException("No common reachable destination found.");
	}
	
	return closestDestination; // Return the node with the shortest total travel time from all start locations
    }
}
