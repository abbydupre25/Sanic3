package com.DavidDupre.github;

import java.util.ArrayList;

import com.DavidDupre.github.utils.Boundry;
import com.DavidDupre.github.utils.Vector2D;

public abstract class Entity {
	Vector2D position;
	ArrayList<Boundry> boundries;
	ArrayList<Enemy> enemies;
	double azimuth;
	
	public abstract void update();
}
