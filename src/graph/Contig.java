package graph;

public class Contig extends MetaNode {
	
	private static int nextIdx = 1;
	
	public int idx;
	public boolean hub;

	public Contig() {
		super();
		this.idx = nextIdx++;
	}
	
	public Contig(MetaNode node) {
		super(node);
		this.idx = nextIdx++;
	}

}
