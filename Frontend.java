import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface {
	private BackendInterface backend;

	/**
	 * Implementing classes should support the constructor below.
	 * 
	 * @param backend2 is used for shortest path computations
	 */
	public Frontend(BackendInterface backend2) {
		// now use Backend_Placeholder to test
		this.backend = backend2;
	}

	/**
	 * Returns an HTML fragment that can be embedded within the body of a larger
	 * html page. This HTML output should include: - a text input field with the
	 * id="start", for the start location - a text input field with the id="end",
	 * for the destination - a button labelled "Find Shortest Path" to request this
	 * computation Ensure that these text fields are clearly labelled, so that the
	 * user can understand how to use them.
	 * 
	 * @return an HTML string that contains input controls that the user can make
	 *         use of to request a shortest path computation
	 */
	@Override
	public String generateShortestPathPromptHTML() {
		return "<div>" +
		// a text input field with the id="start", for the start location
				"  <label for='start'>Start Location:</label>" + "  <input type='text' id='start' name='start'>" +
				// a text input field with the id="end", for the destination
				"  <label for='end'>End Location:</label>" + "  <input type='text' id='end' name='end'>" +
				// a button labelled "Find Shortest Path" to request this computation
				"  <button onclick='backend.findLocationsOnShortestPath()'>Find Shortest Path</button>" + "</div>";
	}

	/**
	 * Returns an HTML fragment that can be embedded within the body of a larger
	 * html page. This HTML output should include: - a paragraph (p) that describes
	 * the path's start and end locations - an ordered list (ol) of locations along
	 * that shortest path - a paragraph (p) that includes the total travel time
	 * along this path Or if there is no such path, the HTML returned should instead
	 * indicate the kind of problem encountered.
	 * 
	 * @param start is the starting location to find a shortest path from
	 * @param end   is the destination that this shortest path should end at
	 * @return an HTML string that describes the shortest path between these two
	 *         locations
	 */
	@Override
	public String generateShortestPathResponseHTML(String start, String end) {
		try {
			// create the variable path and times to store the value from
			// findLocationsOnShortestPath() and findTimesOnShortestPath()
			List<String> path = backend.findLocationsOnShortestPath(start, end);
			List<Double> times = backend.findTimesOnShortestPath(start, end);

			// if the path is empty, then return the prompt string
			if (path.isEmpty()) {
				return "<div><p>No path found from " + start + " to " + end + ".</p></div>";
			}

			// output the shortest path from, which is a list
			StringBuilder html = new StringBuilder("<div>");
			html.append("<p>Shortest path from ").append(start).append(" to ").append(end).append(":</p>");
			html.append("<ol>");
			for (String location : path) {
				html.append("<li>").append(location).append("</li>");
			}
			html.append("</ol>");

			// output the total travel time along this path
			double totalTime = times.stream().mapToDouble(Double::doubleValue).sum();
			html.append("<p>Total travel time: ").append(totalTime).append(" minutes</p>");
			html.append("</div>");
			return html.toString();

			// catch the error
		} catch (Exception e) {
			return "<div><p>Error finding path: " + e.getMessage() + "</p></div>";
		}
	}

	/**
	 * Returns an HTML fragment that can be embedded within the body of a larger
	 * html page. This HTML output should include: - a text input field with the
	 * id="from", for the start locations - a button labelled "Closest From All" to
	 * submit this request Ensure that this text field is clearly labelled, so that
	 * the user can understand that they should enter a comma separated list of as
	 * many locations as they would like into this field
	 * 
	 * @return an HTML string that contains input controls that the user can make
	 *         use of to request a ten closest destinations calculation
	 */
	@Override
	public String generateClosestDestinationsFromAllPromptHTML() {
		return "<div>" +
		// a text input field with the id="from", for the start locations
				"  <label for='from'>Enter Start Locations (comma-separated):</label>"
				+ "  <input type='text' id='from' name='from'>" +
				// a button labelled "Closest From All" to submit this request
				"  <button onclick='backend.getClosestDestinationFromAll()'>Closest From All</button>" + "</div>";
	}

	/**
	 * Returns an HTML fragment that can be embedded within the body of a larger
	 * html page. This HTML output should include: - an unordered list (ul) of the
	 * start Locations - a paragraph (p) describing the destination that is reached
	 * most quickly from all of those start locations (summing travel times) - a
	 * paragraph that displays the total/summed travel time that it take to reach
	 * this destination from all specified start locations Or if no such
	 * destinations can be found, the HTML returned should instead indicate the kind
	 * of problem encountered.
	 * 
	 * @param starts is the comma separated list of starting locations to search
	 *               from
	 * @return an HTML string that describes the closest destinations from the
	 *         specified start location.
	 */
	@Override
	public String generateClosestDestinationsFromAllResponseHTML(String starts) {
		try {
			// creat the variable startLocations,closestDestination,times,totalTime
			List<String> startLocations = Arrays.stream(starts.split(",")).map(String::trim) // Trim whitespace from
																								// each location
					.filter(s -> !s.isEmpty()) // Remove empty entries
					.toList();
			String closestDestination = backend.getClosestDestinationFromAll(startLocations);
			List<Double> times = new ArrayList<>();
			double totalTime = 0.0;

			// if the closestDestination is empty, then return the prompt string
			if (closestDestination == null || closestDestination.isEmpty()) {
				return "<div><p>No closest destination found for the specified locations.</p></div>";
			}

			// Calculate the travel time to reach this destination
			for (String start : startLocations) {
				times = backend.findTimesOnShortestPath(start, closestDestination);
				times.addAll(times);

			}
			// add each travel time to the total time
			totalTime = times.stream().mapToDouble(Double::doubleValue).sum();

			// an unordered list (ul) of the start Locations
			StringBuilder html = new StringBuilder("<div>");
			html.append("<ul>");
			for (String start : startLocations) {
				html.append("<li>").append(start.trim()).append("</li>");
			}
			// a paragraph (p) describing the destination that is reached most quickly from
			// all of those start
			// locations (summing travel times)
			html.append("</ul>");
			html.append("<p>Closest destination: ").append(closestDestination).append("</p>");

			// a paragraph that displays the total/summed travel time that it take
			// to reach this destination from all specified start locations
			html.append("<p>Total travel time: ").append(totalTime).append("</p>");
			html.append("</div>");
			return html.toString();

			// catch the NoSuchElementException error
		} catch (NoSuchElementException e) {
			return "<div><p>Error: No such locations found.</p></div>";
		} catch (Exception e) {
			// Handle any other unexpected errors
			return "<p>An error occurred while calculating the closest destination.</p>";
		}
	}

}
