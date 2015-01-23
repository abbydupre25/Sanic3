package game.gui;

import java.util.HashMap;

import game.Defines;
import game.Quest;
import game.player.Player;
import game.util.ItemLoader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ScriptRunner {
	private Document doc;
	private Player player;
	
	public ScriptRunner(Document doc, Player player) {
		this.player = player;
		this.doc = doc;
		initScript();
	}
	
	/**
	 * Loads the first dialog. This would normally be id=0, but this enables the
	 * use of switch statements to determine the first dialog.
	 */
	protected void initScript() {
		Node init = doc.getElementsByTagName("init").item(0);
		select(init);
	}

	/** Home of all the parsing logic */
	public void select(Node optionNode) {
		if(optionNode != null) {
			String action = optionNode.getAttributes().getNamedItem("action")
					.getTextContent();
			switch (action) {
			case "switch":
				int id = Integer.parseInt(optionNode.getAttributes().getNamedItem("id")
						.getTextContent());
				Element e = getElement("switch", id);
				String context = e.getAttribute("context");
				String param = e.getAttribute("item");
				String targetValue = "false";
				if (context.equals("inv")) {
					targetValue = player.getInv().contains(param) ? "true" : "false";
				} else if (context.equals("history")) {
					if(!player.getHistory().getQuests().containsKey(param)) {
						targetValue = "undiscovered";
					} else {
						Quest quest = player.getHistory().getQuests().get(param);
						if(quest.getState().equals("done")){
							targetValue = "done";
						} else {
							targetValue = String.valueOf(quest.getObjectiveID());
						}
					}
				} else if (context.equals("bank")) {
					targetValue = player.getInv().count(Defines.CURRENCY_NAME) >= Integer
							.parseInt(param) ? "true" : "false";
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
				Element h = getElement("history", id);
				player.getHistory().set(h.getAttribute("flag"), h.getAttribute("value"));
				Node redirect = h.getElementsByTagName("redirect").item(0);
				select(redirect);
				break;
			case "quest":
				id = Integer.parseInt(optionNode.getAttributes().getNamedItem("id")
						.getTextContent());
				Element q = getElement("quest", id);
				String path = q.getAttribute("path");
				HashMap<String, Quest> quests = player.getHistory().getQuests();
				if(!quests.containsKey(path)){
					quests.put(path, ItemLoader.getQuest(path));
				}
				Quest quest = quests.get(path);
				NodeList objectiveNodes = q.getElementsByTagName("objective");
				for(int i=0; i<objectiveNodes.getLength(); i++){
					Node oNode = objectiveNodes.item(i);
					id = Integer.parseInt(oNode.getAttributes().getNamedItem("id").getTextContent());
					if(oNode.getAttributes().getNamedItem("complete").getTextContent().equals("true")) {
						quest.getObjectives().get(id).complete();
					}
					quest.getObjectives().get(id).discover();
				}
				redirect = q.getElementsByTagName("redirect").item(0);
				select(redirect);
				break;
			}
		}
	}
	
	protected Element getElement(String name, int id) {
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
}
