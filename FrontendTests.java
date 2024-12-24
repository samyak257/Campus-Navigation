import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


/**
 * Test class for the Frontend class to verify the functionality of its methods.
 * These tests aim to validate that the frontend can generate expected HTML
 * snippets and process input based on placeholder backend responses.
 */
public class FrontendTests {

    private Frontend frontend;
    private Backend_Placeholder backend;
    private GraphADT Graph_Placeholder = new Graph_Placeholder();


    public void setUp() {
        backend = new Backend_Placeholder(Graph_Placeholder);
        frontend = new Frontend(backend);
    }




    /**
     * Test that generateShortestPathPromptHTML() method generates HTML with the required elements:
     * input fields for 'start' and 'end' locations and a "Find Shortest Path" button.
     */
    @Test
    public void roleTest1() {
        // creat the backend and frontend instants to test
        Backend_Placeholder backend = new Backend_Placeholder(Graph_Placeholder);
        Frontend frontend = new Frontend(backend);

        String html = frontend.generateShortestPathPromptHTML();


        // Check for presence of required HTML elements
        Assertions.assertTrue(html.contains("input") && html.contains("id='start'"), "Should contain start input field");
        Assertions.assertTrue(html.contains("input") && html.contains("id='end'"), "Should contain end input field");
        Assertions.assertTrue(html.contains("button") && html.contains("Find Shortest Path"), "Should contain button");
    }

    /**
     * Test that generateShortestPathResponseHTML() returns HTML with an ordered list of path locations
     * and the total travel time, as returned by the backend placeholder.
     */
    @Test
    public void roleTest2() {
        // creat the backend and frontend instants to test
        Backend_Placeholder backend = new Backend_Placeholder(Graph_Placeholder);
        Frontend frontend = new Frontend(backend);

        // use "Union South", "Computer Sciences and Statistics" to tes
        String html = frontend.generateShortestPathResponseHTML("Union South", "Computer Sciences and Statistics");


        // Verify that the HTML includes the ordered list for locations and paragraph for travel time
        Assertions.assertTrue(html.contains("<ol>") && html.contains("</ol>"), "Should contain ordered list for path");
        Assertions.assertTrue(html.contains("Total travel time"), "Should contain paragraph for travel time");
    }

    /**
     * Test that generateClosestDestinationsFromAllPromptHTML() includes input for comma-separated locations
     * and a "Closest From All" button.
     * Test for generateClosestDestinationsFromAllResponseHTML() method
     */
    @Test
    public void roleTest3() {
        // creat the backend and frontend instants to test
        Backend_Placeholder backend = new Backend_Placeholder(Graph_Placeholder);
        Frontend frontend = new Frontend(backend);


        String html = frontend.generateClosestDestinationsFromAllPromptHTML();

        // Check for presence of required HTML elements for closest destinations prompt
        Assertions.assertTrue(html.contains("input") && html.contains("id='from'"), "Should contain from input field");
        Assertions.assertTrue(html.contains("button") && html.contains("Closest From All"), "Should contain closest button");


        String html2 = frontend.generateClosestDestinationsFromAllResponseHTML("Union South");


        // Check if the HTML output correctly shows the closest destination and total travel time
        Assertions.assertTrue(html2.contains("<li>Union South</li>"), "HTML should list 'Union South' as a starting location.");
        Assertions.assertTrue(html2.contains("Closest destination:"), "HTML should contain a paragraph for closest destination.");
        Assertions.assertTrue(html2.contains("Total travel time:"), "HTML should contain a paragraph for total travel time.");

    }
}
