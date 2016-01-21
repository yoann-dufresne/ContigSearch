package main;

import java.util.Map;

import algo.BFS;
import algo.Contraction;
import graph.Graph;
import graph.Node;
import misc.GraphIO;

public class Main {

	public static void main(String[] args) {
		String verticies = "data/16sp/verticies.csv";
		String edges = "data/16sp/edges.csv";
		String ctrVerticies = verticies.substring(0, verticies.lastIndexOf('.')) + "_contracted.csv";
		String ctrEdges = edges.substring(0, edges.lastIndexOf('.')) + "_contracted.csv";
		
		Graph graph = GraphIO.load(verticies, edges);
		System.out.println("--- Loading ---");
		System.out.println("Nb nodes: " + graph.nodes.size());
		System.out.println("Nb edges: " + graph.edges.size());
		
		Graph contracted = null;
		System.out.println("--- Contraction ---");
		Map<Node, Integer> annotations = BFS.search(graph, graph.nodes.values().iterator().next());
		System.out.println("Nb annotations : " + annotations.size());
		contracted = Contraction.contract(annotations, graph);
		System.out.println("Nb contracted nodes: " + contracted.nodes.size());
		System.out.println("Nb contracted edges: " + contracted.edges.size());
		
		System.out.println("--- Absorb fingers ---");
		Contraction.absorbFingers(contracted);
		System.out.println("Nb contracted nodes: " + contracted.nodes.size());
		System.out.println("Nb contracted edges: " + contracted.edges.size());
		
		System.out.println("--- Filter small nodes ---");
		Contraction.filterNodes(1, contracted);
		Contraction.filterEdges(1, contracted);
		System.out.println("Nb contracted nodes: " + contracted.nodes.size());
		System.out.println("Nb contracted edges: " + contracted.edges.size());
		
		System.out.println("--- Save ---");
		GraphIO.save(contracted, ctrVerticies, ctrEdges);/**/
	}

}
