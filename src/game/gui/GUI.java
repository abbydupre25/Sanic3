package game.gui;

import game.Defines;
import game.player.Player;
import game.util.ItemLoader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.w3c.dom.Document;

public class GUI {
	private int width;
	private int height;
	private int x;
	private int y;
	private TrueTypeFont font;
	private DialogPanel dp;
	private GameContainer gc;
	private Player player;
	
	public GUI(GameContainer container, Player player) throws SlickException {
		this.gc = container;
		this.player = player;
		width = Defines.WINDOW_WIDTH-20;
		height = 120;
		x = 10;
		y = Defines.WINDOW_HEIGHT-height-10;
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, false);
		startDialog("res/dialog/exampleDialog.xml");
		dp.setVisible(false);
	}
	
	public void startDialog(String filePath) {
		Document doc = ItemLoader.getDoc(filePath);
		dp = new DialogPanel(doc, width, height, x, y, font, player);
		dp.setVisible(true);
		gc.getInput().clearKeyPressedRecord();
	}
	
	public void startScript(String filePath) {
		Document doc = ItemLoader.getDoc(filePath);
		ScriptRunner sr = new ScriptRunner(doc, player);
	}
	
	public void update() {
		if(dp.isVisible()) {
			dp.update(gc);
		}
		
		/*if(dp.isVisible()){
			gc.setPaused(true); // Pause game during dialogue
			dp.update(gc);
		} else {
			gc.setPaused(false);
		}*/
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
	
	public boolean isVisible() {
		return dp.isVisible();
	}
}
