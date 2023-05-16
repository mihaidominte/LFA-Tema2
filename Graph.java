import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Graph{

	private String graphTitle;
	private List<SpecialNode> nodes;
	private List<SpecialEdge> edges;

	public Graph(String title) {
		setGraphTitle(title);
		setNodes(new ArrayList<SpecialNode>());
		setEdges(new ArrayList<SpecialEdge>());
		
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public void setGraphTitle(String graphTitle) {
		if(graphTitle == null)
			graphTitle = "";
		else
			this.graphTitle = graphTitle;
	}
	
	public List<SpecialNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<SpecialNode> nodes) {
		this.nodes = nodes;
	}

	public List<SpecialEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<SpecialEdge> edges) {
		this.edges = edges;
	}
	
	public void draw(Graphics g) {
		for (SpecialEdge edge : getEdges()) {
			edge.draw(g);
		}
		
		for (SpecialNode node : getNodes()) {
			node.draw(g);
		}
	}
	
	public void addNode(SpecialNode n) {
		nodes.add(n);
	}
	
	public void addEdge(SpecialEdge e) {
		for (SpecialEdge edge : edges) {
			if(e.equals(edge))
				return;
		}
		edges.add(e);
	}

	public SpecialNode findNodeUnderCursor(int mx, int my) {
		for (SpecialNode node : nodes) {
			if(node.isUnderCursor(mx, my)) {
				return node;
			}
		}
		return null;
	}
	
	public SpecialEdge findEdgeUnderCursor(int mx, int my) {
		for (SpecialEdge edge : edges) {
			if(edge.isUnderCursor(mx, my)) {
				return edge;
			}
		}
		return null;
	}

	public void removeNode(SpecialNode nodeUnderCursor) {
		removeAttachedEdges(nodeUnderCursor);
		nodes.remove(nodeUnderCursor);		
	}
	
	protected void removeAttachedEdges(SpecialNode nodeUnderCursor) {
		edges.removeIf(e -> {
			return e.getNodeA().equals(nodeUnderCursor) 
				|| e.getNodeB().equals(nodeUnderCursor);
		});
	}
	
	public void removeEdge(SpecialEdge e) {
		edges.remove(e);
	}
	
	public void moveGraph(int dx, int dy) {
		for (SpecialNode node : nodes) {
			node.move(dx, dy);
		}
	}
	

	@Override
	public String toString() {
		return graphTitle + "("+ nodes.size() + " nodes, " + edges.size() + " edges)";
	}
	
}
