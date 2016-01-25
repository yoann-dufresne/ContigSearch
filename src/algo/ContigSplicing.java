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
				contig.hub = true;
				
				contigs.nodes.put(contig.id, contig);
				this.indexMetaNode(current, contig, contigs);
				continue;
			}
			
			// Otherwise
			contig.hub = false;
			Queue<MetaNode> queue = new LinkedList<>();
			queue.add(current);
			while (!queue.isEmpty()) {
				current = queue.poll();
				nodes.remove(current);
				contig.addNode(current);
				this.indexMetaNode(current, contig, contigs);
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
	
	public int enlargeContigs (ContigGraph cg) {
		int nodesTransfered = 0;
		
		// Analysis each contig that is a hub
		for (MetaNode cmn : cg.nodes.values()) {
			Contig cont = (Contig)cmn;
			if (!cont.hub)
				continue;
		
			// Get the only meta-node in the hub
			MetaNode mn = (MetaNode) cont.nodes.iterator().next();
			
			/* Find reads in the meta-node with only one meta-node neighbor.
			 * Transfer theses nodes to the corresponding meta-node */
			Set<Node> nodes = new HashSet<Node>();
			nodes.addAll(mn.nodes);
			for (Node node : nodes) {
				MetaNode extMn = null;
				for (Node nei : node.neighbors) {
					MetaNode neiMn = cg.contractedIndex.get(nei);
					// TODO : Look why there are null neighbors
					if (neiMn == null)
						continue;
					
					if (!neiMn.equals(mn)) {
						if (extMn == null)
							extMn = neiMn;
						else {
							extMn = null;
							break;
						}
					}
				}
				
				// If extMn != null then extMn is the only neighbor of the node
				if (extMn == null)
					continue;
				
				nodesTransfered += 1;
				extMn.nodes.add(node);
				mn.nodes.remove(node);
				this.indexMetaNode(extMn, cg.contigIndex.get(extMn), cg);
			}
		}
		
		return nodesTransfered;
	}
	
	private void indexMetaNode (MetaNode node, Contig contig, ContigGraph graph) {
		graph.contigIndex.put(node, contig);
		
		for (Node n : node.nodes)
			graph.contractedIndex.put(n, node);
	}
	
}
