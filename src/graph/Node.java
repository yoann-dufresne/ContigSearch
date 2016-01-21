package graph;

import java.util.HashSet;
import java.util.Set;

public class Node {

	public String id;
	public String species;
	public Set<Node> neighbors;
	
	public Node(String id, String species) {
		this.id = id;
		this.species = species;
		this.neighbors = new HashSet<>();
	}
	
	public int arity () {
		return this.neighbors.size();
	}
	
	@Override
	public String toString() {
		return this.id + ";" + this.species;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		Node n = (Node)obj;
		
		return n.id.equals(this.id);
	}
	
}
