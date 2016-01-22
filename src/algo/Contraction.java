package algo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import graph.BasicEdge;
import graph.BasicGraph;
import graph.ContractedGraph;
import graph.Edge;
import graph.MetaNode;
import graph.Node;

public class Contraction {
	
	public static ContractedGraph contract (Map<Node, Integer> annotations, BasicGraph graph) {
		ContractedGraph contracted = new ContractedGraph();
		Map<Node, MetaNode> ctrNodes = new HashMap<>();
		
		// Nodes
		Set<Node> toProcess = new HashSet<Node>();
		toProcess.addAll(annotations.keySet());
		while (!toProcess.isEmpty()) {
			Node current = toProcess.iterator().next();
			toProcess.remove(current);
			
			MetaNode mn = new MetaNode(current);
			
			mn.addNode(current);
			ctrNodes.put(current, mn);
			
			Queue<Node> currentProcess = new LinkedList<>();
			currentProcess.add(current);
			
			while (!currentProcess.isEmpty()) {
				Node top = currentProcess.poll();
				
				for (Node nei : top.neighbors)
					if (toProcess.contains(nei)) {
						if (annotations.get(nei).equals(annotations.get(top))) {
							toProcess.remove(nei);
							currentProcess.add(nei);
							mn.addNode(nei);
							ctrNodes.put(nei, mn);
						}
					}
			}
			
			contracted.nodes.put(mn.id, mn);
		}
		
		// Edges
		HashMap<String, Edge> edges = new HashMap<>();
		for (Edge e : graph.edges) {
			MetaNode mn1 = ctrNodes.get(e.n1);
			MetaNode mn2 = ctrNodes.get(e.n2);
			
			if (!mn1.equals(mn2)) {
				Edge ne = new BasicEdge(mn1, mn2);
				mn1.neighbors.add(mn2);
				mn2.neighbors.add(mn1);
				if (!edges.containsKey(ne.toString()))
					edges.put(ne.toString(), ne);
				else {
					edges.get(ne.toString()).degree += 1;
				}
			}
		}
		contracted.edges.addAll(edges.values());
		
		return contracted;
	}
	
	
	public static void absorbFingers (ContractedGraph graph) {
		Set<MetaNode> nodes = new HashSet<>();
		for (MetaNode n : graph.nodes.values())
			nodes.add(n);
		
		for (MetaNode node : nodes) {
			if (node.arity() == 1) {
				MetaNode nei = (MetaNode) node.neighbors.iterator().next();
				if (nei.arity() > 2) {
					graph.nodes.remove(node.id);
					nei.neighbors.remove(node);
					node.neighbors.remove(nei);
					graph.edges.remove(new BasicEdge(node, nei));
					
					for (Node content : node.nodes)
						nei.addNode(content);
				}
			}
		}
	}
	
	public static void filterNodes (int nodeSize, ContractedGraph graph) {
		Set<MetaNode> nodes = new HashSet<>();
		for (Node n : graph.nodes.values())
			nodes.add((MetaNode)n);
		
		for (MetaNode node : nodes) {
			if (node.nodes.size() <= nodeSize) {
				for (Node nei : node.neighbors) {
					Edge e = new BasicEdge(node, nei);
					graph.edges.remove(e);
					
					nei.neighbors.remove(node);
				}
				graph.nodes.remove(node.id);
			}
		}
	}
	
	public static void filterEdges (int edgeDegree, ContractedGraph graph) {
		Set<Edge> edges = new HashSet<>();
		edges.addAll(graph.edges);
		
		for (Edge e : edges) {
			if (e.degree <= edgeDegree) {
				Node n1 = e.n1;
				Node n2 = e.n2;
				
				n1.neighbors.remove(n2);
				n2.neighbors.remove(n1);
				graph.edges.remove(e);
			}
		}
	}
	
}
