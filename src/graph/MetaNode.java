package graph;

import java.util.HashSet;
import java.util.Set;

public class MetaNode extends Node {
	
	private static int nextId = 0;
	
	public Set<Node> nodes;

	public MetaNode(Node n) {
		super("_meta_" + nextId++, n.species);
		this.nodes = new HashSet<>();
		this.nodes.add(n);
	}
	
	public void addNode (Node n) {
		if (!n.species.equals(this.species))
			this.species = "?";
		
		this.nodes.add(n);
	}

}
