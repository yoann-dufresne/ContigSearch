package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Graph <N extends Node, E extends Edge> {

	public Map<String, N> nodes;
	public List<E> edges;
	
	public Graph() {
		this.nodes = new HashMap<>();
		this.edges = new ArrayList<>();
	}
	
}
