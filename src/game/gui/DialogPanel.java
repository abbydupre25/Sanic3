package game.gui;

import game.History;
import game.Inventory;
import game.util.ItemLoader;

import java.awt.Button;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO this whole class could be cleaned up a lot
public class DialogPanel {
	private Document doc;
	private NPCTextPanel textPanel;
	private OptionPanel optionPanel;
	private boolean visible = false;
	private int width;
	private int height;
	private int x;
	private int y;
	private TrueTypeFont font;
	private int maxLines;
	private Inventory inv;
	private History history;

	// Rendered objects
	private ArrayList<String> lines;
	private RoundedRectangle rect;
	private boolean isDrawingOptions = false;

	// TODO more than four options
	public DialogPanel(Document doc, int width, int height, int x, int y,
			TrueTypeFont font, Inventory inv, History history) {
		this.doc = doc;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.font = font;
		this.inv = inv;
		this.history = history;
		this.maxLines = (height - 20) / font.getHeight() - 1;
		lines = new ArrayList<String>();

		textPanel = new NPCTextPanel();
		optionPanel = new OptionPanel();
		updateLines();
		initDialog();
	}

	/**
	 * Loads the first dialog. This would normally be id=0, but this enables the
	 * use of switch statements to determine the first dialog.
	 */
	private void initDialog() {
		Node init = doc.getElementsByTagName("init").item(0);
		select(init);
		updateLines();
	}

	/** Home of all the parsing logic */
	public void select(Node optionNode) {
		String action = optionNode.getAttributes().getNamedItem("action")
				.getTextContent();
		switch (action) {
		case "dialog":
			int id = Integer.parseInt(optionNode.getAttributes()
					.getNamedItem("id").getTextContent());
			addItemsFromDialog(id);
			textPanel.setID(id);
			optionPanel.setID(id);
			break;
		case "switch":
			id = Integer.parseInt(optionNode.getAttributes().getNamedItem("id")
					.getTextContent());
			Element e = getElement("switch", id);
			String context = e.getAttribute("context");
			String param = e.getAttribute("item");
			String targetValue = "false";
			if (context.equals("inv")) {
				targetValue = inv.contains(param) ? "true" : "false";
			} else if (context.equals("history")) {
				targetValue = history.get(param);
			}
			NodeList caseNodes = e.getElementsByTagName("case");
			Node caseNode = null;
			for (int i = 0; i < caseNodes.getLength(); i++) {
				if (caseNodes.item(i).getAttributes().getNamedItem("value")
						.getTextContent().equals(targetValue)) {
					caseNode = caseNodes.item(i);
				}
			}
			select(caseNode); // Recursive as fuck
			break;
		case "history":
			id = Integer.parseInt(optionNode.getAttributes().getNamedItem("id")
					.getTextContent());
			Element q = getElement("history", id);
			history.set(q.getAttribute("flag"), q.getAttribute("value"));
			Node redirect = q.getElementsByTagName("redirect").item(0);
			select(redirect);
			break;
		case "quit":
			setVisible(false);
			break;
		}
	}

	private void addItemsFromDialog(int id) {
		Element e = getElement("dialog", id);
		NodeList items = e.getElementsByTagName("giveItem");
		if (items.getLength() > 0) {
			for (int i = 0; i < items.getLength(); i++) {
				String filePath = items.item(i).getAttributes()
						.getNamedItem("path").getTextContent();
				inv.add(ItemLoader.getItem(filePath));
				System.out.println("added " + filePath + " to inventory");
			}
		}

		// Also remove items
		items = e.getElementsByTagName("getItem");
		if (items.getLength() > 0) {
			for (int i = 0; i < items.getLength(); i++) {
				String name = items.item(i).getAttributes()
						.getNamedItem("name").getTextContent();
				int quantity = Integer.parseInt(items.item(i).getAttributes()
						.getNamedItem("quantity").getTextContent());
				inv.remove(name, quantity);
				System.out.println("removed " + quantity + " " + name
						+ " from inventory");
			}
		}
	}

	public void render(Graphics g) {
		if (visible) {
			Color colorAlpha = new Color(0f, 0f, 1f, 0.5f);
			RoundedRectangle dialogRect = new RoundedRectangle(x, y, width,
					height, 20);
			g.setColor(Color.white);
			g.draw(dialogRect); // Draw outline
			g.setColor(colorAlpha);
			g.fill(dialogRect); // Fill shape
			g.setColor(Color.white);
			g.setFont(font);

			for (int i = 0; i < lines.size(); i++) {
				g.drawString(lines.get(i), x + 10,
						y + 10 + i * font.getHeight());
			}
			if (isDrawingOptions) {
				g.draw(rect);
			}
		}
	}

	/**
	 * Define what needs to be rendered. Minimizes load on render method, which
	 * is called more often
	 */
	private void updateLines() {
		lines = new ArrayList<String>();
		ArrayList<String> list = textPanel.getList();
		ArrayList<OptionButton> buttons = optionPanel.getButtons();
		
		// If there is no room
		if (list.size() + buttons.size() > maxLines + 1 || buttons.size() == 0) {
			for (int i = 0; i < list.size() && i < maxLines; i++) {
				lines.add(list.get(i));
			}
			lines.add("Press SPACE to continue...");
			isDrawingOptions = false;
		} else { // If everything fits nicely
			for (int i = 0; i < list.size(); i++) {
				lines.add(list.get(i));
			}
			int newY = y + list.size() * font.getHeight();

			for (int i = 0; i < buttons.size(); i++) {
				lines.add(buttons.get(i).getText());
			}

			rect = new RoundedRectangle(x + 10, newY + 10
					+ optionPanel.getSelected() * font.getHeight(), width - 20,
					font.getHeight(), 5);
			isDrawingOptions = true;
		}
	}

