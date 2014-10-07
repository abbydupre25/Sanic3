package panels;

import javax.swing.JPanel;

public class LowerPanel extends JPanel {
	protected int width;
	protected int height;
	
	public LowerPanel(int width, int height) {
		this.width = width;
		this.height = height;
		
		setVisible(true);
		setSize(width, height);
	}
}
