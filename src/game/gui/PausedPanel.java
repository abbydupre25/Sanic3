package game.gui;

import game.Defines;
import game.Defines.MoveKey;

import java.util.EnumMap;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

public class PausedPanel {
	private TrueTypeFont font;
	private int selected = 0;
	private int SPACE = 2;
	private int VOLUME_BAR_HEIGHT = 10;
	private VolumePanel musicVolumePanel;
	private VolumePanel soundVolumePanel;
	private Music music;

	private enum OPTION {
		RESUME, MUSIC_VOLUME, SOUND_VOLUME, EXIT
	}
	
	private EnumMap<OPTION, OptionPanel> options;

	public PausedPanel(int x, int y, int width, int height, TrueTypeFont font, Music music) {
		this.font = font;
		this.music = music;
		options = new EnumMap<OPTION, OptionPanel>(OPTION.class);
		int nextY = 50;
		options.put(OPTION.RESUME, new OptionPanel(width/2, nextY, "Resume Game"));
		nextY += font.getHeight()+SPACE;
		musicVolumePanel = new VolumePanel(width/2, nextY, "Music Volume");
		options.put(OPTION.MUSIC_VOLUME, musicVolumePanel);
		nextY += musicVolumePanel.height;
		soundVolumePanel = new VolumePanel(width/2, nextY, "Sound Volume");
		options.put(OPTION.SOUND_VOLUME, soundVolumePanel);
		nextY += soundVolumePanel.height;
		options.put(OPTION.EXIT, new OptionPanel(width/2, nextY, "Quit"));
		nextY += font.getHeight()+SPACE;
	}

	private void shiftSelected(int increment) {
		int requestedID = selected + increment;
		if(requestedID >= 0 && requestedID < options.size()) {
			selected = requestedID;
		}
	}
	
	private class OptionPanel {
		protected String text;
		public int x, y;
		public int height = font.getHeight()+SPACE;
		public int width;
		
		public OptionPanel(int x, int y, String text) {
			this.text = text;
			this.x = x;
			this.y = y;
			width = font.getWidth(text);
		}
		
		public void render(Graphics g) {
			g.drawString(text, x, y);
		}
	}
	
	private class VolumePanel extends OptionPanel {		
		private int volume = 5;
		
		public VolumePanel(int x, int y, String text) {
			super(x, y, text);
			height = font.getHeight()+VOLUME_BAR_HEIGHT+SPACE*2;
		}
		
		@Override
		public void render(Graphics g) {
			g.drawString(text, x, y);
			for(int i=0; i<volume; i++) {
				g.fillRoundRect(x+i*10, y+font.getHeight()+SPACE, 8, VOLUME_BAR_HEIGHT, 1);
			}
		}
		
		public int getVolume() {
			return volume;
		}
		
		public void incrementVolume(int increment) {
			int requestedVolume = volume + increment;
			if(requestedVolume >= 0 && requestedVolume <= 10) {
				volume = requestedVolume;
			}
		}
	}

	public void render(Graphics g) {
		g.setFont(font);
		for(OptionPanel o: options.values()) {
			o.render(g);
		}
		OptionPanel lePanel = (OptionPanel) options.values().toArray()[selected];
		g.drawRoundRect(lePanel.x, lePanel.y, lePanel.width, lePanel.height, 2);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input input = gc.getInput();
		if (input.isKeyPressed(MoveKey.MOVE_DOWN.get())) {
			shiftSelected(1);
		} else if (input.isKeyPressed(MoveKey.MOVE_UP.get())) {
			shiftSelected(-1);
		} else if (input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			sbg.enterState(Defines.ID_EXPLORE);
			gc.getInput().clearKeyPressedRecord();
		} else {
			switch (OPTION.values()[selected]) {
			case RESUME:
				if (isSelecting(input)) {
					sbg.enterState(Defines.ID_EXPLORE);
					gc.getInput().clearKeyPressedRecord();
				}
				break;
			case EXIT:
				if (isSelecting(input)) {
					System.exit(0);
				}
				break;
			case MUSIC_VOLUME:
				if(input.isKeyPressed(MoveKey.MOVE_LEFT.get())) {
					musicVolumePanel.incrementVolume(-1);;
					updateMusicVolume();
				} else if (input.isKeyPressed(MoveKey.MOVE_RIGHT.get())) {
					musicVolumePanel.incrementVolume(1);
					updateMusicVolume();
				}
				break;
			case SOUND_VOLUME:
				if(input.isKeyPressed(MoveKey.MOVE_LEFT.get())) {
					soundVolumePanel.incrementVolume(-1);
					updateSoundVolume();
				} else if (input.isKeyPressed(MoveKey.MOVE_RIGHT.get())) {
					soundVolumePanel.incrementVolume(1);
					updateSoundVolume();
				}
				break;
			}
		}
	}
	
	private void updateMusicVolume() {
		music.setVolume(((float) musicVolumePanel.getVolume())/100.0f);
	}
	
	private void updateSoundVolume() {
		Defines.soundVolume = ((float) soundVolumePanel.getVolume())/100.0f;
	}

	private boolean isSelecting(Input input) {
		return (input.isKeyPressed(Keyboard.KEY_RETURN) || input
				.isKeyPressed(Keyboard.KEY_SPACE));
	}
}
