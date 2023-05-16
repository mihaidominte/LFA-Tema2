import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, ComponentListener {

	private Grid grid;
	private boolean drawGrid;
	
	private Graph graph;
	
	private boolean mouseLeftButton = false;
	@SuppressWarnings("unused")
	private boolean mouseRightButton = false;	
	
	private int mouseX;
	private int mouseY;
	
	private SpecialNode nodeUnderCursor;
	private SpecialEdge edgeUnderCursor;
	
	private boolean chooseNodeB = false;
	private SpecialNode newEdgeNodeA;
	private SpecialNode newEdgeNodeB;

	public GraphPanel(Graph g) {
		if(g == null) {
			graph = new Graph("Graf");
		}else {
			setGraph(g);
		}
		
		grid = new Grid(getSize(), 50);
		drawGrid = false;
		addMouseMotionListener(this);
		addMouseListener(this);
		addComponentListener(this);
		setFocusable(true);
		requestFocus();
		
	}

	public void setGraph(Graph graph) {
		if(graph == null)
			this.graph = new Graph("Graph");
		else
			this.graph = graph;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(grid != null && drawGrid) 
			grid.draw(g);
		if(graph != null)
			graph.draw(g);
	}

	public void createNewGraph() {
		setGraph(new Graph("Graph"));
		repaint();
	}
	

	public void enableGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
		if(this.drawGrid) {
			grid.scaleGrid(getSize());
		}
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(mouseLeftButton) {
			moveGraphDrag(e.getX(), e.getY());
		}else {
			setMouseCursor(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouseCursor(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouseLeftButton = true;
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			mouseRightButton = true;
		}
		
		setMouseCursor(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			mouseLeftButton = false;
			finalizeAddEdge();
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			mouseRightButton = false;
			chooseNodeB = false;
			if(nodeUnderCursor != null) {
				createNodePopupMenu(e, nodeUnderCursor);
			}else if(edgeUnderCursor != null){
				createEdgePopupMenu(e, edgeUnderCursor);
			}else {
				createPlainPopupMenu(e);
			}
		}
		setMouseCursor(e);
	}

	private void createPlainPopupMenu(MouseEvent e){
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem newNodeMenuItem = new JMenuItem("New node");
		popupMenu.add(newNodeMenuItem);
		newNodeMenuItem.addActionListener((action)->{
			createNewNode(e.getX(), e.getY());
		});
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}
	
	private void createNodePopupMenu(MouseEvent e, SpecialNode n){
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeNodeMenuItem = new JMenuItem("Remove node");
		popupMenu.add(removeNodeMenuItem);
		removeNodeMenuItem.addActionListener((action)->{
			removeNode(n);
		});
		
		popupMenu.addSeparator();
		
		JMenuItem addEdgeMenuItem = new JMenuItem("Add edge");
		popupMenu.add(addEdgeMenuItem);
		addEdgeMenuItem.addActionListener((action)->{
			initializeAddEdge(n);
		});

		if(nodeUnderCursor instanceof SpecialNode) {
			popupMenu.addSeparator();
		
			JMenuItem changeTextMenuItem = new JMenuItem("Change node name ");
			popupMenu.add(changeTextMenuItem);
			changeTextMenuItem.addActionListener((action)->{		
				changeNodeText(n);
			});
			JMenuItem changeTextWeightMenuItem = new JMenuItem("Change node population ");
			popupMenu.add(changeTextWeightMenuItem);
			changeTextWeightMenuItem.addActionListener((action)->{		
				changeNodeTextWeight(n);
			});
			
		}
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private void createEdgePopupMenu(MouseEvent event, SpecialEdge e) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeEdgeMenuItem = new JMenuItem("Remove edge");
		popupMenu.add(removeEdgeMenuItem);
		removeEdgeMenuItem.addActionListener((action)->{
			removeEdge(e);
		});
		
		if(e instanceof SpecialEdge) {
			popupMenu.addSeparator();
			JMenuItem changeEdgeTextMenuItem = new JMenuItem("Change edge name");
			popupMenu.add(changeEdgeTextMenuItem);
			changeEdgeTextMenuItem.addActionListener((action)->{
				changeEdgeText(e);
			});
			JMenuItem changeEdgeTextDistanceMenuItem = new JMenuItem("Change edge distance");
			popupMenu.add(changeEdgeTextDistanceMenuItem);
			changeEdgeTextDistanceMenuItem.addActionListener((action)->{
				changeEdgeTextDistance(e);
			});
		}
		
		popupMenu.show(event.getComponent(), event.getX(), event.getY());
	}

	public void setMouseCursor(MouseEvent e) {
		if(e != null) {
			nodeUnderCursor = graph.findNodeUnderCursor(e.getX(), e.getY());
			if(nodeUnderCursor == null) {
				edgeUnderCursor = graph.findEdgeUnderCursor(e.getX(), e.getY());
			}
			mouseX = e.getX();
			mouseY = e.getY();
		}
		
		int mouseCursor;
		if (nodeUnderCursor != null) {
			mouseCursor = Cursor.HAND_CURSOR;
		}else if(edgeUnderCursor != null) {
			mouseCursor = Cursor.CROSSHAIR_CURSOR;
		}else if(chooseNodeB) {
			mouseCursor = Cursor.WAIT_CURSOR;
		} else if (mouseLeftButton) {
			mouseCursor = Cursor.MOVE_CURSOR;
		} else {
			mouseCursor = Cursor.DEFAULT_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(mouseCursor));
		
	}
	private void moveGraphDrag(int mx, int my) {
		int dx = mx - mouseX;
		int dy = my - mouseY;
		
		if(nodeUnderCursor != null) {
			nodeUnderCursor.move(dx, dy);
		}else if(edgeUnderCursor != null){
			edgeUnderCursor.move(dx, dy);
		}else {
			graph.moveGraph(dx, dy);
		}
		
		mouseX = mx;
		mouseY = my;
		repaint();
	}
	
	private void createNewNode(int mx, int my) {
		try {
		
			String text = JOptionPane.showInputDialog(this, "Input name:", "New node", JOptionPane.QUESTION_MESSAGE);
			String text2 = JOptionPane.showInputDialog(this, "Input population:", "New node", JOptionPane.QUESTION_MESSAGE);
			graph.addNode(new SpecialNode(mx, my, text, text2));
			repaint();
		}catch(NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	private void removeNode(SpecialNode n){
		graph.removeNode(n);
		repaint();
	}
	
	private void initializeAddEdge(SpecialNode n) {
		if(nodeUnderCursor != null) {
			newEdgeNodeA = n;
			chooseNodeB = true;
			setMouseCursor(null);
		}
	}
	
	private void finalizeAddEdge() {
		if(chooseNodeB) {
			if(nodeUnderCursor != null) {
				if(nodeUnderCursor.equals(newEdgeNodeA)) {
					JOptionPane.showMessageDialog(this, "Choose different node!", "Error!", JOptionPane.ERROR_MESSAGE);
				}else {
					try {
						newEdgeNodeB = nodeUnderCursor;
						String text = JOptionPane.showInputDialog(this, "Input name:", "New edge", JOptionPane.QUESTION_MESSAGE);
						String text2 = JOptionPane.showInputDialog(this, "Input distance:", "New edge", JOptionPane.QUESTION_MESSAGE);
						graph.addEdge(new SpecialEdge(newEdgeNodeA, newEdgeNodeB, text, text2));
					
						repaint();
					}catch (NullPointerException e){
						JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
			}
			chooseNodeB = false;
		}
	}
	
	private void removeEdge(SpecialEdge e) {
		graph.removeEdge(e);
		repaint();
	}
	private void changeEdgeText(SpecialEdge e) {
		String text = JOptionPane.showInputDialog(this, "Input name:", "Edit edge", JOptionPane.QUESTION_MESSAGE);
		try {
			((SpecialEdge)e).setTextEdgeName(text);
			repaint();
		}catch(ClassCastException exc) {
			JOptionPane.showMessageDialog(this, "This edge cannot have text.", "Error!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException exc) {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void changeEdgeTextDistance(SpecialEdge e) {
		String text = JOptionPane.showInputDialog(this, "Input distance:", "Edit edge", JOptionPane.QUESTION_MESSAGE);
		try {
			((SpecialEdge)e).setTextEdgeDistance(text);
			repaint();
		}catch(ClassCastException exc) {
			JOptionPane.showMessageDialog(this, "This edge cannot have text.", "Error!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException exc) {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void changeNodeText(SpecialNode n) {
		String text = JOptionPane.showInputDialog(this, "Input name:", "Edit node", JOptionPane.QUESTION_MESSAGE);
		try {
			((SpecialNode)n).setText(text);
			repaint();
		}catch(ClassCastException e) {
			JOptionPane.showMessageDialog(this, "This node cannot have text.", "Error!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void changeNodeTextWeight(SpecialNode n) {
		String text = JOptionPane.showInputDialog(this, "Input  population:", "Edit node", JOptionPane.QUESTION_MESSAGE);
		try {
			((SpecialNode)n).setTextWeight(text);
			repaint();
		}catch(ClassCastException e) {
			JOptionPane.showMessageDialog(this, "This node cannot have text.", "Error!", JOptionPane.INFORMATION_MESSAGE);
		}catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Operation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Object eSource = e.getSource();
		if(eSource == this && drawGrid) {
			grid.scaleGrid(getSize());
			repaint();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
