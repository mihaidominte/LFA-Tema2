import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class GraphEditor extends JFrame {

	WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent e) {
			JOptionPane.showMessageDialog(null, "Program closed!");
		}

		@Override
		public void windowClosing(WindowEvent e) {
			
			windowClosed(e);
		}

	};
	
	public static void main(String[] args) {
		new GraphEditor();

	}
	
	GraphPanel graphPanel = new GraphPanel(null);

	private GraphEditor() {
		
		setSize(800, 600);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(graphPanel);
		UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.BOLD, 12));
		
		addWindowListener(new WindowAdapter() {
	
			public void windowClosing(WindowEvent event) {
				windowClosed(event);
			}
		});
		
		setVisible(true);
	}
	
}
