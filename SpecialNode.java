import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class SpecialNode {
	private String name;
	private String population;
	protected int x;
	protected int y;
	protected int r = 14;
	public SpecialNode(int x, int y, String name, String population) {
		this.x = x;
		this.y = y;
		setText(name);
		setTextWeight(population);
	}
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}


	public String getText() {
		return name;
	}

	public void setText(String text){
		if(text == null) {
			this.name = "";
		}else {
			this.name = text;
		}
	}
	public String getTextWeight() {
		return population;
	}

	public void setTextWeight(String text){
		if(text == null) {
			this.population = "";
		}else {
			this.population = text;
		}
	}
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(x-r, y-r, r+r, r+r);
		g.setColor(Color.BLACK);
		g.drawOval(x-r, y-r, r+r, r+r);
		
		FontMetrics fm = g.getFontMetrics();
		int tx = x - fm.stringWidth(name)/2;
		int ty = y - fm.getHeight()/2 + fm.getAscent();
		g.drawString(name, tx, ty-25);
		int tWx = x - fm.stringWidth(population)/2;
		int tWy = y - fm.getHeight()/2 + fm.getAscent();
		g.drawString(population, tWx, tWy+25);
	}
	
	public boolean isUnderCursor(int mx, int my) {
		int a = x - mx;
		int b = y - my;
		
		return a*a + b*b <= r*r;
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}	

}
