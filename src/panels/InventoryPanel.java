package panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;

public class InventoryPanel extends LowerPanel{
	private JLabel blank;
	
	public InventoryPanel (int width, int height) {
		super(width, height);
		setLayout(new GridLayout());
		blank = new JLabel("shit's labled, yo");
		blank.setPreferredSize(new Dimension(width, height));
		add(blank);
	}
}
