package utils;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Square {
	public double size = 1;
	public Vector2D position;
	public Vector2D[] verticies;
	public double theta = 0;
	public Texture texture;
	public boolean isFlipped = false;
	
	public Square(double size, String imageUrl){
		this.position = new Vector2D();
		this.verticies = new Vector2D[4];
		this.size = size;
		verticies[0] = new Vector2D(-size, -size);
		verticies[1] = new Vector2D(size, -size);
		verticies[2] = new Vector2D(size, size);
		verticies[3] = new Vector2D(-size, size);
		
		try{
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(imageUrl)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw() {		
		//draw transparent texture    
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glPushMatrix();    
		
		glColor3f(1f, 1f, 1f);
		glTranslated(position.x, position.y, 0);
		theta -= 90;
		glRotated(theta, 0, 0, 1);
		glTranslated(-position.x, -position.y, 0);
			
		texture.bind();
		glBegin(GL_QUADS);
		if (isFlipped) {
			glTexCoord2f(1, 1);
			glVertex2d(verticies[2].x + position.x, verticies[2].y + position.y);	
			glTexCoord2f(1, 0);
			glVertex2d(verticies[3].x + position.x, verticies[3].y + position.y);
			glTexCoord2f(0, 0);
			glVertex2d(verticies[0].x + position.x, verticies[0].y + position.y);
			glTexCoord2f(0, 1);
			glVertex2d(verticies[1].x + position.x, verticies[1].y + position.y);
		} else {
			glTexCoord2f(0, 1);
			glVertex2d(verticies[0].x + position.x, verticies[0].y + position.y);
			glTexCoord2f(0, 0);
			glVertex2d(verticies[1].x + position.x, verticies[1].y + position.y);
			glTexCoord2f(1, 0);
			glVertex2d(verticies[2].x + position.x, verticies[2].y + position.y);
			glTexCoord2f(1, 1);
			glVertex2d(verticies[3].x + position.x, verticies[3].y + position.y);
		}
		texture.release();
		glDisable(GL_TEXTURE_2D);
		glEnd();
		glPopMatrix();
	}
}