	public void update(GameContainer gc) {
		Input input = gc.getInput();
		if (input.isKeyPressed(Keyboard.KEY_SPACE)) {
			if (isDrawingOptions) {
				Node optionNode = optionPanel.getSelectedNode();
				select(optionNode);
			} else {
				textPanel.next();
			}
			updateLines();
		} else if (input.isKeyPressed(Keyboard.KEY_UP)
				|| input.isKeyPressed(Keyboard.KEY_W)) {
			optionPanel.shiftSelected(-1);
			updateLines();
		} else if (input.isKeyPressed(Keyboard.KEY_DOWN)
				|| input.isKeyPressed(Keyboard.KEY_S)) {
			optionPanel.shiftSelected(1);
			updateLines();
		}
	}

	private class OptionPanel {
		private ArrayList<OptionButton> buttons;
		private int selected = 0;

		public OptionPanel() {
			setButtons(getOptionButtons(0));
		}

		private ArrayList<OptionButton> getOptionButtons(int id) {
			Element element = getElement("dialog", id);
			NodeList optionList = element.getElementsByTagName("option");
			ArrayList<OptionButton> optionButtons = new ArrayList<OptionButton>();
			for (int i = 0; i < optionList.getLength(); i++) {
				Node oNode = optionList.item(i);
				String optionText = oNode.getTextContent();
				OptionButton newButton = new OptionButton(oNode);
				newButton.setText("- " + optionText);
				optionButtons.add(newButton);
			}
			return optionButtons;
		}

		/** Up or down action */
		public void shiftSelected(int increment) {
			int requestedID = selected + increment;
			if (requestedID >= 0 && requestedID < buttons.size()) {
				selected += increment;
			}
		}

		// Updates the options to match the current dialogue
		public void setID(int id) {
			setButtons(getOptionButtons(id));
			selected = 0;
		}

		public ArrayList<OptionButton> getButtons() {
			return buttons;
		}

		public void setButtons(ArrayList<OptionButton> buttons) {
			this.buttons = buttons;
		}

		public Node getSelectedNode() {
			return buttons.get(selected).getNode();
		}

		public int getSelected() {
			return selected;
		}
	}

	private class OptionButton extends Button {
		private Node node;
		private String text;

		public OptionButton(Node node) {
			this.node = node;
			this.text = "";
		}

		public Node getNode() {
			return node;
		}

		public void setNode(Node node) {
			this.node = node;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	private class NPCTextPanel {
		private String text;
		private String nextString;
		private ArrayList<String> list;

		public NPCTextPanel() {
			this.setText(getNPCText(0));
		}

		private String getNPCText(int id) {
			return getElement("dialog", id).getElementsByTagName("text")
					.item(0).getTextContent();
		}

		// Update the text to match the text of the current dialogue
		public void setID(int id) {
			setText(getNPCText(id));
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
			list = wrap(text, font, width - 10);
			if (list.size() > maxLines) {
				int beginIndex = 0;
				for (int i = 0; i < maxLines; i++) {
					beginIndex += (list.get(i).length() + 1);
				}
				nextString = text.substring(beginIndex);
			} else {
				nextString = "";
			}
		}

		public String getNextString() {
			return nextString;
		}

		public void setNextString(String nextString) {
			this.nextString = nextString;
		}

		public ArrayList<String> getList() {
			return list;
		}

		public void next() {
			if (nextString.equals("") && optionPanel.getButtons().size() == 0) {
				setVisible(false);
			} else {
				setText(nextString);
			}
		}
	}

	private Element getElement(String name, int id) {
		NodeList dList = doc.getElementsByTagName(name);
		for (int i = 0; i < dList.getLength(); i++) {
			if (Integer.parseInt(dList.item(i).getAttributes()
					.getNamedItem("id").getTextContent()) == id) {
				return (Element) dList.item(i);
			}
		}
		System.out.println("Oh no! No " + name + " was found with an id of "
				+ id);
		return null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		
	}

	private ArrayList<String> wrap(String text, Font font, int width) {
		ArrayList<String> list = new ArrayList<String>();
		String str = text;
		String line = "";

		int i = 0;
		int lastSpace = -1;
		while (i < str.length()) {
			char c = str.charAt(i);
			if (Character.isWhitespace(c)) {
				lastSpace = i;
			}

			if (c == '\n' || font.getWidth(line + c) > width) {
				int split = lastSpace != 1 ? lastSpace : i;
				int splitTrimmed = split;

				if (lastSpace != -1 && split < str.length() - 1) {
					splitTrimmed++;
				}

				line = str.substring(0, split);
				str = str.substring(splitTrimmed);

				list.add(line);
				line = "";
				i = 0;
				lastSpace = -1;
			} else {
				line += c;
				i++;
			}
		}
		if (str.length() != 0) {
			list.add(str);
		}
		return list;
	}
}
