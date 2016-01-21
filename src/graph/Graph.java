package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

	public Map<String, Node> nodes;
	public List<Edge> edges;
	
	public Graph() {
		this.nodes = new HashMap<>();
		this.edges = new ArrayList<>();
	}
	
}
