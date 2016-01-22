package graph;

import java.util.HashSet;
import java.util.Set;

public class MetaNode extends Node {
	
	private static int nextId = 0;
	
	public Set<Node> nodes;

	public MetaNode() {
		super("_meta_" + nextId++, "?");
		this.nodes = new HashSet<>();
	}
	
	public MetaNode(Node n) {
		this();
		this.addNode(n);
	}
	
	public void addNode (Node n) {
		if (this.nodes.size() == 0)
			this.species = n.species;
		else if (!n.species.equals(this.species))
			this.species = "?";
		
		this.nodes.add(n);
	}

}
