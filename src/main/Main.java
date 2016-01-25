package main;

import java.io.File;
import java.util.Map;

import algo.BFS;
import algo.ContigSplicing;
import algo.Contraction;
import graph.BasicGraph;
import graph.ContigGraph;
import graph.ContractedGraph;
import graph.Node;
import misc.GraphIO;

public class Main {

	public static void main(String[] args) {
		
		String nodesFile = "nodes.csv";
		String edgesFile = "edges.csv";
		String basename = "";
		int nodeFilter = 0;
		int edgeFilter = 0;
		boolean optimizeContigs = false;
		
		/* --- Arguments parsing --- */
		for (int idx=0 ; idx<args.length ; idx++) {
			switch (args[idx]) {
			case "-n":
				nodesFile = args[++idx];
				break;
			case "-e":
				edgesFile = args[++idx];
				break;
			case "-b":
				basename = args[++idx];
				break;
			case "-N":
				nodeFilter = new Integer(args[++idx]);
				break;
			case "-E":
				edgeFilter = new Integer(args[++idx]);
				break;
			case "-oc":
				optimizeContigs = true;
				break;

			default:
				break;
			}
		}
		
		/* Tests for files */
		File nf = new File(nodesFile);
		if (!nf.exists()) {
			System.err.println(nodesFile + " don't exist");
			System.exit(1);
		}
		File ef = new File(edgesFile);
		if (!ef.exists()) {
			System.err.println(edgesFile + " don't exist");
			System.exit(1);
		}
		
		Main main = new Main();
		main.exec(nodesFile, edgesFile, basename, nodeFilter, edgeFilter, optimizeContigs);
	}
	
	public Main() {}
	
	public void exec (String verticies, String edges, String basename, int nodeFilter, int edgeFilter, boolean oc) {
		
		if (basename.equals("")) {
			String tmp = verticies.substring(0, verticies.lastIndexOf('.'));
			basename = tmp.substring(0, tmp.lastIndexOf('.'));
		}
		
		String ctrVerticies = basename + ".nodes_contracted.csv";
		String ctrEdges = basename + ".edges_contracted.csv";
		String configFile = basename + "contigs.csv";
		
		BasicGraph graph = GraphIO.load(verticies, edges);
		System.out.println("--- Loading ---");
		System.out.println("Nb nodes: " + graph.nodes.size());
		System.out.println("Nb edges: " + graph.edges.size());
		
		ContractedGraph contracted = null;
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
		
		if (nodeFilter != 0 || edgeFilter != 0) {
			System.out.println("--- Filter small nodes and weak edges ---");
			Contraction.filterNodes(nodeFilter, contracted);
			Contraction.filterEdges(edgeFilter, contracted);
			Contraction.absorbFingers(contracted);/**/
			System.out.println("Nb contracted nodes: " + contracted.nodes.size());
			System.out.println("Nb contracted edges: " + contracted.edges.size());
		}
		
		System.out.println("--- Contigs splicings ---");
		ContigSplicing cs = new ContigSplicing(contracted);
		ContigGraph contigs = cs.basicSplicing();
		System.out.println("Nb of contigs: " + contigs.nodes.size());
		if (oc) {
			int nbTransfered = 0;
			int sum = 0;
			do {
				nbTransfered = cs.enlargeContigs(contigs);
				sum += nbTransfered;
			} while (nbTransfered != 0);
			System.out.println("Nb reads transfered from hubs to contigs: " + sum);
		}
		
		System.out.println("--- Save ---");
		GraphIO.save(contracted, ctrVerticies, ctrEdges);/**/
		GraphIO.saveContigs(contigs, configFile);
	}

}
