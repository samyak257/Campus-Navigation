// 
// Name: Samyak Jain
// Email: sjain252@wisc.edu
// Group and Team: P2.1804
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
	// Check if graph contains the start and end nodes
    if (!containsNode(start) || !containsNode(end)) {
        throw new NoSuchElementException("Start or end node not in graph.");
    }

    // PriorityQueue for SearchNodes, please note this is ordered by cost for Dijkstra's algorithm
    PriorityQueue<SearchNode> queue = new PriorityQueue<>();
    
    MapADT<NodeType, SearchNode> visitedNode = new HashtableMap<>();

    // Initialize the start node with cost 0
    Node startNode = nodes.get(start);
    SearchNode firstSearchNode = new SearchNode(startNode, 0, null);
    queue.add(firstSearchNode);

    // Dijkstra's Algorithm
    while (!queue.isEmpty()) {
        // node with the lowest cost
        SearchNode currentSearchNode = queue.poll();
        Node currentNode = currentSearchNode.node;

        // skip if already visited
        if (visitedNode.containsKey(currentNode.data)) {
            continue;
        }

        // Mark node as visited
        visitedNode.put(currentNode.data, currentSearchNode);

        // Check if reached end node
        if (currentNode.data.equals(end)) {
            return currentSearchNode; // Return the SearchNode representing the end node
        }

       
        for (Edge edge : currentNode.edgesLeaving) {
            Node neighbor = edge.successor;
            double newCost = currentSearchNode.cost + edge.data.doubleValue();

            // consider this neighbor if it hasn't been visited or if the new cost is lower
            if (!visitedNode.containsKey(neighbor.data)) {
                queue.add(new SearchNode(neighbor, newCost, currentSearchNode));
            }
        }
    }

    // If the end node was not reached, there is no path
    throw new NoSuchElementException("error: there is no path from start to end node.");
    }
    

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
	// Call computeShortestPath to find the end 
    SearchNode endNode = computeShortestPath(start, end);
    
    // If computeShortestPath returned null, there's no valid path
    if (endNode == null) {
        throw new NoSuchElementException("No path found between start and end nodes.");
    }
    
    // Initialize the list to store path from start to end
    LinkedList<NodeType> path = new LinkedList<>();
    
    // Traverse from end to start using the predecessor 
    for (SearchNode current = endNode; current != null; current = current.predecessor) {
	path.addFirst(current.node.data); // Accessing data field from the node
    }
    
    // Return the path
    return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
   
	SearchNode endNode = computeShortestPath(start, end);
    
	return endNode.cost;
    }

  
    
    @Test
    /**
     *Tests the shortest path computation from node "A" to node "G" in a graph
     * set up to match a sample traced in lecture.
     */
    public void testShortestPath1() {
        
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        
        // Set up the example graph from lecture
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "C", 2.0);
	graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
	graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "F", 0.0);
	graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);

        // Test the shortest path from A to G
        List<String> path = graph.shortestPathData("A", "E");
        double cost = graph.shortestPathCost("A", "E");
	
        // Expected path and cost based on manual calculation
        Assertions.assertEquals(List.of("A", "B", "D", "E"), path);
        Assertions.assertEquals(8.0, cost, 0.01);
    }

    @Test
    /**
     * Test case for testing the shortest path method.
     * This test verifies that the graph correctly computes the shortest path 
     * and its associated cost from node "A" to node "H"
     * when there is a different start and end
     */
    public void testShortestPath2() {
	
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        
	graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
	
        // Test the shortest path from A to H
        List<String> path = graph.shortestPathData("B", "E");
        double cost = graph.shortestPathCost("B", "E");

        // Expected path and cost
        Assertions.assertEquals(List.of("B", "D", "E"), path);
        Assertions.assertEquals(4.0, cost, 0.01);
    }

    @Test
    /**
     * Test case for handling the scenario where no path exists between two nodes in the graph.
     * This test verifies that the DijkstraGraph class correctly throws a NoSuchElementException
     * when attempting to find the shortest path or its cost between disconnected nodes.
     */
    public void testNoPath() {
        // Create a disconnected graph
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        
        // Add nodes with no connecting path between them
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 5.0);

        // Try to find the shortest path from A to C (no path should exist)
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("A", "C");
        });

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("A", "C");
        });
    }
}
