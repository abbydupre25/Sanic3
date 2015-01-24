package game.util;

import game.Ability;
import game.Defines;
import game.Defines.Action;
import game.Defines.ComponentType;
import game.Defines.MoveKey;
import game.Effect;
import game.GameObject;
import game.Inventory;
import game.Objective;
import game.Quest;
import game.component.Component;
import game.component.InteractComponent;
import game.gui.GUI;
import game.item.Item;
import game.item.ItemInteract;
import game.item.ItemRender;
import game.item.ScriptedItemInteract;
import game.mob.Mob;
import game.mob.MobInput;
import game.mob.MobPhysics;
import game.mob.MobRender;
import game.player.Player;
import game.player.PlayerInput;
import game.player.PlayerInteract;
import game.player.PlayerPhysics;
import game.player.PlayerRender;
import game.sharedComponents.ChattyInteract;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO use one file to define multiple items?
public class ItemLoader {
	private static StateBasedGame sbg;
	private static GUI gui;

	public static Document getDoc(String filePath) {
		File file = new File(filePath);
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static GameObject getGameObjectFromFile(String filePath) {
		Document doc = getDoc(filePath);
		final String type = doc.getDocumentElement().getNodeName();
		switch (type) {
		case "item":
			return getItem(doc);
		case "mob":
			return getMob(doc);
		case "player":
			return getPlayer(doc);
		default:
			System.out.println("Invalid type in " + filePath + " - " + type);
			return null;
		}
	}

	private static Mob getMob(Document doc) {
		String name = doc.getElementsByTagName("name").item(0).getTextContent();
		final float speed = Float.parseFloat(doc.getElementsByTagName("speed")
				.item(0).getTextContent());
		final String dialogPath = doc.getElementsByTagName("dialogFile")
				.item(0).getTextContent();

		Mob mob = new Mob(new Vector2D(), sbg,
				new HashMap<ComponentType, Component>() {
					{
						put(ComponentType.INPUT, new MobInput(speed));
						put(ComponentType.PHYSICS, new MobPhysics(speed));
						put(ComponentType.RENDER, new MobRender());
						put(ComponentType.INTERACT, new ChattyInteract(gui,
								dialogPath));
					}
				});
		return mob;
	}

	private static Player getPlayer(Document doc) {
		final float speed = Float.parseFloat(doc.getElementsByTagName("speed")
				.item(0).getTextContent());
		NamedNodeMap posAttributes = doc.getElementsByTagName("pos").item(0)
				.getAttributes();
		double posX = Integer.parseInt(posAttributes.getNamedItem("x")
				.getTextContent()) * Defines.SIZE;
		double posY = Integer.parseInt(posAttributes.getNamedItem("y")
				.getTextContent()) * Defines.SIZE;

		Player player = new Player(new Vector2D(posX, posY), sbg,
				new HashMap<ComponentType, Component>() {
					{
						put(ComponentType.INPUT, new PlayerInput());
						put(ComponentType.PHYSICS, new PlayerPhysics(speed));
						put(ComponentType.RENDER, new PlayerRender());
						put(ComponentType.INTERACT, new PlayerInteract());
					}
				});
		return player;
	}

	private static Item getItem(Document doc) {
		String name = doc.getElementsByTagName("name").item(0).getTextContent();
		final String imagePath = doc.getElementsByTagName("image").item(0)
				.getTextContent();
		final String desc;
		Node descNode = doc.getElementsByTagName("desc").item(0);
		if (descNode == null) {
			desc = "";
		} else {
			desc = descNode.getTextContent();
		}
		
		final String gearType;
		Node gearTypeNode = doc.getElementsByTagName("type").item(0);
		if (gearTypeNode == null) {
			gearType = "";
		} else {
			gearType = gearTypeNode.getTextContent();
		}
		NodeList effectNodes = doc.getElementsByTagName("effect");
		ArrayList<Effect> effects = new ArrayList<Effect>();
		for (int i = 0; i < effectNodes.getLength(); i++) {
			Node n = effectNodes.item(i);
			String stat = n.getAttributes().getNamedItem("stat")
					.getTextContent();
			float boost = Float.parseFloat(n.getAttributes()
					.getNamedItem("boost").getTextContent());
			effects.add(new Effect(stat, boost));
		}
		NodeList abilityNodes = doc.getElementsByTagName("ability");
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		for (int i = 0; i < abilityNodes.getLength(); i++) {
			Node n = abilityNodes.item(i);
			Element e = (Element) n;
			String move = n.getAttributes().getNamedItem("move")
					.getTextContent();
			NodeList soundNodes = e.getElementsByTagName("sound");
			String soundPath = null;
			if(soundNodes.getLength() > 0) {
				soundPath = soundNodes.item(0).getTextContent();
			}
			Action action = null;
			MoveKey key = null;
			Ability.InputType inputType = null;
			switch (move) {
			case "run":
				action = Action.ACTION_RUN;
				key = MoveKey.RUN;
				inputType = Ability.InputType.KEY_DOWN;
				break;
			}
			try {
				abilities.add(new Ability(action, key, inputType, soundPath));
			} catch (SlickException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Use dialog files as scripts for use on pickup
		final InteractComponent itemInteract;
		Node dialogNode = doc.getElementsByTagName("dialogFile").item(0);
		if (dialogNode == null) {
			itemInteract = new ItemInteract();
		} else {
			itemInteract = new ScriptedItemInteract(gui, dialogNode.getTextContent());
		}
		
		Item item = null;
		try {
			item = new Item(new Vector2D(), sbg, effects, abilities,
					new HashMap<ComponentType, Component>() {
						{
							put(ComponentType.RENDER, new ItemRender(imagePath));
							put(ComponentType.INTERACT, itemInteract);
						}
					});
		} catch (SlickException e) {
			e.printStackTrace();
		}
		item.setName(name);
		item.setDescription(desc);
		item.setGearType(gearType);
		return item;
	}

	public static Quest getQuest(Document doc) {
		String name = doc.getDocumentElement().getAttributes().getNamedItem("name").getTextContent();
		String desc = doc.getElementsByTagName("desc").item(0).getTextContent();
		NodeList objectiveNodes = doc.getElementsByTagName("objective");
		ArrayList<Objective> objectives = new ArrayList<Objective>();
		for (int i = 0; i < objectiveNodes.getLength(); i++) {
			Node objectiveNode = objectiveNodes.item(i);
			String text = ((Element) objectiveNode)
					.getElementsByTagName("text").item(0).getTextContent();
			objectives.add(new Objective(text));
		}
		Quest quest = new Quest(name, desc, objectives);
		return quest;
	}

	public static Item getItem(String filePath) {
		Document doc = getDoc(filePath);
		return getItem(doc);
	}

	public static Mob getMob(String filePath) {
		Document doc = getDoc(filePath);
		return getMob(doc);
	}

	public static Player getPlayer(String filePath) {
		return getPlayer(getDoc(filePath));
	}

	public static Quest getQuest(String filePath) {
		return getQuest(getDoc(filePath));
	}

	public static void setGame(StateBasedGame game) {
		sbg = game;
	}

	public static void setGUI(GUI legui) {
		gui = legui;
	}
}
