package panels;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.DavidDupre.github.utils.XMLParser;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.*;

public class DialoguePanel extends LowerPanel {
	private XMLParser p;
	private JPanel imagePanel;
	private JScrollPane scrollPane;
	private JLabel label;

	public DialoguePanel(String filePath, int width, int height){
		super(width, height);
		p = new XMLParser(filePath);
		
		setLayout(new GridBagLayout());

		initImagePanel();
		initNPCPanel();
		initOptionPanel();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.gridwidth = 1;
		add(imagePanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(scrollPane, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 20;
		add(label, gbc);
	}
	
	private String getNpcName(){
		return p.doc.getDocumentElement().getAttributes().item(0).getTextContent();
	}
	
	private NodeList getDialogues(){
		return p.doc.getElementsByTagName("dialogue");
	}
	
	private Element getDialogue(int id) {
		NodeList dList = getDialogues();
		for (int i=0; i < dList.getLength(); i++) {
			if (Integer.parseInt(dList.item(i).getAttributes().getNamedItem("id").getTextContent()) == id){
				return (Element) dList.item(i);
			}
		}
		System.out.println("Oh no! No dialogue was found with an id of " + id);
		return null;
	}
	
	private ActionListener getButtonAction(final Node optionNode){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String action = optionNode.getAttributes().getNamedItem("action").getTextContent();
				String identifier = "dialogue";
				if(action.startsWith(identifier)){
					int destId = Integer.parseInt(action.substring(identifier.length(), action.length()));
					label.setText(getNpcText(destId));
					scrollPane.setViewportView(getOptions(destId));
					revalidate();
				} else if (action.startsWith("quit")){
					setVisible(false);
					
				}
			}
		};
	}
	
	private String getNpcText(int id) {
		return getDialogue(id).getElementsByTagName("text").item(0).getTextContent();
	}
	
	private String getImageFilePath() {
		return p.doc.getElementsByTagName("imagePath").item(0).getTextContent();
	}
	
	private JPanel getOptions(int id) {
		Element eElement = getDialogue(id);
		NodeList optionList = eElement.getElementsByTagName("option");
		java.util.List<JButton> optionButtons = new ArrayList<JButton>();
		for (int i = 0; i < optionList.getLength(); i++){
			Node oNode = optionList.item(i);
			String optionText = oNode.getTextContent();
			JButton newButton = new JButton(optionText);
			newButton.addActionListener(getButtonAction(oNode));
			optionButtons.add(newButton);
		}
		JPanel optionPanel = new JPanel();
		GridLayout optionLayout = new GridLayout(optionList.getLength(),1);
		optionPanel.setLayout(optionLayout);
		for (int i = 0; i < optionButtons.size(); i++) {
			JButton b = optionButtons.get(i);
			b.setText("<html>" + b.getText() + "<html>"); //enables text wrapping
			b.setPreferredSize(new Dimension(width-height, 33));
			optionPanel.add(b);
			b.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		}
		return optionPanel;
	}
	
	private void initImagePanel() {
		//Set up image
		imagePanel = new JPanel();
		BufferedImage npcPic = null;
		int imageSize = height-50;
		String imageFilePath = getImageFilePath();
		try {
			npcPic = ImageIO.read(new File(imageFilePath));
			npcPic = Thumbnails.of(npcPic).size(imageSize, imageSize).asBufferedImage();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(npcPic));
		picLabel.setPreferredSize(new Dimension(imageSize, imageSize));
		imagePanel.add(picLabel);
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	private void initNPCPanel(){
		//Set up NPC text
		int imageSize = height-50;
		int textHeight = imageSize/3;
		String npcText = getDialogue(0).getElementsByTagName("text").item(0).getTextContent();
		String labelText = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", width-height-70, npcText);  //wrap text
		label = new JLabel(labelText);
		label.setPreferredSize(new Dimension(width-height, textHeight));
		label.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
	}
	
	private void initOptionPanel(){
		//Set up option buttons
		int imageSize = height-50;
		int textHeight = imageSize/3;
		JPanel optionPanel = getOptions(0);
		scrollPane = new JScrollPane(optionPanel);
		scrollPane.setPreferredSize(new Dimension(width-imageSize-30, imageSize-textHeight+2));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}
}