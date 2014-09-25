package com.DavidDupre.github;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
	public static Music music;
	public static Sound intervention;
	
	public Sounds() {
		
	}
	public static void load() throws SlickException {
		music = new Music("res/sanicTheme.ogg"); //I was listening to music, this was loud, oww
		intervention = new Sound("res/interventionSound.ogg");
	}
}
