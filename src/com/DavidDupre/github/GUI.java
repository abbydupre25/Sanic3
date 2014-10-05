package com.DavidDupre.github;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.opengl.Display;

import dialogueTree.DialoguePanel;

/*
 * Creates an canvas to hold the game's display and various UIs.
 * Automatically sticks the display into its canvas when constructed.
 */

public class GUI {
	private Canvas canvas;
	private JFrame rootFrame = new JFrame();
	private JPanel rootPanel;
	private JPanel gameView = new JPanel();
	private JPanel dialoguePanel = new JPanel();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public GUI(int width, int height){
		canvas = new Canvas();
		canvas.setSize(width, height);
		canvas.setFocusable(true);                                
	    canvas.setIgnoreRepaint(true);
		
		gameView.add(canvas);
		gameView.setVisible(true);
		
		canvas.setVisible(true);
		
		rootPanel = new JPanel(new GridBagLayout());
		rootFrame.getContentPane().add(rootPanel);
		
		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(new Dimension(width+100, height+250)); //TODO get rid of magic numbers
		rootFrame.setVisible(true);
		rootFrame.setTitle("Sanic 3");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		rootPanel.add(gameView, gbc);	
		
		try {
			Display.setParent(canvas);
		} catch (Exception e){
		}
		
		rootPanel.revalidate();
	}
		
	private void setDialoguePanel(JPanel panel) {
		//remove the old panel
		rootPanel.remove(dialoguePanel);
		
		//set the local panel to the new panel
		dialoguePanel = panel;
		
		//place appropriately
		gbc.gridx = 0;
		gbc.gridy = 1;
		rootPanel.add(dialoguePanel, gbc);
		
		//refresh the root panel
		rootPanel.revalidate();
	}
	
	public void setDialogue(String filePath) {
		DialoguePanel dp = new DialoguePanel(filePath, canvas.getWidth(), 200);
		setDialoguePanel(dp);
	}
}
