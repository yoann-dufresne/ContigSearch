package graph;

public class Edge {

	public Node n1;
	public Node n2;
	public int degree;
	
	public Edge(Node n1, Node n2, int degree) {
		this.n1 = n1;
		this.n2 = n2;
		this.degree = degree;
	}
	
	public Edge(Node n1, Node n2) {
		this(n1, n2, 1);
	}
	
	public int arity () {
		return n1.arity() + n2.arity();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Edge))
			return false;
		return this.toString().equals(obj.toString());
	}
	
	@Override
	public String toString() {
		if (n1.id.compareTo(n2.id) < 0)
			return n1.toString() + "_$$$_" + n2.toString();
		else
			return n2.toString() + "_$$$_" + n1.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
