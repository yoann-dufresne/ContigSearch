package algo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import graph.Contig;
import graph.ContigGraph;
import graph.ContractedGraph;
import graph.MetaNode;
import graph.Node;

public class ContigSplicing {

	private ContractedGraph contracted;

	public ContigSplicing(ContractedGraph contracted) {
		this.contracted = contracted;
	}
	
	public ContigGraph basicSplicing () {
		ContigGraph contigs = new ContigGraph();
		
		Set<MetaNode> nodes = new HashSet<>();
		nodes.addAll(this.contracted.nodes.values());
		
		// Nodes creation
		while (!nodes.isEmpty()) {
			MetaNode current = nodes.iterator().next();
			Contig contig = new Contig();
			
			// In case of hub
			if (current.arity() > 2) {
				nodes.remove(current);
				
				contig.addNode(current);
				current.contigId = contig.idx;
				
				contigs.nodes.put(contig.id, contig);
				continue;
			}
			
			// Otherwise
			Queue<MetaNode> queue = new LinkedList<>();
			queue.add(current);
			while (!queue.isEmpty()) {
				current = queue.poll();
				nodes.remove(current);
				contig.addNode(current);
				current.contigId = contig.idx;
				
				for (Node nei : current.neighbors) {
					if (nei.arity() <= 2 &&  nodes.contains(nei))
						queue.add((MetaNode) nei);
				}
			}
			contigs.nodes.put(contig.id, contig);
		}
		
		return contigs;
	}
	
}
