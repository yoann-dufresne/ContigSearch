package algo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import graph.Graph;
import graph.Node;

public class BFS {
	
	public static Map<Node, Integer> search (Graph gr, Node start) {
		Map<Node, Integer> annotations = new HashMap<>();
		Set<Node> allNodes = new HashSet<Node>();
		allNodes.addAll(gr.nodes.values());
		allNodes.remove(start);
		Queue<Node> queue = new LinkedList<>();
		queue.add(start);
		annotations.put(start, 1);
		
		while (!allNodes.isEmpty()) {
			if (queue.isEmpty()) {
				Node n = allNodes.iterator().next();
				allNodes.remove(n);
				queue.add(n);
				annotations.put(n, 1);
				start = n;
			}
			
			while (!queue.isEmpty()) {
				Node current = queue.poll();
				
				int val = annotations.get(current).intValue() + 1;
				
				for (Node neighbor : current.neighbors)
					if (!annotations.containsKey(neighbor)) {
						annotations.put(neighbor, val);
						allNodes.remove(neighbor);
						queue.add(neighbor);
					}
			}
			
			annotations.put(start, 2);
		}
		
		return annotations;
	}
	
}
