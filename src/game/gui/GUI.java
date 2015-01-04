package game.gui;

import game.Defines;
import game.History;
import game.Inventory;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;
import org.w3c.dom.Document;

public class GUI {
	private int width;
	private int height;
	private int x;
	private int y;
	private TrueTypeFont font;
	private DialogPanel dp;
	private GameContainer gc;
	private Inventory inv;
	private History history;
	
	public GUI(GameContainer container, Inventory inv, History history) throws SlickException {
		this.gc = container;
		this.inv = inv;
		this.history = history;
		width = Defines.WINDOW_WIDTH-20;
		height = 120;
		x = 10;
		y = Defines.WINDOW_HEIGHT-height-10;
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, false);
		startDialog("res/dialog/exampleDialog.xml");
		dp.setVisible(false);
	}
	
	// TODO optimize by taking preloaded 'doc' as input
	public void startDialog(String filePath) {
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
		dp = new DialogPanel(doc, width, height, x, y, font, inv, history);
		dp.setVisible(true);
		gc.getInput().clearKeyPressedRecord();
	}
	
	public void update() {
		if(dp.isVisible()){
			gc.setPaused(true); // Pause game during dialogue
			dp.update(gc);
		} else {
			gc.setPaused(false);
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		dp.render(g);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
