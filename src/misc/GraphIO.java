package misc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import graph.BasicEdge;
import graph.BasicGraph;
import graph.BasicNode;
import graph.Contig;
import graph.ContigGraph;
import graph.Edge;
import graph.Graph;
import graph.MetaNode;
import graph.Node;

public class GraphIO {

	public static BasicGraph load (String verticiesFile, String edgesFile) {
		BasicGraph graph = new BasicGraph();
		
		try {
			BufferedReader vbr = new BufferedReader(new FileReader(verticiesFile));
			GraphIO.parseVerticies (vbr, graph);
			vbr.close();
			
			BufferedReader ebr = new BufferedReader(new FileReader(edgesFile));
			GraphIO.parseEdges (ebr, graph);
			ebr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graph;
	}

	private static void parseVerticies(BufferedReader vbr, BasicGraph graph) throws IOException {
		String titles = vbr.readLine();
		if (!titles.contains("Id")) {
			System.err.println("The node file must contain an \"Id\" column.");
			System.exit(1);
		}
		
		String[] vTitles = titles.split(";");
		int vid = 0, /*vLabel, */vSpecies = 0;
		for (int idx=0 ; idx<vTitles.length ; idx++) {
			switch (vTitles[idx]) {
			case "Id":
				vid = idx;
				break;
			/*case "Label":
				vLabel = idx;
				break;/**/
			case "Specie":
				vSpecies = idx;
				break;
			}
		}
		
		String line = null;
		while ((line = vbr.readLine()) != null) {
			String[] split = line.split(";");
			if (split.length != vTitles.length)
				continue;
			
			BasicNode n = null;
			if (vSpecies != 0)
				n = new BasicNode(split[vid], split[vSpecies]);
			else
				n = new BasicNode(split[vid], "?");
			graph.nodes.put(n.id, n);
		}
	}
	
	private static void parseEdges(BufferedReader ebr, BasicGraph graph) throws IOException {
		String titles = ebr.readLine();
		if (!titles.contains("Source") || !titles.contains("Target")) {
			System.err.println("The edge file must contain a \"Source\" and a \"Target\" columns.");
			System.exit(1);
		}
		
		String[] vTitles = titles.split(";");
		int source = 0, target = 0;
		for (int idx=0 ; idx<vTitles.length ; idx++) {
			switch (vTitles[idx]) {
			case "Source":
				source = idx;
				break;
			/*case "Label":
				vLabel = idx;
				break;/**/
			case "Target":
				target = idx;
				break;
			}
		}
		
		String line = null;
		while ((line = ebr.readLine()) != null) {
			String[] split = line.split(";");
			if (split.length != vTitles.length)
				continue;
			
			Node s = graph.nodes.get(split[source]);
			Node t = graph.nodes.get(split[target]);
			s.neighbors.add(t);
			t.neighbors.add(s);
			BasicEdge e = new BasicEdge(s, t);
			graph.edges.add(e);
		}
	}
	
	public static void save (Graph<? extends Node, ? extends Edge> graph, String nodeFile, String edgeFile) {
		try {
			// Nodes
			BufferedWriter bwv = new BufferedWriter(new FileWriter(nodeFile));
			
			bwv.write("Id;Label;Size;Species\n");
			for (Node n : graph.nodes.values()) {
				MetaNode mn = (MetaNode)n;
				bwv.write(n.id + ';' + n.species + ';' + mn.nodes.size() + ';' + mn.species + '\n');
			}
			
			bwv.close();
			
			// Edges
			BufferedWriter bwe = new BufferedWriter(new FileWriter(edgeFile));
			
			// Max degree for normalization
			int avgDegree = 0;
			for (Edge e : graph.edges)
				avgDegree += e.degree;
			avgDegree /= graph.edges.size();
			
			bwe.write("Source;Target;Type;Degree;Category\n");
			for (Edge e : graph.edges) {
				bwe.write(e.n1.id + ';' + e.n2.id + ";Undirected;" + e.degree + ';' + (e.degree * 10 / avgDegree) + '\n');
			}
			
			bwe.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveContigs (ContigGraph contigs, String filename) {
		try {
			BufferedWriter bwv = new BufferedWriter(new FileWriter(filename));
			
			bwv.write("Id;read\n");
			int contIdx = 0;
			for (MetaNode c : contigs.nodes.values()) {
				contIdx += 1;
				Contig cont = (Contig)c;
				for (Node mn : cont.nodes) {
					MetaNode meta = (MetaNode)mn;
					for (Node node : meta.nodes)
						bwv.write("" + contIdx + ';' + node.id + '\n');
				}
			}
			
			bwv.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
