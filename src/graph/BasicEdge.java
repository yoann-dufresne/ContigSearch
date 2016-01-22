package graph;

public class BasicEdge extends Edge {

	public BasicEdge(Node n1, Node n2) {
		super(n1, n2, 1);
	}
	
	public BasicEdge(Node n1, Node n2, int degree) {
		super(n1, n2, degree);
	}

}
