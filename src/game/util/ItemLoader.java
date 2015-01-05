package game.util;

import game.Defines.ComponentType;
import game.Effect;
import game.GameObject;
import game.Inventory;
import game.component.Component;
import game.gui.GUI;
import game.item.Item;
import game.item.ItemInteract;
import game.item.ItemRender;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO use one file to define multiple items?
public class ItemLoader {
	private static StateBasedGame sbg;
	private static GUI gui;
	private static Inventory inv;
	
	public static Document getDoc(String filePath) {
		File file = new File(filePath);
		Document doc = null;
		try {
			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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
		switch(type){
		case "item":
			return getItem(doc);
		case "mob":
			return getMob(doc);
		case "player":
			return getPlayer(doc, inv);
		default:
			System.out.println("Invalid type in " + filePath + " - " + type);
			return null;
		}
	}
	
	private static Mob getMob(Document doc) {
		String name = doc.getElementsByTagName("name").item(0).getTextContent();
		final float speed = Float.parseFloat(doc.getElementsByTagName("speed").item(0).getTextContent());
		final String dialogPath = doc.getElementsByTagName("dialogFile").item(0).getTextContent();
		
		Mob mob = new Mob(new Vector2D(), sbg,
				new HashMap<ComponentType, Component>() {
			{
				put(ComponentType.INPUT, new MobInput(speed));
				put(ComponentType.PHYSICS, new MobPhysics(speed));
				put(ComponentType.RENDER, new MobRender());
				put(ComponentType.INTERACT, new ChattyInteract(gui, dialogPath));
			}
		});
		return mob;
	}
	
	/** This doesn't actually work lmao */
	private static Player getPlayer(Document doc, Inventory inv) {
		final float speed = Float.parseFloat(doc.getElementsByTagName("speed").item(0).getTextContent());

		Player player = new Player(new Vector2D(), inv, sbg,
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
		final String imagePath = doc.getElementsByTagName("image").item(0).getTextContent();
		NodeList effectNodes = doc.getElementsByTagName("effect");
		ArrayList<Effect> effects = new ArrayList<Effect>();
		for(int i=0; i<effectNodes.getLength(); i++){
			Node n = effectNodes.item(i);
			String stat = n.getAttributes().getNamedItem("stat").getTextContent();
			float boost = Float.parseFloat(n.getAttributes().getNamedItem("boost").getTextContent());
			effects.add(new Effect(stat, boost));
		}
		
		Item item = null;
		try {
			item = new Item(new Vector2D(), sbg, effects,
					new HashMap<ComponentType, Component>() {
				{
					put(ComponentType.RENDER, new ItemRender(imagePath));
					put(ComponentType.INTERACT, new ItemInteract());
				}
			});
		} catch (SlickException e) {
			e.printStackTrace();
		}
		item.setName(name);
		return item;
	}
	
	public static Item getItem(String filePath) {
		Document doc = getDoc(filePath);
		return getItem(doc);
	}
	
	public static Mob getMob(String filePath) {
		Document doc = getDoc(filePath);
		return getMob(doc);
	}
	
	public static Player getPlayer(String filePath, Inventory inv) {
		return getPlayer(getDoc(filePath), inv);
	}

	public static void setGame(StateBasedGame game) {
		sbg = game;
	}

	public static void setGUI(GUI legui) {
		gui = legui;
	}	
	
	public static void setInv(Inventory inventory) {
		inv = inventory;
	}
}
