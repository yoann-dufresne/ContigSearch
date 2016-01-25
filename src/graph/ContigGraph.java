package graph;

import java.util.HashMap;
import java.util.Map;

public class ContigGraph extends ContractedGraph {

	public Map<MetaNode, Contig> contigIndex;
	public Map<Node, MetaNode> contractedIndex;
	
	public ContigGraph() {
		super();
		this.contigIndex = new HashMap<>();
		this.contractedIndex = new HashMap<>();
	}
	
}
