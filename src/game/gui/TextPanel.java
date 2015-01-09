package game.gui;

import java.util.ArrayList;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextPanel {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected ArrayList<String> lines;
	protected int maxLines;
	private TrueTypeFont font;
	
	public TextPanel(int x, int y, int width, int height, TrueTypeFont font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		lines = new ArrayList<String>();
		maxLines = height / font.getHeight();
		this.font = font;
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < lines.size() && i < maxLines; i++) {
			String s = lines.get(i);
			g.drawString(s, x, y + i * font.getHeight());
		}
		g.drawRoundRect(x, y, width, height, 4);
	}
	
	public void setText(String text) {
		lines = wrap(text, font, width);
	}
	
	public static ArrayList<String> wrap(String text, Font font, int width) {
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
