import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SpecialEdge  {
	protected int stroke = 2;
	private String name;
	private String distance;
	protected SpecialNode nodeA;
	protected SpecialNode nodeB;
	public SpecialEdge(SpecialNode a, SpecialNode b,  String name,String distance) {
		nodeA = a;
		nodeB = b;
		setTextEdgeName(name);
		setTextEdgeDistance(distance);
	}

	public SpecialNode getNodeA() {
		return nodeA;
	}

	public void setNodeA(SpecialNode nodeA) {
		this.nodeA = nodeA;
	}

	public SpecialNode getNodeB() {
		return nodeB;
	}

	public void setNodeB(SpecialNode nodeB) {
		this.nodeB = nodeB;
	}
	
	public void setTextEdgeName(String text){
		if(text == null) {
			this.name = "";
		}else {
			this.name = text;
		}
		
	}
	public void setTextEdgeDistance(String text){
		if(text == null) {
			this.distance = "";
		}else {
			this.distance = text;
		}
		
	}
	public void draw(Graphics g) {
		int xa = nodeA.getX();
		int ya = nodeA.getY();
		int xb = nodeB.getX();
		int yb = nodeB.getY();
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(stroke));
		g2.setColor(Color.BLACK);
		g2.drawLine(xa, ya, xb, yb);

		g2.drawString(name, (xa+xb)/2, ((ya+yb)/2)-5);
		g2.drawString(distance, (xa+xb)/2, ((ya+yb)/2)+15);
		g2.setStroke(new BasicStroke());
	}
	public boolean isUnderCursor(int mx, int my) {
		
		if( mx < Math.min(nodeA.getX(), nodeB.getX()) ||
			mx > Math.max(nodeA.getX(), nodeB.getX()) ||
			my < Math.min(nodeA.getY(), nodeB.getY()) ||
			my > Math.max(nodeA.getY(), nodeB.getY()) ) {
			return false;
		}
		
		int A = nodeB.getY() - nodeA.getY();
		int B = nodeB.getX() - nodeA.getX();
		
		double distance = Math.abs(A*mx - B*my + nodeB.getX()*nodeA.getY() - nodeB.getY()*nodeA.getX())/Math.sqrt(A*A+B*B);
		return distance <= 5;
	}
	
	public void move(int dx, int dy) {
		nodeA.move(dx, dy);
		nodeB.move(dx, dy);
	}

}
